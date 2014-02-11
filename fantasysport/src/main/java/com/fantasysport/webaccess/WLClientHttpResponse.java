package com.fantasysport.webaccess;

import android.util.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpStatus;

/**
 * Created by bylynka on 2/10/14.
 */
public class WLClientHttpResponse implements ClientHttpResponse {

    private final ClientHttpResponse _response;

    private byte[] _body;

    WLClientHttpResponse(ClientHttpResponse response) {
        _response = response;
    }


    public HttpStatus getStatusCode() throws IOException {
        return _response.getStatusCode();
    }

    public int getRawStatusCode() throws IOException {
        return _response.getRawStatusCode();
    }

    public String getStatusText() throws IOException {
        return _response.getStatusText();

    }

    public HttpHeaders getHeaders() {
        return _response.getHeaders();
    }

    public InputStream getBody() throws IOException {
        if (_body == null) {
           _body = FileCopyUtils.copyToByteArray(_response.getBody());
        }
        if(_body != null){
            String value = new String(_body);
            Log.i("ClientHttpResponse", value);
        }
        return new ByteArrayInputStream(_body);
    }

    public void close() {
        _response.close();
    }

}