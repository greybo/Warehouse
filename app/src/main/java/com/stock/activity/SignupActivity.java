package com.stock.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stock.R;
import com.stock.dao.UserDao;
import com.stock.utils.StockConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by greybo on 06.08.2017.
 */

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();

    @BindView(R.id.upUserName)
    EditText nameText;
    @BindView(R.id.upEmail)
    EditText emailText;
    @BindView(R.id.upPassword)
    EditText passwordText;
    @BindView(R.id.upPasswordConfirm)
    EditText passwordConfirmText;
    @BindView(R.id.up_sign_up_btn)
    Button signupButton;
    @BindView(R.id.link_login)
    TextView loginLink;

    private String email;
    private String name;
    private String password;
    private UserDao dao;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        dao = new UserDao(handler);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StockConstants.HANDLER_USER_NEW_CREATE:
                    onSignupSuccess();
                    break;
                case StockConstants.HANDLER_USER_RESULT_ERR:
                    onSignupFailed();
                    break;
            }
        }
    };

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signup_act_creating_account));
        progressDialog.show();

        name = nameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        dao.createAccount(this, email, password);

        // TODO: Implement your own signup logic here.
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.INTENT_EMAIL, email);
        intent.putExtra(LoginActivity.INTENT_PASSWORD, password);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), R.string.signup_act_login_failed, Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        //TODO
//        if (name.isEmpty() || name.length() < 3) {
//            nameText.setError("at least 3 characters");
//            valid = false;
//        } else {
//            nameText.setError(null);
//        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getString(R.string.signup_act_email_error));
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError(getString(R.string.signup_act_password_error));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
