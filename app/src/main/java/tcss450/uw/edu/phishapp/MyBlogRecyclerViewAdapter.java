package tcss450.uw.edu.phishapp;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.phishapp.BlogFragment.OnBlogListFragmentInteractionListener;
import tcss450.uw.edu.phishapp.blog.BlogPost;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BlogPost} and makes a call to the
 * specified {@link OnBlogListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBlogRecyclerViewAdapter extends RecyclerView.Adapter<MyBlogRecyclerViewAdapter.ViewHolder> {

    private final List<BlogPost> mValues;
    private final OnBlogListFragmentInteractionListener mListener;

    public MyBlogRecyclerViewAdapter(List<BlogPost> items, BlogFragment.OnBlogListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBlogTitle.setText(mValues.get(position).getTitle());
        holder.mPubDate.setText(mValues.get(position).getPubDate());
        holder.mBlogTeaser.setText(Html.fromHtml(mValues.get(position).getTeaser()
                ,Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE,null,null));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onBlogListInteraction(holder.mItem);
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
        public final TextView mPubDate;
        public final TextView mBlogTitle;
        public final TextView mBlogTeaser;
        public BlogPost mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBlogTitle = (TextView) view.findViewById(R.id.text_fragment_blog_blogTitle);
            mPubDate = (TextView) view.findViewById(R.id.text_fragment_blog_publishDate);
            mBlogTeaser = (TextView) view.findViewById(R.id.text_fragment_blog_sample);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBlogTitle.getText() + "'";
        }
    }
}
