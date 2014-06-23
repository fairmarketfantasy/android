package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fantasysport.webaccess.responses.AutoFillResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;

/**
 * Created by bylynka on 3/4/14.
 */
public class AutoFillRequest extends BaseRequest<AutoFillResponse> {

    private int _rosterId;
    private int _marketId;

    public AutoFillRequest(int marketId, int rosterId) {
        super(AutoFillResponse.class);
        _rosterId = rosterId;
        _marketId = marketId;
    }

    @Override
    public AutoFillResponse loadDataFromNetwork() throws Exception {
        if (_rosterId < 0) {
            Roster roster = createRoster();
            _rosterId = roster.getId();
        }

        Roster roster = autoFillRoster();
        return new AutoFillResponse(roster);
    }

    private Roster autoFillRoster() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath(Integer.toString(_rosterId))
                .appendPath("autofill")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return getObjectMapper().readValue(result, Roster.class);
    }

    private Roster createRoster() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        CreateRosterRequestBody body = new CreateRosterRequestBody(_marketId);
        String js = getObjectMapper().writeValueAsString(body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return getObjectMapper().readValue(result, Roster.class);
    }
}
