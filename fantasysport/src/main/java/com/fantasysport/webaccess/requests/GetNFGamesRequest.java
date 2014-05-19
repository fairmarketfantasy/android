package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.NFDataContainer;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.responses.GeTNFGamesResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 5/13/14.
 */
public class GetNFGamesRequest extends BaseRequest<NFDataContainer> {

    public String _sport;

    public GetNFGamesRequest(String sport) {
        super(NFDataContainer.class);
        _sport = sport;
    }

    @Override
    public NFDataContainer loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("game_predictions")
                .appendPath("day_games")
                .appendQueryParameter("access_token", getAccessToken())
                .appendQueryParameter("sport", _sport);
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, NFDataContainer.class);
    }
}
