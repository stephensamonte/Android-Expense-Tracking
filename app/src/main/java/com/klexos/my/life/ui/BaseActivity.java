package com.klexos.my.life.ui;

/**
 * Created by steph on 6/29/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.klexos.my.life.R;
import com.klexos.my.life.ui.login.CreateAccountActivity;
import com.klexos.my.life.ui.login.LoginActivity;
import com.klexos.my.life.utils.Constants;

/**
 * BaseActivity class is used as a base class for all activities in the app
 * It implements GoogleApiClient callbacks to enable "Logout" in all activities
 * and defines variables that are being shared across all activities
 */
public abstract class BaseActivity extends AppCompatActivity {

    // This is for toast messages
    public static Toast toast;

    private String TAG = "BaseActivity.java";

    public String mEncodedEmail;

    // This is for Firebase user authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Toast message method
    public static void displayToastMessage(Context context, String message) {
        /**
         * This checks to see if there's a toast message currently being displayed.
         * If so the current toast is canceled.
         */
        if (toast != null) {
            toast.cancel();
        }

        // This makes the new toast messages
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing FirebaseAuthen instance
        mAuth = FirebaseAuth.getInstance();

        /**
         * Getting mProvider and mEncodedEmail from SharedPreferences
         */
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
        /* Get mEncodedEmail from SharedPreferences, use null as default value */
        mEncodedEmail = sp.getString(Constants.KEY_ENCODED_EMAIL, null);

        /**
         * If not on login activity or create activity then listen for authentication change.
         * If the user is signed out then open the login page
          */
        if (!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthState:signed_out");
                        /* Clear out shared preferences */
                        sp.edit().remove(Constants.KEY_ENCODED_EMAIL).apply();

                        takeUserToLoginScreenOnUnAuth();
                    }
                    // ...
                }
            };
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    protected void initializeBackground(LinearLayout linearLayout) {
//
//        /**
//         * Set different background image for landscape and portrait layouts
//         */
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            linearLayout.setBackgroundResource(R.drawable.background_loginscreen_land);
//        } else {
//            linearLayout.setBackgroundResource(R.drawable.background_loginscreen);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();

        // If not on the LoginActivity or the CreateAccountActivity
        if (!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            // Attach FirebaseAuth listener instance
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove FirebaseAuth listener instance
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void takeUserToLoginScreenOnUnAuth() {
        /* Move user to LoginActivity, and remove the backstack */
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}