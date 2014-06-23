package com.fantasysport.models.fantasy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by bylynka on 2/26/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player implements Serializable, IPlayer {

    @JsonProperty("id")
    private int _id;

    @JsonProperty("team")
    private String _team;

    @JsonProperty("name")
    private String _name;

    @JsonProperty("position")
    private String _position;

    @JsonProperty("status")
    private String _status;

    @JsonProperty("ppg")
    private double _ppg;

    @JsonProperty("buy_price")
    private double _buyPrice;

    @JsonProperty("sell_price")
    private double _sellPrice;

    @JsonProperty("score")
    private double _score;

    @JsonProperty("headshot_url")
    private String _imageUrl;

    @JsonProperty("purchase_price")
    private double _purchasePrice;

    @JsonProperty("stats_id")
    private String _statsId;

    @JsonProperty("benched")
    private boolean _isBenched;

    public boolean getIsBenched(){
        return _isBenched;
    }

    public void setIsBenched(boolean isBenched){
        _isBenched = isBenched;
    }

    public String getStatsId(){
        return _statsId;
    }

    public void setStatsId(String id){
        _statsId = id;
    }

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
