package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.image.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bylynka on 4/3/14.
 */
public class MenuHeaderFragment extends BaseActivityFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_header_menu, container, false);
        updateView();
        return _rootView;
    }

    public void updateView(){
        setUserImage();
        UserData userData = _storage.getUserData();
        TextView pointslbl = getViewById(R.id.funbucks_lbl);
        pointslbl.setText(String.format("%.2f", (double)(userData.getFanBucks()/100)));
        TextView balansLbl = getViewById(R.id.balance_lbl);
        balansLbl.setText(String.format("$%.2f",(double)(userData.getBalance()/100)));
        TextView winsLbl = getViewById(R.id.wins_lbl);
        winsLbl.setText(String.format("%d", userData.getTotalWins()));
        TextView prestigelbl = getViewById(R.id.prestige_lbl);
        prestigelbl.setText(Integer.toString(userData.getPrestige()));
        TextView userRegTxt = getViewById(R.id.user_reg_txt);
        Date regDate = userData.getRegistrationdDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        userRegTxt.setText(String.format(getString(R.string.member_since_f, sdf.format(regDate))));
        TextView userNameTxt = getViewById(R.id.user_name_txt);
        userNameTxt.setText(userData.getRealName());
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
}
