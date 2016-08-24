package com.coop.grady.coop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MyRequestsActivity extends AppCompatActivity {

   private ListView listView;
   private ArrayList<Request> myRequests;
   private RequestHolder holder;
   private User user = UserInfo.getInstance();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_my_requests);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      listView = (ListView) findViewById(R.id.my_requests);
      myRequests = new ArrayList<Request>();

      MyRequestListTask getList = new MyRequestListTask(user.getFullName());
      getList.execute();



      boolean requestCompleted = getIntent().getExtras().getBoolean("requestCompleted");

      if(requestCompleted) {
         Toast.makeText(MyRequestsActivity.this, "Request completed successfully", Toast.LENGTH_LONG).show();
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

   public class MyRequestListTask extends AsyncTask<Void, Void, Boolean> {

      private String user;
      private ProgressDialog dialog = new ProgressDialog(MyRequestsActivity.this);

      public MyRequestListTask(String User) {
         user = User;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/getMyRequestList.php";

            String data = URLEncoder.encode("accepter", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");

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
               responseFromServer = builder.toString().split("<br>");
               for (String s : responseFromServer) {
                  String[] rawRequest = s.split(",");
                  myRequests.add(new Request(rawRequest[0], rawRequest[1], Integer.valueOf(rawRequest[2]), Integer.valueOf(rawRequest[3]), rawRequest[4]));
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
      protected void onPostExecute(Boolean success) {
         if(dialog.isShowing()) {
            dialog.dismiss();
         }
         final RequestListAdapter adapter = new RequestListAdapter(MyRequestsActivity.this, myRequests);
         listView.setAdapter(adapter);

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               holder = new RequestHolder(myRequests.get(position));
               Intent intent = new Intent(MyRequestsActivity.this, AcceptActivity.class);
               intent.putExtra("fromMyRequests", true);
               startActivity(intent);
               finish();
            }
         });
      }

      @Override
      protected void onCancelled() {
         super.onCancelled();
      }

      @Override
      protected void onPreExecute() {
         dialog.setMessage("Pleast Wait");
         dialog.show();
      }
   }

}
