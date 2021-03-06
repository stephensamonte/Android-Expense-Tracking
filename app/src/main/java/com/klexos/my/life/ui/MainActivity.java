package com.klexos.my.life.ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.klexos.my.life.R;
import com.klexos.my.life.SelectDisplayFragment.DisplayContent;
import com.klexos.my.life.SelectDisplayFragment.GeneralDisplayFragment;
import com.klexos.my.life.SelectDisplayFragment.ListsDisplayTabsFragment;
import com.klexos.my.life.model.User;
import com.klexos.my.life.ui.activeListDetails.ActiveListDetailsActivity;
import com.klexos.my.life.ui.activeLists.AddListDialogFragment;
import com.klexos.my.life.ui.login.LoginActivity;
import com.klexos.my.life.utils.Constants;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GeneralDisplayFragment.OnListFragmentInteractionListener {

    private String TAG = ("MainActivity");

    FirebaseUser mUser;
    private String mUID = "";
    private String mUsername = "";
    private String mEmail = "";

    public static int DisplayNumber = 0;

    // Fragments that are in my application
    private GeneralDisplayFragment[] GeneralFragments = new GeneralDisplayFragment[4];
    private ListsDisplayTabsFragment ListsDisplayFragment = new ListsDisplayTabsFragment();

    private ValueEventListener mUserRefListener;
    private Firebase mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // other setup code

        setContentView(R.layout.activity_main);

        // Here

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // This is to get a Firebase User's profile
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        // This is to get a Firebase User's profile from firebase User authentication
        if (mUser != null) {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            mUID = mUser.getUid();

            // Name, email address, and profile photo Url
            mUsername = mUser.getDisplayName();
            mEmail = mUser.getEmail();

            Log.v("User1 UID", mUID + "");
            Log.v("User1 Name", mUsername + "");
            Log.v("User1 email", mEmail + "");
        }

        /**
         * This is to get UserProfile from my firebase
         * Create Firebase references
         */
        if (mEncodedEmail != null){
            mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);

            /**
             * This is to get UserProfile from my firebase
             * Add ValueEventListeners to Firebase references
             * to control get data and control behavior and visibility of elements
             */
            mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);

                    /**
                     * Set the activity title to current user name if user is not null
                     */
                    if (user != null) {
                    /* Assumes that the first word in the user's name is the user's first name. */
                        mUsername = user.getName();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e(TAG,
                            getString(R.string.log_error_the_read_failed) +
                                    firebaseError.getMessage());
                }
            });
        }

        //Set the fragment initially
        if (savedInstanceState == null) {  // <- important this prevents making another instance
            OpenDisplayListActivityFragment(); // Opens List Display Fragment
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_monthly) {
            displayToastMessage(this, "Monthly Selected");
            return true;
        } else if (id == R.id.action_sign_out) {
            displayToastMessage(this, "Sign Out Selected");

            signOutUser();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lists) {
            OpenDisplayListActivityFragment();
        } else if (id == R.id.nav_overview) {
            // Handle the camera action
            DisplayNumber = 0;
            OpenDisplayActivityFragment(DisplayNumber);
            (MainActivity.this).setActionBarTitle(getResources().getString(R.string.nav_overview));

        } else if (id == R.id.nav_expenses) {
            DisplayNumber = 1;
            OpenDisplayActivityFragment(DisplayNumber);
            (MainActivity.this).setActionBarTitle(getResources().getString(R.string.nav_expenses));
        } else if (id == R.id.nav_income) {
            DisplayNumber = 2;
            OpenDisplayActivityFragment(DisplayNumber);
            (MainActivity.this).setActionBarTitle(getResources().getString(R.string.nav_income));

        } else if (id == R.id.nav_savings) {
            DisplayNumber = 3;
            OpenDisplayActivityFragment(DisplayNumber);
            (MainActivity.this).setActionBarTitle(getResources().getString(R.string.nav_savings));

        } else if (id == R.id.nav_share) {

            // this is to get the package name
            String packageName = getApplicationContext().getPackageName();

            // Share application intent
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    getResources().getString(R.string.app_share_introduction) + " " +
                            getResources().getString(R.string.app_name) + " " +
                            getResources().getString(R.string.app_share_subject) + "\n\n" +
                            getResources().getString(R.string.app_download_website) +
                            packageName);
            sendIntent.setType("text/plain");
            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(sendIntent);
            }

        } else if (id == R.id.nav_help_and_feedback) {
            Intent intent = new Intent(this, ActiveListDetailsActivity.class);
            /* Get the list ID using the adapter's get ref method to get the Firebase
             * ref and then grab the key.
             */

            Firebase ref = new Firebase("https://console.firebase.google.com/u/0/project/my-life-tool/database/data/userLists/klexosinc@gmail,com/-Ko8zUGo0XN00PIOM4aK");

            String listId = ref.getKey();

            Log.v("listID", listId + "");

            intent.putExtra(Constants.KEY_LIST_ID, listId);
                    /* Starts an active showing the details for the selected list */
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            /**
             * Open SettingsActivity with sort options when settings icon is clicked
             */
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void OpenDisplayActivityFragment(int x) {

        // This prevents the same fragment from being created more than once
        if (GeneralFragments[x] == null) {
            //Set the fragment initially
            GeneralFragments[x] = new GeneralDisplayFragment();
        }

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, GeneralFragments[x]);
        fragmentTransaction.commit();
    }

    // This sets the Title on the App Bar
    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void OpenDisplayListActivityFragment(){
        DisplayNumber = 4; // This is not required or used

        // Make a new fragment every time you return to the activity
        ListsDisplayFragment = new ListsDisplayTabsFragment();

        // Send Encoded String to the ListDisplayTabsFragment
        Bundle bundle = new Bundle();
        bundle.putString("encodedEmail", mEncodedEmail); // key value pair encoded email

        // set Fragmentclass Arguments
        ListsDisplayFragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, ListsDisplayFragment);
        fragmentTransaction.commit();

        // Title is the user's first name + "Lists"
        int indexOfSpace;
        if (mUsername.indexOf(' ') != -1){ // if there is a " "
            indexOfSpace = mUsername.indexOf(' ');
        } else { // if there is no " "
            indexOfSpace = mUsername.length() - 1;
        }

        (MainActivity.this).setActionBarTitle(mUsername.substring(0, indexOfSpace) + "\'s " + getResources().getString(R.string.nav_lists));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    @Override
    public void onListFragmentInteraction(DisplayContent.DisplayItem item) {

    }


    /**
     * Create an instance of the AddList dialog fragment and show it
     */
    public void showAddListDialog(View view) {
        /* Create an instance of the dialog fragment and show it */
        DialogFragment dialog = AddListDialogFragment.newInstance(mEncodedEmail);
        dialog.show(MainActivity.this.getFragmentManager(), "AddListDialogFragment");
    }

    private void checkIfUserExits(){
        // if the user is not logged in then open the login screen
        if (mUser == null) {
            // open login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        checkIfUserExits();
    }

    private void signOutUser (){
        // sign out the current person
        FirebaseAuth.getInstance().signOut();

        // Erase User SharedPreference Values
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove(Constants.KEY_SIGNUP_EMAIL).apply();

//        Log.v("HERE1", prefs.getString(Constants.KEY_SIGNUP_EMAIL, ""));

        // open login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
