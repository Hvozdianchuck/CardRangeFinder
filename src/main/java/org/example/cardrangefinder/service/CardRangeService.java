package org.example.cardrangefinder.service;

import org.example.cardrangefinder.dto.CardInfoDto;

public interface CardRangeService {
    CardInfoDto findCardInfoByCardNumber(String cardNumber);
}
