package com.coop.grady.coop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RosterListActivity extends AppCompatActivity {

   private ListView mListView;
   private ArrayList<User> mRosterList;
   private UserHolder holder;
   private User user = UserInfo.getInstance();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_roster_list);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      mListView = (ListView) findViewById(R.id.roster_list_view);
      mRosterList = new ArrayList<User>();

      RosterListTask getList = new RosterListTask();
      getList.execute();

//      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//      fab.setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View view) {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                  .setAction("Action", null).show();
//         }
//      });
   }

   public class RosterAdapter extends ArrayAdapter {

      private Context context;

      public RosterAdapter(Context context, ArrayList<User> rosterList) {
         super(context, R.layout.roster_item, rosterList);
         this.context = context;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
         View view = convertView;
         ViewHolder holder;
         if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.roster_item, null, false);
            holder = new ViewHolder();
            holder.userName = (TextView) view.findViewById(R.id.roster_item_name);
            holder.userNumKids = (TextView) view.findViewById(R.id.roster_item_numKids);
            holder.userPicture = (ImageView) view.findViewById(R.id.roster_item_picture);
            view.setTag(holder);
         }else{
            holder = (ViewHolder) view.getTag();
         }

         User user = mRosterList.get(position);
         holder.userName.setText(user.getFullName());
         holder.userNumKids.setText("Kids: " + String.valueOf(user.getNumKids()));
         holder.userPicture.setImageBitmap(user.getImage());
         return view;
      }

      public class ViewHolder {
         TextView userName, userNumKids;
         ImageView userPicture;
      }
   }

   private Bitmap decodeImage(String data) {
      byte[] bytes = Base64.decode(data, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
   }


   public class RosterListTask extends AsyncTask<Void, Void, Boolean> {

      private ProgressDialog dialog = new ProgressDialog(RosterListActivity.this);

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/getRosterList.php";

            String data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user.getFullName(), "UTF-8");

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
            }

            String[] responseFromServer;
            if(builder.length() > 10) {
               responseFromServer = builder.toString().split("<br>");
               int i = 0;
               for (String s : responseFromServer) {
                  String[] rawRequest = s.split(",");
                  mRosterList.add(new User(rawRequest[0], rawRequest[1], Integer.valueOf(rawRequest[2]), rawRequest[4],
                                 rawRequest[3]));
                  if(rawRequest.length > 5) {
                     mRosterList.get(i).setImage(decodeImage(rawRequest[5]));
                  }
                  i++;
               }
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
         super.onCancelled();
      }

      @Override
      protected void onPostExecute(Boolean aBoolean) {
         if(dialog.isShowing()) {
            dialog.dismiss();
         }

         final RosterAdapter adapter = new RosterAdapter(RosterListActivity.this, mRosterList);
         mListView.setAdapter(adapter);

         mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               holder = new UserHolder(mRosterList.get(position));
               Intent intent = new Intent(RosterListActivity.this, RosterActivity.class);
               startActivity(intent);
            }
         });

      }

      @Override
      protected void onPreExecute() {
         dialog.setMessage("Pleast Wait");
         dialog.show();
      }
   }

}
