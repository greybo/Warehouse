package com.stock.fragment;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.stock.R;
import com.stock.MainActivity;
import com.stock.adapter.MeatAdapter;
import com.stock.dao.MeatDao;
import com.stock.entity.Meat;
import com.stock.utils.StockConstants;
import com.stock.utils.StockUtil;
import com.stock.utils.UpdateAdapter;

import java.util.ArrayList;

/**
 * Created by m on 02.08.2017.
 */

public class AllListFragment extends Fragment implements UpdateAdapter {

    private ListView listView;
    private MeatDao dao;
    private MeatAdapter adapter;
    private static ArrayList<Meat> allMeats;
    private static String key;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private static int positionScroll;
    private boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_list, container, false);
        listView = (ListView) view.findViewById(R.id.listViewAll);

        progressBar = new ProgressBar(getActivity());
//        isLoading = true;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter != null) {
                    DetailsFragment frg = DetailsFragment.newInstance((Meat) adapter.getItem(position));
                    StockUtil.changeFragment(getActivity(), frg, "allListFragment");
                }

            }
        });
        listView.setOnScrollListener(scrollListener);
        MainActivity.setTitle(getActivity(), getString(R.string.alllist_frg_load_title_warehouse));

        dao = new MeatDao(handler);
        if (allMeats == null || allMeats.size() == 0) {
            showProgressDialog();
            if (allMeats == null)
                allMeats = new ArrayList<>();
            dao.getAllMeats(key);
        } else {
            recreateAdapter();
        }
        dao.setUpdateAdapter(this);
        return view;
    }

    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem != totalItemCount)
                positionScroll = listView.getFirstVisiblePosition();
            if (view.getLastVisiblePosition() == totalItemCount - 1 && totalItemCount > 8 && !isLoading) {
                if (listView.getCount() > 0 && listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(progressBar);
                }
                isLoading = true;
                dao.getAllMeats(key);
            }
        }
    };

    private void displayDelay(final ArrayList<Meat> list) {
        if (key == null) {
            addEntriesToAdapter(list);
            return;
        }
        Handler h = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntriesToAdapter(list);
            }
        };
        h.postDelayed(runnable, 2000);
    }

    private void recreateAdapter() {
        adapter = null;
        addEntriesToAdapter(allMeats);
    }

    private void addEntriesToAdapter(ArrayList<Meat> list) {
        hideProgress();
        if (adapter == null) {
            createAdapter(list);
        } else {
            adapter.addMeatsToAdapter(list);
        }
        isLoading = false;
        if (positionScroll > 0) listView.setSelection(positionScroll + 1);
    }

    private void createAdapter(ArrayList<Meat> list) {
        adapter = new MeatAdapter(getActivity(), list);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            listView.addFooterView(progressBar);
        }
        listView.setAdapter(adapter);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StockConstants.HANDLER_RESULT_LIST:
                    ArrayList<Meat> list = (ArrayList<Meat>) msg.obj;
                    allMeats.addAll(list);
                    displayDelay(list);
//                    addEntriesToAdapter(list);
                    key = list.get(list.size() - 1).getId();
                    break;
                case StockConstants.HANDLER_NOT_FOUND:
                    hideProgress();
                    break;
            }
        }
    };

    private String getLastRowId() {
        if (allMeats == null || allMeats.size() == 0) {
            return null;
        }
        return allMeats.get(allMeats.size() - 1).getId();
    }

    private void hideProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        if (listView.getFooterViewsCount() == 1) {
            listView.removeFooterView(progressBar);
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.alllist_frg_load_list));
        progressDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        MeatDao.unregisterObserver(this);
    }

    @Override
    public void onUpdate() {
        allMeats = null;
        key = null;
    }
}
