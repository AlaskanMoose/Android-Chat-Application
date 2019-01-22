package tcss450.uw.edu.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tcss450.uw.edu.phishapp.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnRegisterFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register, container, false);
        // Inflate the layout for this fragment

        Button b = (Button) v.findViewById(R.id.button_register_register);
        b.setOnClickListener(this::register);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
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


    public void register(View view) {

        TextView email = getActivity().findViewById(R.id.text_email_register);
        TextView password = getActivity().findViewById(R.id.text_password_register);
        TextView verifyPassword = getActivity().findViewById(R.id.text_verify_password_register);
        String username = email.getText().toString();

        if(isFormValid(email, password, verifyPassword)) {
            Credentials.Builder credBuilder = new Credentials.Builder(username, password.getText().toString());
            mListener.onRegisterSuccess(credBuilder.build());
        }
    }

    private boolean isFormValid(TextView email, TextView password, TextView verifyPassword) {
        boolean canRegister  = true;
        if(TextUtils.isEmpty(email.getText())){
            email.setError("Email field left blank");
            canRegister = false;
        } else if (!email.getText().toString().contains("@")){
            email.setError("Email with domain is required");
            canRegister = false;
        }

        if(TextUtils.isEmpty(password.getText())){
            password.setError("Password field left blank");
            canRegister = false;
        }
        if(TextUtils.isEmpty(verifyPassword.getText())) {
            verifyPassword.setError("Retype password field left blank");
            canRegister = false;
        }
        if(password.getText().toString().length() < 6 ){
            password.setError("Password needs to be at least 6 characters");
            canRegister = false;
        }
        if(!password.getText().toString().equals(verifyPassword.getText().toString())){
            verifyPassword.setError("Password do not match");
            canRegister = false;
        }

        return canRegister;
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
    public interface OnRegisterFragmentInteractionListener {
        void onRegisterSuccess(Credentials credentials);
    }



}
