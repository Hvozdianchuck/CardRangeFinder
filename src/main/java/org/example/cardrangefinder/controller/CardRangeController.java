package org.example.cardrangefinder.controller;

import org.example.cardrangefinder.dto.CardInfoDto;
import org.example.cardrangefinder.dto.CardRequest;
import org.example.cardrangefinder.service.CardRangeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart-ranges")
public class CardRangeController {
    private CardRangeService cardRangeService;

    public CardRangeController(CardRangeService cardRangeService) {
        this.cardRangeService = cardRangeService;
    }

    @PostMapping
    CardInfoDto findCardInfoByCardNumber(@RequestBody CardRequest cardRequest) {
        return cardRangeService.findCardInfoByCardNumber(cardRequest.card());
    }

}
