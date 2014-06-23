package com.fantasysport.webaccess;

import android.app.Application;
import android.util.Log;
import com.google.api.client.extensions.android.AndroidUtils;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.octo.android.robospice.GoogleHttpClientSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.googlehttpclient.json.GsonObjectPersisterFactory;
import com.octo.android.robospice.persistence.googlehttpclient.json.JacksonObjectPersisterFactory;

import java.security.GeneralSecurityException;

/**
 * Simple service
 *
 * @author sni
 */
public class GsonGoogleHttpClientSpiceService extends GoogleHttpClientSpiceService {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            if (AndroidUtils.isMinimumSdkLevel(9)) {
                NetHttpTransport.Builder builder = new NetHttpTransport.Builder();
                httpRequestFactory = builder
                        .doNotValidateCertificate()
                        .build()
                        .createRequestFactory();
            } else {
                ApacheHttpTransport.Builder builder = new ApacheHttpTransport.Builder();
                httpRequestFactory = builder
                        .doNotValidateCertificate()
                        .build()
                        .createRequestFactory();
            }

        } catch (GeneralSecurityException e) {
            Log.e("SampleSpiceService", "Error: " + e.getMessage());
            httpRequestFactory = createRequestFactory();
        }
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        // init
//        GsonObjectPersisterFactory gsonObjectPersisterFactory = new GsonObjectPersisterFactory(application);
//        cacheManager.addPersister(gsonObjectPersisterFactory);
        JacksonObjectPersisterFactory jacksonObjectPersisterFactory = new JacksonObjectPersisterFactory(application);

        cacheManager.addPersister(jacksonObjectPersisterFactory);
        return cacheManager;
    }
}