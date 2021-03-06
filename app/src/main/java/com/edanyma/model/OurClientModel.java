package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import java.util.List;

@AutoProperty
public class OurClientModel {

    private Integer id;
    private String nickName;
    private String primaryAddress;
    private String email;
    private String phone;
    private String password;
    private String confirm;
    private String uuid;
    private String photo;
    private String additionalMessage;
    private String payType;
    private ClientLocationModel clientLocationModel;
    private List<FavoriteCompanyModel> favoriteCompanies;
    private List<BonusModel> bonusModels;

    public Integer getId() {
        return id;
    }

    public void setId( Integer id ) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName( String nickName ) {
        this.nickName = nickName;
    }

    public String getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress( String primaryAddress ) {
        this.primaryAddress = primaryAddress;
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

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm( String confirm ) {
        this.confirm = confirm;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid( String uuid ) {
        this.uuid = uuid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto( String photo ) {
        this.photo = photo;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage( String additionalMessage ) {
        this.additionalMessage = additionalMessage;
    }

    public List< FavoriteCompanyModel > getFavoriteCompanies() {
        return favoriteCompanies;
    }

    public void setFavoriteCompanies( List< FavoriteCompanyModel > favoriteCompanies ) {
        this.favoriteCompanies = favoriteCompanies;
    }

    public ClientLocationModel getClientLocationModel() {
        return clientLocationModel;
    }

    public void setClientLocationModel( ClientLocationModel clientLocationModel ) {
        this.clientLocationModel = clientLocationModel;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType( String payType ) {
        this.payType = payType;
    }

    public List< BonusModel > getBonusModels() {
        return bonusModels;
    }

    public void setBonusModels( List< BonusModel > bonusModels ) {
        this.bonusModels = bonusModels;
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
