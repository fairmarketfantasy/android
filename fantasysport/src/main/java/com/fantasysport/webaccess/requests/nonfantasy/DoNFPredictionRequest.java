package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fantasysport.webaccess.responses.MsgResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

/**
 * Created by bylynka on 5/23/14.
 */
public class DoNFPredictionRequest extends BaseRequest<String> {

    private String _gameStatsId;
    private String _teamStatsId;

    public DoNFPredictionRequest(String gameStatsId, String teamStatsId) {
        super(String.class);
        _gameStatsId = gameStatsId;
        _teamStatsId = teamStatsId;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("game_predictions")
                .appendQueryParameter("game_stats_id", _gameStatsId)
                .appendQueryParameter("team_stats_id", _teamStatsId)
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        MsgResponse msgRes = getObjectMapper().readValue(result, MsgResponse.class);
        return msgRes.getMessage();
    }
}
