package com.klexos.samonte.intelligentexpense.ui.activeLists;

/**
 * Created by steph on 6/28/2017.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.klexos.samonte.intelligentexpense.R;
import com.klexos.samonte.intelligentexpense.model.ShoppingList;
import com.klexos.samonte.intelligentexpense.ui.BaseActivity;
import com.klexos.samonte.intelligentexpense.ui.activeListDetails.ActiveListDetailsActivity;
import com.klexos.samonte.intelligentexpense.utils.Constants;


/**
 * A simple {@link Fragment} subclass that shows a list of all shopping lists a user can see.
 * Use the {@link ShoppingListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListsFragment extends Fragment {
    private String mEncodedEmail;
    private ActiveListAdapter mActiveListAdapter;
    private ListView mListView;

    public ShoppingListsFragment() {
        /* Required empty public constructor */
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * Right now there are not arguments...but eventually there will be.
     */
    public static ShoppingListsFragment newInstance(String encodedEmail) {
        ShoppingListsFragment fragment = new ShoppingListsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_shopping_lists, container, false);
        initializeScreen(rootView);

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

        /**
         * Set interactive bits, such as click events and adapters
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShoppingList selectedList = mActiveListAdapter.getItem(position);
                if (selectedList != null) {
                    Intent intent = new Intent(getActivity(), ActiveListDetailsActivity.class);
                    /* Get the list ID using the adapter's get ref method to get the Firebase
                     * ref and then grab the key.
                     */
                    String listId = mActiveListAdapter.getRef(position).getKey();

                    Log.v("listID", listId + "");

                    intent.putExtra(Constants.KEY_LIST_ID, listId);
                    /* Starts an active showing the details for the selected list */
                    startActivity(intent);
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

        // Getting sort selection from shared preferences. Sort selection can be configured in the settings activity
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sharedPref.getString(Constants.KEY_PREF_SORT_ORDER_LISTS, Constants.ORDER_BY_KEY);

        Query orderedActiveUserListsRef;

        /**
         * Create Firebase references (With user data mattering) this is the new one
         */
//        Firebase activeListsRef = new Firebase(Constants.FIREBASE_URL_USER_LISTS)
//                .child(mEncodedEmail);

        /**
         * Create Firebase references
         */
        Firebase activeListsRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_LISTS);

        /**
         * Sort active lists by "date created"
         * if it's been selected in the SettingsActivity
         */
        if (sortOrder.equals(Constants.ORDER_BY_KEY)) {
            orderedActiveUserListsRef = activeListsRef.orderByKey(); // Sort by time with firebase call
        } else {

            /**
             * Sort active by lists by name or datelastChanged. Otherwise
             * depending on what's been selected in SettingsActivity
             */

            orderedActiveUserListsRef = activeListsRef.orderByChild(sortOrder);
        }

        /**
         * Create the adapter with selected sort order
         */
        mActiveListAdapter = new ActiveListAdapter(getActivity(), ShoppingList.class,
                R.layout.single_active_list, orderedActiveUserListsRef,
                mEncodedEmail);

        /**
         * Set the adapter to the mListView
         */
        mListView.setAdapter(mActiveListAdapter);
    }

    /**
     * Cleanup the adapter when activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        BaseActivity.displayToastMessage(getContext(), "onDestroy Called");

        mActiveListAdapter.cleanup();
    }

    /**
     * Link list view from XML
     */
    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_active_lists);
    }
}