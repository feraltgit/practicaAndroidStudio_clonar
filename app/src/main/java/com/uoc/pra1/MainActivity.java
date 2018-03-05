package com.uoc.pra1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uoc.datalevel.DataLowLevel;
import com.uoc.datalevel.DataObject;
import com.uoc.datalevel.DataQuery;

import java.util.ArrayList;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity  {




    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressBar mProgressView;
    private View mLoginFormView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UOC - BEGIN
        // Initialize and open Singleton Data Level
        DataLowLevel.Open(this);
        // UOC - END

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("PR1 :: Login");



        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = (ProgressBar)findViewById(R.id.login_progress);


    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);


            Log.d("UOC-DEBUG", String.format("Main thread: %d", Thread.currentThread().getId()));



            // ************************************************************************
            // UOC - BEGIN - CODE1
            //

            Log.d("UOC-DEBUG","Begin query");
            DataQuery query = DataQuery.get("user");
            ArrayList<DataObject> dataObjects = query.find("email", email, DataQuery.OPERATOR_EQUAL);
              Log.d("UOC-DEBUG", "Results");
            if (dataObjects.size() != 0) {
                DataObject object = dataObjects.get(0);
                String password_text =  mPasswordView.getText().toString();
                String password_bd = (String) object.get("password");
                if(password_text.equals(password_bd)){
                    Log.d("UOC-DEBUG", "End results");
                    Log.d("UOC-DEBUG", "Set breakpoint 1");

                    Intent intent;

                    intent = new Intent(MainActivity.this, ResultsActivity.class);

                    // Pass parameters to new activity
                    intent.putExtra("user_id",  object.m_objectId);
                    intent.putExtra("user_email", (String) object.get("email"));

                    startActivity(intent);
                    finish();
                }
                else{
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    showProgress(false);
                }
            }
            else{
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                showProgress(false);
            }
             Log.d("UOC-DEBUG", "End query");


            // UOC - END - CODE1
            // ************************************************************************



            // ************************************************
            // UOC - BEGIN - CODE2
/*
            Log.d("UOC-DEBUG", "Begin query");

            DataQuery query = DataQuery.get("user");
            query.findInBackground("email", email, DataQuery.OPERATOR_EQUAL, new FindCallback<DataObject>() {
                @Override
                public void done(ArrayList<DataObject> dataObjects, DataException e) {
                    Log.d("UOC-DEBUG", "Results");
                    if (e == null) {
                        if (dataObjects.size() != 0) {
                            DataObject object = dataObjects.get(0);
                            String password_text = mPasswordView.getText().toString();
                            String password_bd = (String) object.get("password");
                            if (password_text.equals(password_bd)) {
                                Log.d("UOC-DEBUG", "End results");
                                Log.d("UOC-DEBUG", "Set breakpoint 2");
                                Intent intent;

                                intent = new Intent(MainActivity.this, ResultsActivity.class);

                                // Pass parameters to new activity
                                intent.putExtra("user_id", object.m_objectId);
                                intent.putExtra("user_email", (String) object.get("email"));

                                startActivity(intent);
                                finish();
                            } else {
                                mPasswordView.setError(getString(R.string.error_incorrect_password));
                                mPasswordView.requestFocus();
                                showProgress(false);
                            }
                        } else {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            showProgress(false);
                        }
                    } else {
                        // Error

                    }
                }
            });
            Log.d("UOC-DEBUG", "End query");


            // UOC - END - CODE2
            // ************************************************************************
*/



                }
            }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    private void showProgress(final boolean show) {

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

    }



}

