package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.Roster;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 4/3/14.
 */
public class RemoveBenchedPlayersRequest extends BaseRequest<Roster> {

    private int _rosterId;

    public RemoveBenchedPlayersRequest(int rosterId) {
        super(Roster.class);
        _rosterId = rosterId;
    }

    @Override
    public Roster loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath(Integer.toString(_rosterId))
                .appendPath("toggle_remove_bench")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, getResultType());
    }
}