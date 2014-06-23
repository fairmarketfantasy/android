package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fantasysport.webaccess.responses.SubmitRosterResponse;
import com.google.api.client.http.*;

/**
 * Created by bylynka on 3/4/14.
 */
public class SubmitRosterRequest extends BaseRequest<SubmitRosterResponse> {

    public static final String TOP6 = "100/30/30";
    public static final String H2H = "27 H2H";

    private int _rosterId;
    private SubmitRequestBody _body;

    public SubmitRosterRequest(int rosterId, String contestType) {
        super(SubmitRosterResponse.class);
        _body = new SubmitRequestBody(contestType);
        _rosterId = rosterId;
    }

    @Override
    public SubmitRosterResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath(Integer.toString(_rosterId))
                .appendPath("submit")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        String js = getObjectMapper().writeValueAsString(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        String msg = null;
        HttpHeaders responseHeaders = request.getResponseHeaders();
        if(responseHeaders != null
                && responseHeaders.getHeaderStringValues("X-CLIENT-FLASH") != null
                && responseHeaders.getHeaderStringValues("X-CLIENT-FLASH").size() > 0){
            msg = responseHeaders.getHeaderStringValues("X-CLIENT-FLASH").get(0);
        }
        return new SubmitRosterResponse(msg);
    }
}
