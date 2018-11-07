package com.klexos.samonte.intelligentexpense.SelectDisplayFragment;

/**
 * Created by steph on 6/27/2017.
 */

import com.klexos.samonte.intelligentexpense.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DisplayContent {

    /**
     * An array of sample (playlist) items.
     */
    static List<DisplayItem> OVERVIEW = new ArrayList<>();
    static List<DisplayItem> EXPENSES = new ArrayList<>();
    static List<DisplayItem> INCOME = new ArrayList<>();
    static List<DisplayItem> SAVINGS = new ArrayList<>();
    static List<DisplayItem> LISTS = new ArrayList<>();

    static {
        // Add some display items.
        OVERVIEW.add(createDisplayItem("1", "Net Flow", R.drawable.side_nav_bar));

        EXPENSES.add(createDisplayItem("1", "Summary", R.drawable.side_nav_bar));
        EXPENSES.add(createDisplayItem("2", "House", R.drawable.ic_menu_camera));
        EXPENSES.add(createDisplayItem("3", "Utilities", R.drawable.ic_menu_camera));
        EXPENSES.add(createDisplayItem("4", "Food", R.drawable.ic_menu_camera));
        EXPENSES.add(createDisplayItem("5", "Transportation", R.drawable.ic_menu_camera));
        EXPENSES.add(createDisplayItem("6", "Goods", R.drawable.ic_menu_camera));
        EXPENSES.add(createDisplayItem("7", "Other", R.drawable.ic_menu_camera));

        INCOME.add(createDisplayItem("1", "Summary", R.drawable.side_nav_bar));
        INCOME.add(createDisplayItem("2", "Job", R.drawable.ic_menu_camera));
        INCOME.add(createDisplayItem("3", "Other", R.drawable.ic_menu_camera));

        SAVINGS.add(createDisplayItem("1", "Summary", R.drawable.side_nav_bar));
        SAVINGS.add(createDisplayItem("2", "Wallet", R.drawable.ic_menu_camera));
        SAVINGS.add(createDisplayItem("3", "Bank Account", R.drawable.ic_menu_camera));

        LISTS.add(createDisplayItem("1", "To Do List", R.drawable.side_nav_bar));
        LISTS.add(createDisplayItem("2", "Shopping List", R.drawable.ic_menu_camera));
        LISTS.add(createDisplayItem("3", "BPersonal List", R.drawable.ic_menu_camera));


    }

    private static DisplayItem createDisplayItem(String id, String name, int image) {
        return new DisplayItem(id, name, image);
    }

    /**
     * A playlist item representing a piece of content.
     */
    public static class DisplayItem {
        public final String id;
        final String item_name;
        final int image;

        DisplayItem(String id, String item_name, int image) {
            this.id = id;
            this.item_name = item_name;
            this.image = image;
        }

        @Override
        public String toString() {
            return item_name;
        }
    }
}