package com.fantasysport.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.activities.BaseMainActivity;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.adapters.GameAdapter;
import com.fantasysport.adapters.RosterPlayersAdapter;
import com.fantasysport.models.Market;
import com.fantasysport.models.Player;
import com.fantasysport.models.Roster;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.image.ImageLoader;
import com.fantasysport.views.LockableScrollView;
import com.fantasysport.views.drawable.BitmapButtonDrawable;
import com.fantasysport.views.listeners.ViewPagerOnPageSelectedListener;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 3/17/14.
 */
public abstract class MainActivityFragment extends BaseActivityFragment implements MainActivity.IListener,
        MainFragmentMediator.IMarketListener, MainFragmentMediator.IRemainingSalaryListener,
        MainFragmentMediator.IPlayerAddListener, BaseMainActivity.IRosterLoadedListener,
        BaseMainActivity.IUpdateListener, OnRefreshListener, BaseMainActivity.IAvatarListener {

    protected TextView _moneyTxt;
    protected View _headerView;
    protected FrameLayout _listWrapper;
    protected LockableScrollView _scrollView;
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

    protected MainFragmentMediator getFragmentMediator(){
        return  getMainActivity().getFragmentMediator();
    }

    protected BaseMainActivity getMainActivity(){
        return (BaseMainActivity)getActivity();
    }

    protected Roster getRoster(){
        return getMainActivity().getRoster();
    }

    protected PredictionRoster getPredictionRoster(){
        return getMainActivity().getPredictionRoster();
    }

    protected void setRoster(Roster roster){
        getMainActivity().setRoster(roster);
    }

    protected Market getMarket(){
        return getMainActivity().getMarket();
    }

    protected List<Market> getMarkets(){
        return getMainActivity().getMarkets();
    }

    protected void setMarket(Market market){
        getMainActivity().setMarket(market);
    }

    protected void setCanBenched(boolean canBenched){
        getMainActivity().setCanBenchedPlayers(canBenched);
    }

    protected boolean canBenched(){
        return getMainActivity().canRemoveBenchedPlayers();
    }

    protected void init(){
        getMainActivity().addListener(this);
        getMainActivity().addUpdateListener(this);
        getMainActivity().addAvatarListener(this);
        _pager = getViewById(R.id.pager);
        _moneyTxt = getViewById(R.id.money_lbl);
        _moneyTxt.setTypeface(getProhibitionRound());
        _listWrapper = getViewById(R.id.list_wrapper);
        _scrollView = getViewById(R.id.scroll_view);
        _pullToRefreshLayout = getViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(_pullToRefreshLayout);
        getMainActivity().addRosterLoadedListener(this);
        setUserData();
    }

    protected void setMoneyTxt(double price) {
        _moneyTxt.setText(String.format("$%.0f", price));
    }

    protected void setUserData() {
        setUserImage();
        UserData userData = _storage.getUserData();
        TextView userNameTxt = getViewById(R.id.user_name_txt);
        userNameTxt.setText(userData.getRealName());
        TextView userRegTxt = getViewById(R.id.user_reg_txt);
        Date regDate = userData.getRegistrationdDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        userRegTxt.setText(String.format(getString(R.string.member_since_f, sdf.format(regDate))));
        TextView pointsTxt = getViewById(R.id.user_points_txt);
        pointsTxt.setText(String.format(getString(R.string.points_f, userData.getTotalPoints())));
        TextView winsTxt = getViewById(R.id.user_wins_txt);
        String wins = String.format(getString(R.string.wins_f, userData.getTotalWins(), userData.getWinPercentile())) + " %)";
        winsTxt.setText(wins);
        TextView ballanceTxt = getViewById(R.id.ballance_txt);
        ballanceTxt.setText(String.format("%.2f",(double)(userData.getBalance()/100)));
    }

    private void setUserImage() {
        final String userImgUrl = _storage.getUserData().getUserImageUrl();
        final ImageView view = getViewById(R.id.user_img);
        if (userImgUrl == null) {
            return;
        }
        ImageLoader loader = new ImageLoader(getActivity());
        loader.displayImage(userImgUrl, view);
    }

    protected void initHeaderView() {
        _headerView = getViewById(R.id.header_view);
        ViewTreeObserver observer = _headerView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    _headerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    _headerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                setListWrapper();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        toggleHeaderView();
                    }
                }, 1000);
            }
        });
    }

    private void setListWrapper() {
        final int padding = _headerView.getHeight();
        int height = _listWrapper.getHeight();
        _listWrapper.getLayoutParams().height = height + padding;
        getActivity().getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
    }

    private void toggleHeaderView() {
        final int height = _headerView.getHeight();
        final int scrollTo = _scrollView.getYPosition() == 0 ? height : 0;
        final int padding = _scrollView.getYPosition() == 0 ? 0 : height;
        _scrollView.setScrollingEnabled(true);
        if (scrollTo != 0) {
            _listWrapper.setPadding(0, 0, 0, padding);
            getActivity().getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        }
        _scrollView.setListener(new LockableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(LockableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y != scrollTo) {
                    return;
                }
                _scrollView.setListener(null);
                _scrollView.setScrollingEnabled(false);
                if (scrollTo == 0) {
                    _listWrapper.setPadding(0, 0, 0, padding);
                    getActivity().getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                }
            }
        });
        _scrollView.smoothScrollTo(0, scrollTo);
    }

    @Override
    public void onHeaderToggle() {
        toggleHeaderView();
    }

    protected void remainingSalaryChanged(double remainingSalary){
        Roster roster = getRoster();
        roster.setRemainingSalary(remainingSalary);
        getFragmentMediator().changeRemainingSalary(this, remainingSalary);
    }

    protected void addPlayerToRoster(Player player){
        Roster roster = getRoster();
        List<Player> players = roster.getPlayers();
        if(player == null){
            players = new ArrayList<Player>();
        }
        players.add(player);
        roster.setPlayers(players);
        getFragmentMediator().addPlayer(this, player);
    }

    protected void updateMarkets(){
        List<Market> markets = getMarkets();
        _pagerAdapter.setMarkets(markets);
        _pagerAdapter.notifyDataSetChanged();
    }

    protected void setPager(int id) {
        List<Market> markets = getMarkets();
        _pager.setId(id);
        _pagerAdapter = new GameAdapter(getActivity().getSupportFragmentManager(), markets);
        _pager.setOnPageChangeListener(_pageChangeListener);
        _pager.setAdapter(_pagerAdapter);
        Button prevBtn = getViewById(R.id.previous_pager_button);
        setPagerNavigateButton(prevBtn, R.drawable.ic_action_previous_item);
        Button nextBtn = getViewById(R.id.next_pager_button);
        setPagerNavigateButton(nextBtn, R.drawable.ic_action_next_item);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curItem = _pager.getCurrentItem();
                if (curItem > 0) {
                    _pager.setCurrentItem(curItem - 1, false);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curItem = _pager.getCurrentItem();
                int lastItem = _pagerAdapter.getCount() - 1;
                if (curItem < lastItem) {
                    _pager.setCurrentItem(curItem + 1, false);
                }
            }
        });
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
    public void onRemainingSalaryChanged(Object sender, double remainingSalary){
        setMoneyTxt(remainingSalary);
    }

    @Override
    public void onAvatarChanged(){
        setUserImage();
    }

    @Override
    public abstract void onMarketChanged(Object sender, Market market);

    @Override
    public abstract void onPlayerAdded(Object sender, Player player);
}
