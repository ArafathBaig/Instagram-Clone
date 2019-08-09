/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

  RelativeLayout layout;
  ImageView logo;
  EditText username;
  EditText password;
  TextView login;
  Boolean signUpMode = true;

  public void showUserList(){
    Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
      signUpClicked(view);
    }

    return false;
  }

  @Override
  public void onClick(View view) {
    if(view.getId() == R.id.loginTextView){
      Button button = (Button)  findViewById(R.id.signButton);

      if(signUpMode){
        signUpMode = false;
        button.setText("Login");
        login.setText("or Signup");
      }else{
        signUpMode = true;
        button.setText("Sign Up");
        login.setText("or Login");

      }
    }else if(view.getId() == R.id.loginTextView || view.getId() == R.id.bgLayout){
      InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0 );

    }
  }

  public void signUpClicked(View view){


    if(username.getText().toString().matches("") || password.getText().toString().matches("")){
      Toast.makeText(this, "A username and a password is required", Toast.LENGTH_SHORT).show();
    }else{
      if(signUpMode) {
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Sign Up ", "Sucesss");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }

        });

      }else{
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser parseUser, ParseException e) {
            if (parseUser != null) {
              Log.i("Log In", "USER LOGGED IN!");
              showUserList();
            } else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instagram");

     login = (TextView) findViewById(R.id.loginTextView);
    login.setOnClickListener(this);

    username = (EditText) findViewById(R.id.userEditText);
    password = (EditText) findViewById(R.id.passwordEditText);
    logo = (ImageView) findViewById(R.id.logoImageView);
    layout = (RelativeLayout) findViewById(R.id.bgLayout);
    logo.setOnClickListener(this);
    layout.setOnClickListener(this);
    password.setOnKeyListener(this);

    if(ParseUser.getCurrentUser() != null){
      showUserList();
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}