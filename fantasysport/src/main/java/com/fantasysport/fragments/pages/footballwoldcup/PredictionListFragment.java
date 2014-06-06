package com.fantasysport.fragments.pages.footballwoldcup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.activities.PredictionsListActivity;
import com.fantasysport.adapters.ILoadListener;
import com.fantasysport.adapters.fantasy.IndividualPredictionAdapter;
import com.fantasysport.fragments.pages.BasePredictionListFragment;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.responseListeners.ListResponseListener;
import com.fantasysport.webaccess.responseListeners.RequestError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 6/5/14.
 */
public class PredictionListFragment extends BasePredictionListFragment
        implements ILoadListener {

    private int _currentPage;
    private List<IndividualPrediction> _individualPredictions;
    private IndividualPredictionAdapter _individualPredictionAdapter;

    private boolean _isHistory = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        _individualPredictions = null;
        loadIndividualPredictions(showLoadPopup);

    }

    private void loadIndividualPredictions(boolean showLoadPopup){
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
                    _individualPredictionAdapter.setLoadListener(PredictionListFragment.this);
                }
                _individualPredictions.addAll(predictions);
                _individualPredictionAdapter.setItems(_individualPredictions);
                _individualPredictionAdapter.notifyDataSetChanged();
                setRefreshComplete();
            }
        });
    }

    @Override
    public void onLoad() {
       loadIndividualPredictions(false);
    }
}
