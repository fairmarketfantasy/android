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

    private Player _player;
    private int _marketId;

    public GetStatEventsRequest(Player player, int market_id) {
        super(StatEventsResponse.class);
        _player = player;
        _marketId = market_id;
    }

    @Override
    public StatEventsResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("events")
                .appendPath("for_players")
                .appendQueryParameter("access_token", getAccessToken())
                .appendQueryParameter("player_ids", _player.getStatsId())
                .appendQueryParameter("average", "true")
                .appendQueryParameter("market_id", Integer.toString(_marketId))
                .appendQueryParameter("position", _player.getPosition());

        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, getResultType());
    }
}
