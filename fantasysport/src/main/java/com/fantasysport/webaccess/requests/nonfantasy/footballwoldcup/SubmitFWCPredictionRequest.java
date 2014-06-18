package com.fantasysport.webaccess.requests.nonfantasy.footballwoldcup;

import android.net.Uri;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fantasysport.webaccess.responses.MsgResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Modifier;

/**
 * Created by bylynka on 6/4/14.
 */
public class SubmitFWCPredictionRequest extends BaseRequest<MsgResponse> {

    private RequestBody _body;

    public SubmitFWCPredictionRequest(String predictionType, String predictableId, String gameId) {
        super(MsgResponse.class);
        _body = new RequestBody();
        _body._predictionType = predictionType;
        _body._predictableId = predictableId;
        _body._gameId = gameId;
    }

    @Override
    public MsgResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("create_prediction")
                .appendQueryParameter("access_token", getAccessToken());

        String url = uriBuilder.build().toString();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.STATIC)
                .create();
        String js = _body != null? gson.toJson(_body): null;
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request =  getHttpRequestFactory().buildPostRequest(new GenericUrl(url), content);

        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, MsgResponse.class);
    }

    class RequestBody{
        @SerializedName("prediction_type")
        private String _predictionType;

        @SerializedName("predictable_id")
        private String _predictableId;

        @SerializedName("game_stats_id")
        private String _gameId;

        @SerializedName("sport")
        private String _sport = "FWC";
    }

}
