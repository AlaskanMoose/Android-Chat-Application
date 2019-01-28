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
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import tcss450.uw.edu.phishapp.model.Credentials;
import tcss450.uw.edu.phishapp.utils.SendPostAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private OnLoginFragmentInteractionListener mListener;
    private Credentials mCredentials;

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
        b.setOnClickListener(this::attemptLogin);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            // Could pass
            Credentials credentials = (Credentials) getArguments().
                    getSerializable(getString(R.string.keys_intent_credentials));
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
                    + " must implement OnSuccessFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR",  result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        mListener.onWaitFragmentInteractionShow();

    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));

            if (success) {
                //Login was successful. Switch to the loadSuccessFragment.
                mListener.onLoginSuccess(mCredentials,
                        resultsJSON.getString(
                                getString(R.string.keys_json_login_jwt)));
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.text_email_sign))
                        .setError("Login Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());

            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.text_email_sign))
                    .setError("Login Unsuccessful");
        }
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
    public interface OnLoginFragmentInteractionListener extends WaitFragment.OnWaitFragmentInteractionListener{
        // TODO: Update argument type and name
        void onLoginSuccess(Credentials credentials, String jwt);

       void onRegisteredClicked();
    }

    public void register(View view){
        mListener.onRegisteredClicked();

    }

//    public void signin (View view) {
//            TextView email = getActivity().findViewById(R.longDate.text_email_sign);
//        TextView password = getActivity().findViewById(R.longDate.text_password_sign);
//        String username = email.getText().toString();
//        String pWord = password.getText().toString();
//
//            if(isFormValid(email, password)){
//                Credentials.Builder credentials =
//                        new Credentials.Builder(username, pWord);
//                mListener.onLoginSuccess(credentials.build(), null);
//                    }
//            }

    private void attemptLogin(final View theButton) {

        EditText emailEdit = getActivity().findViewById(R.id.text_email_sign);
        EditText passwordEdit = getActivity().findViewById(R.id.text_password_sign);

        boolean hasError = false;
        if (emailEdit.getText().length() == 0) {
            hasError = true;
            emailEdit.setError("Field must not be empty.");
        }  else if (emailEdit.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            emailEdit.setError("Field must contain a valid email address.");
        }
        if (passwordEdit.getText().length() == 0) {
            hasError = true;
            passwordEdit.setError("Field must not be empty.");
        }

        if (!hasError) {
            Credentials credentials = new Credentials.Builder(
                    emailEdit.getText().toString(),
                    passwordEdit.getText().toString())
                    .build();

            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_login))
                    .build();

            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();

            mCredentials = credentials;

            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleLoginOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }
    }


//    private boolean isFormValid(TextView email, TextView password) {
//        boolean canPass = true;
//        if(TextUtils.isEmpty(email.getText())){
//                email.setError("Email field left blank");
//                canPass = false;
//            } else if(!email.getText().toString().contains("@")){
//                email.setError("Email address with domain is required");
//                canPass = false;
//            }
//
//        if(TextUtils.isEmpty(password.getText())){
//            password.setError("Password required");
//            canPass = false;
//        }
//        return canPass;
//    }

}




