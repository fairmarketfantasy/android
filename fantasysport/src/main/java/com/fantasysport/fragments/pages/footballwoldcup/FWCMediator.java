package com.fantasysport.fragments.pages.footballwoldcup;

import com.fantasysport.fragments.pages.IMediator;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.fwc.IFWCModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 6/4/14.
 */
public class FWCMediator implements IMediator {

    private List<ISubmittingPredictionListener> _submittingPredictionListeners = new ArrayList<ISubmittingPredictionListener>();
    private List<ISubmittedPredictionListener> _submittedPredictionListeners = new ArrayList<ISubmittedPredictionListener>();
    private List<IUpdatingDataListener> _updatingDataListeners = new ArrayList<IUpdatingDataListener>();

    public void addUpdatingDataListener(IUpdatingDataListener listener){
        _updatingDataListeners.add(listener);
    }

    public void addSubmittedPrediction(ISubmittedPredictionListener listener){
        _submittedPredictionListeners.add(listener);
    }

    public void addSubmittingPrediction(ISubmittingPredictionListener listener){
        _submittingPredictionListeners.add(listener);
    }

    public void submittingPrediction(Object sender, IFWCModel predictableItem, String predictionType, String gameStatsId){
        for (ISubmittingPredictionListener listener : _submittingPredictionListeners){
            listener.onSubmittingPrediction(sender, predictableItem, predictionType, gameStatsId);
        }
    }

    public void submittedIndividualPrediction(Object sender,IFWCModel predictableItem, String predictionType, String gameStatsId){
        for (ISubmittedPredictionListener listener : _submittedPredictionListeners){
            listener.onSubmittedPrediction(sender, predictableItem, predictionType, gameStatsId);
        }
    }

    public void updatingData(Object sender, FWCData data){
        for(IUpdatingDataListener listener : _updatingDataListeners){
            listener.onUpdatingData(sender, data);
        }
    }


    public interface ISubmittingPredictionListener {
        void onSubmittingPrediction(Object sender, IFWCModel predictableItem, String predictionType, String gameStatsId);
    }

    public interface ISubmittedPredictionListener {
        void onSubmittedPrediction(Object sender, IFWCModel predictableItem, String predictionType, String gameStatsId);
    }

    public interface IUpdatingDataListener{
        void onUpdatingData(Object sender, FWCData data);
    }
}
