package com.fantasysport.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import com.fantasysport.activities.MainActivity;

/**
 * Created by bylynka on 6/19/14.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity _activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _activity = getActivity();
    }

    @SmallTest
    public void loginTextViewTest(){
        assertNotNull(null);
    }
}
