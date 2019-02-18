package com.edanyma.model;


import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class MenuEntityModel {

    private String id;
    private String companyId;
    private String companyName;
    private String typeId;
    private String categoryId;
    private String name;
    private String displayName;
    private String description;
    private String imageUrl;
    private String weightOne;
    private String sizeOne;
    private String priceOne;
    private String weightTwo;
    private String sizeTwo;
    private String priceTwo;
    private String weightThree;
    private String sizeThree;
    private String priceThree;
    private String weightFour;
    private String sizeFour;
    private String priceFour;
    private String status;
    private Integer count;
    private String wspType;

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId( String companyId ) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId( String typeId ) {
        this.typeId = typeId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId( String categoryId ) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName( String displayName ) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl( String imageUrl ) {
        this.imageUrl = imageUrl;
    }

    public String getWeightOne() {
        return weightOne;
    }

    public void setWeightOne( String weightOne ) {
        this.weightOne = weightOne;
    }

    public String getSizeOne() {
        return sizeOne;
    }

    public void setSizeOne( String sizeOne ) {
        this.sizeOne = sizeOne;
    }

    public String getPriceOne() {
        return priceOne;
    }

    public void setPriceOne( String priceOne ) {
        this.priceOne = priceOne;
    }

    public String getWeightTwo() {
        return weightTwo;
    }

    public void setWeightTwo( String weightTwo ) {
        this.weightTwo = weightTwo;
    }

    public String getSizeTwo() {
        return sizeTwo;
    }

    public void setSizeTwo( String sizeTwo ) {
        this.sizeTwo = sizeTwo;
    }

    public String getPriceTwo() {
        return priceTwo;
    }

    public void setPriceTwo( String priceTwo ) {
        this.priceTwo = priceTwo;
    }

    public String getWeightThree() {
        return weightThree;
    }

    public void setWeightThree( String weightThree ) {
        this.weightThree = weightThree;
    }

    public String getSizeThree() {
        return sizeThree;
    }

    public void setSizeThree( String sizeThree ) {
        this.sizeThree = sizeThree;
    }

    public String getPriceThree() {
        return priceThree;
    }

    public void setPriceThree( String priceThree ) {
        this.priceThree = priceThree;
    }

    public String getWeightFour() {
        return weightFour;
    }

    public void setWeightFour( String weightFour ) {
        this.weightFour = weightFour;
    }

    public String getSizeFour() {
        return sizeFour;
    }

    public void setSizeFour( String sizeFour ) {
        this.sizeFour = sizeFour;
    }

    public String getPriceFour() {
        return priceFour;
    }

    public void setPriceFour( String priceFour ) {
        this.priceFour = priceFour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount( Integer count ) {
        this.count = count;
    }

    public String getWspType() {
        return wspType;
    }

    public void setWspType( String wspType ) {
        this.wspType = wspType;
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
