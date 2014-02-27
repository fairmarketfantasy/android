package com.fantasysport.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.GameAdapter;
import com.fantasysport.adapters.MenuListAdapter;
import com.fantasysport.adapters.PlayerItem;
import com.fantasysport.adapters.RosterPlayersAdapter;
import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.models.UserData;
import com.fantasysport.views.Drawable.BitmapButtonDrawable;
import com.fantasysport.views.MenuItem;
import com.fantasysport.webaccess.RequestListeners.CreateRosterResponseListener;
import com.fantasysport.webaccess.RequestListeners.RequestError;
import com.fantasysport.webaccess.WebProxy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 2/11/14.
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout _drawerLayout;
    private ListView _menuList;
    private MenuListAdapter _menuAdapter;
    private boolean _isDrawerLayoutOpened;
    private GameAdapter _pagerAdapter;
    private RosterPlayersAdapter _playerAdapter;
    private Market _currentMarket;
    private Roster _currentRoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isDrawerLayoutOpened = false;
        setContentView(R.layout.activity_main);

        Button myGamesBtn = getViewById(R.id.my_games_btn);
        myGamesBtn.setTypeface(getProhibitionRound());

        Button createGameBtn = getViewById(R.id.create_games_btn);
        createGameBtn.setTypeface(getProhibitionRound());

        Button autofillBtn = getViewById(R.id.autofill_btn);
        autofillBtn.setTypeface(getProhibitionRound());

        _drawerLayout = getViewById(R.id.drawer_layout);
        _drawerLayout.setDrawerListener(_drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true);
        _menuList = getViewById(R.id.left_drawer);
        setUserData();
        setMenu();
        setRoster();
        setPager();
    }

    private void setRoster(){
        TextView moneyTxt = getViewById(R.id.money_lbl);
        moneyTxt.setTypeface(getProhibitionRound());
        ListView rosterList = getViewById(R.id.roster_list);
        rosterList.setOnItemClickListener(this);
        List<PlayerItem> items = new ArrayList<PlayerItem>();
        _playerAdapter = new RosterPlayersAdapter(items, this);
        rosterList.setAdapter(_playerAdapter);
    }

    private void setRoster(Market market){
        _currentMarket = market;
        setEmptyRoster();
    }

    private void setEmptyRoster(){
        DefaultRosterData rosterData = _storage.getDefaultRosterData();
        TextView moneyTxt = getViewById(R.id.money_lbl);
        moneyTxt.setText("$" + rosterData.getRemainingSalary());
        List<PlayerItem> items =  _playerAdapter.getItems();
        items.clear();
        List<String> positions = rosterData.getPositions();
        for (int i = 0; i < positions.size(); i++){
            items.add(new PlayerItem(positions.get(i)));
        }
        _playerAdapter.notifyDataSetChanged();
    }

    private void setPager(){
        final ViewPager pager = getViewById(R.id.pager);
        List<Market> markets = _storage.getMarkets();
        _pagerAdapter = new GameAdapter(getSupportFragmentManager(), markets);
        pager.setOnPageChangeListener(_pageChangeListener);
        pager.setAdapter(_pagerAdapter);
        Button prevBtn = getViewById(R.id.previous_pager_button);
        setPagerNavigateButton(prevBtn, R.drawable.ic_action_previous_item);
        Button nextBtn = getViewById(R.id.next_pager_button);
        setPagerNavigateButton(nextBtn, R.drawable.ic_action_next_item);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int curItem =  pager.getCurrentItem();
                if(curItem > 0){
                    pager.setCurrentItem(curItem - 1, true);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curItem =  pager.getCurrentItem();
                int lastItem = _pagerAdapter.getCount() - 1;
                if(curItem < lastItem){
                    pager.setCurrentItem(curItem + 1, true);
                }
            }
        });
        if(markets != null && markets.size() > 0){
            setRoster(markets.get(0));
        }
    }

    ViewPager.OnPageChangeListener _pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int position) {
            List<Market> markets = _pagerAdapter.getMarkets();
            if(position < 0 || markets == null){
                return;
            }
            setRoster(markets.get(position));
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void setPagerNavigateButton(Button button, int drawableId){
        BitmapDrawable drawable =  (BitmapDrawable)getResources().getDrawable(drawableId);
        BitmapButtonDrawable btnDrawable = new BitmapButtonDrawable(drawable.getBitmap(), Color.rgb(137, 137, 137), Color.rgb(117, 117, 117));
        button.setBackgroundDrawable(btnDrawable);
    }

    private void setMenu(){
       String[] menuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        for (int i = 0; i < menuTitles.length; i++){
            menuItems.add(new MenuItem(i, menuTitles[i]));
        }
        _menuAdapter = new MenuListAdapter(menuItems, this);
        _menuList.setAdapter(_menuAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            toggleDrawerState();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUserData(){
        setUserImage();

        UserData userData = _storage.getUserData();
        TextView userNameTxt = getViewById(R.id.user_name_txt);
        userNameTxt.setText(userData.getRealName());
        TextView userRegTxt = getViewById(R.id.user_reg_txt);
        Date regDate = userData.getRegistrationdDate();
        userRegTxt.setText(String.format(getString(R.string.member_since_f, regDate,regDate,regDate)));
        TextView pointsTxt = getViewById(R.id.user_points_txt);
        pointsTxt.setText(String.format(getString(R.string.points_f, userData.getTotalPoints())));
        TextView winsTxt = getViewById(R.id.user_wins_txt);
        winsTxt.setText(String.format(getString(R.string.wins_f,userData.getTotalWins(), userData.getWinPercentile())));
    }

    @Override
    public void onBackPressed() {
        setResult(Const.FINISH_ACTIVITY);
        super.onBackPressed();
    }

    private Bitmap downloadBitmap(String url) {
        final DefaultHttpClient client = new DefaultHttpClient();
        Bitmap image = null;
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                inputStream = entity.getContent();
                image = BitmapFactory.decodeStream(inputStream);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }
        return image;
    }

    private void setUserImage(){
        final String userImgUrl = _storage.getUserData().getUserImageUrl();
        final ImageView view = getViewById(R.id.user_img);
        if(userImgUrl == null){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                  final Bitmap btm =  downloadBitmap(userImgUrl);
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(btm == null){
                            return;
                        }
                        view.setImageBitmap(btm);
                    }
                });
            }
        }).start();
    }

    private void toggleDrawerState(){
        if(_isDrawerLayoutOpened){
            _drawerLayout.closeDrawer(Gravity.LEFT);
        }else {
            _drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    DrawerLayout.DrawerListener _drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View view, float v) {

        }

        @Override
        public void onDrawerOpened(View view) {
            _isDrawerLayoutOpened = true;
        }

        @Override
        public void onDrawerClosed(View view) {
            _isDrawerLayoutOpened = false;
        }

        @Override
        public void onDrawerStateChanged(int i) {

        }
    };

    private void navigateToPlayersActivity(String playerPosition){
        Intent intent = new Intent(this, PlayersActivity.class);
        intent.putExtra(Const.PLAYER_POSITION, playerPosition);
        intent.putExtra(Const.ROSTER_ID,(_currentRoster == null? -1:_currentRoster.getId()));
        intent.putExtra(Const.MARKET_ID, _currentMarket.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlayerItem item = (PlayerItem)_playerAdapter.getItem(position);
        navigateToPlayersActivity(item.getPosition());
    }
}