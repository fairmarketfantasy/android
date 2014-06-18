package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.parsers.IndividualPredictionParser;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

import java.util.List;

/**
 * Created by bylynka on 5/27/14.
 */
public class GetNFIndividualPredictionRequest extends BaseRequest<List> {
    private int _page = 0;
    private String _sport;
    private String _category;
    private boolean _isHistory;

    public GetNFIndividualPredictionRequest(String category, String sport, int page, boolean isHistory) {
        super(List.class);
        _page = page;
        _sport = sport;
        _isHistory = isHistory;
        _category = category;
    }

    @Override
    public List<IndividualPrediction> loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("individual_predictions")
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
        IndividualPredictionParser parser = new IndividualPredictionParser();
        return parser.parse(result);
    }

}

