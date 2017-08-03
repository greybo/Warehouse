package com.stock.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.stock.R;
import com.stock.MainActivity;

/**
 * Created by m on 29.07.2017.
 */

public class StockUtil {
    public static void changeFragment(FragmentActivity activity, Fragment fragment, String TAG) {
        MainActivity.setTagFrg(activity,fragment.getClass().getSimpleName());
        FragmentManager manager = activity.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.containerFragment, fragment, TAG)
                .addToBackStack(null)
                .commit();
    }

}
