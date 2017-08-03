package com.stock;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.stock.entity.Meat;

/**
 * Created by m on 01.08.2017.
 */

public class StockApp extends Application {
    private Meat meat;
    private String tagFrg;

    public Meat getHomeController() {
        if (meat == null) {
            meat = new Meat();
        }
        return meat;
    }

    public String getTagFrg() {
        return tagFrg;
    }

    public void setTagFrg(String tagFrg) {
        this.tagFrg = tagFrg;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        FirebaseApp.initializeApp(this);
    }
}
