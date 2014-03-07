package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.StatsItem;
import com.fantasysport.models.Player;
import com.fantasysport.webaccess.responses.StatEventsResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bylynka on 3/5/14.
 */
public class GetStatEventsRequest extends BaseRequest<StatEventsResponse> {

    private String _accessToken;
    private Player _player;

    public GetStatEventsRequest(Player player, String accessToken) {
        super(StatEventsResponse.class);
        _accessToken = accessToken;
        _player = player;
    }

    @Override
    public StatEventsResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("events")
                .appendPath("for_players")
                .appendQueryParameter("access_token", _accessToken)
                .appendQueryParameter("player_ids", _player.getStatsId())
                .appendQueryParameter("average", "true");
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, getResultType());
    }
}
