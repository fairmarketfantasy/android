package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/7/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerObject {

    @JsonProperty("contest_winnings_multiplier")
    private double _contestWinningsMultiplier;

    @JsonProperty("monthly_contest_entries")
    private int _monthlyContestEntries;

    @JsonProperty("net_monthly_winnings")
    private double _netMonthlyWinnings;

    @JsonProperty("monthly_award")
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
