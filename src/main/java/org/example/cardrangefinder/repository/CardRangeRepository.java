package org.example.cardrangefinder.repository;

import org.example.cardrangefinder.dto.CardInfoDto;
import org.example.cardrangefinder.entity.CardRange;

import java.math.BigDecimal;
import java.util.List;

public interface CardRangeRepository {
    List<CardInfoDto> findCardInfoByMinAndMaxPossibleCardNumber(BigDecimal minCardNumber, BigDecimal maxCardNumber,
                                                                boolean isShadow);

    boolean saveCardInfo(List<CardRange> cardInfoDetails, boolean isMain);

    void deleteAllRecords(boolean isMain);
}
