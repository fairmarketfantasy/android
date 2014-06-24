package com.fantasysport.fragments.main.nonfantasy;

import com.fantasysport.models.nonfantasy.NFData;

/**
 * Created by bylynka on 5/28/14.
 */
public interface INFMainFragment {

    NFData getData();
    boolean isEditable();
    boolean isPredicted();
    boolean canSubmit();

}
