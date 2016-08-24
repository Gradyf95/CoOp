package com.coop.grady.coop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RosterActivity extends AppCompatActivity {

   private User user = UserHolder.getInstance();
   private TextView mRosterName, mRosterNumKids, mRosterAddress;
   private Button mMessageButton, mGetDirectionsButton;
   private ImageView image;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_roster);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      mRosterName = (TextView) findViewById(R.id.roster_name);
      mRosterNumKids = (TextView) findViewById(R.id.roster_numKids);
      mRosterAddress = (TextView) findViewById(R.id.roster_address);
      mGetDirectionsButton = (Button) findViewById(R.id.get_directions_button);
      image = (ImageView) findViewById(R.id.roster_picture);

      mRosterName.setText(user.getFullName());
      mRosterNumKids.setText(String.valueOf(user.getNumKids()));
      mRosterAddress.setText(user.getAddress());


      if(user.hasImage()) {
         image.setImageBitmap(user.getImage());
      }

      mGetDirectionsButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Uri gmapsIntentUri = Uri.parse("geo:0,0?q=" + user.getAddress());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmapsIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
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

}
