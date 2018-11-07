package com.klexos.samonte.intelligentexpense.SelectDisplayFragment;

/**
 * Created by steph on 6/27/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.klexos.samonte.intelligentexpense.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a
 * {@link DisplayContent.DisplayItem}
 * and makes a call to the
 * specified {@link GeneralDisplayFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
class GeneralDisplayItemRecyclerViewAdapter extends
        RecyclerView.Adapter<GeneralDisplayItemRecyclerViewAdapter.ViewHolder> {

    private final List<DisplayContent.DisplayItem> mValues;
    private final GeneralDisplayFragment.OnListFragmentInteractionListener mListener;

    GeneralDisplayItemRecyclerViewAdapter(List<DisplayContent.DisplayItem> items, GeneralDisplayFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        // If 1st item on the list inflate a different layout
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_displayitem1, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
//                    Intent intent = new Intent(context, ActiveListDetailsActivity.class);
//                    context.startActivity(intent);
                }
            });
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_displayitem2, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);

        holder.mThumbnailView.setImageResource(mValues.get(position).image);

        // This is to get the image from online and replace teh original
//        Picasso picasso = Picasso.with(holder.mContext);
//        picasso.setIndicatorsEnabled(false); //removes the picasso load indicator (colored corners)
//        // load the video thumbnail image
//        picasso.load(mValues.get(position).image)
////                .placeholder(R.drawable.ic_video_placeholder_symbol)
//                .resize(355,200)
//                .centerCrop()
//                .into(holder.mThumbnailView);

        holder.mContentView.setText(mValues.get(position).item_name);

        if (position != 0) { // If not the 1st card open proper activity
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
                        // this creates the youtube fragment in a separate activity
//                        Intent intent = new Intent(context, ActiveListDetailsActivity.class);
//                    intent.putExtra(YoutubeFragmentPlaylistView.ARG_Playlist, holder.mItem.playlist);
//                    intent.putExtra(YoutubeFragmentPlaylistView.ARG_Name, holder.mItem.item_name);
//                    //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        context.startActivity(intent);
                    }
                }
            });
        }

        // what to do when an item is selected
        holder.mSecondaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);

                    Context context = v.getContext();

//                    Intent intent;
//                    intent = YouTubeIntents.createOpenPlaylistIntent(context, holder.mItem.playlist);
//                    context.startActivity(intent);
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
        final ImageView mThumbnailView;
        final TextView mContentView;
        final ImageView mSecondaryButton;
        DisplayContent.DisplayItem mItem;
        Context mContext;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mContext = view.getContext();
//            mIdView = (TextView) view.findViewById(R.id.id);
            mThumbnailView = (ImageView) view.findViewById(R.id.playlist_thumbnail_image_view);
            mContentView = (TextView) view.findViewById(R.id.content);
            mSecondaryButton = (ImageView) view.findViewById(R.id.secondary_imageview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }


}
