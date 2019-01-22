package tcss450.uw.edu.phishapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import tcss450.uw.edu.phishapp.model.Credentials;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener, SuccessFragment.OnFragmentInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            if (findViewById(R.id.frame_main_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main_container, new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onLoginSuccess(Credentials credentials, String jwt) {
        Log.wtf(TAG, "Login Success!!!");

        SuccessFragment successFragment = new SuccessFragment();
        Bundle args = new Bundle();
        args.putSerializable("key", credentials);
        successFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, successFragment);

        transaction.commit();
    }

    @Override
    public void onRegisteredClicked() {
        Log.wtf(TAG, "onRegisterClicked");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_container, new RegisterFragment())
        .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onRegisterSuccess(Credentials credentials) {
        Log.wtf(TAG, "register success!!!");

        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable("key", credentials);
        loginFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, loginFragment);

        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Credentials credentials) {

    }
}
