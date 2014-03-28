package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.UserData;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 3/28/14.
 */
public class ResetPasswordRequest extends BaseRequest<Object> {

    private Email _body;

    public ResetPasswordRequest(String email) {
        super(Object.class);
        _body = new Email(email);
    }

    @Override
    public Object loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users")
                .appendPath("reset_password");
        String url = uriBuilder.build().toString();
        String js = new Gson().toJson(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return null;
    }

    public class Email{
        @SerializedName("email")
        private String _email;

        public Email(String email){
            _email = email;
        }
    }
}
