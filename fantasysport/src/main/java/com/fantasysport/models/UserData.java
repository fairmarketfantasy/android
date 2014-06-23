package com.fantasysport.models;

import com.fantasysport.Config;
import com.fantasysport.parsers.jackson.DateDeserializer;
import com.fantasysport.parsers.jackson.DateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 2/13/14.
 */
public class UserData {

    @JsonProperty("updated_at")
    private long _updatedAt;

    @JsonProperty("id")
    private int _id;

    @JsonProperty("name")
    private String _realName;

    @JsonProperty("username")
    private String _login;

    @JsonProperty("email")
    private String _email;

    @JsonProperty("prestige")
    private int _prestige;

    @JsonProperty("balance")
    private float _balance;

    @JsonProperty("image_url")
    private String _imageUrl;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("joined_at")
    private Date _joinedAt;

    @JsonProperty("token_balance")
    private int _tokenBalance;

    @JsonProperty("provider")
    private String _provider;

    @JsonProperty("amount")
    private Float _amount;

    @JsonProperty("winnings")
    private Integer _winnings;

    @JsonProperty("total_wins")
    private int _totalWins;

    @JsonProperty("in_progress_roster_id")
    private int _inProgressRosterId;

    @JsonProperty("currentSport")
    private String _currentSport;

    @JsonProperty("currentCategory")
    private String _currentCategory;


    @JsonProperty("categories")
    private List<Category> _categories;

    @JsonProperty("customer_object")
    private CustomerObject _customerObject;

    public String getSport(){
        return _currentSport;
//        return "MLB";
    }

    public String getCategory(){
        return  _currentCategory != null? _currentCategory:"sports";
    }

    public void setCurrentCategory(String category){
        _currentCategory = category;
    }

    public void setCurrentSport(String sport){
        _currentSport = sport;
    }

    public List<Category> getCategories() {
        return _categories;
    }

    public String getUserImageUrl() {
        if (_imageUrl != null && !_imageUrl.toLowerCase().contains("http")) {
            String imgUrl = Config.SERVER + (_imageUrl.charAt(0) == '/' ? _imageUrl.substring(1) : _imageUrl);
            return imgUrl;
        }
        return _imageUrl;
    }

    public Date getRegistrationDate() {
        return _joinedAt;
    }

    public int getInProgressRosterId() {
        return _inProgressRosterId;
    }

    public int getTotalWins() {
        return _totalWins;
    }

    public String getRealName() {
        return _realName;
    }

    public float getBalance() {
        return _balance;
    }

    public String getEmail() {
        return _email;
    }

    public int getId() {
        return _id;
    }

    public int getPrestige() {
        return _prestige;
    }

    public double getFanBucks() {
        return _customerObject.getFanBucks();
    }

    public double getWinningsMultiplier() {
        return _customerObject.getWinningsMultiplier();
    }

    public int getMonthlyPredictions() {
        return _customerObject.getMonthlyPredictions();
    }

    public double getMonthlyAward() {
        return _customerObject.getMonthlyAward();
    }

    public void setUpdatedAt(long updatedAt){
        _updatedAt = updatedAt;
    }

    public long getUpdatedAt(){
        return _updatedAt;
    }
}

