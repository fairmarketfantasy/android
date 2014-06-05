package com.fantasysport.models.fwc;

/**
 * Created by bylynka on 6/4/14.
 */
public interface IFWCModel {

    String getName();
    double getPT();
    String getStatsId();
    boolean isPredicted();
    void setIsPredicted(boolean isPredicted);
}
