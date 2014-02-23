package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.User;
import com.fantasysport.models.UserData;
import com.google.api.client.http.*;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/3/14.
 */
//public class SignUpRequest extends BaseRequest<UserData> {
//
//    private SignUpRequestBody _requestBody;
//
//    public SignUpRequest(User user) {
//        super(UserData.class);
//        _requestBody = new SignUpRequestBody(user);
//        ClientHttpRequest request;
////        request.getHeaders()
//    }
//
//    @Override
//    public UserData loadDataFromNetwork() throws Exception {
//        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
//        uriBuilder.appendPath("users");
//        String url = uriBuilder.build().toString();
////        HttpHeaders headers = new HttpHeaders();
////        headers.add("Content-Type", "application/json");
////        headers.add("Accept", "application/json");
////        ResponseEntity<SignUpResponse> entity = getRestTemplate()
////                .exchange(url, HttpMethod.POST, new HttpEntity<Object>(_requestBody, headers), SignUpResponse.class);
//        return getRestTemplate().postForObject(url, _requestBody, UserData.class);
////        doExecute(new URI(url),HttpMethod.POST);
////        return null;
////        return entity.getBody();
//    }
//
//}

public class SignUpRequest extends BaseRequest<UserData> {

    private SignUpRequestBody _requestBody;

    public SignUpRequest(User user) {
        super(UserData.class);
        _requestBody = new SignUpRequestBody(user);
//        request.getHeaders()
    }

    @Override
    public UserData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users");
        String url = uriBuilder.build().toString();

        String js = new Gson().toJson(_requestBody);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");

        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, this.getResultType());

    }

}