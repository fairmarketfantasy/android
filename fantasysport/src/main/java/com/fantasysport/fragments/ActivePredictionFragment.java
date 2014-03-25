package com.fantasysport.fragments;

import android.content.Intent;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.activities.MainPredictionActivity;
import com.fantasysport.activities.PredictionActivity;
import com.fantasysport.adapters.IndividualPredictionAdapter;
import com.fantasysport.adapters.PredictionAdapter;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.models.Market;
import com.fantasysport.models.Prediction;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.IndividualPredictionsResponseListener;
import com.fantasysport.webaccess.requestListeners.PredictionsResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;

import java.util.List;

/**
 * Created by bylynka on 3/21/14.
 */
public class ActivePredictionFragment extends BasePredictionFragment {

    public ActivePredictionFragment(WebProxy proxy) {
        super(proxy);
    }

    @Override
    public void onLoad(PredictionActivity.TimeType timeType, PredictionActivity.PredictionType predictionType) {
        if(timeType != PredictionActivity.TimeType.Active){
            return;
        }

        if(predictionType == PredictionActivity.PredictionType.Individual){
            loadIndividualPredictions();
        }else {
            loadPredictions();
        }
    }

    private void loadPredictions(){
        showProgress();
        _webProxy.getPredictions(new PredictionsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(List list) {
                dismissProgress();
                PredictionAdapter adapter = new PredictionAdapter(getActivity(), (List<Prediction>)list, getProhibitionRound());
                adapter.setOnShowRosterListener(ActivePredictionFragment.this);
                _predictionListView.setAdapter(adapter);
            }
        });
    }

    private void loadIndividualPredictions(){
        showProgress();
        _webProxy.getIndividualPredictions(new IndividualPredictionsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(List list) {
                dismissProgress();
                IndividualPredictionAdapter adapter = new IndividualPredictionAdapter(getActivity(), (List<IndividualPrediction>)list, getProhibitionRound());
                _predictionListView.setAdapter(adapter);
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
