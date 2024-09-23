package org.example.cardrangefinder.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cardrangefinder.dto.CardInfoDto;
import org.example.cardrangefinder.entity.CardRange;
import org.example.cardrangefinder.exceptions.DatabaseNotReadyException;
import org.example.cardrangefinder.exceptions.FileDownloadException;
import org.example.cardrangefinder.exceptions.InvalidCardNumberFormatException;
import org.example.cardrangefinder.exceptions.MoreThanOneRangeException;
import org.example.cardrangefinder.repository.CardRangeRepository;
import org.example.cardrangefinder.service.CardRangeService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class CardRangeServiceImpl implements CardRangeService, ApplicationListener<ContextRefreshedEvent> {
    private final CardRangeRepository cardRangeRepository;
    private final RestTemplate restTemplate;


    private static final String URL = "https://ecom-bininfo.s3.eu-west-1.amazonaws.com/bininfo.json.zip";

    private volatile boolean isMainTableActiveForRead = true;
    private volatile boolean isMainTableActiveForWrite = true;
    private volatile boolean isDatabaseReady = false;

    public CardRangeServiceImpl(RestTemplate restTemplate,
                                CardRangeRepository cardRangeRepository) {
        this.restTemplate = restTemplate;
        this.cardRangeRepository = cardRangeRepository;
    }

    @Override
    public CardInfoDto findCardInfoByCardNumber(String cardNumber) {
        if (!isDatabaseReady) {
            throw new DatabaseNotReadyException("Database is updating now. Please wait 15 seconds.");
        }
        if (cardNumber.length() != 16) {
            throw new InvalidCardNumberFormatException("Invalid card number format");
        }

        String prefix = cardNumber.substring(0, 6);
        String suffix = cardNumber.substring(12);

        String minCardNumber = prefix + "000000" + suffix + "000";
        String maxCardNumber = prefix + "999999" + suffix + "000";

        BigDecimal minValue = new BigDecimal(minCardNumber);
        BigDecimal maxValue = new BigDecimal(maxCardNumber);

        List<CardInfoDto> cardInfoList =
                cardRangeRepository.findCardInfoByMinAndMaxPossibleCardNumber(minValue, maxValue,
                        isMainTableActiveForRead);
        if (cardInfoList.size() > 1) {
            throw new MoreThanOneRangeException("There are more than one card range available");
        }
        return cardInfoList.get(0);
    }


    @Scheduled(cron = "0 * * * * *")
    public void scheduledUpdateCardRanges() {
        isMainTableActiveForWrite = !isMainTableActiveForWrite;
        updateCardRanges();
        isMainTableActiveForRead = !isMainTableActiveForRead;
        cardRangeRepository.deleteAllRecords(!isMainTableActiveForWrite);
    }

    public void updateCardRanges() {
        byte[] zipBytes = downloadZipFile();
        processZipFile(zipBytes);
    }

    private byte[] downloadZipFile() {
        byte[] zipBytes = restTemplate.getForObject(URL, byte[].class);
        if (zipBytes == null) {
            throw new FileDownloadException("Failed to download file from URL " + URL);
        }
        return zipBytes;
    }

    private void processZipFile(byte[] zipBytes) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            if (entry == null || !entry.getName().endsWith(".json")) {
                throw new FileDownloadException("Expected JSON file in ZIP archive");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonParser jsonParser = objectMapper.getFactory().createParser(zipInputStream);


            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new FileDownloadException("Expected START_ARRAY token");
            }
            List<CardRange> allCardRanges = new ArrayList<>();

            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                CardRange cardRange = jsonParser.readValueAs(CardRange.class);
                if (cardRange != null) {
                    allCardRanges.add(cardRange);
                }
                if (allCardRanges.size() == 200) {
                    cardRangeRepository.saveCardInfo(allCardRanges, isMainTableActiveForWrite);
                    allCardRanges = new ArrayList<>();
                }
            }
            if (!allCardRanges.isEmpty()) {
                cardRangeRepository.saveCardInfo(allCardRanges, isMainTableActiveForWrite);
            }


        } catch (IOException e) {
            e.printStackTrace();
            throw new FileDownloadException("Error processing ZIP file", e);
        }
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        cardRangeRepository.deleteAllRecords(false);
        cardRangeRepository.deleteAllRecords(true);
        updateCardRanges();
        isDatabaseReady = true;
    }


}
