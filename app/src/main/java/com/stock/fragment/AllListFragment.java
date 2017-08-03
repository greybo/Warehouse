package com.stock.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stock.R;
import com.stock.MainActivity;
import com.stock.adapter.MeatAdapter;
import com.stock.dao.MeatDao;
import com.stock.entity.Meat;
import com.stock.utils.StockUtil;

import java.util.ArrayList;

/**
 * Created by m on 02.08.2017.
 */

public class AllListFragment extends Fragment {

    private ListView listView;
    private MeatDao dao;
    private MeatAdapter adapter;
    //    private static String key;
    private ArrayList<Meat> allMeats;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_list, container, false);
        listView = (ListView) view.findViewById(R.id.listViewAll);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter != null) {
                    DetailsFragment frg = DetailsFragment.newInstans((Meat) adapter.getItem(position));
                    StockUtil.changeFragment(getActivity(), frg, "allListFragment");
                }

            }
        });
        MainActivity.setTitle(getActivity(), "Список");

        dao = new MeatDao(handler);
        if (allMeats == null || allMeats.size() == 0) {
            allMeats = new ArrayList<>();
            dao.getAllMeats(getLastRowId());
        } else {
            meatsToAdapter(allMeats);
        }

        return view;
    }

    private void meatsToAdapter(ArrayList<Meat> list) {
        if (adapter == null) {
            createAdapter(list);
        } else {
            adapter.addMeatsToAdapter(list);
        }
    }

    private void createAdapter(ArrayList<Meat> list) {
        adapter = new MeatAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MeatDao.HANDLER_RESULT_LIST:
                    ArrayList<Meat> list = (ArrayList<Meat>) msg.obj;
                    allMeats.addAll(list);
                    meatsToAdapter(list);
                    break;
            }
        }
    };

    private String getLastRowId() {
        if (allMeats == null||allMeats.size()==0) {
            return null;
        }
        return allMeats.get(allMeats.size() - 1).getId();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter = null;
    }
}
