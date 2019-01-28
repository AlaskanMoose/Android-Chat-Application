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

import tcss450.uw.edu.phishapp.SetLists.SetList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetListItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SetListItemFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SetListItemFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            // Could pass
            SetList setlist = (SetList) getArguments()
                    .getSerializable(getString(R.string.key_setList));

            TextView longDate = getActivity().findViewById(R.id.text_fragment_setlist_item_longDate);
            TextView location = getActivity().findViewById(R.id.text_fragment_setlist_item_location);
            TextView setlistdata = getActivity().findViewById(R.id.text_fragment_setlist_item_setListData);
            TextView setlistnotes = getActivity().findViewById(R.id.text_fragment_setlist_item_setListNotes);



            longDate.setText(setlist.getLongDate());
            location.setText(Html.fromHtml(setlist.getLocation(),
                    Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE, null, null));
            setlistdata.setText(Html.fromHtml(setlist.getSetListData(),
                    Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE, null, null));
            setlistnotes.setText(Html.fromHtml(setlist.getSetListNotes(),
                    Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE, null, null));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_set_list_item, container, false);

        Button b = v.findViewById(R.id.button_fragment_setlist_item_fullText);
        b.setOnClickListener(this::viewFullSetList);

        return v;
    }
    public void viewFullSetList(View view){
        SetList setlist = (SetList) getArguments()
                .getSerializable(getString(R.string.key_setList));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(setlist.getUrl()));
        startActivity(intent);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        void onFragmentInteraction(Uri uri);
    }
}
