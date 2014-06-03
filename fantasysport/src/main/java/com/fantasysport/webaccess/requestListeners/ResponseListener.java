package com.fantasysport.webaccess.requestListeners;

import com.fantasysport.App;
import com.fantasysport.R;
import com.google.api.client.http.HttpResponseException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.exception.RequestCancelledException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by bylynka on 2/4/14.
 */
public abstract class ResponseListener<T> implements RequestListener<T> {

    @Override
    public void onRequestFailure(SpiceException e) {
        RequestError error = null;
        try{
            if(e instanceof NoNetworkException){
                error = new RequestError("Network is not available");
            }
            else if(e.getCause() instanceof HttpResponseException){
                HttpResponseException exception = (HttpResponseException)e.getCause();
//                exception.getStatusCode()
                String responseBody = exception.getContent();
                final GsonBuilder builder = new GsonBuilder();
                final Gson gson = builder.create();
                error = gson.fromJson(responseBody, RequestError.class);
            }else if(e instanceof RequestCancelledException){
                error = new RequestError(((RequestCancelledException)e).getLocalizedMessage());
                error.setIsCanceledRequest(true);
            }
            else {
                error = new RequestError(App.getCurrent().getString(R.string.unknown_error));
            }
        }catch (Exception ex){
            error = new RequestError(App.getCurrent().getString(R.string.unknown_error));
        }
        if(error == null){
            error = new RequestError(App.getCurrent().getString(R.string.unknown_error));
        }
        onRequestError(error);
    }

    public abstract void onRequestError(RequestError message);
}
