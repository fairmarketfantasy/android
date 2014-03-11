package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.User;
import com.fantasysport.models.UserData;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 3/11/14.
 */
public class UpdateUserRequest extends BaseRequest<UserData> {

    private UserRequestBody _body;

    public UpdateUserRequest(User user) {
        super(UserData.class);
        _body = new UserRequestBody(user);
    }

    @Override
    public UserData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("users")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        String js = new Gson().toJson(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPutRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, UserData.class);
    }
}
