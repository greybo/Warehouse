package com.stock.dao;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stock.utils.StockConstants;

/**
 * Created by greybo on 07.08.2017.
 */

public class UserDao extends ObjectDao {
    private static final String TAG = UserDao.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public UserDao(Handler handler) {
        super(handler);
        mAuth = FirebaseAuth.getInstance();
        registerListener();
    }

    public void exitingUser(Activity activity, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            success(StockConstants.HANDLER_USER_EXSIST_OK);
                            Log.i(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        } else {
                            Log.i(TAG, "signInWithEmail:failed", task.getException());
                            success(StockConstants.HANDLER_USER_NOT_FOUND);
                        }
                    }
                });
    }

    public void createAccount(Activity activity, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            success(StockConstants.HANDLER_USER_NEW_CREATE);
                        } else {
                            error(StockConstants.HANDLER_USER_RESULT_ERR);
                        }
                    }
                });
    }

    private void registerListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    public boolean isCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser()!=null;
    }

    public void unregister() {
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
