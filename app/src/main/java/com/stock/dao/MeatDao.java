package com.stock.dao;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stock.entity.Meat;
import com.stock.utils.StockConstants;
import com.stock.utils.UpdateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greybo on 01.08.2017.
 */

public class MeatDao extends ObjectDao {
    private static final String TAG = MeatDao.class.getSimpleName();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference meatRef;
    private final static String MEAT_DATABASE_PATH = "meat";
    private static UpdateAdapter observer;

    public MeatDao(Handler handler) {
        super(handler);
        if (meatRef == null)
            meatRef = database.getReference(MEAT_DATABASE_PATH);
    }

    public void save(final Meat meat) {
        @SuppressWarnings("ConstantConditions")
        String uid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String key = meatRef.push().getKey();
        meat.setUserId(uid);
        meat.setId(key);
        meatRef.child(key).setValue(meat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateAdapter();
                    success(StockConstants.HANDLER_RESULT_OK);
                } else {
                    error(StockConstants.HANDLER_RESULT_ERR);
                }
            }
        });
    }

    public void updateByObject(final Meat meat) {
        if (meat.getId() == null) return;
//        meat.setLastUpdate(new Date().getTime());
        meatRef.child(meat.getId())
                .getRef().setValue(meat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateAdapter();
                    success(StockConstants.HANDLER_RESULT_OK, meat.getId());
                }
            }
        });
    }

    public void getAllMeats(final String key) {
        //noinspection ConstantConditions
        Query query;
        final int limit = 30;
        if (key != null) {
            query = meatRef.orderByKey().endAt(key).limitToLast(limit);
        } else {
            query = meatRef.orderByChild(MEAT_DATABASE_PATH).limitToLast(limit);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Meat> meats = new ArrayList<>();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Meat m = s.getValue(Meat.class);
                    assert m != null;
                    if (key == null || !m.getId().equals(key)) {
                        meats.add(m);
                    }
                }
                if (meats.size() == 0) {
                    success(StockConstants.HANDLER_NOT_FOUND);
                } else {
                    List<Meat> list = new ArrayList<>();
                    int j = meats.size();
                    for (int i = 0; i < meats.size(); i++) {
                        list.add(meats.get(--j));
                    }
                    success(StockConstants.HANDLER_RESULT_LIST, list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                error(StockConstants.HANDLER_RESULT_ERR);
            }
        });
    }

    public void recordsCurrentUser(ArrayList<Meat> list) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        ArrayList<Meat> meatsFilter = new ArrayList<>();
        Log.i(TAG, "recordsCurrentUser " + uid + " size: " + list.size());
        if (list.size() > 0) {
            for (Meat m : list) {
                String email = m.getUserId();
                if (email != null && m.getUserId().equals(uid)) {
                    meatsFilter.add(m);
                }
            }
        }
        success(StockConstants.HANDLER_MEAT_FITER_LIST_OK, meatsFilter);
    }

    public void setUpdateAdapter(UpdateAdapter observer) {
        this.observer = observer;
    }

    private void updateAdapter() {
        observer.onUpdate();
    }


}
