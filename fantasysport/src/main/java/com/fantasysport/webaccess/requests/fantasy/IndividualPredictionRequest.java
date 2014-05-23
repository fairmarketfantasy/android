package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.parsers.IndividualPredictionParser;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
public class IndividualPredictionRequest extends BaseRequest<List> {

    private int _page = 0;
    private String _sport;
    private String _category;

    public IndividualPredictionRequest(String category, String sport) {
        super(List.class);
        _sport = sport;
        _category = category;
    }

    public IndividualPredictionRequest(String category, String sport, int page) {
        super(List.class);
        _page = page;
        _sport = sport;
    }

    @Override
    public List<IndividualPrediction> loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("individual_predictions")
                .appendPath("mine")
                .appendQueryParameter("sport", _sport)
                .appendQueryParameter("category", _category)
                .appendQueryParameter("access_token", getAccessToken());
        if(_page > 0){
            uriBuilder.appendQueryParameter("historical", "true");
            uriBuilder.appendQueryParameter("page", Integer.toString(_page));
        }
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        IndividualPredictionParser parser = new IndividualPredictionParser();
        return parser.parse(result);
    }

}
