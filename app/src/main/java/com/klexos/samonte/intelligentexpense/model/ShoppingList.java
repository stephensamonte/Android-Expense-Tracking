package com.klexos.samonte.intelligentexpense.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;
import com.klexos.samonte.intelligentexpense.utils.Constants;

import java.util.HashMap;

/**
 * Created by steph on 6/28/2017.
 */

public class ShoppingList {
    private String listName;
    private String owner;
    private HashMap<String, Object> timestampLastChanged;

    public ShoppingList() {

    }

    /**
     * Use this constructor to create new ShoppingLists.
     * Takes shopping list listName and owner. Set's the last
     * changed time to what is stored in ServerValue.TIMESTAMP
     *
     * @param name
     * @param owner
     *
     */
    public ShoppingList(String name, String owner) {
        this.listName = name;
        this.owner = owner;
        HashMap<String, Object> timestampLastChangedObj = new HashMap<String, Object>();
        timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampLastChangedObj;
    }

    public String getListName() {
        return listName;
    }

    public String getOwner() {
        return owner;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    @JsonIgnore
    public long getTimestampLastChangedLong() {

        return (long) timestampLastChanged.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }
}
