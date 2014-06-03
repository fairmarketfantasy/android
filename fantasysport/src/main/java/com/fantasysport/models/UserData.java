package com.fantasysport.models;

import com.fantasysport.Config;
import com.fantasysport.utility.Converter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 2/13/14.
 */
public class UserData {

    @SerializedName("updated_at")
    private long _updatedAt;

    @SerializedName("id")
    private int _id;

    @SerializedName("name")
    private String _realName;

    @SerializedName("username")
    private String _login;

    @SerializedName("email")
    private String _email;

    @SerializedName("prestige")
    private int _prestige;

    @SerializedName("balance")
    private float _balance;

    @SerializedName("image_url")
    private String _imageUrl;

    @SerializedName("joined_at")
    private String _joinedAt;

    @SerializedName("token_balance")
    private int _tokenBalance;

    @SerializedName("provider")
    private String _provider;

    @SerializedName("amount")
    private Float _amount;

//    @SerializedName("bets")
//    private Float _bets;

    @SerializedName("winnings")
    private Integer _winnings;

    @SerializedName("total_wins")
    private int _totalWins;

    @SerializedName("in_progress_roster_id")
    private int _inProgressRosterId;

    @SerializedName("currentSport")
    private String _currentSport;

    @SerializedName("currentCategory")
    private String _currentCategory;


    @SerializedName("categories")
    private List<Category> _categories;

    @SerializedName("customer_object")
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

    public Date getRegistrationdDate() {
        return Converter.toDate(_joinedAt);
    }

//    public Roster getProgressRoster(){
//        return _inProgressRoster;
//    }

    public int getInProgressRosterId() {
        return _inProgressRosterId;
    }

//    public int getTotalPoints() {
//        return _totalPoints;
//    }

    public int getTotalWins() {
        return _totalWins;
    }

//    public float getWinPercentile() {
//        return _winPercentile;
//    }


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

