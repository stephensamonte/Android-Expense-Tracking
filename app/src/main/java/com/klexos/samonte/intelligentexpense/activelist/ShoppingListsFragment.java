package com.klexos.samonte.intelligentexpense.activelist;

/**
 * Created by steph on 6/28/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.klexos.samonte.intelligentexpense.R;
import com.klexos.samonte.intelligentexpense.model.ShoppingList;
import com.klexos.samonte.intelligentexpense.utils.Constants;
import com.klexos.samonte.intelligentexpense.utils.Utils;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass that shows a list of all shopping lists a user can see.
 * Use the {@link ShoppingListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListsFragment extends Fragment {
    private ListView mListView;
    private TextView mTextViewListName, mTextViewListOwner, mTextViewEditTime;

    public ShoppingListsFragment() {
        /* Required empty public constructor */
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * Right now there are not arguments...but eventually there will be.
     */
    public static ShoppingListsFragment newInstance() {
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Initalize UI elements
         */
        View rootView = inflater.inflate(R.layout.fragment_shopping, container, false);
        initializeScreen(rootView);

        // Reaing from: https://console.firebase.google.com/u/0/project/shoplistplusplus-tan/database/data/listName
        Firebase refListName = new Firebase(Constants.FIREBASE_URL).child("activeList");
        refListName.addValueEventListener(new ValueEventListener(){ // listener to list for changes made on the database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                Log.e("ShoppingListFragment", "The data changed");
//                String listName = (String) dataSnapshot.getValue();
//                mTextViewListName.setText(listName);

                // You can use getValue to deserialize the data at dataSnapshot
                // into a ShoppingList.
                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);

                // If there was no data at the location we added the listener, then
                // shoppingList will be null.
                if (shoppingList != null) {
                    // If there was data, take the TextViews and set the appropriate values.
                    mTextViewListName.setText(shoppingList.getListName());
                    mTextViewListOwner.setText(shoppingList.getOwner());
                    if (shoppingList.getTimestampLastChanged() != null) {
                        mTextViewEditTime.setText(
                                Utils.SIMPLE_DATE_FORMAT.format(
                                        new Date(shoppingList.getTimestampLastChangedLong())));
                    } else {
                        mTextViewEditTime.setText("");
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /**
         * Set interactive bits, such as click events and adapters
         */
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Link layout elements from XML
     */
    private void initializeScreen(View rootView) {
//        mListView = (ListView) rootView.findViewById(R.id.list_view_active_lists);
        mTextViewListName = (TextView) rootView.findViewById(R.id.text_view_list_names);
        mTextViewListOwner = (TextView) rootView.findViewById(R.id.text_view_created_by_users);
        mTextViewEditTime = (TextView) rootView.findViewById(R.id.text_view_edit_times);
    }}
