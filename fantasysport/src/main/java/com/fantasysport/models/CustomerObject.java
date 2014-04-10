package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 2/7/14.
 */
public class CustomerObject {

//    @SerializedName("id")
//    private int _id;
//
//    @SerializedName("balance")
//    private int _balance;
//
//    @SerializedName("contest_entries_deficit")
//    private float _contestEntriesDeficit;

    @SerializedName("contest_winnings_multiplier")
    private double _contestWinningsMultiplier;

//    @SerializedName("has_agreed_terms")
//    private boolean _hasAgreedTerms;
//
//    @SerializedName("is_active")
//    private boolean _isActive;
//
//    @SerializedName("locked")
//    private boolean _isLocked;
//
//    @SerializedName("locked_reason")
//    private String _lockedReason;
//
    @SerializedName("monthly_contest_entries")
    private int _monthlyContestEntries;

    @SerializedName("net_monthly_winnings")
    private double _netMonthlyWinnings;

    @SerializedName("monthly_award")
    private double _monthlyAward;

//    @SerializedName("cards")
//    private List<Card> _cards;

    public double getFanBucks(){
        return _netMonthlyWinnings;
    }

    public double getWinningsMultiplier(){
        return _contestWinningsMultiplier;
    }

    public int getMonthlyPredictions(){
        return _monthlyContestEntries;
    }

    public double getMonthlyAward(){
        return _monthlyAward;
    }

}
