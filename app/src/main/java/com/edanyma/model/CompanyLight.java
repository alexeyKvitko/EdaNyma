package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class CompanyLight {

    private String id;
    private String displayName;
    private String thumbUrl;
    private String delivery;
    private String commentCount;
    private String deliveryTimeMin;
    private String weekdayWork;
    private String dayoffWork;
    private Integer feedbackRate;
    private boolean isFavorite;

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName( String displayName ) {
        this.displayName = displayName;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl( String thumbUrl ) {
        this.thumbUrl = thumbUrl;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery( String delivery ) {
        this.delivery = delivery;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount( String commentCount ) {
        this.commentCount = commentCount;
    }

    public String getDeliveryTimeMin() {
        return deliveryTimeMin;
    }

    public void setDeliveryTimeMin( String deliveryTimeMin ) {
        this.deliveryTimeMin = deliveryTimeMin;
    }

    public Integer getFeedbackRate() {
        return feedbackRate;
    }

    public void setFeedbackRate( Integer feedbackRate ) {
        this.feedbackRate = feedbackRate;
    }

    public String getWeekdayWork() {
        return weekdayWork;
    }

    public void setWeekdayWork( String weekdayWork ) {
        this.weekdayWork = weekdayWork;
    }

    public String getDayoffWork() {
        return dayoffWork;
    }

    public void setDayoffWork( String dayoffWork ) {
        this.dayoffWork = dayoffWork;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite( boolean favorite ) {
        isFavorite = favorite;
    }

    @Override public boolean equals( Object o) {
        return Pojomatic.equals(this, o);
    }

    @Override public int hashCode() {
        return Pojomatic.hashCode(this);
    }

    @Override public String toString() {
        return Pojomatic.toString(this);
    }
}
