package com.fantasysport.models;

import android.graphics.Bitmap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bylynka on 2/26/14.
 */
public class Player implements Serializable, IPlayer {

    @SerializedName("id")
    private int _id;

    @SerializedName("team")
    private String _team;

    @SerializedName("name")
    private String _name;

    @SerializedName("position")
    private String _position;

    @SerializedName("status")
    private String _status;

    @SerializedName("ppg")
    private double _ppg;

    @SerializedName("buy_price")
    private double _buyPrice;

    @SerializedName("sell_price")
    private double _sellPrice;

    @SerializedName("score")
    private double _score;

    @SerializedName("headshot_url")
    private String _imageUrl;

    @SerializedName("purchase_price")
    private double _purchasePrice;

    public double getPurchasePrice() {
        return _purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        _purchasePrice = purchasePrice;
    }

    public String getImageUrl() {
        return _imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        _imageUrl = imageUrl;
    }

    public double getScore() {
        return _score;
    }

    public void setScore(double score) {
        _score = score;
    }

    public double getSellPrice() {
        return _sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        _sellPrice = sellPrice;
    }

    public double getBuyPrice() {
        return _buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        _buyPrice = buyPrice;
    }

    public double getPPG() {
        return _ppg;
    }

    public void setPPG(double ppg) {
        _ppg = ppg;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    @Override
    public String getPosition() {
        return _position;
    }

    public void setPosition(String position) {
        _position = position;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getTeam() {
        return _team;
    }

    public void setTeam(String team) {
        _team = team;
    }

}
