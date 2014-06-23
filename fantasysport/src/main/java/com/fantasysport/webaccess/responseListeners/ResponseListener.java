package com.fantasysport.webaccess.responseListeners;

import com.fantasysport.App;
import com.fantasysport.R;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpResponseException;
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
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                error = mapper.readValue(responseBody, RequestError.class);
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
