package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.User;
import com.fantasysport.webaccess.RequestSender;
import com.fantasysport.webaccess.Responses.SignInResponse;
import com.fantasysport.webaccess.Responses.SignUpResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by bylynka on 2/3/14.
 */
public class SignUpRequest extends BaseRequest<SignUpResponse> {

    private SignUpRequestBody _requestBody;

    public SignUpRequest(User user) {
        super(SignUpResponse.class);
        _requestBody = new SignUpRequestBody(user);
        ClientHttpRequest request;
//        request.getHeaders()
    }

    @Override
    public SignUpResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users");
        String url = uriBuilder.build().toString();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        headers.add("Accept", "application/json");
//        ResponseEntity<SignUpResponse> entity = getRestTemplate()
//                .exchange(url, HttpMethod.POST, new HttpEntity<Object>(_requestBody, headers), SignUpResponse.class);
        return getRestTemplate().postForObject(url, _requestBody, SignUpResponse.class);
//        doExecute(new URI(url),HttpMethod.POST);
//        return null;
//        return entity.getBody();
    }

}