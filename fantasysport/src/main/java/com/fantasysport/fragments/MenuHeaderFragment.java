package com.fantasysport.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
        init();
        return _rootView;
    }

    private void init() {
        View fanBucksContainer = getViewById(R.id.fanbucks_container);
        fanBucksContainer.setOnClickListener(_fanBucksContainerClickListener);
    }

    public void updateView() {
        setUserImage();
        UserData userData = _storage.getUserData();
        TextView pointslbl = getViewById(R.id.funbucks_lbl);
        pointslbl.setText(String.format("%.2f", (double) (userData.getFanBucks() / 100)));
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

    View.OnClickListener _fanBucksContainerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.account_balance_popup, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            TextView headerLbl = (TextView) popupView.findViewById(R.id.title_lbl);
            headerLbl.setTypeface(getProhibitionRound());

            TextView thisMonthLbl = (TextView) popupView.findViewById(R.id.this_month_lbl);
            thisMonthLbl.setTypeface(getProhibitionRound());
            Button closeBtn = (Button) popupView.findViewById(R.id.close_btn);
            closeBtn.setTypeface(getProhibitionRound());
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            UserData userData = _storage.getUserData();
            TextView pointslbl = (TextView) popupView.findViewById(R.id.funbucks_lbl);
            pointslbl.setText(String.format("%.2f", (double) (userData.getFanBucks() / 100)));

            TextView balansLbl = (TextView) popupView.findViewById(R.id.balance_lbl);
            balansLbl.setTypeface(getProhibitionRound());
            balansLbl.setText(String.format("$%.2f",(double)(userData.getBalance()/100)));

            TextView awardLbl = (TextView) popupView.findViewById(R.id.award_lbl);
            awardLbl.setText(String.format("%.2f", userData.getMonthlyAward()));

            TextView predictionLbl = (TextView) popupView.findViewById(R.id.prediction_lbl);
            predictionLbl.setText(String.format("%d", userData.getMonthlyPredictions()));

            TextView multiplierLbl = (TextView) popupView.findViewById(R.id.multiplier_lbl);
            multiplierLbl.setText(String.format("%.2f", userData.getWinningsMultiplier()));

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        }
    };

}
