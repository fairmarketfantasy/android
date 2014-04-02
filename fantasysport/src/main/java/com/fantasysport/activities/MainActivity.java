package com.fantasysport.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.MenuItem;
import com.fantasysport.adapters.MenuListAdapter;
import com.fantasysport.models.Market;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.webaccess.requestListeners.MarketsResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.responses.MarketResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 2/11/14.
 */
public class MainActivity extends BaseMainActivity {

    private final int PLAYER_CANDIDATE = 123;
    private DrawerLayout _drawerLayout;
    private ActionBarDrawerToggle _drawerToggle;
    private MenuListAdapter _menuAdapter;
    protected ListView _menuList;
    protected List<IOnMarketsListener> _marketListeners = new ArrayList<IOnMarketsListener>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _menuList = getViewById(R.id.left_drawer);
        setMenu();
        _drawerLayout = getViewById(R.id.drawer_layout);
        _drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                _drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        _drawerLayout.setDrawerListener(_drawerToggle);

    }

    public void addOnMarketsListener(IOnMarketsListener listener) {
        _marketListeners.add(listener);
    }

    private void raiseOnMarketListener(List<Market> markets) {
        for (IOnMarketsListener listener : _marketListeners) {
            listener.onMarkets(markets);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        _drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setMenu() {
        _menuAdapter = new MenuListAdapter(this);
        _menuList.setAdapter(_menuAdapter);
        _menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = (MenuItem) _menuAdapter.getItem(position);
                switch (item.getId()) {
                    case LegalStuff:
                        showWebView("pages/mobile/terms", "How it works/support");
                        break;
                    case Rules:
                        showWebView("pages/mobile/rules", "Rules");
                        break;
                    case Support:
                        showWebView("pages/mobile/conditions", "Subscription terms");
                        break;
                    case Settings:
                        showSettingsView();
                        break;
                    case SignOut:
                        signOut();
                        break;
                    case Predictions:
                        showPredictions();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.arrow_close:
                item.setVisible(false);
                _menu.findItem(R.id.arrow_open).setVisible(true);
                raiseOnToggleHeader();
                return true;
            case R.id.arrow_open:
                item.setVisible(false);
                _menu.findItem(R.id.arrow_close).setVisible(true);
                raiseOnToggleHeader();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(true);
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        long nowTime = DateUtils.getCurrentDate().getTime();
        long marketsTime = _storage.getMarketsContainer().getUpdatedAt();
        long deltaTime = nowTime - marketsTime;
        long deltaTimeInMin = deltaTime / 60000;
        if (deltaTimeInMin > 35) {
            showProgress();
            _webProxy.getGames(_marketsResponseListener);
        }
    }

    private boolean marketChanged(List<Market> newMarkets, List<Market> oldMarkets) {
        if (newMarkets == null && oldMarkets != null ||
                newMarkets != null && oldMarkets == null) {
            return true;
        }
        if (newMarkets == null && oldMarkets == null) {
            return false;
        }
        if(_markets.size() != _markets.size()){
            return true;
        }
        for (Market newMarket : newMarkets){
            boolean has = false;
            for (Market oldMarket : oldMarkets){
                if(oldMarket.getId() == newMarket.getId()){
                    has = true;
                }
            }
            if(!has){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Const.NEW_AVATAR){
          raiseOnAvatarChanged();
        }
    }

    MarketsResponseListener _marketsResponseListener = new MarketsResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
        }

        @Override
        public void onRequestSuccess(MarketResponse response) {
            _storage.setDefaultRosterData(response.getDefaultRosterData());
            _storage.setMarketsContainer(response.getMarketsContainer());
            List<Market> tmpMarkets =  _storage.getMarkets();
            if (marketChanged(tmpMarkets, _markets)){
                _markets = tmpMarkets;
                raiseOnMarketListener(_markets);
            }
            dismissProgress();
        }
    };

    public interface IOnMarketsListener {
        public void onMarkets(List<Market> markets);
    }
}