package com.fantasysport.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.*;
import com.fantasysport.models.*;
import com.fantasysport.utility.image.ImageLoader;
import com.fantasysport.views.ScrollDisabledListView;
import com.fantasysport.views.Switcher;
import com.fantasysport.views.drawable.BitmapButtonDrawable;
import com.fantasysport.views.listeners.SimpleDrawerListen;
import com.fantasysport.views.listeners.ViewPagerOnPageSelectedListener;
import com.fantasysport.webaccess.requestListeners.AutofillResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SubmitRosterResponseListener;
import com.fantasysport.webaccess.requestListeners.TradePlayerResponseListener;
import com.fantasysport.webaccess.requests.SubmitRosterRequest;
import com.fantasysport.webaccess.responses.AutofillResponse;
import com.fantasysport.webaccess.responses.TradePlayerResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 2/11/14.
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final int PLAYER_CANDIDATE = 123;
    private DrawerLayout _drawerLayout;
    private ListView _menuList;
    private MenuListAdapter _menuAdapter;
    private boolean _isDrawerLayoutOpened;
    private GameAdapter _pagerAdapter;
    private RosterPlayersAdapter _playerAdapter;
    private Market _market;
    private Roster _roster;
    private Switcher _switcher;
    private TextView _moneyTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _isDrawerLayoutOpened = false;
        setContentView(R.layout.activity_main);

        Button submit100fbBtn = getViewById(R.id.submit_100fb_btn);
        submit100fbBtn.setTypeface(getProhibitionRound());
        submit100fbBtn.setOnClickListener(_submitClickListenere);

        Button submitHth27fb = getViewById(R.id.submit_hth_btn);
        submitHth27fb.setTypeface(getProhibitionRound());
        submitHth27fb.setOnClickListener(_submitClickListenere);

        Button autofillBtn = getViewById(R.id.autofill_btn);
        autofillBtn.setTypeface(getProhibitionRound());
        autofillBtn.setOnClickListener(_autofillClickListener);

        _switcher = getViewById(R.id.switcher);
        _switcher.setSelected(true);

        _moneyTxt = getViewById(R.id.money_lbl);

        _drawerLayout = getViewById(R.id.drawer_layout);
        _drawerLayout.setDrawerListener(_drawerListener);
        getSupportActionBar().setHomeButtonEnabled(true);
        _menuList = getViewById(R.id.left_drawer);
        setUserData();
        setMenu();
        setRoster();
        setPager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (_market == null) {
            return;
        }
        outState.putSerializable(Const.MARKET, _market);
        if (_roster != null) {
            outState.putSerializable(Const.ROSTER, _roster);
        }
    }

    private void setRoster() {
        _moneyTxt.setTypeface(getProhibitionRound());
        ScrollDisabledListView rosterList = getViewById(R.id.roster_list);
        rosterList.setOnItemClickListener(this);
        List<IPlayer> items = new ArrayList<IPlayer>();
        _playerAdapter = new RosterPlayersAdapter(items, this);
        rosterList.setAdapter(_playerAdapter);
        _playerAdapter.setListener(_playerAdapterListener);
    }

    RosterPlayersAdapter.IListener _playerAdapterListener = new RosterPlayersAdapter.IListener() {
        @Override
        public void onTrade(final Player player) {
            showProgress();
            _webProxy.tradePlayer(_roster.getId(), player, new TradePlayerResponseListener() {
                @Override
                public void onRequestError(RequestError error) {
                    dismissProgress();
                    showAlert(getString(R.string.error), error.getMessage());
                }

                @Override
                public void onRequestSuccess(TradePlayerResponse response) {
                    double salary = _roster.getRemainingSalary();
                    _roster.getPlayers().remove(player);
                    salary += response.getPrice();
                    _roster.setRemainingSalary(salary);
                    updatePlayersList();
                    dismissProgress();
                }
            });
        }
    };

    private void setRoster(Market market) {
        _market = market;
        setEmptyRoster();
    }

    private void setEmptyRoster() {
        DefaultRosterData rosterData = _storage.getDefaultRosterData();
        setMoneyTxt(rosterData.getRemainingSalary());
        List<IPlayer> items = _playerAdapter.getItems();
        items.clear();
        List<String> positions = rosterData.getPositions();
        for (int i = 0; i < positions.size(); i++) {
            items.add(new PlayerItem(positions.get(i)));
        }
        _playerAdapter.notifyDataSetChanged();
    }

    private void setPager() {
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
                int curItem = pager.getCurrentItem();
                if (curItem > 0) {
                    pager.setCurrentItem(curItem - 1, true);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curItem = pager.getCurrentItem();
                int lastItem = _pagerAdapter.getCount() - 1;
                if (curItem < lastItem) {
                    pager.setCurrentItem(curItem + 1, true);
                }
            }
        });
        if (markets != null && markets.size() > 0) {
            setRoster(markets.get(0));
        }
    }

    ViewPager.OnPageChangeListener _pageChangeListener = new ViewPagerOnPageSelectedListener() {
        @Override
        public void onPageSelected(int position) {
            List<Market> markets = _pagerAdapter.getMarkets();
            if (position < 0 || markets == null) {
                return;
            }
            setRoster(markets.get(position));
        }
    };

    private void setPagerNavigateButton(Button button, int drawableId) {
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(drawableId);
        BitmapButtonDrawable btnDrawable = new BitmapButtonDrawable(drawable.getBitmap(), Color.rgb(137, 137, 137), Color.rgb(117, 117, 117));
        button.setBackgroundDrawable(btnDrawable);
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
                toggleDrawerState();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            toggleDrawerState();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUserData() {
        setUserImage();
        UserData userData = _storage.getUserData();
        TextView userNameTxt = getViewById(R.id.user_name_txt);
        userNameTxt.setText(userData.getRealName());
        TextView userRegTxt = getViewById(R.id.user_reg_txt);
        Date regDate = userData.getRegistrationdDate();
        userRegTxt.setText(String.format(getString(R.string.member_since_f, regDate, regDate, regDate)));
        TextView pointsTxt = getViewById(R.id.user_points_txt);
        pointsTxt.setText(String.format(getString(R.string.points_f, userData.getTotalPoints())));
        TextView winsTxt = getViewById(R.id.user_wins_txt);
        winsTxt.setText(String.format(getString(R.string.wins_f, userData.getTotalWins(), userData.getWinPercentile())));
    }

    @Override
    public void onBackPressed() {
        setResult(Const.FINISH_ACTIVITY);
        super.onBackPressed();
    }

    private void setUserImage() {
        final String userImgUrl = _storage.getUserData().getUserImageUrl();
        final ImageView view = getViewById(R.id.user_img);
        if (userImgUrl == null) {
            return;
        }
        ImageLoader loader = new ImageLoader(this);
        loader.displayImage(userImgUrl, view);
    }

    private void toggleDrawerState() {
        if (_isDrawerLayoutOpened) {
            _drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            _drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    SimpleDrawerListen _drawerListener = new SimpleDrawerListen() {
        @Override
        public void onStateChanged(boolean isOpened) {
            _isDrawerLayoutOpened = isOpened;
        }
    };

    private void updatePlayersList() {
        if (_roster == null) {
            return;
        }
        setMoneyTxt(_roster.getRemainingSalary());
        List<Player> players = _roster.getPlayers();
        List<IPlayer> playerItems = _playerAdapter.getItems();
        for (int i = 0; i < playerItems.size(); i++) {
            boolean updated = false;
            IPlayer iPlayer = playerItems.get(i);
            for (int j = 0; j < players.size(); j++) {
                Player player = players.get(j);
                if (iPlayer.getPosition().compareToIgnoreCase(player.getPosition()) == 0) {
                    playerItems.set(i, player);
                    updated = true;
                }
            }
            if (!updated && iPlayer instanceof Player) {
                playerItems.set(i, new PlayerItem(iPlayer.getPosition()));
            }
        }
        _playerAdapter.notifyDataSetChanged();
    }

    public void setMoneyTxt(double price) {
        _moneyTxt.setText(String.format("$%.0f", price));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLAYER_CANDIDATE && resultCode == Activity.RESULT_OK) {
            _roster = (Roster) data.getSerializableExtra(Const.ROSTER);
            setMoneyTxt(_roster.getRemainingSalary());
            updatePlayersList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void navigateToPlayersActivity(String playerPosition) {
        Intent intent = new Intent(this, PlayersActivity.class);
        intent.putExtra(Const.PLAYER_POSITION, playerPosition);
        intent.putExtra(Const.ROSTER, _roster);
        intent.putExtra(Const.MARKET_ID, _market.getId());
        intent.putExtra(Const.REMOVE_BENCHED_PLAYERS, _switcher.isSelected());
        DefaultRosterData rosterData = _storage.getDefaultRosterData();
        double moneyFoster = _roster != null ? _roster.getRemainingSalary() : rosterData.getRemainingSalary();
        intent.putExtra(Const.MONEY_FOR_ROSTER, moneyFoster);
        startActivityForResult(intent, PLAYER_CANDIDATE);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IPlayer item = (IPlayer) _playerAdapter.getItem(position);
        if (item instanceof Player) {
            return;
        }
        navigateToPlayersActivity(item.getPosition());
    }

    View.OnClickListener _autofillClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int rosterId = _roster == null ? -1 : _roster.getId();
            showProgress();
            _webProxy.autofillRoster(_market.getId(), rosterId, new AutofillResponseListener() {
                @Override
                public void onRequestError(RequestError error) {
                    dismissProgress();
                    showAlert(getString(R.string.error), error.getMessage());
                }

                @Override
                public void onRequestSuccess(AutofillResponse response) {
                    _roster = response.getRoster();
                    updatePlayersList();
                    dismissProgress();
                }
            });
        }
    };

    View.OnClickListener _submitClickListenere = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_roster == null) {
                showAlert("", getString(R.string.fill_roster));
                return;
            }
            showProgress();
            String contestType = v.getId() == R.id.submit_100fb_btn ? SubmitRosterRequest.TOP6 : SubmitRosterRequest.H2H;
            _webProxy.submitRoster(_roster.getId(), contestType, new SubmitRosterResponseListener() {
                @Override
                public void onRequestError(RequestError error) {
                    dismissProgress();
                    showAlert(getString(R.string.error), error.getMessage());
                }

                @Override
                public void onRequestSuccess(Object o) {
                    _roster = null;
                    setEmptyRoster();
                    dismissProgress();
                }
            });

        }
    };
}