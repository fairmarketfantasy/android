package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.Roster;
import com.fantasysport.models.StatsItem;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by bylynka on 3/6/14.
 */
public class SubmitPredictionRequest extends BaseRequest<Object> {

    private SubmitPredictionsRequestBody _body;
    private String _accessToken;

    public SubmitPredictionRequest(int rosterId, int marketId, String statsId, List<StatsItem> events, String accessTokent) {
        super(Object.class);
        _body = new SubmitPredictionsRequestBody(rosterId, marketId, statsId, events);
        _accessToken = accessTokent;
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("individual_predictions")
                .appendQueryParameter("access_token", _accessToken);
        String url = uriBuilder.build().toString();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.STATIC)
                .create();
        String js = gson.toJson(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, Roster.class);
    }
}
