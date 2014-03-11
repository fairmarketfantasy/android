package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.Player;
import com.fantasysport.webaccess.responses.TradePlayerResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 3/3/14.
 */
public class TradePlayerRequest extends BaseRequest<TradePlayerResponse> {

    private int _rosterId;
    private int _playerId;
    private TradePlayerRequestBody _body;


    public TradePlayerRequest(int rosterId, Player player) {
        super(TradePlayerResponse.class);
        _rosterId = rosterId;
        _playerId = player.getId();
        _body = new TradePlayerRequestBody(player.getPurchasePrice());
    }

    @Override
    public TradePlayerResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath(Integer.toString(_rosterId))
                .appendPath("remove_player")
                .appendPath(Integer.toString(_playerId))
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        String js = new Gson().toJson(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, getResultType());
    }
}
