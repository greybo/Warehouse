package com.stock.dao;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stock.entity.Meat;
import com.stock.utils.StockConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by m on 01.08.2017.
 */

public class MeatDao extends ObjectDao {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference meatRef;
    private final static String MEAT_DATABASE_PATH = "meat";
    public final static int HANDLER_RESULT_OK = 10101;
    public final static int HANDLER_RESULT_LIST = 10102;
    public final static int HANDLER_NOT_FOUND = 10103;
    public final static int HANDLER_RESULT_ERR = 10104;

    public MeatDao(Handler handler) {
        super(handler);
        if (meatRef == null)
            meatRef = database.getReference(MEAT_DATABASE_PATH);
    }

    public void save(final Meat meat) {
        String key = meatRef.push().getKey();
        meat.setId(key);
        meatRef.child(key).setValue(meat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    success(HANDLER_RESULT_OK);
                } else {
                    error(HANDLER_RESULT_ERR);
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
                    success(HANDLER_RESULT_OK, meat.getId());
                }
            }
        });
    }

    public void getAllMeats(final String key) {
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
                    success(HANDLER_NOT_FOUND);
                } else {
                    List<Meat> list = new ArrayList<>();
                    int j = meats.size();
                    for (int i = 0; i < meats.size(); i++) {
                        list.add(meats.get(--j));
                    }
                    success(HANDLER_RESULT_LIST, list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                error(HANDLER_RESULT_ERR);
            }
        });
    }
}
