package com.klexos.samonte.intelligentexpense.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.klexos.samonte.intelligentexpense.SelectDisplayFragment.DisplayContent;
import com.klexos.samonte.intelligentexpense.ui.BaseActivity;

import com.klexos.samonte.intelligentexpense.R;
import com.klexos.samonte.intelligentexpense.ui.MainActivity;
import com.klexos.samonte.intelligentexpense.utils.Constants;

/**
 * Represents Sign in screen and functionality of the app
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    /* A dialog that is presented until the Firebase authentication finished. */
    private ProgressDialog mAuthProgressDialog;
    /* References to the Firebase */
    private Firebase mFirebaseRef;
    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;
    private EditText mEditTextEmailInput, mEditTextPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        String email = mEditTextEmailInput.getText().toString();
        String password = mEditTextPasswordInput.getText().toString();

        /**
         * If email and password are not empty show progress dialog and try to authenticate
         */
        if (email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }

        if (password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            return;
        }
        mAuthProgressDialog.show();

        /**
         * Save name and email to sharedPreferences to create User database record
         * when the registered user will sign in for the first time
         */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(Constants.KEY_SIGNUP_EMAIL, email).apply();

        mAuth.signInWithEmailAndPassword(email, password)
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditTextEmailInput.setText(prefs.getString(Constants.KEY_SIGNUP_EMAIL, ""));
    }
}