package tcss450.uw.edu.phishapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import tcss450.uw.edu.phishapp.model.Credentials;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private OnLoginFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        Button b = (Button) v.findViewById(R.id.button_register_sign);
        b.setOnClickListener(this::register);

        b = (Button) v.findViewById(R.id.button_signin_sign);
        b.setOnClickListener(this::signin);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            // Could pass
            Credentials credentials = (Credentials) getArguments().getSerializable("key");
            updateContent(credentials);

        }
    }

    public void updateContent(Credentials credentials) {
        TextView signIn = getActivity().findViewById(R.id.text_email_sign);
        signIn.setText(credentials.getEmail());
        TextView password = getActivity().findViewById(R.id.text_password_sign);
        password.setText(credentials.getPassword());

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
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
    public interface OnLoginFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoginSuccess(Credentials credentials, String jwt);

       void onRegisteredClicked();
    }

    public void register(View view){
        mListener.onRegisteredClicked();

    }

    public void signin (View view) {
            TextView email = getActivity().findViewById(R.id.text_email_sign);
        TextView password = getActivity().findViewById(R.id.text_password_sign);
        String username = email.getText().toString();
        String pWord = password.getText().toString();

            if(isFormValid(email, password)){
                Credentials.Builder credentials =
                        new Credentials.Builder(username, pWord);
                mListener.onLoginSuccess(credentials.build(), null);
                    }
            }

    private boolean isFormValid(TextView email, TextView password) {
        boolean canPass = true;
        if(TextUtils.isEmpty(email.getText())){
                email.setError("Email field left blank");
                canPass = false;
            } else if(!email.getText().toString().contains("@")){
                email.setError("Email address with domain is required");
                canPass = false;
            }

        if(TextUtils.isEmpty(password.getText())){
            password.setError("Password required");
            canPass = false;
        }
        return canPass;
    }

}




