package com.edanyma.owncomponent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

@SuppressLint( "AppCompatCustomView" )
public class MenuTextView extends TextView {

    private String mMenuTypeId;
    private String mMenuCategoryId;

    public MenuTextView( Context context ) {
        super( context );
    }

    public String getMenuTypeId() {
        return mMenuTypeId;
    }

    public void setMenuTypeId( String menuTypeId ) {
        this.mMenuTypeId = menuTypeId;
    }

    public String getMenuCategoryId() {
        return mMenuCategoryId;
    }

    public void setMenuCategoryId( String menuCategoryId ) {
        this.mMenuCategoryId = menuCategoryId;
    }
}
