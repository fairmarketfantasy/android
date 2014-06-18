package com.fantasysport.webaccess.requests.nonfantasy.footballwoldcup;

import android.net.Uri;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 6/2/14.
 */
public class GetFWCDataRequest extends BaseRequest<FWCData> {

    public GetFWCDataRequest() {
        super(FWCData.class);
    }

    @Override
    public FWCData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("home")
                .appendQueryParameter("sport", "FWC")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        FWCData data = new Gson().fromJson(result, FWCData.class);
        _rHelper.loadFWCData(data);
        return data;
    }
}
