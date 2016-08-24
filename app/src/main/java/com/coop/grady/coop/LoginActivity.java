package com.coop.grady.coop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

   private UserLoginTask mAuthTask = null;

   // UI references.
   private EditText mEmailView;
   private EditText mPasswordView;
   private TextView mCreateAccount;
   private Button mEmailSignInButton;
   private View mProgressView;
   private View mLoginFormView;
   private UserInfo user;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);
      // Set up the login form.
      mEmailView = (EditText) findViewById(R.id.email);

      mCreateAccount = (TextView) findViewById(R.id.create_account);
      mCreateAccount.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, AccountCreationActivity.class);
            startActivity(intent);
         }
      });

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

      mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
      mEmailSignInButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View view) {
            attemptLogin();
         }
      });

      mLoginFormView = findViewById(R.id.login_form);
      mProgressView = findViewById(R.id.login_progress);

   }


   /**
    * Attempts to sign in or register the account specified by the login form.
    * If there are form errors (invalid email, missing fields, etc.), the
    * errors are presented and no actual login attempt is made.
    */
   private boolean attemptLogin() {
      if (mAuthTask != null) {
         return false;
      }

      // Reset errors.
      mEmailView.setError(null);
      mPasswordView.setError(null);

      // Store values at the time of the login attempt.
      String email = mEmailView.getText().toString();
      String password = mPasswordView.getText().toString();

      boolean cancel = false;
      View focusView = null;

      // Check for a valid password, if the user entered one.
      if (TextUtils.isEmpty(password)) {
         mPasswordView.setError(getString(R.string.error_field_required));
         focusView = mPasswordView;
         cancel = true;
      }
         else if(!isPasswordValid(password)) {
         focusView = mPasswordView;
         cancel = true;
      }

      // Check for a valid email address.
      if (TextUtils.isEmpty(email)) {
         mEmailView.setError(getString(R.string.error_field_required));
         focusView = mEmailView;
         cancel = true;
      }
      else if (!isEmailValid(email)) {
         mEmailView.setError(getString(R.string.error_invalid_email));
         focusView = mEmailView;
         cancel = true;
      }

      if (cancel) {
         // There was an error; don't attempt login and focus the first
         // form field with an error.
         focusView.requestFocus();
         return false;
      } else {
         // Show a progress spinner, and kick off a background task to
         // perform the user login attempt.
         showProgress(true);
         mAuthTask = new UserLoginTask(email, password);
         mAuthTask.execute();
         return true;
      }
   }

   private boolean isEmailValid(String email) {
      return email.contains("@");
   }

   private boolean isPasswordValid(String password) {
      if(password.length() < 5) {
         mPasswordView.setError(getString(R.string.error_password_short));
         return false;
      }
      else {
         boolean upper = false;
         boolean lower = false;
         boolean num = false;
         for(char c : password.toCharArray()) {
            if(Character.isUpperCase(c)) {
               upper = true;
            }
            else if(Character.isLowerCase(c)) {
               lower = true;
            }
         }
         if(!upper) {
            mPasswordView.setError(getString(R.string.password_no_upper));
            return false;
         }
         else if(!lower) {
            mPasswordView.setError(getString(R.string.password_no_lower));
            return false;
         }
      }
      return true;
   }

   /**
    * Shows the progress UI and hides the login form.
    */
   @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
   private void showProgress(final boolean show) {
      // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
      // for very easy animations. If available, use these APIs to fade-in
      // the progress spinner.
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
         int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

         mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
         mLoginFormView.animate().setDuration(shortAnimTime).alpha(
               show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
               mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
         });

         mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
         mProgressView.animate().setDuration(shortAnimTime).alpha(
               show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
               mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
         });
      } else {
         // The ViewPropertyAnimator APIs are not available, so simply show
         // and hide the relevant UI components.
         mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
         mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      }
   }

   /**
    * Represents an asynchronous login/registration task used to authenticate
    * the user.
    */
   public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

      private final String mEmail;
      private final String mPassword;

      UserLoginTask(String email, String password) {
         mEmail = email;
         mPassword = password;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/login.php";

               String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(mEmail, "UTF-8");
               data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(mPassword, "UTF-8");

               URL url = new URL(link);

            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null) {
               builder.append(line);
               break;
            }

            String[] responseFromServer;
            if(builder.length() > 10) {
               responseFromServer = builder.toString().split(",");
               user = new UserInfo(responseFromServer[0], responseFromServer[1], Integer.valueOf(responseFromServer[2]), responseFromServer[3],
                                       Integer.valueOf(responseFromServer[4]));
               UserInfo.setEmail(mEmail);
               UserInfo.setPassword(mPassword);
               if(responseFromServer.length > 5) {
                  UserInfo.setImage(decodeImage(responseFromServer[5]));
               }
            }
            else {

               return false;
            }



         } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
            return false;
         }


         return true;
      }

      @Override
      protected void onPostExecute(final Boolean success) {
         mAuthTask = null;
         showProgress(false);
         if(success) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
         }
         else {
            Toast.makeText(LoginActivity.this, "Account not found", Toast.LENGTH_LONG).show();
         }
      }



      @Override
      protected void onCancelled() {
         mAuthTask = null;
         showProgress(false);
      }
   }

   private Bitmap decodeImage(String data) {
      byte[] bytes = Base64.decode(data, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
   }
}

