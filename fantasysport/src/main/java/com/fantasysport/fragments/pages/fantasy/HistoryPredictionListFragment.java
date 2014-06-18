package com.fantasysport.fragments.pages.fantasy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.activities.MainPredictionActivity;
import com.fantasysport.activities.PredictionsListActivity;
import com.fantasysport.adapters.ILoadListener;
import com.fantasysport.adapters.fantasy.IndividualPredictionAdapter;
import com.fantasysport.adapters.fantasy.PredictionAdapter;
import com.fantasysport.fragments.pages.BasePredictionListFragment;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.models.Prediction;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.responseListeners.IndividualPredictionsResponseListener;
import com.fantasysport.webaccess.responseListeners.PredictionsResponseListener;
import com.fantasysport.webaccess.responseListeners.RequestError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bylynka on 3/21/14.
 */
public class HistoryPredictionListFragment extends BasePredictionListFragment implements PredictionAdapter.IOnShowRosterListener {

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
    public void onLoadRequest(PredictionsListActivity.TimeType timeType, PredictionsListActivity.PredictionType predictionType, boolean showLoadPopup) {
        if(timeType != PredictionsListActivity.TimeType.History){
            return;
        }
        super.onLoadRequest(timeType, predictionType, showLoadPopup);
        _currentPage = 0;
        _predictions = null;
        _individualPredictions = null;
        if(predictionType == PredictionsListActivity.PredictionType.Individual){
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
        String cat = data.getCategory();
        String sport = data.getSport();
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
        String cat = data.getCategory();
        String sport = data.getSport();
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
                Collections.sort(predictions, HistoryPredictionListFragment.this);
                if(predictions.size() >= 25){
                    IndividualPrediction prediction = new IndividualPrediction();
                    prediction.setIsEmpty(true);
                    predictions.add(prediction);
                    _individualPredictionAdapter.setLoadListener(_individualPredictionOnLoadListener);
                }
                _individualPredictions.addAll(predictions);
                _individualPredictionAdapter.setItems(_individualPredictions);
                _individualPredictionAdapter.notifyDataSetChanged();
                setRefreshComplete();
            }
        }, _currentPage);
    }

    ILoadListener _individualPredictionOnLoadListener = new ILoadListener() {
        @Override
        public void onLoad() {
            _individualPredictionAdapter.setLoadListener(null);
            loadIndividualPredictions(false);
        }
    };

    ILoadListener _predictionOnLoadListener = new ILoadListener() {
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
        intent.putExtra(Const.CATEGORY_TYPE, Const.FANTASY_SPORT);
        getPredictionActivity().startActivity(intent);
    }
}
