package com.klexos.samonte.intelligentexpense.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.klexos.samonte.intelligentexpense.R;
import com.klexos.samonte.intelligentexpense.ui.BaseActivity;
import com.klexos.samonte.intelligentexpense.ui.MainActivity;
import com.klexos.samonte.intelligentexpense.utils.Constants;
import com.klexos.samonte.intelligentexpense.utils.Utils;

/**
 * Represents Sign in screen and functionality of the app
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;
    /* References to the Firebase */
    private FirebaseAuth mAuth;
    private EditText mEditTextEmailInput, mEditTextPasswordInput;

    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mSharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPrefEditor = mSharedPref.edit();

        /**
         * Create Firebase references
         */
        mAuth = FirebaseAuth.getInstance();

        /**
         * Link layout elements from XML and setup progress dialog
         */
        initializeScreen();

        /**
         * Call signInPassword() when user taps "Done" keyboard action
         */
        mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    signIn();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Checks if the user is already signed in
         * and signs in automatically signes in
         */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = user.getUid();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        /**
         * Get the newly registered user email if present, use null as default value
         */
        String signupEmail = mSharedPref.getString(Constants.KEY_SIGNUP_EMAIL, null);

        /**
         * Fill in the email editText and remove value from SharedPreferences if email is present
         */
        if (signupEmail != null) {
            mEditTextEmailInput.setText(signupEmail);

            /**
             * Clear signupEmail sharedPreferences to make sure that they are used just once
             */
            mSharedPrefEditor.putString(Constants.KEY_SIGNUP_EMAIL, null).apply();
        }
    }

    /**
     * Link layout elements from XML and setup the progress dialog
     */
    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);
        LinearLayout linearLayoutLoginActivity = (LinearLayout) findViewById(R.id.linear_layout_login_activity);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
    }

    /**
     * Sign in with Password provider (used when user taps "Done" action on keyboard)
     */
    public void signIn() {
        String unprocessedEmail = mEditTextEmailInput.getText().toString();
        String password = mEditTextPasswordInput.getText().toString();

        /**
         * If email and password are not empty show progress dialog and try to authenticate
         */
        if (unprocessedEmail.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();

        /**
         * Encode user email replacing "." with ","
         * to be able to use it as a Firebase db key
         */
        mEncodedEmail = Utils.encodeEmail(unprocessedEmail);

        // Save user data in shared preferences
        mSharedPrefEditor.putString(Constants.KEY_ENCODED_EMAIL, mEncodedEmail).apply();

        /**
         * Save name and email to sharedPreferences to create User database record
         * when the registered user will sign in for the first time
         */
        mAuth.signInWithEmailAndPassword(unprocessedEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        mAuthProgressDialog.hide();

                        if (task.isSuccessful()) {
                            if (!isEmailVerified()) {
                                displayToastMessage(getBaseContext(), "User Is not Verified");
                            } else {
                                displayToastMessage(getBaseContext(), "User Is Verified");

                                /* Go to main activity */
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            displayToastMessage(getBaseContext(), "signInWithEmail:failed");
                        }

                        // ...
                    }
                });
    }

    /**
     * Sign in with Password provider when user clicks sign in button
     */
    public void onSignInPressed(View view) {
        signIn();
    }

    /**
     * Open CreateAccountActivity when user taps on "Sign up" TextView
     */
    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!isEmailVerified()) {
            // logout (don't allow to use application
            FirebaseAuth.getInstance().signOut();

            // Exit application
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else {
            displayToastMessage(getBaseContext(), "User Is Verified");
        }
    }

    public boolean isEmailVerified(){
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            displayToastMessage(getBaseContext(), "User Is not Verified");
            return user.isEmailVerified(); // Check if user's email is verified
        } else {
            displayToastMessage(this, "There is no user.");
            return false;
        }
    }

    /**
     * Sends a password reset to an email address
     */
    public void onForgotPasswordPressed(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String emailAddress = mEditTextEmailInput.getText().toString();

        // check if email address is valid
        if (isEmailValid(emailAddress)){
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                displayToastMessage(getBaseContext(), "Email sent to: " + emailAddress);
                            } else {
                                displayToastMessage( getBaseContext(), "Error. No Email sent.");
                            }
                        }
                    });
        }
    }

    // checks if the user inputed a valied email address
    private boolean isEmailValid(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEditTextEmailInput.setError(String.format(getString(R.string.error_invalid_email_not_valid),
                    email));
            return false;
        }
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        /**
         * Get the newly registered user email if present, use null as default value
         */
        String signupEmail = mSharedPref.getString(Constants.KEY_SIGNUP_EMAIL, null);

        /**
         * Fill in the email editText and remove value from SharedPreferences if email is present
         */
        if (signupEmail != null) {
            mEditTextEmailInput.setText(signupEmail);

            /**
             * Clear signupEmail sharedPreferences to make sure that they are used just once
             */
            mSharedPrefEditor.putString(Constants.KEY_SIGNUP_EMAIL, null).apply();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }
}