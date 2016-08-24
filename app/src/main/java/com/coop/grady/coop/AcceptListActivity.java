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

public class AcceptListActivity extends AppCompatActivity {

   private ListView mListView;
   private ArrayList<Request> requestList;
   private RequestHolder holder;
   private User user = UserInfo.getInstance();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_accept_list);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      mListView = (ListView) findViewById(R.id.accept_list_view);
      requestList = new ArrayList<Request>();

      AcceptListTask getList = new AcceptListTask(user.getFullName());
      getList.execute();



      boolean requestAccepted = getIntent().getExtras().getBoolean("requestAccepted");

      if(requestAccepted) {
         Toast.makeText(AcceptListActivity.this, "Request accepted successfully", Toast.LENGTH_LONG).show();
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



   public class AcceptListTask extends AsyncTask <Void, Void, Boolean> {

      private String user;
      private ProgressDialog dialog = new ProgressDialog(AcceptListActivity.this);

      public AcceptListTask(String User) {
         user = User;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/getRequestList.php";

            String data = URLEncoder.encode("requester", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");

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
                  requestList.add(new Request(rawRequest[0], rawRequest[1], Integer.valueOf(rawRequest[2]), Integer.valueOf(rawRequest[3])));
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
         final RequestListAdapter adapter = new RequestListAdapter(AcceptListActivity.this, requestList);
         mListView.setAdapter(adapter);

         mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               holder = new RequestHolder(requestList.get(position));
               Intent intent = new Intent(AcceptListActivity.this, AcceptActivity.class);
               intent.putExtra("fromMyRequests", false);
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
