package com.klexos.samonte.intelligentexpense.ui.activeListDetails;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.ServerValue;
import com.firebase.ui.FirebaseListAdapter;
import com.klexos.samonte.intelligentexpense.R;
import com.klexos.samonte.intelligentexpense.model.ShoppingList;
import com.klexos.samonte.intelligentexpense.model.ShoppingListItem;
import com.klexos.samonte.intelligentexpense.utils.Constants;

import java.util.HashMap;


/**
 * Populates list_view_shopping_list_items inside ActiveListDetailsActivity
 */
public class ActiveListItemAdapter extends FirebaseListAdapter<ShoppingListItem> {
    private ShoppingList mShoppingList;
    private String mListId;
    private String mEncodedEmail;

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    public ActiveListItemAdapter(Activity activity, Class<ShoppingListItem> modelClass, int modelLayout,
                                 Query ref, String listId, String encodedEmail) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mListId = listId;
        this.mEncodedEmail = encodedEmail;
    }

    /**
     * Public method that is used to pass shoppingList object when it is loaded in ValueEventListener
     */
    public void setShoppingList(ShoppingList shoppingList) {
        this.mShoppingList = shoppingList;
        this.notifyDataSetChanged();
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_friends_autocomplete)
     * with items inflated from single_active_list_item.xml
     * populateView also handles data changes and updates the listView accordingly
     */
    @Override
    protected void populateView(View view, final ShoppingListItem item, int position) {

        ImageButton buttonRemoveItem = (ImageButton) view.findViewById(R.id.button_remove_item);
        TextView textViewItemName = (TextView) view.findViewById(R.id.text_view_active_list_item_name);
        final TextView textViewBoughtByUser = (TextView) view.findViewById(R.id.text_view_bought_by_user);
        TextView textViewBoughtBy = (TextView) view.findViewById(R.id.text_view_bought_by);

        String owner = item.getOwner();

        textViewItemName.setText(item.getItemName());

        setItemAppearanceBaseOnBoughtStatus(owner, textViewBoughtByUser, textViewBoughtBy, buttonRemoveItem,
                textViewItemName, item);

        /* Gets the id of the item to remove */
        final String itemToRemoveId = this.getRef(position).getKey();

        /**
         * Set the on click listener for "Remove list item" button
         */
        buttonRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity, R.style.CustomTheme_Dialog)
                        .setTitle(mActivity.getString(R.string.remove_item_option))
                        .setMessage(mActivity.getString(R.string.dialog_message_are_you_sure_remove_item))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                removeItem(itemToRemoveId);
                                /* Dismiss the dialog */
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                /* Dismiss the dialog */
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.ic_report_problem_black_24dp);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void removeItem(String itemId) {
        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);

        /* Make a map for the removal */
        HashMap<String, Object> updatedRemoveItemMap = new HashMap<String, Object>();

        /* Remove the item by passing null */
        updatedRemoveItemMap.put("/" + Constants.FIREBASE_LOCATION_SHOPPING_LIST_ITEMS + "/"
                + mListId + "/" + itemId, null);

        /* Make the timestamp for last changed */
        HashMap<String, Object> changedTimestampMap = new HashMap<>();
        changedTimestampMap.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

        /* Add the updated timestamp */
        updatedRemoveItemMap.put("/" + Constants.FIREBASE_LOCATION_ACTIVE_LISTS +
                "/" + mListId + "/" + Constants.FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED, changedTimestampMap);

        /* Do the update */
        firebaseRef.updateChildren(updatedRemoveItemMap);
    }

    private void setItemAppearanceBaseOnBoughtStatus(String owner, final TextView textViewBoughtByUser,
                                                     TextView textViewBoughtBy, ImageButton buttonRemoveItem,
                                                     TextView textViewItemName, ShoppingListItem item) {
        /**
         * If selected item is bought
         * Set "Bought by" text to "You" if current user is owner of the list
         * Set "Bought by" text to userName if current user is NOT owner of the list
         * Set the remove item button invisible if current user is NOT list or item owner
         */
        if (item.isBought() && item.getBoughtBy() != null) {

            textViewBoughtBy.setVisibility(View.VISIBLE);
            textViewBoughtByUser.setVisibility(View.VISIBLE);
            buttonRemoveItem.setVisibility(View.INVISIBLE);

            /* Add a strike-through */
            textViewItemName.setPaintFlags(textViewItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (item.getBoughtBy().equals(mEncodedEmail)) {
                textViewBoughtByUser.setText(mActivity.getString(R.string.text_you));
            } else {
                textViewBoughtByUser.setText(item.getBoughtBy()); // displays the item's owner's name
            }
        } else {
            /**
             * If selected item is NOT bought
             * Set "Bought by" text to be empty and invisible
             * Set the remove item button visible if current user is owner of the list or selected item
             */

            /* Remove the strike-through */
            textViewItemName.setPaintFlags(textViewItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            textViewBoughtBy.setVisibility(View.INVISIBLE);
            textViewBoughtByUser.setVisibility(View.INVISIBLE);
            textViewBoughtByUser.setText("");
            /**
             * If you are the owner of the item or the owner of the list, then the remove icon
             * is visible.
             */
            if (owner.equals(mEncodedEmail) || (mShoppingList != null && mShoppingList.getOwner().equals(mEncodedEmail))) {
                buttonRemoveItem.setVisibility(View.VISIBLE);
            } else {
                buttonRemoveItem.setVisibility(View.INVISIBLE);
            }
        }
    }
}