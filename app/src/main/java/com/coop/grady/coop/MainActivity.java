package com.coop.grady.coop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

   private Button requestButton, calendarButton, acceptButton, rosterButton, messageBoardButton;
   private TextView welcome;
   private User user = UserInfo.getInstance();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      requestButton = (Button) findViewById(R.id.goto_request_button);
      calendarButton = (Button) findViewById(R.id.set_date_button);
      acceptButton = (Button) findViewById(R.id.goto_accept_button);
      rosterButton = (Button) findViewById(R.id.goto_roster_button);
      messageBoardButton = (Button) findViewById(R.id.goto_my_requests_button);
      welcome = (TextView) findViewById(R.id.welcome);
      String welcomeMessage = "Welcome " + user.getFullName();
      welcome.setText(welcomeMessage);

      requestButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RequestActivity.class);
            startActivity(intent);
         }
      });

      calendarButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
         }
      });

      acceptButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, AcceptListActivity.class);
            intent.putExtra("requestAccepted", false);
            startActivity(intent);
         }
      });

      messageBoardButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MyRequestsActivity.class);
            intent.putExtra("requestCompleted", false);
            startActivity(intent);
         }
      });

      rosterButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RosterListActivity.class);
            startActivity(intent);
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

   @Override
   public void onBackPressed() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage("Are you sure you want to logout?")
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
         }
      })
            .setNegativeButton("No", null)
            .show();
   }
}
