package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.nonfantasy.NFPrediction;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

import java.util.List;

/**
 * Created by bylynka on 5/27/14.
 */
public class GetNFPredictionsRequest extends BaseRequest<List> {

    private int _page = 0;
    private String _sport;
    private String _category;
    private boolean _isHistory;

    public GetNFPredictionsRequest(String category, String sport, int page, boolean isHistory) {
        super(List.class);
        _page = page;
        _sport = sport;
        _category = category;
        _isHistory = isHistory;
    }

    @Override
    public List<NFPrediction> loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("rosters")
                .appendPath("mine")
                .appendQueryParameter("sport", _sport)
                .appendQueryParameter("category", _category)
                .appendQueryParameter("access_token", getAccessToken())
                .appendQueryParameter("page", Integer.toString(_page));
        if(_isHistory){
            uriBuilder.appendQueryParameter("historical", "true");

        }
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return getObjectMapper().readValue(result, new TypeReference<List<NFPrediction>>() {});
    }
}