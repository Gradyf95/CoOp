package com.coop.grady.coop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.regex.Pattern;

public class AcceptActivity extends AppCompatActivity {

   private TextView mRequster, mDate, mNumKids, mNumHours;
   private Button acceptButton, completeButton;
   private Request request = RequestHolder.getInstance();
   private User user = UserInfo.getInstance();
   private Calendar c = Calendar.getInstance();
   private int mCurrentDay = c.get(Calendar.DAY_OF_MONTH);
   private int mCurrentMonth = c.get(Calendar.MONTH) + 1;
   private int mCurrentYear = c.get(Calendar.YEAR);
   private boolean fromMyRequests;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_accept);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      mRequster = (TextView) findViewById(R.id.accept_requester);
      mDate = (TextView) findViewById(R.id.accept_date);
      mNumKids = (TextView) findViewById(R.id.accept_numKids);
      mNumHours = (TextView) findViewById(R.id.accept_numHours);

      mRequster.setText(request.getRequester());
      mDate.setText(request.getDate());
      mNumKids.setText(request.getNumKids());
      mNumHours.setText(request.getNumHours());

      acceptButton = (Button) findViewById(R.id.accept_button);
      acceptButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            user = UserInfo.getInstance();
            AcceptRequestTask accept = new AcceptRequestTask(request.getRequester(), user.getFullName(), request.getDate());
            accept.execute();
         }
      });

      completeButton = (Button) findViewById(R.id.complete_button);
      completeButton.setVisibility(View.INVISIBLE);
      completeButton.setClickable(true);
      completeButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            completeButton.setClickable(false);
            CompleteRequestTask complete = new CompleteRequestTask(request.getRequester(), request.getAccepter(), request.getDate(), request.getPointValue());
            complete.execute();
         }
      });

      fromMyRequests = getIntent().getExtras().getBoolean("fromMyRequests");

      if (isPast(request.getDate()) && fromMyRequests) {
         completeButton.setVisibility(View.VISIBLE);
      }

//      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//      fab.setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View view) {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                  .setAction("Action", null).show();
//         }
//      });
   }

   public boolean isPast(String date) {
      String[] dateArray = date.split(Pattern.quote("/"));
      int month = Integer.valueOf(dateArray[0]);
      int day = Integer.valueOf(dateArray[1]);
      int year = Integer.valueOf(dateArray[2]);

      if (year <= mCurrentYear) {
         if (month < mCurrentMonth) {
            return true;
         } else if (month == mCurrentMonth && day < mCurrentDay) {
            return true;
         }
         return false;
      }
      return false;
   }

   @Override
   public void onBackPressed() {
      if (fromMyRequests) {
         finish();
         Intent intent = new Intent(AcceptActivity.this, MyRequestsActivity.class);
         intent.putExtra("requestCompleted", false);
         startActivity(intent);
      } else {
         finish();
         Intent intent = new Intent(AcceptActivity.this, AcceptListActivity.class);
         intent.putExtra("requestAccepted", false);
         startActivity(intent);
      }
   }

   public class AcceptRequestTask extends AsyncTask<Void, Void, Boolean> {

      private String requester, accepter, date;

      public AcceptRequestTask(String Requester, String Accepter, String Date) {
         requester = Requester;
         accepter = Accepter;
         date = Date;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/acceptRequest.php";

            String data = URLEncoder.encode("requester", "UTF-8") + "=" + URLEncoder.encode(requester, "UTF-8");
            data += "&" + URLEncoder.encode("accepter", "UTF-8") + "=" + URLEncoder.encode(accepter, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");


            URL url = new URL(link);

            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String success = reader.readLine();

            if (success.equals("true")) {
               return true;
            } else {
               return false;
            }

         } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
            return false;
         }
      }

      @Override
      protected void onCancelled() {
         super.onCancelled();
      }

      @Override
      protected void onPostExecute(Boolean success) {
         if (success) {
            Intent intent = new Intent(AcceptActivity.this, AcceptListActivity.class);
            intent.putExtra("requestAccepted", true);
            startActivity(intent);
            finish();
         } else {
            Toast.makeText(AcceptActivity.this, "Request could not be accepted", Toast.LENGTH_LONG).show();
         }
      }
   }

   public class CompleteRequestTask extends AsyncTask<Void, Void, Boolean> {

      private String requester, accepter, date;
      private int points;

      public CompleteRequestTask(String Requester, String Accepter, String Date, int Points) {
         requester = Requester;
         accepter = Accepter;
         date = Date;
         points = Points;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/completeRequest.php";

            String data = URLEncoder.encode("requester", "UTF-8") + "=" + URLEncoder.encode(requester, "UTF-8");
            data += "&" + URLEncoder.encode("accepter", "UTF-8") + "=" + URLEncoder.encode(accepter, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
            data += "&" + URLEncoder.encode("points", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(points), "UTF-8");


            URL url = new URL(link);

            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String success = reader.readLine();


            if (success.equals("true")) {
               return true;
            } else {
               return false;
            }

         } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
            return false;
         }
      }

      @Override
      protected void onCancelled() {
         super.onCancelled();
      }

      @Override
      protected void onPostExecute(Boolean success) {
         if (success) {
            Intent intent = new Intent(AcceptActivity.this, MyRequestsActivity.class);
            intent.putExtra("requestCompleted", true);
            startActivity(intent);
            finish();
         } else {
            completeButton.setClickable(true);
            Toast.makeText(AcceptActivity.this, "Request could not be completed", Toast.LENGTH_LONG).show();
         }
      }
   }
}
