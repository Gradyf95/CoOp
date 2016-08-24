package com.coop.grady.coop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class AccountCreationActivity extends AppCompatActivity {

   private EditText mFirstName, mLastName, mNumKids, mEmail, mPassword, mCheckPassword, mAddress;
   private Button mCreateAccountButton;
   private View mFocusView;
   private CreateAccountTask mCreate;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_account_creation);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      mFirstName = (EditText) findViewById(R.id.create_first_name);
      mLastName = (EditText) findViewById(R.id.create_last_name);
      mNumKids = (EditText) findViewById(R.id.create_numKids);
      mAddress = (EditText) findViewById(R.id.create_address);
      mEmail = (EditText) findViewById(R.id.create_email);
      mPassword = (EditText) findViewById(R.id.create_password);
      mCheckPassword = (EditText) findViewById(R.id.check_password);
      mCreateAccountButton = (Button) findViewById(R.id.create_account_button);

      mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            View focusView = null;
            int numKids;
            String firstName = mFirstName.getText().toString();
            String lastName = mLastName.getText().toString();
            if(!mNumKids.getText().toString().isEmpty()) {
               numKids = Integer.parseInt(mNumKids.getText().toString());
            }
            else {
               numKids = 0;
            }
            String address = mAddress.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String checkPassword = mCheckPassword.getText().toString();
            if(validForm(password, checkPassword, email, address, numKids, lastName, firstName)) {
               mCreate = new CreateAccountTask(firstName, lastName, numKids, email, password, address);
               mCreate.execute();
            }
         }
      });

//      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//      fab.setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View view) {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                  .setAction("Action", null).show();
//         }
//      });
   }

   public boolean validForm(String password, String checkPassword, String email, String address, int numKids, String lastName, String firstName) {
      mFocusView = null;

      if(password.isEmpty()) {
         mPassword.setError("This field is required");
         mFocusView = mPassword;
      }
      else if(!password.equals(checkPassword)) {
         mCheckPassword.setError("Passwords do not match");
         mFocusView = mCheckPassword;
      }
      if(email.isEmpty()) {
         mEmail.setError("This field is required");
         mFocusView = mEmail;
      }
      else if(!email.contains("@")) {
         mEmail.setError("Must be a valid email");
         mFocusView = mEmail;
      }
      if(address.isEmpty()) {
         mAddress.setError("This field is required");
         mFocusView = mAddress;
      }
      if(numKids <= 0) {
         mNumKids.setError("Number of kids must be greater than 0");
         mFocusView= mNumKids;
      }
      if(lastName.isEmpty()) {
         mLastName.setError("This field is required");
         mFocusView = mLastName;
      }
      if(firstName.isEmpty()) {
         mFirstName.setError("This field is required");
         mFocusView = mFirstName;
      }

      if(mFocusView == null) {
         return true;
      }

      else{
         mFocusView.requestFocus();
         return false;
      }
   }

   public class CreateAccountTask extends AsyncTask<Void, Void, Boolean> {

      private final String mFirstName, mLastName, mEmail, mPassword, mAddress;
      private final int mNumKids;

      public CreateAccountTask(String firstName, String lastName, int numKids, String email, String password, String address) {
         mFirstName = firstName; mLastName = lastName; mNumKids = numKids; mEmail = email; mPassword = password; mAddress = address;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/createAccount.php";

            String data = URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(mFirstName, "UTF-8");
            data += "&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(mLastName, "UTF-8");
            data += "&" + URLEncoder.encode("numKids", "UTF-8") + "=" + mNumKids;
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(mEmail, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(mPassword, "UTF-8");
            data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(mAddress, "UTF-8");

            URL url = new URL(link);

            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String success = reader.readLine();

            if(success.equals("true")) {
               UserInfo user = new UserInfo(mFirstName, mLastName, mNumKids, mAddress, 0, mEmail, mPassword);
               return true;
            }
            else {
               return false;
            }

         } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
            return false;
         }
      }

      @Override
      protected void onCancelled() {

      }

      @Override
      protected void onPostExecute(Boolean success) {
         if(success) {
            Intent intent = new Intent(AccountCreationActivity.this, MainActivity.class);
            startActivity(intent);
         }
         else {
            Toast.makeText(AccountCreationActivity.this, "Account Creation Failed", Toast.LENGTH_SHORT).show();
         }
      }
   }



}
