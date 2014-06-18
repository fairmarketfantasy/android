package com.fantasysport.webaccess.requests.nonfantasy;


import android.net.Uri;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

/**
 * Created by bylynka on 6/12/14.
 */
public class TradeIndividualPredictionRequest extends BaseRequest<Object> {

    private int _id;
    private String _sport;

    public TradeIndividualPredictionRequest(int id, String sport) {
        super(Object.class);
        _id = id;
        _sport = sport;
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("trade_prediction")
                .appendQueryParameter("sport", _sport)
                .appendQueryParameter("id", Integer.toString(_id))
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildDeleteRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        request.execute();
        return null;
    }
}
