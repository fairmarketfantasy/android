package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.DateUtils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 4/4/14.
 */
public class UserRequest extends BaseRequest<UserData> {

    private int _userId;
    private String _sport;
    private String _category;

    public UserRequest(int userId, String sport, String category) {
        super(UserData.class);
        _userId = userId;
        _sport = sport;
        _category = category;
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
        data.setCurrentSport(_sport);
        data.setCurrentCategory(_category);
        data.setUpdatedAt(DateUtils.getCurrentDate().getTime());
        _rHelper.loadUserData(data);
        return data;
    }
}
