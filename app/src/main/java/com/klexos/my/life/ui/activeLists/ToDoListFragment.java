package com.klexos.my.life.ui.activeLists;

/**
 * Created by steph on 7/5/2017.
 */

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.klexos.my.life.R;
import com.klexos.my.life.model.ShoppingList;
import com.klexos.my.life.model.ShoppingListItem;
import com.klexos.my.life.model.User;
import com.klexos.my.life.ui.BaseActivity;
import com.klexos.my.life.ui.activeListDetails.ActiveListDetailsActivity;
import com.klexos.my.life.ui.activeListDetails.ActiveListItemAdapter;
import com.klexos.my.life.ui.activeListDetails.AddListItemDialogFragment;
import com.klexos.my.life.ui.activeListDetails.EditListItemNameDialogFragment;
import com.klexos.my.life.utils.Constants;
import com.klexos.my.life.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass that shows a list of all shopping lists a user can see.
 * Use the {@link ListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoListFragment extends Fragment {
    private static final String LOG_TAG = ActiveListDetailsActivity.class.getSimpleName();
    private String mEncodedEmail;
    private ActiveListItemAdapter mActiveListItemAdapter;
    private ListView mListView;
    private Firebase mCurrentListRef, mSharedWithRef;
    private String mListId;
    private ShoppingList mShoppingList;
    private ValueEventListener mCurrentListRefListener, mSharedWithListener;
    /* Stores whether the current user is shopping */
    private boolean mShopping = false;
    private HashMap<String, User> mSharedWithUsers;

    final String userToDoListKey = "userToDoListId";

    FloatingActionButton mfab;

    public ToDoListFragment() {
        /* Required empty public constructor */
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * Right now there are not arguments...but eventually there will be.
     */
    public static ToDoListFragment newInstance(String encodedEmail) {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_ENCODED_EMAIL, encodedEmail);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEncodedEmail = getArguments().getString(Constants.KEY_ENCODED_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Initialize UI elements
         */
        View rootView = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        initializeScreen(rootView);

        // Get to do list name id
        SharedPreferences sharedPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        String defaultValue = "";
        mListId = sharedPref.getString(userToDoListKey, defaultValue);

        if (mListId == "") {
            addActiveList();
        }

        Firebase ref = new Firebase("https://console.firebase.google.com/u/0/project/my-life-tool/database/data/userLists/klexosinc@gmail,com/-Ko8zUGo0XN00PIOM4aK");

        mListId = ref.getKey();

        mCurrentListRef = new Firebase(Constants.FIREBASE_URL_USER_LISTS).child(mEncodedEmail).child(mListId);

        mSharedWithRef = new Firebase (Constants.FIREBASE_URL_LISTS_SHARED_WITH).child(mListId);

        Firebase listItemsRef = new Firebase(Constants.FIREBASE_URL_SHOPPING_LIST_ITEMS).child(mListId);

        /**
         * Setup the adapter
         */
        mActiveListItemAdapter = new ActiveListItemAdapter(getActivity(), ShoppingListItem.class,
                R.layout.single_active_list_item, listItemsRef.orderByChild(Constants.FIREBASE_PROPERTY_BOUGHT_BY),
                mListId, mEncodedEmail);

        /* Create ActiveListItemAdapter and set to listView */
        mListView.setAdapter(mActiveListItemAdapter);


        /**
         * Save the most recent version of current shopping list into mShoppingList instance
         * variable an update the UI to match the current list.
         */
        mCurrentListRefListener = mCurrentListRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                /**
                 * Saving the most recent version of current shopping list into mShoppingList if present
                 * finish() the activity if the list is null (list was removed or unshared by it's owner
                 * while current user is in the list details activity)
                 */
                ShoppingList shoppingList = snapshot.getValue(ShoppingList.class);

                if (shoppingList == null) {
//                    finish();
                    /**
                     * Make sure to call return, otherwise the rest of the method will execute,
                     * even after calling finish.
                     */
                    return;
                }
                mShoppingList = shoppingList;
                /**
                 * Pass the shopping list to the adapter if it is not null.
                 * We do this here because mShoppingList is null when first created.
                 */
                mActiveListItemAdapter.setShoppingList(mShoppingList);

//                HashMap<String, User> usersShopping = mShoppingList.getUsersShopping();
//                if (usersShopping != null && usersShopping.size() != 0 &&
//                        usersShopping.containsKey(mEncodedEmail)) {
//                    mShopping = true;
//                    mButtonShopping.setText(getString(R.string.button_stop_shopping));
//                    mButtonShopping.setBackgroundColor(ContextCompat.getColor(ActiveListDetailsActivity.this, R.color.dark_grey));
//                } else {
//                    mButtonShopping.setText(getString(R.string.button_start_shopping));
//                    mButtonShopping.setBackgroundColor(ContextCompat.getColor(ActiveListDetailsActivity.this, R.color.primary_dark));
//                    mShopping = false;
//
//                }
//
//                setWhosShoppingText(mShoppingList.getUsersShopping());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG,
                        getString(R.string.log_error_the_read_failed) +
                                firebaseError.getMessage());
            }
        });


        mSharedWithListener = mSharedWithRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSharedWithUsers = new HashMap<String, User>();
                for (DataSnapshot currentUser : dataSnapshot.getChildren()) {
                    mSharedWithUsers.put(currentUser.getKey(), currentUser.getValue(User.class));
                }
                mActiveListItemAdapter.setSharedWithUsers(mSharedWithUsers);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(LOG_TAG,
                        getString(R.string.log_error_the_read_failed) +
                                firebaseError.getMessage());
            }
        });


    /**
     * Set up click listeners for interaction.
     */

        /* Show edit list item name dialog on listView item long click event */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /* Check that the view is not the empty footer item */
                if (view.getId() != R.id.list_view_footer_empty) {
                    ShoppingListItem shoppingListItem = mActiveListItemAdapter.getItem(position);

                    if (shoppingListItem != null) {
                        /*
                        If the person is the owner and not shopping and the item is not bought, then
                        they can edit it.
                         */
                        if (shoppingListItem.getOwner().equals(mEncodedEmail) && !mShopping && !shoppingListItem.isBought()) {
                            String itemName = shoppingListItem.getItemName();
                            String itemId = mActiveListItemAdapter.getRef(position).getKey();
                            showEditListItemNameDialog(itemName, itemId);
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        /**
         * Create Firebase references
         */
//        Firebase activeListsRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_LISTS);

        /**
         * Create the adapter, giving it the activity, model class, layout for each row in
         * the list and finally, a reference to the Firebase location with the list data
         */
//        mActiveListAdapter = new ActiveListAdapter(getActivity(), ShoppingList.class,
//                R.layout.single_active_list, activeListsRef, mEncodedEmail);


        /**
         * Set the adapter to the mListView
         */
//        mListView.setAdapter(mActiveListAdapter);


        /* Perform buy/return action on listView item click event if current user is shopping. */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* Check that the view is not the empty footer item */
                if (view.getId() != R.id.list_view_footer_empty) {
                    final ShoppingListItem selectedListItem = mActiveListItemAdapter.getItem(position);
                    String itemId = mActiveListItemAdapter.getRef(position).getKey();

                    if (selectedListItem != null) {
                        /* If current user is shopping */
//                        if (mShopping) {

                            /* Create map and fill it in with deep path multi write operations list */
                        HashMap<String, Object> updatedItemBoughtData = new HashMap<String, Object>();

                            /* Buy selected item if it is NOT already bought */
                        if (!selectedListItem.isBought()) {
                            updatedItemBoughtData.put(Constants.FIREBASE_PROPERTY_BOUGHT, true);
                            updatedItemBoughtData.put(Constants.FIREBASE_PROPERTY_BOUGHT_BY, mEncodedEmail);
                        } else {
                                /* Return selected item only if it was bought by current user */
                            if (selectedListItem.getBoughtBy().equals(mEncodedEmail)) {
                                updatedItemBoughtData.put(Constants.FIREBASE_PROPERTY_BOUGHT, false);
                                updatedItemBoughtData.put(Constants.FIREBASE_PROPERTY_BOUGHT_BY, null);
                            }
                        }

                            /* Do update */
                        Firebase firebaseItemLocation = new Firebase(Constants.FIREBASE_URL_SHOPPING_LIST_ITEMS)
                                .child(mListId).child(itemId);
                        firebaseItemLocation.updateChildren(updatedItemBoughtData, new Firebase.CompletionListener() {
                            @Override
                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                if (firebaseError != null) {
                                    Log.d(LOG_TAG, getString(R.string.log_error_updating_data) +
                                            firebaseError.getMessage());
                                }
                            }
                        });
//                        }
                    }
                }
            }
        });

        return rootView;
    }

    /**
     * Updates the order of mListView onResume to handle sortOrderChanges properly
     */
    @Override
    public void onResume() {
        super.onResume();


    }

    /**
     * Cleanup the adapter when activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        BaseActivity.displayToastMessage(getContext(), "onDestroy Called");

        mCurrentListRef.removeEventListener(mCurrentListRefListener);

        mActiveListItemAdapter.cleanup();

        mSharedWithRef.removeEventListener(mSharedWithListener);
    }

    /**
     * Link list view from XML
     */
    private void initializeScreen(final View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_shopping_list_items);

        mfab = (FloatingActionButton) rootView.findViewById(R.id.fab_detail_add_item);

        mfab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAddListItemDialog(rootView);
            }
        });

        /* Inflate the footer, set root layout to null*/
        View footer = getActivity().getLayoutInflater().inflate(R.layout.footer_empty, null);
        mListView.addFooterView(footer);
    }

    /**
     * Show the add list item dialog when user taps "Add list item" fab
     */
    private void showAddListItemDialog(View view) {
        /* Create an instance of the dialog fragment and show it */
        DialogFragment dialog = AddListItemDialogFragment.newInstance(mShoppingList, mListId,
                mEncodedEmail, mSharedWithUsers);
        dialog.show(getActivity().getFragmentManager(), "AddListItemDialogFragment");
    }

    /**
     * Show the edit list item name dialog after longClick on the particular item
     *
     * @param itemName
     * @param itemId
     */
    public void showEditListItemNameDialog(String itemName, String itemId) {
        /* Create an instance of the dialog fragment and show it */
        DialogFragment dialog = EditListItemNameDialogFragment.newInstance(mShoppingList, itemName,
                itemId, mListId, mEncodedEmail, mSharedWithUsers);

        dialog.show(getActivity().getFragmentManager(), "EditListItemNameDialogFragment");
    }

    /**
     * Add new active list (make a new to do list
     */
    public void addActiveList() {
        String toDoListName = "To Do List";

        /**
         * If toDoListName Is not empty
         */
        if (!toDoListName.equals("")) {

            /**
             * Create Firebase references
             */
            Firebase userListsRef = new Firebase(Constants.FIREBASE_URL_USER_LISTS).
                    child(mEncodedEmail);
            final Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);

            Firebase newListRef = userListsRef.push();

            /* Save listsRef.push() to maintain same random Id */
            final String listId = newListRef.getKey();

            /* HashMap for data to update */
            HashMap<String, Object> updateShoppingListData = new HashMap<>();

            /**
             * Set raw version of date to the ServerValue.TIMESTAMP value and save into
             * timestampCreatedMap
             */
            HashMap<String, Object> timestampCreated = new HashMap<>();
            timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

            /* Build the shopping list */
            ShoppingList newShoppingList = new ShoppingList(toDoListName, mEncodedEmail,
                    timestampCreated);

            HashMap<String, Object> shoppingListMap = (HashMap<String, Object>)
                    new ObjectMapper().convertValue(newShoppingList, Map.class);

            Utils.updateMapForAllWithValue(null, listId, mEncodedEmail,
                    updateShoppingListData, "", shoppingListMap);

            /* Do the update */
            firebaseRef.updateChildren(updateShoppingListData, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    /* Now that we have the timestamp, update the reversed timestamp */
                    Utils.updateTimestampReversed(firebaseError, "AddList", listId,
                            null, mEncodedEmail);
                }
            });


            // Save the id to shared preferences
            SharedPreferences sharedPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(userToDoListKey, listId);
            editor.commit();
        }
    }




}
