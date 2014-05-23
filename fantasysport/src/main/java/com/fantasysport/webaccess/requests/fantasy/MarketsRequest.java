package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.models.Market;
import com.fantasysport.models.MarketsContainer;
import com.fantasysport.parsers.MarketParser;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fantasysport.webaccess.responses.MarketResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 2/19/14.
 */
public class MarketsRequest extends BaseRequest<MarketResponse> {

    public static final String REGULAR_SEASON = "regular_season";
    public static final String SINGLE_ELIMINATION = "single_elimination";
    public String _sport;
    public String _category;

    public MarketsRequest(String category, String sport) {
        super(MarketResponse.class);
        _sport = sport;
        _category = category;
    }

    @Override
    public MarketResponse loadDataFromNetwork() throws Exception {
        List<Market> _markets = new ArrayList<Market>();
        List<Market> regularSeasonMarkets = loadMarkets(REGULAR_SEASON);
        if(regularSeasonMarkets != null){
            _markets.addAll(regularSeasonMarkets);
        }
        List<Market> singleEliminationMarkets = loadMarkets(SINGLE_ELIMINATION);
        if(singleEliminationMarkets != null){
            _markets.addAll(singleEliminationMarkets);
        }
        DefaultRosterData rosterData = loadDefaultRosterData();
        _rHelper.loadDefaultRosterData(rosterData);
        MarketsContainer container = new MarketsContainer();
        container.setMarkets(_markets);
        container.setUpdatedAt(DateUtils.getCurrentDate().getTime());
        MarketResponse marketResponse = new MarketResponse(container, rosterData);
        _rHelper.loadMarkets(container);
        return marketResponse;
    }

    private List<Market> loadMarkets(String gameType) throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("markets")
                .appendQueryParameter("sport", _sport)
                .appendQueryParameter("category", _category)
                .appendQueryParameter("type", gameType)
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new MarketParser().parse(result);
    }

    public DefaultRosterData loadDefaultRosterData() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath("new")
                .appendQueryParameter("category", _category)
                .appendQueryParameter("sport", _sport)
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, DefaultRosterData.class);
    }
}
