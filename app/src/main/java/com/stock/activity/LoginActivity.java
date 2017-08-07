package com.stock.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stock.MainActivity;
import com.stock.R;
import com.stock.dao.UserDao;
import com.stock.utils.StockConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by greybo on 06.08.2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String INTENT_EMAIL = "email";
    public static final String INTENT_PASSWORD = "password";

    @BindView(R.id.inEmail)
    EditText emailText;
    @BindView(R.id.inPassword)
    EditText passwordText;
    @BindView(R.id.inPasswordLayout)
    TextInputLayout passwordLayout;
    @BindView(R.id.in_sing_in_btn)
    Button loginButton;
    @BindView(R.id.in_sign_up_btn)
    TextView signupLink;

    private ProgressDialog progressDialog;
    private UserDao dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dao = new UserDao(handler);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    passwordLayout.setPasswordVisibilityToggleEnabled(true);
                    passwordText.setError(null);
                }
            }
        });
    }

    public void login() {
        Log.i(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);
        showProgressDialog();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        dao.exitingUser(this, email, password);
        // TODO: Implement your own authentication logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.login_act_authenticating));
        progressDialog.show();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StockConstants.HANDLER_USER_EXSIST_OK:
                    progressDialog.dismiss();
                    onLoginSuccess();
                    break;
                case StockConstants.HANDLER_USER_NOT_FOUND:
                    progressDialog.dismiss();
                    onLoginFailed();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                emailText.setText(data.getStringExtra(INTENT_EMAIL));
                passwordText.setText(data.getStringExtra(INTENT_PASSWORD));
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), R.string.login_act_login_failed, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.requestFocus();
            emailText.setError(getString(R.string.login_act_email_error));
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordLayout.setPasswordVisibilityToggleEnabled(false);
            passwordText.setError(getString(R.string.login_act_password_error));
            valid = false;
        } else {
            passwordLayout.setPasswordVisibilityToggleEnabled(true);
            passwordText.setError(null);
        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (dao.isCurrentUser()) {
            onLoginSuccess();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dao.unregister();
    }
}
