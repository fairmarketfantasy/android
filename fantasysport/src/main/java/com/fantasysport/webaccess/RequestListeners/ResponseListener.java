package com.fantasysport.webaccess.RequestListeners;

import com.fantasysport.App;
import com.fantasysport.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.exception.RequestCancelledException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Created by bylynka on 2/4/14.
 */
public abstract class ResponseListener<T> implements RequestListener<T> {

    @Override
    public void onRequestFailure(SpiceException e) {
        RequestError error = null;
        try{
            if(e instanceof NoNetworkException){
                error = new RequestError(e.getLocalizedMessage());
            }else if(e.getCause() instanceof HttpClientErrorException){
                HttpClientErrorException exception = (HttpClientErrorException)e.getCause();
                String responseBody = exception.getResponseBodyAsString();
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                error = gson.fromJson(responseBody, RequestError.class);
            }
            else {
                error = new RequestError(App.getCurrent().getString(R.string.unknown_error));
            }
        }catch (Exception ex){
            error = new RequestError(App.getCurrent().getString(R.string.unknown_error));
        }
        onRequestError(error);
    }

    public abstract void onRequestError(RequestError message);

}
