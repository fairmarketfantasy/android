package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.models.fantasy.Player;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fantasysport.webaccess.responses.AddPlayerResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;

/**
 * Created by bylynka on 2/27/14.
 */
public class AddPlayerRequest extends BaseRequest<Player> {

    private int _rosterId;
    private AddPlayerRequestBody _body;
    private Player _player;

    public AddPlayerRequest(int rosterId, Player player) {
        super(Player.class);
        _rosterId = rosterId;
        _player = player;
        _body = new AddPlayerRequestBody(player.getBuyPrice());
    }

    @Override
    public Player loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath(Integer.toString(_rosterId))
                .appendPath("add_player")
                .appendPath(Integer.toString(_player.getId()))
                .appendPath(_player.getPosition())
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        String js = getObjectMapper().writeValueAsString(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        AddPlayerResponse response = getObjectMapper().readValue(result, AddPlayerResponse.class);
        _player.setPurchasePrice(response.getPrice());
        return _player;
    }
}
