package com.fantasysport.fragments.pages.nonfantasy;

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
import com.fantasysport.adapters.nonfantasy.NFPredictionAdapter;
import com.fantasysport.fragments.pages.BasePredictionListFragment;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.models.UserData;
import com.fantasysport.models.nonfantasy.NFPrediction;
import com.fantasysport.webaccess.responseListeners.ListResponseListener;
import com.fantasysport.webaccess.responseListeners.RequestError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/26/14.
 */
public class NFPredictionListFragment extends BasePredictionListFragment
        implements NFPredictionAdapter.IOnShowRosterListener, ILoadListener {

    private int _currentPage;
    private List<IndividualPrediction> _individualPredictions;
    private List<NFPrediction> _predictions;
    private NFPredictionAdapter _predictionAdapter;
    private IndividualPredictionAdapter _individualPredictionAdapter;

    private boolean _isHistory = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _predictionAdapter = new NFPredictionAdapter(getActivity());
        _predictionAdapter.setOnShowRosterListener(this);
        _individualPredictionAdapter = new IndividualPredictionAdapter(getActivity(), null);
        _isHistory = getArguments().getBoolean(Const.IS_HISTORY);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onLoadRequest(PredictionsListActivity.TimeType timeType, PredictionsListActivity.PredictionType predictionType, boolean showLoadPopup) {
        if(_isHistory && timeType == PredictionsListActivity.TimeType.Active ||
           !_isHistory && timeType == PredictionsListActivity.TimeType.History){
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
            _predictions = new ArrayList<NFPrediction>();
            _predictionListView.setAdapter(_predictionAdapter);
        }
        _currentPage++;
        if(showLoadPopup){
            showProgress();
        }
        UserData data = getStorage().getUserData();
        String cat = data.getCategory();
        String sport = data.getSport();
        getWebProxy().getNFPredictions(cat, sport,_currentPage, _isHistory, new ListResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                setRefreshComplete();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(List list) {
                dismissProgress();
                List<NFPrediction> predictions = (List<NFPrediction>) list;
                setRefreshComplete();
                if (_predictions == null) {
                    _predictions = new ArrayList<NFPrediction>();
                }else if (_predictions.size() > 0 && _predictions.get(_predictions.size() - 1).isEmpty()) {
                    _predictions.remove(_predictions.size() - 1);
                }
                if (predictions == null) {
                    _predictionAdapter.notifyDataSetChanged();
                    return;
                }
                if (predictions.size() >= 25) {
                    predictions.add(new NFPrediction());
                    _predictionAdapter.setLoadListener(NFPredictionListFragment.this);
                }
                _predictions.addAll(predictions);
                _predictionAdapter.setPredictions(_predictions);
                _predictionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadIndividualPredictions(boolean showLoadPopup){
        _predictions = null;
        if(_individualPredictions == null){
            _individualPredictions = new ArrayList<IndividualPrediction>();
            _predictionListView.setAdapter(_individualPredictionAdapter);
        }
        _currentPage++;
        if(showLoadPopup){
            showProgress();
        }
        UserData data = getStorage().getUserData();
        String cat = data.getCategory();
        String sport = data.getSport();
        getWebProxy().getNFIndividualPredictions(cat, sport, _currentPage, _isHistory, new ListResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                setRefreshComplete();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(List list) {
                dismissProgress();
                List<IndividualPrediction> predictions = (List<IndividualPrediction>) list;
                if (_individualPredictions.size() > 0 && _individualPredictions.get(_individualPredictions.size() - 1).isEmpty()) {
                    _individualPredictions.remove(_individualPredictions.size() - 1);
                }
                if (predictions == null) {
                    _individualPredictionAdapter.notifyDataSetChanged();
                    return;
                }
                if (predictions.size() >= 25) {
                    IndividualPrediction prediction = new IndividualPrediction();
                    prediction.setIsEmpty(true);
                    predictions.add(prediction);
                    _individualPredictionAdapter.setLoadListener(NFPredictionListFragment.this);
                }
                _individualPredictions.addAll(predictions);
                _individualPredictionAdapter.setItems(_individualPredictions);
                _individualPredictionAdapter.notifyDataSetChanged();
                setRefreshComplete();
            }
        });
    }

    @Override
    public void onShow(NFPrediction prediction) {
        Intent intent = new Intent(getActivity(), MainPredictionActivity.class);
        intent.putExtra(Const.ROSTER_ID, prediction.getId());
        intent.putExtra(Const.CATEGORY_TYPE, Const.NON_FANTASY_SPORT);
        intent.putExtra(Const.CAN_EDIT, prediction.getState() == NFPrediction.State.Submitted);
        getActivity().startActivity(intent);
    }

    @Override
    public void onLoad() {
        if(_predictionType == PredictionsListActivity.PredictionType.Individual){
            loadIndividualPredictions(false);
        }else {
            loadPredictions(false);
        }
    }
}
