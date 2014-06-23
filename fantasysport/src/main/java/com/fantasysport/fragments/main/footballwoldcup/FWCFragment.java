package com.fantasysport.fragments.main.footballwoldcup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.footballwoldcup.FWCPagerAdapter;
import com.fantasysport.fragments.main.BaseFragment;
import com.fantasysport.fragments.pages.nonfantasy.footballwoldcup.FWCMediator;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.fwc.IFWCModel;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.views.ConfirmDialog;
import com.fantasysport.webaccess.responseListeners.GetFWCDataResponseListener;
import com.fantasysport.webaccess.responseListeners.MessageResponseListener;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responses.MsgResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by bylynka on 6/2/14.
 */
public class FWCFragment extends BaseFragment implements FWCMediator.ISubmittingPredictionListener,
            FWCMediator.IUpdatingDataListener{

    private FWCPagerAdapter _adapter;
    private FWCMediator _mediator = new FWCMediator();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setPageAmount(0);
        _mediator.addSubmittingPrediction(this);
        _mediator.addUpdatingDataListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initStartParams(Bundle savedInstanceState) {

    }

    @Override
    protected void setPager() {
        super.setPager();
        _adapter = new FWCPagerAdapter(getActivity().getSupportFragmentManager());
        _pager.setAdapter(_adapter);
        setPagerData(getStorage().getFWCData());
    }

    private void setPagerData(FWCData data){
        if(data == null){
            return;
        }
        _adapter.setFWCData(data);
        setPageAmount(_adapter.getCount());
        raiseOnPageChanged(0);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void updateData() {
        showProgress();
        getWebProxy().getFWCCategories(_fwcDataResponseListener);
    }

    @Override
    public FWCMediator getMediator() {
        return _mediator;
    }

    GetFWCDataResponseListener _fwcDataResponseListener =  new GetFWCDataResponseListener() {
        @Override
        public void onRequestError(RequestError message) {
            dismissProgress();
        }

        @Override
        public void onRequestSuccess(FWCData data) {
           getStorage().setFWCData(data);
           setPagerData(data);
           dismissProgress();
        }
    };

    private void saveDataToCache(final FWCData data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    String dataStr = data != null ? mapper.writeValueAsString(data) : null;
                    CacheProvider.putString(getActivity(), Const.FWC_DATA, dataStr);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void submitPrediction(final IFWCModel predictableItem, final String predictionType, final String gameStatsId){
        showProgress();
        getWebProxy().submitFWCPrediction(predictionType, predictableItem.getStatsId(), gameStatsId, new MessageResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
                predictableItem.setIsPredicted(false);
                _mediator.submittedIndividualPrediction(FWCFragment.this, predictableItem, predictionType, gameStatsId);
            }

            @Override
            public void onRequestSuccess(MsgResponse msgResponse) {
                dismissProgress();
                showAlert("INFO", msgResponse.getMessage());
            }
        });
    }


    @Override
    public void onSubmittingPrediction(Object sender, final IFWCModel predictableItem, final String predictionType, final String gameStatsId) {
        ConfirmDialog dialog = new ConfirmDialog(getActivity());
        dialog.setTitle(String.format("PT%.0f", predictableItem.getPT()))
                .setContent(String.format("Predict %s?", predictableItem.getName()))
                .setOkAction(new Runnable() {
                    @Override
                    public void run() {
                        predictableItem.setIsPredicted(true);
                        submitPrediction(predictableItem, predictionType, gameStatsId);
                        _mediator.submittedIndividualPrediction(FWCFragment.this, predictableItem, predictionType, gameStatsId);
                    }
                })
                .show();
    }

    @Override
    public void onUpdatingData(Object sender, FWCData data) {
        saveDataToCache(data);
    }
}
