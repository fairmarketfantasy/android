package com.fantasysport.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.MenuItem;
import com.fantasysport.adapters.MenuListAdapter;
import com.fantasysport.fragments.MenuHeaderFragment;
import com.fantasysport.models.Market;
import com.fantasysport.models.Sport;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.fantasysport.webaccess.requestListeners.MarketsResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.UserResponseListener;
import com.fantasysport.webaccess.responses.MarketResponse;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bylynka on 2/11/14.
 */
public class MainActivity extends BaseMainActivity {

    private DrawerLayout _drawerLayout;
    private ActionBarDrawerToggle _drawerToggle;
    private MenuListAdapter _menuAdapter;
    protected ExpandableListView _menuList;
    protected List<IOnMarketsListener> _marketListeners = new ArrayList<IOnMarketsListener>();
    protected MenuHeaderFragment _menuHeaderFragment;
    private android.view.MenuItem _refreshMenuItem;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        _refreshMenuItem = menu.findItem(R.id.refresh);
        _refreshMenuItem.setVisible(!(_progressCounter > 0));
        return super.onCreateOptionsMenu(menu);
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

    protected void setMenuHeaderImage(final View headerView) {
        ViewTreeObserver observer = headerView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    headerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    headerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                updateMenuHeaderImage(headerView);
            }
        });
    }

    protected void updateMenuHeaderImage(View headerView){
       String sport = _storage.getUserData().getCurrentSport();
        int drawableId = sport.equalsIgnoreCase(Sport.NBA)?R.drawable.nba_background:R.drawable.mlb_background;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(drawableId);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int width = headerView.getWidth() > bitmap.getWidth()? bitmap.getWidth(): headerView.getWidth();
        int height = headerView.getHeight() > bitmap.getHeight()? bitmap.getHeight(): headerView.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, null, false);
        ImageView img = (ImageView) headerView.findViewById(R.id.image);
        img.setImageBitmap(newBitmap);
    }

    private void setMenu() {
        final View header = getLayoutInflater().inflate(R.layout.menu_header, null, false);
        setMenuHeaderImage(header);
        _menuList.addHeaderView(header, null, false);
        _menuHeaderFragment = (MenuHeaderFragment) getSupportFragmentManager().findFragmentById(R.id.menu_header_fragment);
        _menuAdapter = new MenuListAdapter(this, _menuList, _storage.getUserData());
        _menuList.setAdapter(_menuAdapter);
        _menuList.setCacheColorHint(Color.TRANSPARENT);
        _menuAdapter.setOnItemClickListener(new MenuListAdapter.IOnItemClickListener() {
            @Override
            public void onClick(MenuItem item) {
                _drawerLayout.closeDrawer(_menuList);
                switch (item.getId()) {
                    case LegalStuff:
                        showWebView("pages/mobile/terms", "How It Works/Support");
                        break;
                    case Rules:
                        showWebView("pages/mobile/rules?sport=NBA", "Rules");
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
                    case Sport:
                        showAlert("MLB", getString(R.string.coming_soon));
                        break;
                    case FantasySport:

                        UserData data = _storage.getUserData();
                        data.setCurrentSport(item.getTitle());
                        _menuAdapter.setMenu(data);
                        _menuAdapter.notifyDataSetChanged();
                        updateMarkets(true);
                        updateMenuHeaderImage(header);
                        break;
                }
            }
        });
    }

    @Override
    protected void beforeLoading() {
        if (_refreshMenuItem == null) {
            return;
        }
        _refreshMenuItem.setVisible(false);
    }

    @Override
    protected void afterLoading() {
        if (_refreshMenuItem == null) {
            return;
        }
        _refreshMenuItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.refresh:
                updateMainData(false);
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

    protected void updateUserData() {
        int userId = _storage.getUserData().getId();
        showProgress();
        _webProxy.getUserData(userId, new UserResponseListener() {
            @Override
            public void onRequestError(RequestError message) {
                dismissProgress();
                showAlert(getString(R.string.error), message.getMessage());
            }

            @Override
            public void onRequestSuccess(UserData data) {
                dismissProgress();
                _storage.setUserData(data);
                if (_menuHeaderFragment != null) {
                    _menuHeaderFragment.updateView();
                }
            }
        });
    }

    private void updateMarkets(final boolean isTimeChanged) {
        UserData data = _storage.getUserData();
        String cat = data.getCurrentCategory();
        String sport = data.getCurrentSport();
        showProgress();
        _webProxy.getMarkets(cat, sport, new MarketsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(MarketResponse response) {
                _storage.setDefaultRosterData(response.getDefaultRosterData());
                _storage.setMarketsContainer(response.getMarketsContainer());
                List<Market> tmpMarkets = _storage.getMarkets();
                if (isTimeChanged || marketChanged(tmpMarkets, _markets)) {
                    _markets = tmpMarkets;
                    raiseOnMarketListener(_markets);
                }
                dismissProgress();
            }
        });
    }

    private void updateMainData(boolean isTimeChanged) {
        updateUserData();
        updateMarkets(isTimeChanged);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long nowTime = DateUtils.getCurrentDate().getTime();
        long marketsTime = _storage.getMarketsContainer().getUpdatedAt();
        long deltaTime = nowTime - marketsTime;
        long deltaTimeInMin = deltaTime / 60000;
        boolean isTimeChanged = CacheProvider.getBoolean(this, Const.TIME_ZONE_CHANGED);
        CacheProvider.putBoolean(this, Const.TIME_ZONE_CHANGED ,false);
        if (deltaTimeInMin > 35 || isTimeChanged) {
            updateMainData(isTimeChanged);
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
        if (newMarkets.size() != oldMarkets.size()) {
            return true;
        }
        int currentGmt = DeviceInfo.getGMTInMinutes();
        int gmt = CacheProvider.getInt(this, Const.GMT_IN_MINUTES);
        if(gmt != -1 && currentGmt != gmt){
            return true;
        }
        for (Market newMarket : newMarkets) {
            boolean has = false;
            for (Market oldMarket : oldMarkets) {
                if (oldMarket.getId() == newMarket.getId()) {
                    has = true;
                }
            }
            if (!has) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.SETTINGS_ACTIVITY && _menuHeaderFragment != null) {
            _menuHeaderFragment.updateView();
        }
    }

    public interface IOnMarketsListener {
        public void onMarkets(List<Market> markets);
    }
}