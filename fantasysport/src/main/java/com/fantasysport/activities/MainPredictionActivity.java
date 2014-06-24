package com.fantasysport.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.factories.FactoryProvider;
import com.fantasysport.fragments.main.fantasy.BaseFantasyFragment;
import com.fantasysport.fragments.main.IMainFragment;

/**
 * Created by bylynka on 3/24/14.
 */
public class MainPredictionActivity extends BaseActivity implements BaseFantasyFragment.IPageChangedListener, IMainActivity {

    protected IMainFragment _rootFragment;
    private final String _fragmentName = "root_fragment";

    protected ImageView _leftSwipeImg;
    protected ImageView _rightSwipeImg;
    private int _fantasyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_main);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initStartParams(savedInstanceState);
        _sportFactory = FactoryProvider.getFactory(_fantasyType);
        if (savedInstanceState == null) {
            _rootFragment = _sportFactory.getPredictionFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_holder, (Fragment) _rootFragment, _fragmentName)
                    .commit();
            _rootFragment.addPageChangedListener(this);
        }
        _leftSwipeImg = getViewById(R.id.left_point_img);
        _rightSwipeImg = getViewById(R.id.right_point_img);
        setPageIndicator(0);
    }

    private void initStartParams(Bundle savedInstanceState){
        if(savedInstanceState == null){
            _fantasyType = getIntent().getIntExtra(Const.CATEGORY_TYPE, Const.FANTASY_SPORT);
        }else {
            _fantasyType = savedInstanceState.getInt(Const.CATEGORY_TYPE, Const.FANTASY_SPORT);
        }
    }

    protected void setPageIndicator(int position) {
        Drawable drawable = position == 0 ? getResources().getDrawable(R.drawable.swipe_active) : getResources().getDrawable(R.drawable.swipe_passive);
        _leftSwipeImg.setBackgroundDrawable(drawable);
        drawable = position != 0 ? getResources().getDrawable(R.drawable.swipe_active) : getResources().getDrawable(R.drawable.swipe_passive);
        _rightSwipeImg.setBackgroundDrawable(drawable);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageChanged(int page) {
        setPageIndicator(page);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, _fragmentName,(Fragment)_rootFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
        _rootFragment = (IMainFragment)getSupportFragmentManager().getFragment(inState, _fragmentName);
        _rootFragment.addPageChangedListener(this);
    }

    @Override
    public IMainFragment getRootFragment() {
        return _rootFragment;
    }
}
