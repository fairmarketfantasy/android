package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/25/14.
 */
public class PlayersPositionRequest extends BaseRequest<DefaultRosterData> {

    private String _sport;
    private String _category;

    public PlayersPositionRequest(String category, String sport) {
        super(DefaultRosterData.class);
        _sport = sport;
        _category = category;
    }

    @Override
    public DefaultRosterData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath("new")
                .appendQueryParameter("sport", _sport)
                .appendQueryParameter("category", _category)
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, getResultType());
    }
}
