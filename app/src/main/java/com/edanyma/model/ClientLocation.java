package com.edanyma.model;

import com.edanyma.utils.AppUtils;

import java.io.Serializable;

public class ClientLocation implements Serializable {

    private String uuid;
    private String city;
    private String street;
    private String house;
    private String entrance;
    private String floor;
    private String intercom;
    private Double latitude;
    private Double longitude;

    public String getUuid() {
        return uuid;
    }

    public void setUuid( String uuid ) {
        this.uuid = uuid;
    }

    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet( String street ) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse( String house ) {
        this.house = house;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance( String entrance ) {
        this.entrance = entrance;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor( String floor ) {
        this.floor = floor;
    }

    public String getIntercom() {
        return intercom;
    }

    public void setIntercom( String intercom ) {
        this.intercom = intercom;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude( Double latitude ) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude( Double longitude ) {
        this.longitude = longitude;
    }

    public String getFullAddressAsString(){
        StringBuilder address = new StringBuilder().append( getCity() ).append( ", " )
                .append( getStreet() ).append( ", " )
                .append( getHouse() );
        if ( !AppUtils.nullOrEmpty( getEntrance() ) ) {
            address.append( ", подъезд: " ).append( getEntrance() );
        }
        if ( !AppUtils.nullOrEmpty( getFloor() ) ) {
            address.append( ", этаж: " ).append( getFloor() );
        }
        if ( !AppUtils.nullOrEmpty( getIntercom() ) ) {
            address.append( ", код: " ).append( getIntercom() );
        }
        return address.toString();
    }

    public boolean isAdditionalInfoExist(){
        return !AppUtils.nullOrEmpty( this.entrance ) ||
               !AppUtils.nullOrEmpty( this.floor ) ||
               !AppUtils.nullOrEmpty( this.intercom );
    }
}
