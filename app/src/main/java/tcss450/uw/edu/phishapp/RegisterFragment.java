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

import org.json.JSONException;
import org.json.JSONObject;

import tcss450.uw.edu.phishapp.model.Credentials;
import tcss450.uw.edu.phishapp.utils.SendPostAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnRegisterFragmentInteractionListener mListener;
    private Credentials mCredentials;

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
                    + " must implement OnSuccessFragmentInteractionListener");
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
//        String username = email.getText().toString();
        TextView userName = getActivity().findViewById(R.id.text_userName_register);
        TextView firstName = getActivity().findViewById(R.id.text_firstName_register);
        TextView lastName = getActivity().findViewById(R.id.text_lastName_register);




        if(isFormValid(email, password, verifyPassword, userName, firstName, lastName)) {
//            Credentials.Builder credBuilder = new Credentials.Builder(username, password.getText().toString());
//            mListener.onRegisterSuccess(credBuilder.build());

            Credentials credentials = new Credentials.Builder(
                    email.getText().toString(),
                    password.getText().toString())
                    .addUsername(userName.getText().toString())
                    .addFirstName(firstName.getText().toString())
                    .addLastName(lastName.getText().toString())
                    .build();

            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_register))
                    .build();

            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();

            mCredentials = credentials;

            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleRegisterOnPre)
                    .onPostExecute(this::handleRegisterPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }
    }


    private boolean isFormValid(TextView email, TextView password, TextView verifyPassword
    , TextView userName, TextView firstName, TextView lastName) {
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
        if(TextUtils.isEmpty(userName.getText())){
            userName.setError("User Name cannot be empty");
            canRegister = false;
        }
        if(TextUtils.isEmpty(firstName.getText())) {
            firstName.setError("First Name cannot be empty");
            canRegister = false;
        }
        if(TextUtils.isEmpty(lastName.getText())){
            lastName.setError("Last Name cannot be empty");
            canRegister = false;
        }
        return canRegister;
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
    private void handleRegisterOnPre() {
        mListener.onWaitFragmentInteractionShow();

    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegisterPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));

            if (success) {
                //Register was successful. Switch to the loadSuccessFragment.
                mListener.onRegisterSuccess(mCredentials);
                return;
            } else {
                //Register was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.text_email_register))
                        .setError("Register Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());

            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.text_email_register))
                    .setError("Register Unsuccessful");
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
    public interface OnRegisterFragmentInteractionListener extends WaitFragment.OnWaitFragmentInteractionListener {
        void onRegisterSuccess(Credentials credentials);
    }



}
