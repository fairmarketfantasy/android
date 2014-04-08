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
import com.fantasysport.activities.BaseMainActivity;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.adapters.GameAdapter;
import com.fantasysport.adapters.RosterPlayersAdapter;
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
public abstract class MainActivityFragment extends BaseActivityFragment implements MainActivity.IListener,
        MainFragmentMediator.IMarketListener, MainFragmentMediator.IRemainingSalaryListener,
        MainFragmentMediator.IPlayerAddListener, BaseMainActivity.IRosterLoadedListener,
        BaseMainActivity.IUpdateListener, OnRefreshListener {

    protected TextView _moneyTxt;
    protected GameAdapter _pagerAdapter;
    protected RosterPlayersAdapter _playerAdapter;
    protected ViewPager _pager;
    protected PullToRefreshLayout _pullToRefreshLayout;

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

    protected MainFragmentMediator getFragmentMediator() {
        return getMainActivity().getFragmentMediator();
    }

    protected BaseMainActivity getMainActivity() {
        return (BaseMainActivity) getActivity();
    }

    protected Roster getRoster() {
        return getMainActivity().getRoster();
    }

    protected PredictionRoster getPredictionRoster() {
        return getMainActivity().getPredictionRoster();
    }

    protected void setRoster(Roster roster) {
        getMainActivity().setRoster(roster);
    }

    protected Market getMarket() {
        return getMainActivity().getMarket();
    }

    protected List<Market> getMarkets() {
        return getMainActivity().getMarkets();
    }

    protected void setMarket(Market market) {
        getMainActivity().setMarket(market);
    }

    protected void setCanBenched(boolean canBenched) {
        getMainActivity().setCanBenchedPlayers(canBenched);
    }

    protected boolean canBenched() {
        return getMainActivity().canRemoveBenchedPlayers();
    }

    protected void init() {
        getMainActivity().addListener(this);
        getMainActivity().addUpdateListener(this);
        TextView noGamesLbl = getViewById(R.id.no_games_lbl);
        noGamesLbl.setTypeface(getProhibitionRound());
        _pager = getViewById(R.id.pager);
        _moneyTxt = getViewById(R.id.money_lbl);
        _moneyTxt.setTypeface(getProhibitionRound());
        _pullToRefreshLayout = getViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(_pullToRefreshLayout);
        getMainActivity().addRosterLoadedListener(this);
    }

    protected void setMoneyTxt(double price) {
        _moneyTxt.setText(String.format("$%.0f", price));
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

    private void setElementVisibility(View view, int visibility){
        if(view == null){
            return;
        }
        view.setVisibility(visibility);
    }

    protected void updateMarkets() {
        List<Market> markets = getMarkets();
        _pagerAdapter.setMarkets(markets);
        _pagerAdapter.notifyDataSetChanged();
        setNoGames((markets == null || markets.size() == 0));

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
                GameAdapter(getActivity()

                .
                        getSupportFragmentManager(), markets

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