package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.webaccess.requestListeners.StringResponseListener;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

/**
 * Created by bylynka on 5/23/14.
 */
public class DoNFPredictionRequest extends BaseRequest<String> {

    private int _gameStatsId;
    private int _teamStatsId;

    public DoNFPredictionRequest(int gameStatsId, int teamStatsId) {
        super(String.class);
        _gameStatsId = gameStatsId;
        _teamStatsId = teamStatsId;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("game_predictions")
                .appendQueryParameter("game_stats_id", Integer.toString(_gameStatsId))
                .appendQueryParameter("team_stats_id", Integer.toString(_teamStatsId))
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        return  request.execute().parseAsString();
    }
}
