package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.User;
import com.fantasysport.webaccess.Responses.SignUpResponse;

/**
 * Created by bylynka on 2/3/14.
 */
public class SignUpRequest extends BaseRequest<SignUpResponse> {

    private SignUpRequestBody _requestBody;

    public SignUpRequest(User user) {
        super(SignUpResponse.class);
        _requestBody = new SignUpRequestBody(user);
    }

    @Override
    public SignUpResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users");
        String url = uriBuilder.build().toString();
        return getRestTemplate().postForObject(url, _requestBody,SignUpResponse.class);
    }
}