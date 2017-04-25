package com.jagod.goodbyehomework.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jagod.goodbyehomework.client.Client;
import com.jagod.goodbyhomework.R;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SignInActivity extends AppCompatActivity {

    private TextView login;
    private TextView password;
    private TextView warn;

    private Button signInButton;
    private Button signUpButton;

    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Client.signInIfCan(SignInActivity.this);

                return null;
            }
        };

        task.execute();

        setContentView(R.layout.sign_in);

        login = (TextView) findViewById(R.id.login);
        password = (TextView) findViewById(R.id.password);
        warn = (TextView) findViewById(R.id.password_or_login_warning);
        progress = (ProgressBar) findViewById(R.id.progress);

        (signInButton = (Button) findViewById(R.id.sign_in_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        (signUpButton = (Button) findViewById(R.id.sign_up_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openSignUp();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    public void signIn() {
        warn.setVisibility(INVISIBLE);

        String sLogin = login.getText().toString();
        String sPassword = password.getText().toString();

        if (sLogin.equals("") || sPassword.equals("")) {
            warn.setVisibility(VISIBLE);
        }



        progress.setVisibility(VISIBLE);

        asyncSignIn(sLogin, sPassword);

    }

    private void asyncSignIn(final String login, final String password) {
        final Activity activity = this;


        final AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return (Client.signIn(login, password, activity));
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (!aBoolean) {
                    warn.setVisibility(VISIBLE);
                }
                progress.setVisibility(INVISIBLE);
            }
        };

        task.execute();

    }

    public void openSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);

        intent.putExtra("mail", login.getText().toString());
        startActivity(intent);
    }
}
