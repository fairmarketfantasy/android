package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.PredictionActivity;
import com.fantasysport.adapters.IndividualPredictionAdapter;
import com.fantasysport.adapters.PredictionAdapter;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.models.Prediction;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.IndividualPredictionsResponseListener;
import com.fantasysport.webaccess.requestListeners.PredictionsResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;

import java.util.List;

/**
 * Created by bylynka on 3/21/14.
 */
public abstract class BasePredictionFragment extends BaseActivityFragment implements PredictionActivity.ILoadContentListener,
        PredictionAdapter.IOnShowRosterListener {

    protected ListView _predictionListView;

    public BasePredictionFragment(WebProxy proxy){
        super(proxy);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        _predictionListView = getViewById(R.id.prediction_list);
        getPredictionActivity().addLoadContentListener(this);
        return _rootView;
    }

    protected PredictionActivity getPredictionActivity(){
        return (PredictionActivity)getActivity();
    }

}
