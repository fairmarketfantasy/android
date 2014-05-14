package com.fantasysport.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.activities.IMainActivity;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.adapters.GameAdapter;
import com.fantasysport.adapters.RosterPlayersAdapter;
import com.fantasysport.fragments.main.BaseFantasyFragment;
import com.fantasysport.fragments.main.FantasyFragment;
import com.fantasysport.models.Market;
import com.fantasysport.models.Player;
import com.fantasysport.models.Roster;
import com.fantasysport.views.drawable.BitmapButtonDrawable;
import com.fantasysport.views.listeners.ViewPagerOnPageSelectedListener;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/17/14.
 */
public abstract class MainActivityFragment extends BaseActivityFragment implements BaseFantasyFragment.IPageChangedListener,
        MainFragmentMediator.IMarketListener, MainFragmentMediator.IRemainingSalaryListener,
        MainFragmentMediator.IPlayerAddListener, BaseFantasyFragment.IRosterLoadedListener,
        BaseFantasyFragment.IUpdateListener, OnRefreshListener {

    protected TextView _moneyTxt;
    protected GameAdapter _pagerAdapter;
    protected RosterPlayersAdapter _playerAdapter;
    protected ViewPager _pager;
    protected PullToRefreshLayout _pullToRefreshLayout;

    public MainActivityFragment(){
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getFragmentMediator().addMarketListener(this);
        getFragmentMediator().addRemainingSalaryListener(this);
        getFragmentMediator().addPlayerAdListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public BaseFantasyFragment getBaseFFragment(){
        return  (BaseFantasyFragment)getMainActivity().getRootFragment();
    }

    public IMainActivity getMainActivity(){
        return (IMainActivity)getActivity();
    }

    protected MainFragmentMediator getFragmentMediator() {
        return getBaseFFragment().getFragmentMediator();
    }


    protected Roster getRoster() {
        return getBaseFFragment().getRoster();
    }

    protected PredictionRoster getPredictionRoster() {
        return getBaseFFragment().getPredictionRoster();
    }

    protected void setRoster(Roster roster) {
        getBaseFFragment().setRoster(roster);
    }

    protected Market getMarket() {
        return getBaseFFragment().getMarket();
    }

    protected List<Market> getMarkets() {
        return getBaseFFragment().getMarkets();
    }

    protected void setMarket(Market market) {
        getBaseFFragment().setMarket(market);
    }

    protected void setCanBenched(boolean canBenched) {
        getBaseFFragment().setCanBenchedPlayers(canBenched);
    }

    protected boolean canBenched() {
        return getBaseFFragment().canRemoveBenchedPlayers();
    }

    protected void init() {
        getBaseFFragment().addPageChangedListener(this);
        getBaseFFragment().addUpdateListener(this);
        _pager = getViewById(R.id.pager);
        _moneyTxt = getViewById(R.id.money_lbl);
        _pullToRefreshLayout = getViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(_pullToRefreshLayout);
        getBaseFFragment().addRosterLoadedListener(this);
    }

    protected void setMoneyTxt(double price) {
        if(_moneyTxt == null){
            return;
        }
        _moneyTxt.setText(String.format("$%.0f", price));
        _moneyTxt.setTextColor(price > 0?Color.parseColor("#6FA648"):Color.parseColor("#E44B3D"));
    }

    protected void remainingSalaryChanged(double remainingSalary) {
        Roster roster = getRoster();
        roster.setRemainingSalary(remainingSalary);
        getFragmentMediator().changeRemainingSalary(this, remainingSalary);
    }

    protected void addPlayerToRoster(Player player) {
        Roster roster = getRoster();
        List<Player> players = roster.getPlayers();
        if (player == null) {
            players = new ArrayList<Player>();
        }
        players.add(player);
        roster.setPlayers(players);
        getFragmentMediator().addPlayer(this, player);
    }


    protected void setNoGames(boolean noGames) {
        TextView noGamesLbl = getViewById(R.id.no_games_lbl);
        View bottomBar = getViewById(R.id.bottom_bar);
        if (noGames) {
            setElementVisibility(noGamesLbl, View.VISIBLE);
            setElementVisibility(bottomBar, View.GONE);
        } else {
            setElementVisibility(noGamesLbl, View.INVISIBLE);
            setElementVisibility(bottomBar, View.VISIBLE);
        }
    }

    private void setElementVisibility(View view, int visibility) {
        if (view == null) {
            return;
        }
        view.setVisibility(visibility);
    }

    protected void updateMarkets() {
        List<Market> markets = getMarkets();
        _pagerAdapter.setMarkets(markets);
        _pagerAdapter.notifyDataSetChanged();
        setNoGames((markets == null || markets.size() == 0));
        setMarket(null);

    }

    protected void setPager(int id) {
        List<Market> markets = getMarkets();
        if (markets == null || markets.size() == 0) {
            setNoGames(true);
        } else {
            setNoGames(false);
        }
        _pager.setId(id);
        _pagerAdapter = new
                GameAdapter(getActivity().getSupportFragmentManager(), markets

        );
        _pager.setOnPageChangeListener(_pageChangeListener);
        _pager.setAdapter(_pagerAdapter);
        Button prevBtn = getViewById(R.id.previous_pager_button);

        setPagerNavigateButton(prevBtn, R.drawable.ic_action_previous_item);

        Button nextBtn = getViewById(R.id.next_pager_button);

        setPagerNavigateButton(nextBtn, R.drawable.ic_action_next_item);

        prevBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                int curItem = _pager.getCurrentItem();
                if (curItem > 0) {
                    _pager.setCurrentItem(curItem - 1, false);
                }
            }
        }

        );

        nextBtn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                int curItem = _pager.getCurrentItem();
                int lastItem = _pagerAdapter.getCount() - 1;
                if (curItem < lastItem) {
                    _pager.setCurrentItem(curItem + 1, false);
                }
            }
        }

        );
    }

    ViewPager.OnPageChangeListener _pageChangeListener = new ViewPagerOnPageSelectedListener() {
        @Override
        public void onPageSelected(int position) {
            List<Market> markets = _pagerAdapter.getMarkets();
            if (position < 0 || markets == null) {
                return;
            }
            getFragmentMediator().changeMarket(MainActivityFragment.this, markets.get(position));
//            setNewRoster(markets.get(position));
        }
    };

    private void setPagerNavigateButton(Button button, int drawableId) {
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(drawableId);
        BitmapButtonDrawable btnDrawable = new BitmapButtonDrawable(drawable.getBitmap(), Color.rgb(137, 137, 137), Color.rgb(117, 117, 117));
        button.setBackgroundDrawable(btnDrawable);
    }

    @Override
    public void onRemainingSalaryChanged(Object sender, double remainingSalary) {
        setMoneyTxt(remainingSalary);
    }

    @Override
    public abstract void onMarketChanged(Object sender, Market market);

    @Override
    public abstract void onPlayerAdded(Object sender, Player player);
}