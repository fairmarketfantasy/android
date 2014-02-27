package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.Player;
import com.fantasysport.parsers.PlayersParser;
import com.fantasysport.webaccess.responses.PlayersRequestResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
public class GetPlayersRequest extends BaseRequest<PlayersRequestResponse> {

    private String _accessToken;
    private String _position;
    private Boolean _removeBenchedPlayers;
    private int _rosterId;

    public GetPlayersRequest(String accessToken, String position, boolean removeBenchedPlayers, int rosterId) {
        super(PlayersRequestResponse.class);
        _accessToken = accessToken;
        _position = position;
        _removeBenchedPlayers = removeBenchedPlayers;
        _rosterId = rosterId;
    }

    @Override
    public PlayersRequestResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("players")
                .appendQueryParameter("access_token", _accessToken)
                .appendQueryParameter("dir", "desc")
                .appendQueryParameter("position", _position)
                .appendQueryParameter("sort","buy_price")
                .appendQueryParameter("roster_id", Integer.toString(_rosterId))
                .appendQueryParameter("removeLow", _removeBenchedPlayers.toString());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        PlayersParser parser = new PlayersParser();
        List<Player> players = parser.parse(result);
        PlayersRequestResponse response = new PlayersRequestResponse(players);
        return response;
    }
}
