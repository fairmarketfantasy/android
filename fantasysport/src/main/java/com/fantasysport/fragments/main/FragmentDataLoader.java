package com.fantasysport.fragments.main;

import com.fantasysport.Const;
import com.fantasysport.models.UserData;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.nonfantasy.NFData;
import com.fantasysport.repo.Storage;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.responseListeners.GetFWCDataResponseListener;
import com.fantasysport.webaccess.responseListeners.GetNFGamesResponseListener;
import com.fantasysport.webaccess.responseListeners.MarketsResponseListener;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responses.MarketResponse;

/**
 * Created by bylynka on 6/9/14.
 */
public class FragmentDataLoader {

   public static void  load(WebProxy proxy, Storage storage, ILoadedDataListener listener){

       switch (storage.getCategoryType()){
           case Const.FANTASY_SPORT:
            loadFantasyData(proxy, storage, listener);
           break;
           case Const.NON_FANTASY_SPORT:
               loadNonFantasyData(proxy, storage, listener);
               break;
           case Const.FWC:
               loadFootballWorldCupData(proxy, storage, listener);
               break;
            default:
            RequestError error = new RequestError("Cannot load data");
             listener.onLoaded(error);
       }
   }

    private static void loadFantasyData(WebProxy proxy, final Storage storage, final ILoadedDataListener listener) {
        UserData data = storage.getUserData();
        String cat = data.getCategory();
        String sport = data.getSport();
        proxy.getMarkets(cat, sport, new MarketsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                listener.onLoaded(error);
            }

            @Override
            public void onRequestSuccess(MarketResponse response) {
                storage.setDefaultRosterData(response.getDefaultRosterData());
                storage.setMarketsContainer(response.getMarketsContainer());
                listener.onLoaded(null);
            }
        });
    }

    private static void loadNonFantasyData(WebProxy proxy, final Storage storage, final ILoadedDataListener listener) {
        String sport = storage.getUserData().getSport();
        proxy.getNFGames(sport, new GetNFGamesResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                listener.onLoaded(error);
            }

            @Override
            public void onRequestSuccess(NFData response) {
                storage.setNFData(response);
                listener.onLoaded(null);
            }
        });
    }

    private static void loadFootballWorldCupData(WebProxy proxy, final Storage storage, final ILoadedDataListener listener) {
        proxy.getFWCCategories(new GetFWCDataResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                listener.onLoaded(error);
            }

            @Override
            public void onRequestSuccess(FWCData data) {
                storage.setFWCData(data);
                listener.onLoaded(null);
            }
        });
    }

   public interface ILoadedDataListener{
        void onLoaded(RequestError error);
    }

}
