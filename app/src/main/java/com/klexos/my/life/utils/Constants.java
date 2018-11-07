package com.klexos.my.life.utils;

/**
 * Created by steph on 6/28/2017.
 */

/**
 * Constants class store most important strings and paths of the app
 */
public final class Constants {

    /**
     * Constants related to locations in Firebase, such as the name of the node
     * where active lists are stored (ie "activeLists")
     */
    public static final String FIREBASE_LOCATION_SHOPPING_LIST_ITEMS = "shoppingListItems";
    public static final String FIREBASE_LOCATION_USERS = "users";
//    public static final String FIREBASE_LOCATION_ACTIVE_LISTS = "activeLists"; // old
    public static final String FIREBASE_LOCATION_USER_LISTS = "userLists";
    public static final String FIREBASE_LOCATION_LISTS_SHARED_WITH = "sharedWith";
    public static final String FIREBASE_LOCATION_USER_FRIENDS = "userFriends";

    /**
     * Constants for Firebase object properties
     */
    public static final String FIREBASE_PROPERTY_LIST_NAME = "listName";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "timestampLastChanged";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    public static final String FIREBASE_PROPERTY_ITEM_NAME = "itemName";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";
    public static final String FIREBASE_PROPERTY_BOUGHT = "bought";
    public static final String FIREBASE_PROPERTY_BOUGHT_BY = "boughtBy";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED_REVERSE = "timestampLastChangedReverse";

    /**
     * Constants for Firebase URL
     */

    public static final String FIREBASE_URL = "https://my-life-tool.firebaseio.com/"; // This is my firebase root URL
//    public static final String FIREBASE_URL_ACTIVE_LISTS = FIREBASE_URL + "/" + FIREBASE_LOCATION_ACTIVE_LISTS; // old
    public static final String FIREBASE_URL_SHOPPING_LIST_ITEMS = FIREBASE_URL + "/" + FIREBASE_LOCATION_SHOPPING_LIST_ITEMS;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_URL_USER_LISTS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USER_LISTS;
    public static final String FIREBASE_URL_LISTS_SHARED_WITH = FIREBASE_URL + "/" + FIREBASE_LOCATION_LISTS_SHARED_WITH;
    public static final String FIREBASE_URL_USER_FRIENDS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USER_FRIENDS;

    /**
     * Constants for bundles, extras and shared preferences keys
     */
    public static final String KEY_LIST_NAME = "LIST_NAME";
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";
    public static final String KEY_LIST_ID = "LIST_ID";
    public static final String KEY_LIST_ITEM_NAME = "ITEM_NAME";
    public static final String KEY_LIST_ITEM_ID = "LIST_ITEM_ID";
    public static final String KEY_SIGNUP_EMAIL = "SIGNUP_EMAIL";
    public static final String KEY_ENCODED_EMAIL = "ENCODED_EMAIL";
    public static final String KEY_LIST_OWNER = "LIST_OWNER";
    public static final String KEY_PREF_SORT_ORDER_LISTS = "PERF_SORT_ORDER_LISTS";
    public static final String KEY_SHARED_WITH_USERS = "SHARED_WITH_USERS";


    /**
     * Constant for sorting
     */
    public static final String ORDER_BY_KEY = "orderByPushKey";
    public static final String ORDER_BY_OWNER_EMAIL = "orderByOwnerEmail";


}
