package org.example.cardrangefinder.entity;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.cardrangefinder.utils.CardRangeDeserializer;

import java.math.BigDecimal;
import java.util.Objects;

@JsonDeserialize(using = CardRangeDeserializer.class)
public class CardRange {
    private Long id;
    private int bin;
    private BigDecimal minRange;
    private BigDecimal maxRange;
    private String alphaCode;
    private String bankName;

    public CardRange() {
    }

    public CardRange(int bin, BigDecimal minRange, BigDecimal maxRange, String alphaCode,
                     String bankName) {
        this.bin = bin;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.alphaCode = alphaCode;
        this.bankName = bankName;
    }

    public int getBin() {
        return bin;
    }

    public void setBin(int bin) {
        this.bin = bin;
    }

    public BigDecimal getMinRange() {
        return minRange;
    }

    public void setMinRange(BigDecimal minRange) {
        this.minRange = minRange;
    }

    public BigDecimal getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(BigDecimal maxRange) {
        this.maxRange = maxRange;
    }

    public String getAlphaCode() {
        return alphaCode;
    }

    public void setAlphaCode(String alphaCode) {
        this.alphaCode = alphaCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardRange cardRange = (CardRange) o;
        return Objects.equals(id, cardRange.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CardRange{" +
                "id=" + id +
                ", bin=" + bin +
                ", minRange=" + minRange +
                ", maxRange=" + maxRange +
                ", alphaCode='" + alphaCode + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
