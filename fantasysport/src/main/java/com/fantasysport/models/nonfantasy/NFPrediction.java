package com.fantasysport.models.nonfantasy;

import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bylynka on 5/27/14.
 */
public class NFPrediction implements Serializable {

    public final String SUBMITTED = "submitted";
    public final String FINISHED = "finished";
    public final String CANCELED = "canceled";

    @SerializedName("id")
    private int _id = -1;

    @SerializedName("state")
    private String _state;

    @SerializedName("contest_rank")
    private Integer _rank;

    @SerializedName("score")
    private Double _points;

    @SerializedName("started_at")
    private String _date;

    @SerializedName("cancelled_at")
    private String _newDate;

    @SerializedName("contest_rank_payout")
    private Double _award;

    public String getNewDate(){
        return _newDate;
    }

    public Double getPoints(){
        return _points;
    }

    public Date getDate() {
        Date date = Converter.toDate(_date);
        int gmtInMinutes = DeviceInfo.getGMTInMinutes();
        return DateUtils.addMinutes(date, gmtInMinutes);
    }

    public Integer getRank(){
        return _rank;
    }

    public int getId(){
        return _id;
    }

    public State getState(){
        if(_state.equalsIgnoreCase(SUBMITTED)){
            return State.Submitted;
        }else if(_state.equalsIgnoreCase(CANCELED)){
            return State.Canceled;
        }else {
            return State.Finished;
        }
    }

    public boolean isEmpty(){
      return _id < 0;
    }

    public Double getAward() {
        return _award == null? 0: _award/100;
    }

    public enum State{
        Submitted,
        Finished,
        Canceled
    }
}
