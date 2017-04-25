package com.jagod.goodbyehomework.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jagod.goodbyehomework.client.Client;
import com.jagod.goodbyhomework.R;

import java.util.regex.Pattern;

import static android.view.View.INVISIBLE;

/**
 * Создано Юрием в 15.04.17.
 * <p>
 * =====================================================
 * =            Магия! Руками не трогать!!!           =
 * =====================================================
 */

public class SignUpActivity extends AppCompatActivity {

    private final Pattern namePattern = Pattern.compile("[a-zA-Zа-яА-Я]+");
    private final Pattern mailPattern = Pattern.compile(".+@[a-zA-Z]+[.][a-zA-Z]+");
    private final Pattern passwordPattern = Pattern.compile("[A-Za-z0-9]{8,}+");
    private TextView name;
    private TextView lastName;
    private TextView mail;
    private TextView firstPassword;
    private TextView secondPassword;
    private TextView nameWarnings;
    private TextView mailWarnings;
    private TextView passwordWarnings;
    private Button signUpButton;
    private ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        mail = (TextView) findViewById(R.id.mail);
        firstPassword = (TextView) findViewById(R.id.password_first);
        secondPassword = (TextView) findViewById(R.id.password_second);
        signUpButton = (Button) findViewById(R.id.sign_up_button);

        nameWarnings = (TextView) findViewById(R.id.name_warnings);
        mailWarnings = (TextView) findViewById(R.id.mail_warnings);
        passwordWarnings = (TextView) findViewById(R.id.password_warnings);

        progress = (ProgressBar) findViewById(R.id.progress);

        if (getIntent() != null) {
            mail.setText(getIntent().getStringExtra("mail"));
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        if (!checkData()) {
            return;
        }

        final String sFirstName = name.getText().toString();
        final String sLastName = lastName.getText().toString();
        final String sMail = mail.getText().toString();
        final String sPassword = firstPassword.getText().toString();
        final Activity activity = this;

        progress.setVisibility(View.VISIBLE);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!Client.signUp(sFirstName, sLastName, sMail, sPassword, activity)) {
                    Toast.makeText(activity, "Ошибка при регистрации. Обратитесь к администратуру!", Toast.LENGTH_SHORT).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progress.setVisibility(INVISIBLE);
            }
        };

        task.execute();
    }

    private boolean checkData() {
        boolean result = true;
        // Name and last name
        {


            mailWarnings.setVisibility(INVISIBLE);
            nameWarnings.setVisibility(INVISIBLE);
            passwordWarnings.setVisibility(INVISIBLE);

            String sFirstName = name.getText().toString();
            String sLastName = lastName.getText().toString();

            if (!namePattern.matcher(sFirstName).matches()) {
                nameWarnings.setText(this.getString(R.string.first_name_mismatch_warning));
                nameWarnings.setVisibility(View.VISIBLE);
                result = false;
            }

            if (!namePattern.matcher(sLastName).matches()) {
                nameWarnings.setText(this.getString(R.string.last_name_mismatch_warning));
                nameWarnings.setVisibility(View.VISIBLE);
                result = false;
            }

            if ((!namePattern.matcher(sFirstName).matches()) && (!namePattern.matcher(sLastName).matches())) {
                nameWarnings.setText(this.getString(R.string.first_last_name_mismatch_warning));
                nameWarnings.setVisibility(View.VISIBLE);
                result = false;
            }

            if (sFirstName.equals("")) {
                nameWarnings.setText(this.getString(R.string.first_name_empty_warning));
                nameWarnings.setVisibility(View.VISIBLE);
                result = false;
            }

            if (sLastName.equals("")) {
                nameWarnings.setText(this.getString(R.string.last_name_empty_warning));
                nameWarnings.setVisibility(View.VISIBLE);
                result = false;
            }

            if ((sFirstName.equals("")) && (sLastName.equals(""))) {
                nameWarnings.setText(this.getString(R.string.first_last_name_empty_warning));
                nameWarnings.setVisibility(View.VISIBLE);
                result = false;
            }
        }

        //Mail
        {
            String sMail = mail.getText().toString();

            if (!mailPattern.matcher(sMail).matches()) {
                mailWarnings.setText(this.getString(R.string.mail_mismatch_warning));
                mailWarnings.setVisibility(View.VISIBLE);
                result = false;
            }

            if (sMail.equals("")) {
                mailWarnings.setText(this.getString(R.string.mail_empty_warning));
                mailWarnings.setVisibility(View.VISIBLE);
                result = false;
            }
        }

        //Password
        {
            String sFirstPassword = firstPassword.getText().toString();
            String sSecondPassword = secondPassword.getText().toString();

            if (!sFirstPassword.equals(sSecondPassword)) {
                passwordWarnings.setText(this.getString(R.string.passwords_dont_match_warning));
                passwordWarnings.setVisibility(View.VISIBLE);
                result = false;
            }

            if (!passwordPattern.matcher(sFirstPassword).matches()) {
                passwordWarnings.setText(this.getString(R.string.password_mismatch_warning));
                passwordWarnings.setVisibility(View.VISIBLE);
                result = false;
            }

            if (sFirstPassword.equals("")) {
                passwordWarnings.setText(this.getString(R.string.password_empty_warning));
                passwordWarnings.setVisibility(View.VISIBLE);
                result = false;
            }
        }

        return result;
    }


}
