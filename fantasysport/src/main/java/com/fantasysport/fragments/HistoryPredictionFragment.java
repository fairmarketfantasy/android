package com.fantasysport.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.activities.MainPredictionActivity;
import com.fantasysport.activities.PredictionActivity;
import com.fantasysport.adapters.IndividualPredictionAdapter;
import com.fantasysport.adapters.PredictionAdapter;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.models.Prediction;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.requestListeners.IndividualPredictionsResponseListener;
import com.fantasysport.webaccess.requestListeners.PredictionsResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/21/14.
 */
public class HistoryPredictionFragment extends BasePredictionFragment {

    private int _currentPage = 1;
    private List<IndividualPrediction> _individualPredictions;
    private List<Prediction> _predictions;
    private PredictionAdapter _predictionAdapter;
    private IndividualPredictionAdapter _individualPredictionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _predictionAdapter = new PredictionAdapter(getActivity(), null);
        _predictionAdapter.setOnShowRosterListener(this);
        _individualPredictionAdapter = new IndividualPredictionAdapter(getActivity(), null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onLoad(PredictionActivity.TimeType timeType, PredictionActivity.PredictionType predictionType, boolean showLoadPopup) {
        if(timeType != PredictionActivity.TimeType.History){
            return;
        }
        super.onLoad(timeType, predictionType, showLoadPopup);
        _currentPage = 0;
        _predictions = null;
        _individualPredictions = null;
        if(predictionType == PredictionActivity.PredictionType.Individual){
            loadIndividualPredictions(showLoadPopup);
        }else {
            loadPredictions(showLoadPopup);
        }
    }

    private void loadPredictions(boolean showLoadPopup){
        _individualPredictions = null;
        if(_predictions == null){
            _predictions = new ArrayList<Prediction>();
            _currentPage = 1;
            _predictionListView.setAdapter(_predictionAdapter);
        }else {
            _currentPage++;
        }
        if(showLoadPopup){
            showProgress();
        }
        UserData data = getStorage().getUserData();
        String cat = data.getCurrentCategory();
        String sport = data.getCurrentSport();
        getWebProxy().getPredictions(cat, sport, new PredictionsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                setRefreshComplete();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(List list) {
                dismissProgress();
                List<Prediction> predictions = (List<Prediction>)list;
                setRefreshComplete();
                if(_predictions == null){
                    _predictions = new ArrayList<Prediction>();
                }
                if(_predictions.size() > 0 && _predictions.get(_predictions.size() - 1).isEmpty()){
                    _predictions.remove(_predictions.size() - 1);
                }
                if(predictions == null){
                    _predictionAdapter.notifyDataSetChanged();
                    return;
                }
                if(predictions.size() >= 25){
                    predictions.add(new Prediction());
                    _predictionAdapter.setLoadListener(_predictionOnLoadListener);
                }
                _predictions.addAll(predictions);
                _predictionAdapter.setItems(_predictions);
                _predictionAdapter.notifyDataSetChanged();

            }
        }, _currentPage);
    }

    private void loadIndividualPredictions(boolean showLoadPopup){
        _predictions = null;
        if(_individualPredictions == null){
            _individualPredictions = new ArrayList<IndividualPrediction>();
            _currentPage = 1;
            _predictionListView.setAdapter(_individualPredictionAdapter);
        }else {
            _currentPage++;
        }
        if(showLoadPopup){
            showProgress();
        }
        UserData data = getStorage().getUserData();
        String cat = data.getCurrentCategory();
        String sport = data.getCurrentSport();
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
                List<IndividualPrediction> predictions = (List<IndividualPrediction>)list;
                if(_individualPredictions.size() > 0 && _individualPredictions.get(_individualPredictions.size() - 1).isEmpty()){
                    _individualPredictions.remove(_individualPredictions.size() - 1);
                }
                if(predictions == null){
                    _individualPredictionAdapter.notifyDataSetChanged();
                    return;
                }
                if(predictions.size() >= 25){
                    predictions.add(new IndividualPrediction());
                    _individualPredictionAdapter.setLoadListener(_individualPredictionOnLoadListener);
                }
                _individualPredictions.addAll(predictions);
                _individualPredictionAdapter.setItems(_individualPredictions);
                _individualPredictionAdapter.notifyDataSetChanged();
                setRefreshComplete();
            }
        }, _currentPage);
    }

    IndividualPredictionAdapter.ILoadListener _individualPredictionOnLoadListener = new IndividualPredictionAdapter.ILoadListener() {
        @Override
        public void onLoad() {
            _individualPredictionAdapter.setLoadListener(null);
            loadIndividualPredictions(false);
        }
    };

    PredictionAdapter.ILoadListener _predictionOnLoadListener = new PredictionAdapter.ILoadListener() {
        @Override
        public void onLoad() {
            _predictionAdapter.setLoadListener(null);
            loadPredictions(false);
        }
    };

    @Override
    public void onShow(Prediction prediction) {
        Intent intent = new Intent(getActivity(), MainPredictionActivity.class);
        intent.putExtra(Const.MARKET, prediction.getMarket());
        intent.putExtra(Const.PREDICTION, PredictionRoster.History);
        intent.putExtra(Const.ROSTER_ID, prediction.getId());
        getPredictionActivity().startActivity(intent);
    }
}
