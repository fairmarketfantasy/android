package com.fantasysport.models;

import android.net.Uri;
import android.support.v7.appcompat.R;
import com.fantasysport.Config;
import com.fantasysport.utility.Converter;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 2/13/14.
 */
public class UserData {

    @SerializedName("id")
    private int _id;

    @SerializedName("name")
    private String _realName;

    @SerializedName("username")
    private String _login;

    @SerializedName("email")
    private String _email;

    @SerializedName("admin")
    private boolean _admin;

    @SerializedName("balance")
    private int _balance;

    @SerializedName("image_url")
    private String _imageUrl;

    @SerializedName("win_percentile")
    private float _winPercentile;

    @SerializedName("total_points")
    private int _totalPoints;

    @SerializedName("joined_at")
    private String _joinedAt;

    @SerializedName("token_balance")
    private int _tokenBalance;

    @SerializedName("provider")
    private String _provider;

    @SerializedName("amount")
    private Float _amount;

    @SerializedName("bets")
    private Float _bets;
//
////
//////    bonuses:     {
//////    },

    @SerializedName("winnings")
    private Integer _winnings;

    @SerializedName("total_wins")
    private int _totalWins;

    @SerializedName("total_losses")
    private Integer _totalLosses;

    @SerializedName("currentSport")
    private String _currentSport;

    @SerializedName("referral_code")
    private String _referralCode;

    @SerializedName("invited_id")
    private Integer _invitedId;


    @SerializedName("confirmed")
    private boolean _confirmed;

    @SerializedName("in_progress_roster")
    private Roster _inProgressRoster;

    @SerializedName("leagues")
    private List<League> _leagues;

    @SerializedName("customer_object")
    private CustomerObject _customerObject;

    public String getCurrentSport() {
        return _currentSport;
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

    public int getTotalPoints() {
        return _totalPoints;
    }

    public int getTotalWins() {
        return _totalWins;
    }

    public float getWinPercentile() {
        return _winPercentile;
    }

    public String getRealName() {
        return _realName;
    }

    public int getBalance() {
        return _balance;
    }

}

