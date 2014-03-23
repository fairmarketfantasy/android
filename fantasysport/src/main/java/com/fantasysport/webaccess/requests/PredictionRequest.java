package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.Prediction;
import com.fantasysport.parsers.PredictionParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
public class PredictionRequest extends BaseRequest<List> {

    private int _page = 0;

    public PredictionRequest() {
        super(List.class);
    }

    public PredictionRequest(int page) {
        super(List.class);
        _page = page;
    }

    @Override
    public List<Prediction> loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("rosters")
                .appendPath("mine")
                .appendQueryParameter("sport", "NBA")
                .appendQueryParameter("access_token", getAccessToken());
        if(_page > 0){
            uriBuilder.appendQueryParameter("historical", "true");
            uriBuilder.appendQueryParameter("page", Integer.toString(_page));
        }
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        PredictionParser parser = new PredictionParser();
        return parser.parse(result);
    }
}
