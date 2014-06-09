package com.fantasysport.fragments.main.fantasy;

import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.fragments.pages.IMediator;
import com.fantasysport.models.Market;
import com.fantasysport.models.UserData;
import com.fantasysport.repo.Storage;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.responseListeners.MarketsResponseListener;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responses.MarketResponse;
import com.octo.android.robospice.SpiceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/14/14.
 */
public class FantasyFragment extends BaseFantasyFragment
        implements IMainFragment {

    protected List<IOnMarketsListener> _marketListeners = new ArrayList<IOnMarketsListener>();

    public void addOnMarketsListener(IOnMarketsListener listener) {
        _marketListeners.add(listener);
    }

    private void raiseOnMarketListener(List<Market> markets) {
        for (IOnMarketsListener listener : _marketListeners) {
            listener.onMarkets(markets);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        long nowTime = DateUtils.getCurrentDate().getTime();
        if(getStorage().getMarketsContainer() == null){
            return;
        }
        long marketsTime = getStorage().getMarketsContainer().getUpdatedAt();
        long deltaTime = nowTime - marketsTime;
        long deltaTimeInMin = deltaTime / 60000;
        boolean isTimeChanged = CacheProvider.getBoolean(getActivity(), Const.TIME_ZONE_CHANGED);
        CacheProvider.putBoolean(getActivity(), Const.TIME_ZONE_CHANGED ,false);
        if (deltaTimeInMin > 35 || isTimeChanged) {
            updateMainData(isTimeChanged);
        }
    }

    private boolean marketChanged(List<Market> newMarkets, List<Market> oldMarkets) {
        if (newMarkets == null && oldMarkets != null ||
                newMarkets != null && oldMarkets == null) {
            return true;
        }
        if (newMarkets == null && oldMarkets == null) {
            return false;
        }
        if (newMarkets.size() != oldMarkets.size()) {
            return true;
        }
        int currentGmt = DeviceInfo.getGMTInMinutes();
        int gmt = CacheProvider.getInt(getActivity(), Const.GMT_IN_MINUTES);
        if(gmt != -1 && currentGmt != gmt){
            return true;
        }
        for (Market newMarket : newMarkets) {
            boolean has = false;
            for (Market oldMarket : oldMarkets) {
                if (oldMarket.getId() == newMarket.getId()) {
                    has = true;
                }
            }
            if (!has) {
                return true;
            }
        }
        return false;
    }

    public void updateMarkets(final boolean isTimeChanged) {
        UserData data = getStorage().getUserData();
        String cat = data.getCategory();
        String sport = data.getSport();
        showProgress();
        getWebProxy().getMarkets(cat, sport, new MarketsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(MarketResponse response) {
                getStorage().setDefaultRosterData(response.getDefaultRosterData());
                getStorage().setMarketsContainer(response.getMarketsContainer());
                List<Market> tmpMarkets = getStorage().getMarkets();
                if (isTimeChanged || marketChanged(tmpMarkets, _markets)) {
                    _markets = tmpMarkets;
                    raiseOnMarketListener(_markets);
                }
                dismissProgress();
            }
        });
    }

    private void updateMainData(boolean isTimeChanged) {
        updateMarkets(isTimeChanged);
    }

    @Override
    public void updateData() {
        updateMainData(true);
    }

    @Override
    public IMediator getMediator() {
        return null;
    }

    public interface IOnMarketsListener {
        public void onMarkets(List<Market> markets);
    }
}
