package com.edanyma.model;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class TripleModel {

    private DictionaryModel leftItem;
    private DictionaryModel centerItem;
    private DictionaryModel rightItem;

    public DictionaryModel getLeftItem() {
        return leftItem;
    }

    public void setLeftItem( DictionaryModel leftItem ) {
        this.leftItem = leftItem;
    }

    public DictionaryModel getCenterItem() {
        return centerItem;
    }

    public void setCenterItem( DictionaryModel centerItem ) {
        this.centerItem = centerItem;
    }

    public DictionaryModel getRightItem() {
        return rightItem;
    }

    public void setRightItem( DictionaryModel rightItem ) {
        this.rightItem = rightItem;
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
