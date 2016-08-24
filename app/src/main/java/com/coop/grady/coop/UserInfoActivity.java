package com.coop.grady.coop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class UserInfoActivity extends AppCompatActivity {

   private EditText mNumKids, mAddress;
   private TextView mPoints, mName;
   private Button mUpdateButton, mUploadButon, mChooseButton;
   private ImageView imageView;
   private User user = UserInfo.getInstance();
   private boolean imageSelected = false;

   private int PICK_IMAGE_REQUEST = 1;
   Uri filePath;
   Bitmap bitmap;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_user_info);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      mNumKids = (EditText) findViewById(R.id.user_kids_fill_space);
      mAddress = (EditText) findViewById(R.id.user_address_fill_space);
      mPoints = (TextView) findViewById(R.id.user_points);
      mName = (TextView) findViewById(R.id.user_info_name);
      mUpdateButton = (Button) findViewById(R.id.update_user_info_button);
      mUploadButon = (Button) findViewById(R.id.upload_image_button);
      mChooseButton = (Button) findViewById(R.id.choose_image_button);
      imageView = (ImageView) findViewById(R.id.user_picture);


      mNumKids.setText(String.valueOf(user.getNumKids()));
      mAddress.setText(user.getAddress());
      mPoints.setText(String.valueOf(user.getPoints()));
      mName.setText(user.getFullName());
      if(user.hasImage()) {
         imageView.setImageBitmap(user.getImage());
      }


      mUpdateButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            UpdateUserInfoTask update = new UpdateUserInfoTask(user.getFullName(), mAddress.getText().toString(), Integer.valueOf(mNumKids.getText().toString()));
            update.execute();
         }
      });

      mUploadButon.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(imageSelected) {
               UploadImageTask upload = new UploadImageTask(bitmap, user.getFullName());
               upload.execute();
            }
            else {
               Toast.makeText(UserInfoActivity.this, "No image was selected", Toast.LENGTH_SHORT).show();
            }
         }
      });

      mChooseButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

         filePath = data.getData();
         try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            imageView.setImageBitmap(bitmap);
            imageSelected = true;
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public String getStringImage(Bitmap bmp){
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
      byte[] imageBytes = baos.toByteArray();
      return Base64.encodeToString(imageBytes, Base64.DEFAULT);
   }

   public class UpdateUserInfoTask extends AsyncTask<Void, Void, Boolean> {

      private String user, address;
      private int numKids;

      public UpdateUserInfoTask(String User, String Address, int NumKids) {
         user = User; address = Address; numKids = NumKids;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/updateUserInfo.php";

            String data = URLEncoder.encode("fullName", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
            data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");
            data += "&" + URLEncoder.encode("numKids", "UTF-8") + "=" + numKids;


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
            UserInfo.setAddress(address);
            UserInfo.setNumKids(numKids);
            Toast.makeText(UserInfoActivity.this, "Information updated successfully", Toast.LENGTH_LONG).show();
         } else {
            Toast.makeText(UserInfoActivity.this, "Request could not be completed", Toast.LENGTH_LONG).show();
         }
      }
   }

   public class UploadImageTask extends AsyncTask<Void, Void, Boolean> {

      private Bitmap bitmap;
      private String fullName, uploadImage;

      public UploadImageTask(Bitmap bmp, String fullname) {
         bitmap = bmp; fullName = fullname;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         uploadImage = getStringImage(bitmap);
         try {
            String link = "http://webco-op.netai.net/uploadImage.php";

            String data = URLEncoder.encode("fullName", "UTF-8") + "=" + URLEncoder.encode(fullName, "UTF-8");
            data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(uploadImage, "UTF-8");

            URL url = new URL(link);

            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String success = reader.readLine();

            if (success.equals("true")) {
               user.setImage(bitmap);
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
      protected void onPostExecute(Boolean success) {
         if(success) {
            Toast.makeText(UserInfoActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
         }
         else {
            Toast.makeText(UserInfoActivity.this, "Image could not be uploaded", Toast.LENGTH_LONG).show();
         }
      }

      @Override
      protected void onCancelled() {
         super.onCancelled();
      }
   }

}
