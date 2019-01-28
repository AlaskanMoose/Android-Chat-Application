package tcss450.uw.edu.phishapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

import tcss450.uw.edu.phishapp.model.Credentials;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        SuccessFragment.OnSuccessFragmentInteractionListener,
        WaitFragment.OnWaitFragmentInteractionListener {

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

//    @Override
//    public void onLoginSuccess(Credentials credentials, String jwt) {
//        Log.wtf(TAG, "Login Success!!!");
//
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra(getString(R.string.key_credentials), credentials);
//        startActivity(intent);
//    }

@Override
public void onLoginSuccess(final Credentials credentials, String jwt) {
    Intent i = new Intent(this, HomeActivity.class);
    i.putExtra(getString(R.string.keys_intent_credentials), (Serializable) credentials);
    i.putExtra(getString(R.string.keys_intent_jwt), jwt);
    startActivity(i);
    finish();
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();

//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra(getString(R.string.keys_intent_credentials), credentials);
//        startActivity(intent);

        LoginFragment loginFragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.keys_intent_credentials), credentials);
        loginFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, loginFragment);

        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Credentials credentials) {

    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }
}
