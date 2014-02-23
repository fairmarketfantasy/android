package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.Market;
import com.fantasysport.models.UserData;
import com.fantasysport.parsers.MarketParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by bylynka on 2/19/14.
 */
public class GamesRequest extends BaseRequest<List>  {

    public static final String REGULAR_SEASON = "regular_season";

    private String _gameType;
    private String _accessToken;

    public GamesRequest(String gameType, String accessToken) {
        super(List.class);
        _gameType = gameType;
        _accessToken = accessToken;
    }

    @Override
    public List<Market> loadDataFromNetwork() throws Exception {

        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("markets")
                .appendQueryParameter("sport","NBA")
                .appendQueryParameter("type", _gameType)
                .appendQueryParameter("access_token", _accessToken);

        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new MarketParser().parse(result);
    }
}
