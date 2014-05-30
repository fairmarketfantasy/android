package com.fantasysport.fragments.main;

import com.fantasysport.models.NFData;

/**
 * Created by bylynka on 5/28/14.
 */
public interface INFMainFragment {

    NFData getData();
    boolean isEditable();
    boolean isPredicted();

}
