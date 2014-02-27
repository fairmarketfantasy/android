package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.DefaultRosterData;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/25/14.
 */
public class PlayersPositionRequest extends BaseRequest<DefaultRosterData> {

    private String _accessToken;

    public PlayersPositionRequest(String accessToken) {
        super(DefaultRosterData.class);
        _accessToken = accessToken;
    }

    @Override
    public DefaultRosterData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath("new")
                .appendQueryParameter("sport", "NBA")
                .appendQueryParameter("access_token", _accessToken);
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, getResultType());
    }
}
