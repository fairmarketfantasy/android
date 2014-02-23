package com.fantasysport.activities;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.adapters.MenuListAdapter;
import com.fantasysport.models.UserData;
import com.fantasysport.views.MenuItem;
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
public class MainActivity extends BaseActivity {

    private DrawerLayout _drawerLayout;
    private ListView _menuList;
    private MenuListAdapter _menuAdapter;
    private boolean _isDrawerLayoutOpened;

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
//        _drawerToogle = new ActionBarDrawerToggle(
//                this,                  /* host Activity */
//                _drawerLayout,         /* DrawerLayout object */
//                R.drawable.invisible_menu,  /* nav drawer icon to replace 'Up' caret */
//                R.string.drawer_open,  /* "open drawer" description */
//                R.string.drawer_close  /* "close drawer" description */
//        );
//        _drawerLayout.setDrawerListener(_drawerToogle);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        _menuList = getViewById(R.id.left_drawer);
        setUserData();
        setMenu();
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        _drawerToogle.syncState();
//    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        _drawerToogle.onConfigurationChanged(newConfig);
//    }

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
}
