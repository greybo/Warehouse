package com.stock;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.stock.entity.Meat;
import com.stock.fragment.AllListFragment;
import com.stock.fragment.DetailsFragment;
import com.stock.utils.StockUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.stock.utils.StockConstants.TAG_ALLLISTFRAGMENT;
import static com.stock.utils.StockConstants.TAG_DETAILFRAGMENT;

public class MainActivity extends AppCompatActivity {

    //    TODO
    private static final String TAG = "log_tag";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        createMenu();
        showCurrentFragment();
    }

    private void createMenu() {
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(160, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("log_tag", "onCreateOptionsMenu: " + getTagFrg(this));
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (getTagFrg(this) == null)
            return false;
        if (getTagFrg(this).equals(TAG_DETAILFRAGMENT)) {
            menu.findItem(R.id.action_save).setVisible(true);
            menu.findItem(R.id.action_add).setVisible(false);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }
        if (getTagFrg(this).equals(TAG_ALLLISTFRAGMENT)) {
            menu.findItem(R.id.action_save).setVisible(false);
            menu.findItem(R.id.action_add).setVisible(true);
            toolbar.setNavigationIcon(null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.containerFragment);
        DetailsFragment frg;
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //TODO
//                frg = (DetailsFragment) fragment;
//                frg.saveMeat();
                StockUtil.changeFragment(this, new AllListFragment(), "AllListFragment");
                break;
            case R.id.action_save:
                if (fragment instanceof DetailsFragment) {
                    frg = (DetailsFragment) fragment;
                    frg.saveMeat();
                    }
                Log.i(TAG, "onOptionsItemSelected: action_settings");
                break;
            case R.id.action_add:
                StockUtil.changeFragment(this,
                        DetailsFragment.newInstans(new Meat()), "AllListFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        super.onBackPressed();
        changeAll();
    }

    private void changeAll() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.containerFragment);
        try {
            ((StockApp) this.getApplication()).setTagFrg(fragment.getClass().getSimpleName());
        } catch (NullPointerException e) {
            Log.i(TAG, "changeAll: NullPointerException");
        }
        createMenu();
    }

    public static void setTitle(Activity activity, String title) {
        ((TextView) activity.findViewById(R.id.toolbar_title)).setText(title);
    }

    public static void setTagFrg(Activity activity, String tagFrg) {
        Log.i(TAG, "setTagFrg: " + tagFrg);
        ((StockApp) activity.getApplication()).setTagFrg(tagFrg);
        ((MainActivity) activity).createMenu();
    }

    public static String getTagFrg(Activity activity) {
        return ((StockApp) activity.getApplication()).getTagFrg();
    }

    //TODO
    private void showCurrentFragment() {
        Fragment frg = null;
        if (getTagFrg(this) == null) {
            setTagFrg(this, TAG_ALLLISTFRAGMENT);
        }
        if (getTagFrg(this).equals(TAG_ALLLISTFRAGMENT)) {
            frg = new AllListFragment();
        }
        if (getTagFrg(this).equals(TAG_DETAILFRAGMENT)) {
            frg = new DetailsFragment();
        }
        StockUtil.changeFragment(this, frg, "main");
    }
}
