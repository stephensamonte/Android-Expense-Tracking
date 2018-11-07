package com.klexos.samonte.intelligentexpense.SelectDisplayFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.klexos.samonte.intelligentexpense.activeListDetails.ActiveListDetailsActivity;
import com.klexos.samonte.intelligentexpense.R;

import java.util.List;

/**
 * Created by steph on 6/28/2017.
 */

/**
 * {@link RecyclerView.Adapter} that can display a
 * {@link DisplayContent.DisplayItem}
 * and makes a call to the
 * specified {@link ListsDisplayFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
class ListDisplayItemRecyclerViewAdapter extends
        RecyclerView.Adapter<ListDisplayItemRecyclerViewAdapter.ViewHolder> {

    private final List<DisplayContent.DisplayItem> mValues;
    private final ListsDisplayFragment.OnListFragmentInteractionListener mListener;

    ListDisplayItemRecyclerViewAdapter(List<DisplayContent.DisplayItem> items, ListsDisplayFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_displayitem3, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListDisplayItemRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mListNameView.setText(mValues.get(position).item_name);

        // what to do when an item is selected
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);

                    Context context = v.getContext();
//
                    // this creates the item fragment in a separate activity
                    Intent intent = new Intent(context, ActiveListDetailsActivity.class);
//                    intent.putExtra(YoutubeFragmentPlaylistView.ARG_Playlist, holder.mItem.playlist);
//                    intent.putExtra(YoutubeFragmentPlaylistView.ARG_Name, holder.mItem.item_name);
//                    //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    context.startActivity(intent);
                }
            }
        });


        // what to do when an item is selected
        holder.mSecondaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);

                    Context context = v.getContext();

//                    showEditUsernameDialog(context);


                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        //        final TextView mIdView;
        final TextView mListNameView;
        final ImageView mSecondaryButton;
        DisplayContent.DisplayItem mItem;
        Context mContext;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mContext = view.getContext();
//            mIdView = (TextView) view.findViewById(R.id.id);
            mListNameView = (TextView) view.findViewById(R.id.text_view_list_name);
            mSecondaryButton = (ImageView) view.findViewById(R.id.secondary_imageview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mListNameView.getText() + "'";
        }
    }

    @Override
    public void onViewDetachedFromWindow(ListDisplayItemRecyclerViewAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }




//    public void showEditUsernameDialog(final Context mContext) {
//
//        // 1. Instantiate an AlertDialog.Builder with its constructor
//        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//
//        TextView title = new TextView(mContext);
//        // You Can Customise your Title here
//        String temporary = "Item";
//        title.setText(temporary);
//        title.setPadding(12, 20, 12, 20);
//        title.setGravity(Gravity.CENTER);
//        title.setTextColor(Color.BLACK);
//        title.setTextSize(22);
//        builder.setCustomTitle(title);
//
//        // 2. Chain together various setter methods to set the dialog characteristics
//        builder.setMessage("Enter Item Name"); // R.string.AppRater_rate_question
//
//        // Set up the input
//        final EditText input = new EditText(mContext); // this was replaced with below line
//
//        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
////        input.setText(showUsername); // showing hint instead
////        input.selectAll(); // dont need because I'm showing a hint instead
//        input.setHint("item");
//        builder.setView(input);
//
//        // Add the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked OK button
//
//                String UserInputText = input.getText().toString();
//
//
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User Cancel the dialog
//
//
//            }
//        });
//
//        // 3. Get the AlertDialog from create()
//        AlertDialog dialog = builder.create();
//
//        // Opens the keyboard when the dialog is displayed
//        if (dialog.getWindow() != null){
//            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        }
//        dialog.show();
//
//        // Must call show() prior to fetching views
//
//        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
//        messageView.setGravity(Gravity.CENTER);
//
//        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//        nbutton.setTextColor(Color.RED);
//        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
////        pbutton.setTextColor(Color.BLUE);
//
//    }

}