package com.fantasysport.webaccess.requests.fantasy;

import android.net.Uri;
import com.fantasysport.models.Roster;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.requestListeners.UpdateMainDataResponse;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

/**
 * Created by bylynka on 3/26/14.
 */
public class UpdateMainDataRequest extends BaseRequest<UpdateMainDataResponse> {
    private int _userId;

    public UpdateMainDataRequest(int userId) {
        super(UpdateMainDataResponse.class);
        _userId = userId;
    }


    @Override
    public UpdateMainDataResponse loadDataFromNetwork() throws Exception {
        UpdateMainDataResponse response = new UpdateMainDataResponse();
        UserData userData = downloadUserData();
        response.setUserData(userData);
        int rosterId = userData.getInProgressRosterId();
        if(rosterId > 0){
            Roster roster = downloadRoster(rosterId);
            response.setRoster(roster);
        }
        return response;
    }

    private UserData downloadUserData() throws Exception {
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

    public Roster downloadRoster(int rosterId) throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("rosters")
                .appendPath(Integer.toString(rosterId))
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result,Roster.class);
    }
}
