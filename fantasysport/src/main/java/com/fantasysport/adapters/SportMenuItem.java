package com.fantasysport.adapters;

import com.fantasysport.models.Category;
import com.fantasysport.models.Sport;

/**
 * Created by bylynka on 5/12/14.
 */
public class SportMenuItem extends MenuItem {

    private Category _category;
    private Sport _sport;

    public SportMenuItem(MenuItemEnum id, String title) {
        super(id, title);
    }

    public Sport getSport(){
        return _sport;
    }

    public Category getCategory(){
        return _category;
    }

    public void setCategory(Category category) {
        _category = category;
    }

    public void setSport(Sport sport) {
        _sport = sport;
    }
}
