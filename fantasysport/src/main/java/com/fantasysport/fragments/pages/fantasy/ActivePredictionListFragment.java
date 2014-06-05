package com.fantasysport.fragments.pages.fantasy;

import android.content.Intent;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.activities.MainPredictionActivity;
import com.fantasysport.activities.PredictionsListActivity;
import com.fantasysport.adapters.fantasy.IndividualPredictionAdapter;
import com.fantasysport.adapters.fantasy.PredictionAdapter;
import com.fantasysport.fragments.pages.BasePredictionListFragment;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.models.Prediction;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.responseListeners.IndividualPredictionsResponseListener;
import com.fantasysport.webaccess.responseListeners.PredictionsResponseListener;
import com.fantasysport.webaccess.responseListeners.RequestError;

import java.util.List;

/**
 * Created by bylynka on 3/21/14.
 */
public class ActivePredictionListFragment extends BasePredictionListFragment implements PredictionAdapter.IOnShowRosterListener {

    @Override
    public void onLoadRequest(PredictionsListActivity.TimeType timeType, PredictionsListActivity.PredictionType predictionType, boolean showLoading) {
        if(timeType != PredictionsListActivity.TimeType.Active){
            return;
        }
        super.onLoadRequest(timeType, predictionType, showLoading);
        if(predictionType == PredictionsListActivity.PredictionType.Individual){
            loadIndividualPredictions();
        }else {
            loadPredictions();
        }
    }

    private void loadPredictions(){
        showProgress();
        UserData data = getStorage().getUserData();
        String cat = data.getCategory();
        String sport = data.getSport();
        getWebProxy().getPredictions(cat, sport, new PredictionsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
                setRefreshComplete();
            }

            @Override
            public void onRequestSuccess(List list) {
                dismissProgress();
                PredictionAdapter adapter = new PredictionAdapter(getActivity(), (List<Prediction>)list);
                adapter.setOnShowRosterListener(ActivePredictionListFragment.this);
                _predictionListView.setAdapter(adapter);
                setRefreshComplete();
            }
        });
    }

    private void loadIndividualPredictions(){
        UserData data = getStorage().getUserData();
        String cat = data.getCategory();
        String sport = data.getSport();
        showProgress();
        getWebProxy().getIndividualPredictions(cat, sport, new IndividualPredictionsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                setRefreshComplete();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(List list) {
                dismissProgress();
                IndividualPredictionAdapter adapter = new IndividualPredictionAdapter(getActivity(), (List<IndividualPrediction>)list);
                _predictionListView.setAdapter(adapter);
                setRefreshComplete();
            }
        });
    }

    @Override
    public void onShow(Prediction prediction){
        Intent intent = new Intent(getActivity(), MainPredictionActivity.class);
        intent.putExtra(Const.MARKET, prediction.getMarket());
        intent.putExtra(Const.PREDICTION, PredictionRoster.Active);
        intent.putExtra(Const.ROSTER_ID, prediction.getId());
      getPredictionActivity().startActivity(intent);
    }
}
