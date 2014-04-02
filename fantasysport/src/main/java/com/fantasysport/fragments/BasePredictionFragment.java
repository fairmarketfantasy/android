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
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.List;

/**
 * Created by bylynka on 3/21/14.
 */
public abstract class BasePredictionFragment extends BaseActivityFragment implements PredictionActivity.ILoadContentListener,
        PredictionAdapter.IOnShowRosterListener, OnRefreshListener {

    protected ListView _predictionListView;
    protected PullToRefreshLayout _pullToRefreshLayout;
    protected PredictionActivity.TimeType _timeType;
    protected PredictionActivity.PredictionType _predictionType;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        _predictionListView = getViewById(R.id.prediction_list);
        _pullToRefreshLayout = getViewById(R.id.ptr_layout);
        getPredictionActivity().addLoadContentListener(this);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(_pullToRefreshLayout);
        return _rootView;
    }

    protected PredictionActivity getPredictionActivity(){
        return (PredictionActivity)getActivity();
    }

    @Override
    public void onRefreshStarted(View view) {
      onLoad(_timeType, _predictionType, false);
    }

    @Override
    public void onLoad(PredictionActivity.TimeType timeType, PredictionActivity.PredictionType predictionType, boolean showLoading) {
        _timeType = timeType;
        _predictionType = predictionType;
    }

    protected void setRefreshComplete(){
        if(!_pullToRefreshLayout.isRefreshing()){
            return;
        }
        _pullToRefreshLayout.setRefreshComplete();
    }

}
