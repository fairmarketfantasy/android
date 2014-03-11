package com.fantasysport.webaccess.requests;


import android.net.Uri;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

/**
 * Created by bylynka on 3/7/14.
 */
public class HTMLContentRequest extends BaseRequest<String> {

    String _path;

    public HTMLContentRequest(String path) {
        super(String.class);
        _path = path;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendEncodedPath(_path);
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        String result = request.execute().parseAsString();
        return result;
    }
}
