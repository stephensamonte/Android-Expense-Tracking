package com.klexos.samonte.intelligentexpense.ui.activeLists;

/**
 * Created by steph on 6/29/2017.
 */

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.klexos.samonte.intelligentexpense.R;
import com.klexos.samonte.intelligentexpense.model.ShoppingList;

/**
 * Populates the list_view_active_lists inside ShoppingListsFragment
 */
public class ActiveListAdapter extends FirebaseListAdapter<ShoppingList> {
    private String mEncodedEmail;

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public ActiveListAdapter(Activity activity, Class<ShoppingList> modelClass, int modelLayout,
                             Query ref, String encodedEmail) {
        super(activity, modelClass, modelLayout, ref);
        this.mEncodedEmail = encodedEmail;
        this.mActivity = activity;
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_active_lists)
     * with items inflated from single_active_list.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, ShoppingList list) {

        /**
         * Grab the needed Textivews and strings
         */
        TextView textViewListName = (TextView) view.findViewById(R.id.text_view_list_name);
        TextView textViewCreatedByUser = (TextView) view.findViewById(R.id.text_view_created_by_user);

        String ownerEmail = list.getOwner();

        /* Set the list name and owner */
        textViewListName.setText(list.getListName());

        /**
         * Set "Created by" text to "You" if current user is owner of the list
         * Set "Created by" text to userName if current user is NOT owner of the list
         */
        if (ownerEmail != null) {
            Log.v("HERE12 Encodedemail", mEncodedEmail + "");
            Log.v("HERE12 Owner Email", ownerEmail + "");

            if (ownerEmail.equals(mEncodedEmail)) {
                textViewCreatedByUser.setText(mActivity.getResources().getString(R.string.text_you));
            } else {
                textViewCreatedByUser.setText(ownerEmail);

//                Firebase userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(ownerEmail);
//                /* Get the user's name */
//                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        User user = dataSnapshot.getValue(User.class);
//
//                        if (user != null) {
//                            textViewCreatedByUser.setText(user.getName());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//                        Log.e(mActivity.getClass().getSimpleName(),
//                                mActivity.getString(R.string.log_error_the_read_failed) +
//                                        firebaseError.getMessage());
//                    }
//                });
            }
        }

        /**
         * Show "1 person is shopping" if one person is shopping
         * Show "N people shopping" if two or more users are shopping
         * Show nothing if nobody is shopping
         */
//        if (list.getUsersShopping() != null) {
//            int usersShopping = list.getUsersShopping().size();
//            if (usersShopping == 1) {
//                textViewUsersShopping.setText(String.format(
//                        mActivity.getResources().getString(R.string.person_shopping),
//                        usersShopping));
//            } else {
//                textViewUsersShopping.setText(String.format(
//                        mActivity.getResources().getString(R.string.people_shopping),
//                        usersShopping));
//            }
//        } else {
//            /* otherwise show nothing */
//            textViewUsersShopping.setText("");
//        }
    }
}