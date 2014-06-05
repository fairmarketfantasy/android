package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 2/7/14.
 */
public class CustomerObject {

    @SerializedName("contest_winnings_multiplier")
    private double _contestWinningsMultiplier;

    @SerializedName("monthly_contest_entries")
    private int _monthlyContestEntries;

    @SerializedName("net_monthly_winnings")
    private double _netMonthlyWinnings;

    @SerializedName("monthly_award")
    private double _monthlyAward;

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
