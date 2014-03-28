package com.fantasysport.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.adapters.MenuItem;
import com.fantasysport.adapters.MenuListAdapter;

/**
 * Created by bylynka on 2/11/14.
 */
public class MainActivity extends BaseMainActivity {

    private final int PLAYER_CANDIDATE = 123;
    private DrawerLayout _drawerLayout;
    private ActionBarDrawerToggle _drawerToggle;
    private MenuListAdapter _menuAdapter;
    protected ListView _menuList;

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
                        showWebView("pages/mobile/terms", "TERMS");
                        break;
                    case Rules:
                        showWebView("pages/mobile/rules", "RULES");
                        break;
                    case Support:
                        showWebView("pages/mobile/support", "SUPPORT");
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(true);
                return true;
        }
        return false;
    }

}