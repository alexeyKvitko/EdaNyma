package com.edanyma.model;

import android.graphics.drawable.Drawable;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class HomeMenuModel {

    private String leftMenuName;
    private Drawable leftMenuImg;
    private String leftMenuCount;
    private String rightMenuName;
    private Drawable rightMenuImg;
    private String rightMenuCount;

    public HomeMenuModel(String leftMenuName, Drawable leftMenuImg, String leftMenuCount, String rightMenuName, Drawable rightMenuImg, String rightMenuCount) {
        this.leftMenuName = leftMenuName;
        this.leftMenuImg = leftMenuImg;
        this.leftMenuCount = leftMenuCount;
        this.rightMenuName = rightMenuName;
        this.rightMenuImg = rightMenuImg;
        this.rightMenuCount = rightMenuCount;
    }

    public String getLeftMenuName() {
        return leftMenuName;
    }

    public void setLeftMenuName(String leftMenuName) {
        this.leftMenuName = leftMenuName;
    }

    public Drawable getLeftMenuImg() {
        return leftMenuImg;
    }

    public void setLeftMenuImg(Drawable leftMenuImg) {
        this.leftMenuImg = leftMenuImg;
    }

    public String getLeftMenuCount() {
        return leftMenuCount;
    }

    public void setLeftMenuCount(String leftMenuCount) {
        this.leftMenuCount = leftMenuCount;
    }

    public String getRightMenuName() {
        return rightMenuName;
    }

    public void setRightMenuName(String rightMenuName) {
        this.rightMenuName = rightMenuName;
    }

    public Drawable getRightMenuImg() {
        return rightMenuImg;
    }

    public void setRightMenuImg(Drawable rightMenuImg) {
        this.rightMenuImg = rightMenuImg;
    }

    public String getRightMenuCount() {
        return rightMenuCount;
    }

    public void setRightMenuCount(String rightMenuCount) {
        this.rightMenuCount = rightMenuCount;
    }

    @Override public boolean equals(Object o) {
        return Pojomatic.equals(this, o);
    }

    @Override public int hashCode() {
        return Pojomatic.hashCode(this);
    }

    @Override public String toString() {
        return Pojomatic.toString(this);
    }
}
