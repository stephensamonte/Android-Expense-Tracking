package com.klexos.samonte.intelligentexpense.model;

/**
 * Created by steph on 6/29/2017.
 */

/**
 * Defines the data structure for ShoppingListItem objects.
 */
public class ShoppingListItem {
    private String itemName;
    private String owner;

    /**
     * Required public constructor
     */
    public ShoppingListItem() {
    }

    /**
     * Use this constructor to create new ShoppingListItem.
     *
     * @param itemName
     */
    public ShoppingListItem(String itemName) {
        this.itemName = itemName;
        /**
         * This is a default value until we can differentiate users.
         * Which will be soon, I promise.
         */
        this.owner = "Anonymous Owner";
    }

    public String getItemName() {
        return itemName;
    }

    public String getOwner() {
        return owner;
    }

}
