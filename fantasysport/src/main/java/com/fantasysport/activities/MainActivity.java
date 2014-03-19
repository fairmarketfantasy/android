package com.fantasysport.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.MainActivityPagerAdapter;
import com.fantasysport.adapters.MenuItem;
import com.fantasysport.adapters.MenuListAdapter;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.views.AnimatedViewPager;
import com.fantasysport.views.animations.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 2/11/14.
 */
public class MainActivity extends BaseActivity{

    private final int PLAYER_CANDIDATE = 123;
    private DrawerLayout _drawerLayout;
    private ActionBarDrawerToggle _drawerToggle;
    private ListView _menuList;
    private MenuListAdapter _menuAdapter;
    private Menu _menu;
    private MainActivityPagerAdapter _mainActivityPagerAdapter;
    private Market _market;
    private Roster _roster;
    private List<IListener> _listeners = new ArrayList<IListener>();
    private boolean _removeBenchedPlayers = true;
    private ImageView _leftSwipeImg;
    private ImageView _rightSwipeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStartParams(savedInstanceState);
        _drawerLayout = getViewById(R.id.drawer_layout);
        _drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                _drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        _drawerLayout.setDrawerListener(_drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _menuList = getViewById(R.id.left_drawer);
        _leftSwipeImg = getViewById(R.id.left_point_img);
        _rightSwipeImg = getViewById(R.id.right_point_img);
        setMenu();
        setPager();

    }

    private void initStartParams(Bundle savedInstanceState){
        if(savedInstanceState == null){
            return;
        }
        _removeBenchedPlayers = savedInstanceState.getBoolean(Const.REMOVE_BENCHED_PLAYERS, true);
        _market = (Market)savedInstanceState.getSerializable(Const.MARKET);
        _roster = (Roster)savedInstanceState.getSerializable(Const.ROSTER);
    }

    public boolean canRemoveBenchedPlayers(){
        return _removeBenchedPlayers;
    }

    public void setCanBenchedPlayers(boolean canBenched){
        _removeBenchedPlayers = canBenched;
    }

    public void addListener(IListener listener){
        _listeners.add(listener);
    }

    private void raiseOnToggleHeader(){
        for (int i = 0; i < _listeners.size(); i++){
            _listeners.get(i).onHeaderToggle();
        }
    }

    private void raiseOnPageChanged(int page){
        for (int i = 0; i < _listeners.size(); i++){
            _listeners.get(i).onPageChanged(page);
        }
    }

    public Market getMarket(){
        return _market;
    }

    public void setMarket(Market market){
        _market = market;
    }

    public Roster getRoster(){
        return _roster;
    }

    public void setRoster(Roster roster){
        _roster = roster;
    }

    private void setPager(){
        _mainActivityPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
        AnimatedViewPager pager = getViewById(R.id.root_pager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(_mainActivityPagerAdapter);
        pager.setOnPageChangeListener(new AnimatedViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Drawable drawable = position == 0? getResources().getDrawable(R.drawable.swipe_active):getResources().getDrawable(R.drawable.swipe_passive);
                _leftSwipeImg.setBackgroundDrawable(drawable);
                drawable = position != 0? getResources().getDrawable(R.drawable.swipe_active):getResources().getDrawable(R.drawable.swipe_passive);
                _rightSwipeImg.setBackgroundDrawable(drawable);
                raiseOnPageChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Const.REMOVE_BENCHED_PLAYERS, _removeBenchedPlayers);
        if (_market == null) {
            return;
        }
        outState.putSerializable(Const.MARKET, _market);
        if (_roster != null) {
            outState.putSerializable(Const.ROSTER, _roster);
        }
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
                        showWebView("pages/mobile/terms");
                        break;
                    case Rules:
                        showWebView("pages/mobile/rules");
                        break;
                    case Support:
                        showWebView("pages/mobile/support");
                        break;
                    case Settings:
                        showSettingsView();
                        break;
                    case SignOut:
                        signOut();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        _menu = menu;
        return super.onCreateOptionsMenu(menu);
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
    public void onBackPressed() {
        setResult(Const.FINISH_ACTIVITY);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLAYER_CANDIDATE && resultCode == Activity.RESULT_OK) {
//            _roster = (Roster) data.getSerializableExtra(Const.ROSTER);
//            setMoneyTxt(_roster.getRemainingSalary());
//            updatePlayersList();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface IListener{
        public void onHeaderToggle();
        public void onPageChanged(int page);
    }
}