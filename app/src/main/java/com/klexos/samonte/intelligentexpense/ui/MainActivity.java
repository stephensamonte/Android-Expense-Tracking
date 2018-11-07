package com.klexos.samonte.intelligentexpense.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.klexos.samonte.intelligentexpense.R;
import com.klexos.samonte.intelligentexpense.SelectDisplayFragment.DisplayContent;
import com.klexos.samonte.intelligentexpense.SelectDisplayFragment.GeneralDisplayFragment;
import com.klexos.samonte.intelligentexpense.SelectDisplayFragment.ListsDisplayTabsFragment;
import com.klexos.samonte.intelligentexpense.ui.activeLists.AddListDialogFragment;

public class MainActivity extends BaseActivity {

    public static int DisplayNumber = 0;

    // This is for toast messages
    public static Toast toast;

    // Fragments that are in my application
    private GeneralDisplayFragment[] GeneralFragments = new GeneralDisplayFragment[4];
    private ListsDisplayTabsFragment ListsDisplayFragment = new ListsDisplayTabsFragment();

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

        Firebase.setAndroidContext(this);
        // other setup code

        setContentView(R.layout.activity_main);

        //Set the fragment initially
        if (savedInstanceState == null) {  // <- important this prevents making another instance
            DisplayNumber = 0; // Opens Overview Display Fragment
            OpenDisplayActivityFragment(DisplayNumber); // Opens Display Fragment
        }

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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_overview) {
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

        } else if (id == R.id.nav_lists) {
            DisplayNumber = 4; // This is not required or used

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, ListsDisplayFragment);
            fragmentTransaction.commit();

            (MainActivity.this).setActionBarTitle(getResources().getString(R.string.nav_lists));
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

        } else if (id == R.id.nav_settings) {

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
        DialogFragment dialog = AddListDialogFragment.newInstance();
        dialog.show(MainActivity.this.getFragmentManager(), "AddListDialogFragment");
    }
}
