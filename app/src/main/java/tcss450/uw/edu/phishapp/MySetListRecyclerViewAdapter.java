package tcss450.uw.edu.phishapp;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.phishapp.SetListFragment.OnListFragmentInteractionListener;
import tcss450.uw.edu.phishapp.SetLists.SetList;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SetList} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySetListRecyclerViewAdapter extends RecyclerView.Adapter<MySetListRecyclerViewAdapter.ViewHolder> {

    private final List<SetList> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySetListRecyclerViewAdapter(List<SetList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_setlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mSetList = mValues.get(position);
        holder.mLongDateView.setText(mValues.get(position).getLongDate());
        holder.mLocationView.setText(mValues.get(position).getLocation());
        holder.mVenueView.setText(Html.fromHtml(mValues.get(position).getVenue(),
                Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE,null,null));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mSetList);
                } else {
                    Log.wtf("SET LIST", "SET LIST IS NULL FROM API");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLongDateView;
        public final TextView mLocationView;
        public final TextView mVenueView;
        public SetList mSetList;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLongDateView = (TextView) view.findViewById(R.id.text_fragment_setList_longDate);
            mLocationView = (TextView) view.findViewById(R.id.text_fragment_setList_location);
            mVenueView = (TextView) view.findViewById(R.id.text_fragment_setList_venue);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationView.getText() + "'";
        }
    }
}
