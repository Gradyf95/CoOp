package com.coop.grady.coop;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

public class RequestActivity extends AppCompatActivity {

   private SeekBar mSeekBar;
   private TextView mDate, mStartTime, mSeekBarProgress;
   private EditText mNumHours;
   private ImageButton mDateButton, mStartTimeButton;
   private Button mSubmitButton;
   final Calendar calendar = Calendar.getInstance();
   private int mYear = calendar.get(Calendar.YEAR);
   private int mMonth = calendar.get(Calendar.MONTH);
   private int mDay = calendar.get(Calendar.DAY_OF_MONTH);
   private int mHour = calendar.get(Calendar.HOUR);
   private int mMinute = calendar.get(Calendar.MINUTE);
   private int mCurrentYear = calendar.get(Calendar.YEAR);
   private int mCurrentMonth = calendar.get(Calendar.MONTH);
   private int mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
   private String mCurrentDate;
   private User user = UserInfo.getInstance();
   private CreateRequestTask mRequestTask;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_request);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      mSeekBar = (SeekBar) findViewById(R.id.seekBar);
      mSeekBarProgress = (TextView) findViewById(R.id.seek_bar_progress);

      mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         @Override
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mSeekBarProgress.setText(String.valueOf(progress));
         }

         @Override
         public void onStartTrackingTouch(SeekBar seekBar) {

         }

         @Override
         public void onStopTrackingTouch(SeekBar seekBar) {

         }
      });

      mDate = (EditText) findViewById(R.id.date_edit_text);
      mDateButton = (ImageButton) findViewById(R.id.set_date_button);

      mDateButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(RequestActivity.this, new StartDatePicker(), mYear, mMonth, mDay);
            dialog.show();
         }
      });

      mStartTime = (TextView) findViewById(R.id.start_edit_time);
      mNumHours = (EditText) findViewById(R.id.number_hours);
      mStartTimeButton = (ImageButton) findViewById(R.id.start_time_button);

      mStartTimeButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            TimePickerDialog dialog = new TimePickerDialog(RequestActivity.this, new StartTimePicker(), mHour, mMinute, true);
            dialog.show();
         }
      });

      mSubmitButton = (Button) findViewById(R.id.submit_button);
      mSubmitButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if(validateRequest()) {
               insertData(getDate(), user.getFullName(), String.valueOf(mSeekBar.getProgress()), mNumHours.getText().toString());
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


   public boolean validateRequest() {
      if(mSeekBar.getProgress() != 0 && validateDate() && validateNumHours()) {
         return true;
      }
      else {
         Toast toast = Toast.makeText(this, "Fields are not filled in correctly", Toast.LENGTH_LONG);
         toast.show();
         return false;
      }
   }

   public String getDate() {
      return (mMonth+1) + "/" + mDay + "/" + mYear;
   }

   public void insertData(String date, String requester, String numKids, String numHours) {
      mRequestTask = new CreateRequestTask(date, requester, Integer.valueOf(numKids), Integer.valueOf(numHours), mStartTime.getText().toString());
      mRequestTask.execute();
   }

   public boolean validateDate() {
      if(mCurrentYear <= mYear) {
         if(mCurrentMonth < mMonth) {
            return true;
         }
         else if(mCurrentMonth == mMonth && mCurrentDay < mDay) {
            return true;
         }
         else {
            return false;
         }
      }
      else {
         //error invalid date
         return false;
      }
   }

   public boolean validateNumHours() {
      if(Integer.valueOf(mNumHours.getText().toString()) > 0) {
         return true;
      }
      else {
         //error invalid num of hours
         return false;
      }


   }

   public class CreateRequestTask extends AsyncTask<Void, Void, Boolean> {

      private final String mDate, mRequester, mTime;
      private final int mNumKids, mNumHours;

      public CreateRequestTask(String date, String requester, int numKids, int numHours, String time) {
         mDate = date; mRequester = requester; mNumKids = numKids; mNumHours = numHours; mTime = time;
      }

      @Override
      protected Boolean doInBackground(Void... params) {
         try {
            String link = "http://webco-op.netai.net/createRequest.php";

            String data = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(mDate, "UTF-8");
            data += "&" + URLEncoder.encode("requester", "UTF-8") + "=" + URLEncoder.encode(mRequester, "UTF-8");
            data += "&" + URLEncoder.encode("numKids", "UTF-8") + "=" + mNumKids;
            data += "&" + URLEncoder.encode("numHours", "UTF-8") + "=" + mNumHours;
            data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(mTime, "UTF-8");

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
      protected void onPostExecute(Boolean success) {
         if(success) {
            Toast.makeText(RequestActivity.this, "success", Toast.LENGTH_SHORT).show();
         }
         else {
            Toast.makeText(RequestActivity.this, "failure", Toast.LENGTH_SHORT).show();
         }
      }

      @Override
      protected void onCancelled() {
         super.onCancelled();
      }
   }


   public class StartDatePicker implements DatePickerDialog.OnDateSetListener {
      @Override
      public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
         mYear = year;
         mMonth = monthOfYear;
         mDay = dayOfMonth;
         mDate.setText((mMonth+1) + "/" + mDay + "/" + mYear);
      }
   }

   public class StartTimePicker implements TimePickerDialog.OnTimeSetListener {
      @Override
      public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
         mHour = hourOfDay;
         mMinute = minute;
         String hour = String.valueOf(mHour);
         String min = String.format("%02d", mMinute);
         mStartTime.setText(hour + ":" + min);
      }
   }


}
