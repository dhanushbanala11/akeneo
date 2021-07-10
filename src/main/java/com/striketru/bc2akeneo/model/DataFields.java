/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.striketru.bc2akeneo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author sudhir
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataFields {
    String sku;
    String marketdescshort;
    String msrp;
    String width;
    String height;
    String weight;
    String depth;
    String seatheight;
    String armheight;
    String clearanceheight;

    public DataFields(String sku, String marketdescshort, String msrp, String width, String height, String weight, String depth, String seatheight, String armheight, String clearanceheight) {
        this.sku = sku;
        this.marketdescshort = marketdescshort;
        this.msrp = msrp;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.weight = weight;
        this.seatheight = seatheight;
        this.armheight = armheight;
        this.clearanceheight = clearanceheight;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getMarketdescshort() {
        return marketdescshort;
    }

    public void setMarketdescshort(String marketdescshort) {
        this.marketdescshort = marketdescshort;
    }

    public String getMsrp() {
        return msrp;
    }

    public void setMsrp(String msrp) {
        this.msrp = msrp;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getSeatheight() {
        return seatheight;
    }

    public void setSeatheight(String seatheight) {
        this.seatheight = seatheight;
    }

    public String getArmheight() {
        return armheight;
    }

    public void setArmheight(String armheight) {
        this.armheight = armheight;
    }

    public String getClearanceheight() {
        return clearanceheight;
    }

    public void setClearanceheight(String clearanceheight) {
        this.clearanceheight = clearanceheight;
    }

    @Override
    public String toString() {
        return "DataFields{" +
                "sku='" + sku + '\'' +
                ", marketdescshort='" + marketdescshort + '\'' +
                ", msrp='" + msrp + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", depth='" + depth + '\'' +
                ", seatheight='" + seatheight + '\'' +
                ", armheight='" + armheight + '\'' +
                ", clearanceheight='" + clearanceheight + '\'' +
                '}';
    }
}
