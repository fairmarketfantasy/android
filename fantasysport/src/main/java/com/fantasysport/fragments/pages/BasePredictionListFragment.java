package com.fantasysport.fragments.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.PredictionsListActivity;
import com.fantasysport.adapters.fantasy.PredictionAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by bylynka on 3/21/14.
 */
public abstract class BasePredictionListFragment extends BaseActivityFragment implements PredictionsListActivity.ILoadContentListener,
         OnRefreshListener {

    protected ListView _predictionListView;
    protected PullToRefreshLayout _pullToRefreshLayout;
    protected PredictionsListActivity.TimeType _timeType = PredictionsListActivity.TimeType.Active;
    protected PredictionsListActivity.PredictionType _predictionType =  PredictionsListActivity.PredictionType.Roster;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_prediction, container, false);
        _predictionListView = getViewById(R.id.prediction_list);
        _pullToRefreshLayout = getViewById(R.id.ptr_layout);
        getPredictionActivity().addLoadContentListener(this);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(_pullToRefreshLayout);
        ontCreatedView();
        onLoadRequest(_timeType, _predictionType, true);
        return _rootView;
    }

    protected void ontCreatedView(){

    }

    protected PredictionsListActivity getPredictionActivity(){
        return (PredictionsListActivity)getActivity();
    }

    @Override
    public void onRefreshStarted(View view) {
      onLoadRequest(_timeType, _predictionType, false);
    }

    @Override
    public void onLoadRequest(PredictionsListActivity.TimeType timeType, PredictionsListActivity.PredictionType predictionType, boolean showLoading) {
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
