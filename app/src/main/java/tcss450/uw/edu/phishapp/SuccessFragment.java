package tcss450.uw.edu.phishapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.phishapp.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSuccessFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SuccessFragment extends Fragment {


    private OnSuccessFragmentInteractionListener mListener;

    public SuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            // Could pass


            Credentials credentials = (Credentials)getArguments().getSerializable((getString(R.string.key_credentials)));
            updateContent(credentials);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_success, container, false);


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSuccessFragmentInteractionListener) {
            mListener = (OnSuccessFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSuccessFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateContent(Credentials credentials) {
        TextView tv = getActivity().findViewById(R.id.text_success_userName);
        tv.setText(credentials.getEmail());
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
    public interface OnSuccessFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Credentials credentials);
    }
}
