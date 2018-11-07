package com.klexos.my.life.SelectDisplayFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.klexos.my.life.R;
import com.klexos.my.life.ui.activeLists.ListsFragment;
import com.klexos.my.life.ui.activeLists.ToDoListFragment;


/**
 * Created by steph on 6/28/2017.
 */

public class ListsDisplayTabsFragment extends Fragment {

    private String encodedEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        encodedEmail = getArguments().getString("encodedEmail");

        // Inflate the tabLayout for the SingleTwitter fragments
        View inflatedView = inflater.inflate(R.layout.fragment_lists_tablayout, container, false);

        // This is the content Adapter
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        PagerAdapter adapter = new PagerAdapter(getFragmentManager());

        // Create an adapter that knows which fragment should be shown on each page
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Using FragmentStatePaterAdapter instead of FragmentPagerAdapter because
     * FragmentStatePaterAdapter destroys fragments that aren't being used.
     */
    public class PagerAdapter extends FragmentStatePagerAdapter {
        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) { // Shopping List

                ToDoListFragment fragment = ToDoListFragment.newInstance(encodedEmail);
                return fragment;
            } else { // Personal List

                ListsFragment fragment = ListsFragment.newInstance(encodedEmail);
                return fragment;
            }

//            ListsFragment fragment = null;
//
//            /**
//             * Set fragment to different fragments depending on position in ViewPager
//             */
//            switch (position) {
//                case 0:
//                    fragment = ListsFragment.newInstance(encodedEmail);
//                    break;
//                case 1:
////                    fragment = MealsFragment.newInstance();
//                    break;
//                default:
//                    fragment = ListsFragment.newInstance(encodedEmail);
//                    break;
//            }
//
//            return fragment;
        }

        /**
         * Return the total number of pages.
         */
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "To Do";
            } else
                return "Lists";
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}