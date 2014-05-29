package com.fantasysport.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.MenuItem;
import com.fantasysport.adapters.MenuListAdapter;
import com.fantasysport.adapters.SportMenuItem;
import com.fantasysport.factories.FactoryProvider;
import com.fantasysport.fragments.MenuHeaderFragment;
import com.fantasysport.fragments.main.BaseFantasyFragment;
import com.fantasysport.fragments.main.FantasyFragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.models.Category;
import com.fantasysport.models.Sport;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.UserResponseListener;


/**
 * Created by bylynka on 2/11/14.
 */
public class MainActivity extends BaseActivity implements BaseFantasyFragment.IPageChangedListener, IMainActivity{

    private final String _fragmentName = "root_fragment";

    protected ImageView _leftSwipeImg;
    protected ImageView _rightSwipeImg;
    protected IMainFragment _rootFragment;


    private DrawerLayout _drawerLayout;
    private ActionBarDrawerToggle _drawerToggle;
    private MenuListAdapter _menuAdapter;
    protected ExpandableListView _menuList;
    protected MenuHeaderFragment _menuHeaderFragment;
    private android.view.MenuItem _refreshMenuItem;
    private int _fantasyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initStartParams(savedInstanceState);
        _sportFactory = FactoryProvider.getFactory(_fantasyType);
        if (savedInstanceState == null) {
            _rootFragment = _sportFactory.getMainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_holder, (Fragment) _rootFragment, _fragmentName)
                    .commit();
            _rootFragment.addPageChangedListener(this);
        }

        _leftSwipeImg = getViewById(R.id.left_point_img);
        _rightSwipeImg = getViewById(R.id.right_point_img);
        setPageIndicator(0);

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

    private void initStartParams(Bundle savedInstanceState){
        if(savedInstanceState == null){
            _fantasyType = getIntent().getIntExtra(Const.CATEGORY_TYPE, Const.FANTASY_SPORT);
        }else {
            _fantasyType = savedInstanceState.getInt(Const.CATEGORY_TYPE, Const.FANTASY_SPORT);
        }
    }


    public MenuHeaderFragment getMenuHeaderFragment(){
        return _menuHeaderFragment;
    }

    public IMainFragment getRootFragment(){
        return _rootFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, _fragmentName,(Fragment)_rootFragment);
        outState.putInt(Const.CATEGORY_TYPE, _fantasyType);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
        _rootFragment = (IMainFragment)getSupportFragmentManager().getFragment(inState, _fragmentName);
        _rootFragment.addPageChangedListener(this);
    }

    protected void setPageIndicator(int position) {
        Drawable drawable = position == 0 ? getResources().getDrawable(R.drawable.swipe_active) : getResources().getDrawable(R.drawable.swipe_passive);
        _leftSwipeImg.setBackgroundDrawable(drawable);
        drawable = position != 0 ? getResources().getDrawable(R.drawable.swipe_active) : getResources().getDrawable(R.drawable.swipe_passive);
        _rightSwipeImg.setBackgroundDrawable(drawable);
    }

    @Override
    public void onBackPressed() {
        setResult(Const.FINISH_ACTIVITY);
        super.onBackPressed();
    }

    @Override
    public void onPageChanged(int page) {
        setPageIndicator(page);
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
       String sport = _storage.getUserData().getSport();
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
                        UserData udata = _storage.getUserData();
                        showWebView(String.format("pages/mobile/rules?sport=%s&category=%s", udata.getSport(), udata.getCategory()), "Rules");
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
                        showPredictionList(_fantasyType);
                        break;
                    case Sport:
                        SportMenuItem sportItem = (SportMenuItem)item;
                        Sport sport = sportItem.getSport();
                        Category category = sportItem.getCategory();
                        if(sport.comingSoon()){
                            showAlert(sport.getName(), getString(R.string.coming_soon));
                        }else {
                            UserData data = _storage.getUserData();
                            data.setCurrentSport(sport.getName());
                            data.setCurrentCategory(category.getName());
                            _menuAdapter.setMenu(data);
                            _menuAdapter.notifyDataSetChanged();
                            updateMenuHeaderImage(header);
                            updateRootFragment();
                            break;
                        }
                }
            }
        });
    }

    private void updateRootFragment(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_holder, (Fragment) _rootFragment, _fragmentName)
                .commit();
        _rootFragment.addPageChangedListener(this);
        _rootFragment.updateMainData();
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

    protected void updateUserData() {
        UserData userData = _storage.getUserData();
        int userId = userData.getId();
        String sport = userData.getSport();
        String cat = userData.getCategory();
        showProgress();
        getWebProxy().getUserData(userId, sport, cat, new UserResponseListener() {
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

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.refresh:
                _rootFragment.updateMainData();
                updateUserData();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.SETTINGS_ACTIVITY && _menuHeaderFragment != null) {
            _menuHeaderFragment.updateView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        long nowTime = DateUtils.getCurrentDate().getTime();
        long gamesTime = _storage.getUserData().getUpdatedAt();
        long deltaTime = nowTime - gamesTime;
        long deltaTimeInMin = deltaTime / 60000;
        if (deltaTimeInMin > 35){
            updateUserData();
        }
    }
}