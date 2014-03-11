package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.Roster;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 3/4/14.
 */
public class SubmitRosterRequest extends BaseRequest<Object> {

    public static final String TOP6 = "100/30/30";
    public static final String H2H = "27 h2h";

    private int _rosterId;
    private SubmitRequestBody _body;

    public SubmitRosterRequest(int rosterId, String contestType) {
        super(Object.class);
        _body = new SubmitRequestBody(contestType);
        _rosterId = rosterId;
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath(Integer.toString(_rosterId))
                .appendPath("submit")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        String js = new Gson().toJson(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, Roster.class);
    }
}
