package tcss450.uw.edu.phishapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tcss450.uw.edu.phishapp.blog.BlogPost;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlogPostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BlogPostFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public BlogPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            // Could pass
            BlogPost blogPost = (BlogPost) getArguments()
                    .getSerializable(getString(R.string.key_blogPost));
            TextView title = getActivity().findViewById(R.id.text_fragment_blog_post_blogTitle);
            title.setText(blogPost.getTitle());

            TextView date = getActivity().findViewById(R.id.text_fragment_blog_post_blogDate);
            date.setText(blogPost.getPubDate());

            TextView details = getActivity().findViewById(R.id.text_fragment_blog_post_sample);
            details.setText(Html.fromHtml(blogPost.getTeaser(),
                    Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE, null, null));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_blog_post, container, false);

        Button b = v.findViewById(R.id.button_fragment_blog_post_fullPost);
        b.setOnClickListener(this::viewPost);

        return v;
    }

    public void viewPost(View view){
        BlogPost post = (BlogPost) getArguments()
                .getSerializable(getString(R.string.key_blogPost));

        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(post.getUrl()));
        startActivity(intent);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWaitFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBlogFragmentInteraction(BlogPost item);
    }
}
