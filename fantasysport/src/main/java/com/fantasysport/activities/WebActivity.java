package com.fantasysport.activities;

import android.content.Intent;
import android.os.Bundle;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.views.WebControlView;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responseListeners.StringResponseListener;

import java.net.URLEncoder;

/**
 * Created by bylynka on 3/7/14.
 */
public class WebActivity extends BaseActivity {

    private WebControlView _webView;
    private String _webLink;
    private String _header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initStartParams(savedInstanceState);
        setHeaderText(_header);
        _webView = getViewById(R.id.web_view);
        showProgress();
        _webProxy.getHTMLContent(_webLink, new StringResponseListener() {
            @Override
            public void onRequestError(RequestError message) {
                dismissProgress();
                showAlert("", message.getMessage());
            }
            @Override
            public void onRequestSuccess(String s) {
               _webView.loadData(URLEncoder.encode(s).replaceAll("\\+"," "),"text/html", "utf-8");
                dismissProgress();
            }
        });
    }

    private void initStartParams(Bundle savedInstanceState){
        if(savedInstanceState == null){
            Intent intent = getIntent();
            _webLink = intent.getStringExtra(Const.WEB_LINK);
            _header = intent.getStringExtra(Const.WEB_ACTIVITY_HEADER);
        }else {
            _webLink = savedInstanceState.getString(Const.WEB_LINK);
            _header = savedInstanceState.getString(Const.WEB_ACTIVITY_HEADER);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Const.WEB_LINK, _webLink);
        outState.putString(Const.WEB_ACTIVITY_HEADER, _header);
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
