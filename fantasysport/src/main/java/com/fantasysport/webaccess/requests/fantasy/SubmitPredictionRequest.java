package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.models.StatsItem;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;

import java.util.List;

/**
 * Created by bylynka on 3/6/14.
 */
public class SubmitPredictionRequest extends BaseRequest<Object> {

    private SubmitPredictionsRequestBody _body;

    public SubmitPredictionRequest(int rosterId, int marketId, String statsId, List<StatsItem> events) {
        super(Object.class);
        _body = new SubmitPredictionsRequestBody(rosterId, marketId, statsId, events);
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("individual_predictions")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();

        String js = getObjectMapper().writeValueAsString(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Object();
    }
}
