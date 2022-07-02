package fr.supavenir.lsts.tokens;

import android.util.Log;

import androidx.annotation.NonNull;

public class Token {

    private String name;
    private float actualPrice;
    private float lowerPrice;
    private float higherPrice;

    public Token(String name, float actualPrice, float lowerPrice, float higherPrice) {
        this.name = name;
        this.actualPrice = actualPrice;
        this.lowerPrice = lowerPrice;
        this.higherPrice = higherPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getActualPrice() {
        return actualPrice;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + " - " + this.actualPrice + " - " + this.lowerPrice + " - " + this.higherPrice;
    }

    public void setActualPrice(float actualPrice) {
        this.actualPrice = actualPrice;
    }

    public float getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(float lowerPrice) {
        if (lowerPrice < this.lowerPrice || this.lowerPrice == 0)
        {
            this.lowerPrice = lowerPrice;
        }
    }

    public float getHigherPrice() {
        return higherPrice;
    }

    public void setHigherPrice(float higherPrice) {
        if (higherPrice > this.higherPrice || this.higherPrice == 0)
        {
            this.higherPrice = higherPrice;
        }
    }
}
