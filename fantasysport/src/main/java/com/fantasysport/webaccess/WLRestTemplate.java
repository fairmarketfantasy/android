package com.fantasysport.webaccess;

import android.util.Log;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.*;

import java.io.IOException;
import java.net.URI;

/**
 * Created by bylynka on 2/8/14.
 */
public class WLRestTemplate extends RestTemplate {

    private static String TAG = "WLRestTemplate";
    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
                              ResponseExtractor<T> responseExtractor) throws RestClientException {

        Assert.notNull(url, "'url' must not be null");
        Assert.notNull(method, "'method' must not be null");
        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            response = wlResponse(request.execute());

            if (!getErrorHandler().hasError(response)) {
                logResponseStatus(method, url, response);
            }
            else {
                handleResponseError(method, url, response);
            }
            if (responseExtractor != null) {
                return responseExtractor.extractData(response);
            }
            else {
                return null;
            }
        }
        catch (IOException ex) {
            throw new ResourceAccessException("I/O error: " + ex.getMessage(), ex);
        }
        finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private ClientHttpResponse wlResponse(ClientHttpResponse response) {
        return new WLClientHttpResponse(response);
    }

    private void logResponseStatus(HttpMethod method, URI url, ClientHttpResponse response) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            try {
                Log.d(TAG,
                        method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() + " (" +
                                response.getStatusText() + ")");
            }
            catch (IOException e) {
                // ignore
            }
        }
    }

    private void handleResponseError(HttpMethod method, URI url, ClientHttpResponse response) throws IOException {
        if (Log.isLoggable(TAG, Log.WARN)) {
            try {
                Log.w(TAG,
                        method.name() + " request for \"" + url + "\" resulted in " + response.getStatusCode() + " (" +
                                response.getStatusText() + "); invoking error handler");
            }
            catch (IOException e) {
                // ignore
            }
        }
        getErrorHandler().handleError(response);
    }
}
