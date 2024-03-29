package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.UserData;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 4/4/14.
 */
public class UserRequest extends BaseRequest<UserData> {

    private int _userId;

    public UserRequest(int userId) {
        super(UserData.class);
        _userId = userId;
    }

    @Override
    public UserData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users")
                .appendPath(Integer.toString(_userId))
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        UserData data = new Gson().fromJson(result, UserData.class);
        _rHelper.loadUserData(data);
        return data;
    }
}
