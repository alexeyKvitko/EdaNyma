package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@AutoProperty
public class ClientOrderModel implements Serializable {

    private Integer id;
    private String clientUuid;
    private Integer companyOneId;
    private String companyOneName;
    private Integer companyTwoId;
    private String companyTwoName;
    private String companyLogo;
    private String orderDate;
    private String orderTime;
    private Integer orderPrice;
    private String orderStatus;
    private String nickName;
    private String email;
    private String phone;
    private String city;
    private String street;
    private String building;
    private String flat;
    private String floor;
    private String entry;
    private String intercom;
    private Integer needChange;
    private String comment;
    private String payType;
    private List<BasketModel> orders;

    public String getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid( String clientUuid ) {
        this.clientUuid = clientUuid;
    }

    public Integer getCompanyOneId() {
        return companyOneId;
    }

    public void setCompanyOneId( Integer companyOneId ) {
        this.companyOneId = companyOneId;
    }

    public String getCompanyOneName() {
        return companyOneName;
    }

    public void setCompanyOneName( String companyOneName ) {
        this.companyOneName = companyOneName;
    }

    public Integer getCompanyTwoId() {
        return companyTwoId;
    }

    public void setCompanyTwoId( Integer companyTwoId ) {
        this.companyTwoId = companyTwoId;
    }

    public String getCompanyTwoName() {
        return companyTwoName;
    }

    public void setCompanyTwoName( String companyTwoName ) {
        this.companyTwoName = companyTwoName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo( String companyLogo ) {
        this.companyLogo = companyLogo;
    }

    public ClientOrderModel() {
        this.orders = new LinkedList<>( );
    }

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName( String nickName ) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding( String building ) {
        this.building = building;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat( String flat ) {
        this.flat = flat;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor( String floor ) {
        this.floor = floor;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry( String entry ) {
        this.entry = entry;
    }

    public String getIntercom() {
        return intercom;
    }

    public void setIntercom( String intercom ) {
        this.intercom = intercom;
    }

    public Integer getNeedChange() {
        return needChange;
    }

    public void setNeedChange( Integer needChange ) {
        this.needChange = needChange;
    }

    public String getComment() {
        return comment;
    }

    public void setComment( String comment ) {
        this.comment = comment;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType( String payType ) {
        this.payType = payType;
    }

    public List< BasketModel > getOrders() {
        return orders;
    }

    public void setOrders( List< BasketModel > orders ) {
        this.orders = orders;
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
