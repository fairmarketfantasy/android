package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.responses.AutofillResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 3/4/14.
 */
public class AutofillRequest extends BaseRequest<AutofillResponse> {

    private String _accessToken;
    private int _rosterId;
    private int _marketId;

    public AutofillRequest(int marketId, int rosterId, String accessToken) {
        super(AutofillResponse.class);
        _accessToken = accessToken;
        _rosterId = rosterId;
        _marketId = marketId;
    }

    @Override
    public AutofillResponse loadDataFromNetwork() throws Exception {
       if(_rosterId < 0){
          Roster roster = createRoster();
          _rosterId = roster.getId();
       }

       Roster roster = autoFillRoster();
       return new AutofillResponse(roster);
    }

    private Roster autoFillRoster() throws Exception{
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath(Integer.toString(_rosterId))
                .appendPath("autofill")
                .appendQueryParameter("access_token", _accessToken);
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, Roster.class);
    }

    private Roster createRoster() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendQueryParameter("access_token", _accessToken);
        String url = uriBuilder.build().toString();
        CreateRosterRequestBody body = new CreateRosterRequestBody(_marketId);
        String js = new Gson().toJson(body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, Roster.class);
    }
}
