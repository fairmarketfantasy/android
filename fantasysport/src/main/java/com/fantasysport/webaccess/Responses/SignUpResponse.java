package com.fantasysport.webaccess.Responses;

import com.fantasysport.models.CustomerObject;
import com.fantasysport.models.League;
import com.fantasysport.models.Roster;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 2/4/14.
 */
public class SignUpResponse extends BaseResponse {

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
    private Float _winPercentile;

    @SerializedName("total_points")
    private Float _totalPoints;

//    @SerializedName("joined_at")
//    private Date _joinedAt;

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
    private Integer _totalWins;

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

    public String getCurrentSport(){
        return _currentSport;
    }

}
