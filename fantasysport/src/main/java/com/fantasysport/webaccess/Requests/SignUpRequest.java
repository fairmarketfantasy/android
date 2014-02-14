package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.User;
import com.fantasysport.models.UserData;
import org.springframework.http.client.ClientHttpRequest;

/**
 * Created by bylynka on 2/3/14.
 */
public class SignUpRequest extends BaseRequest<UserData> {

    private SignUpRequestBody _requestBody;

    public SignUpRequest(User user) {
        super(UserData.class);
        _requestBody = new SignUpRequestBody(user);
        ClientHttpRequest request;
//        request.getHeaders()
    }

    @Override
    public UserData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users");
        String url = uriBuilder.build().toString();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        headers.add("Accept", "application/json");
//        ResponseEntity<SignUpResponse> entity = getRestTemplate()
//                .exchange(url, HttpMethod.POST, new HttpEntity<Object>(_requestBody, headers), SignUpResponse.class);
        return getRestTemplate().postForObject(url, _requestBody, UserData.class);
//        doExecute(new URI(url),HttpMethod.POST);
//        return null;
//        return entity.getBody();
    }

}