package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/26/14.
 */
public class CreateRosterRequest extends BaseRequest<Roster> {
    private CreateRosterRequestBody _requestBody;

    public CreateRosterRequest(int marketId) {
        super(Roster.class);
        _requestBody = new CreateRosterRequestBody(marketId);
    }

    @Override
    public Roster loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        String js = new Gson().toJson(_requestBody);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, getResultType());
    }
}
