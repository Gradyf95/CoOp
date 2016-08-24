package com.coop.grady.coop;

public class Request {

   private int mNumKids, mNumHours, mPointValue;
   private String mRequester, mAccepter, mDate, mTime;

   public Request(String date, String requester, int numKids, int numHours) {
      mDate = date; mRequester = requester; mNumKids = numKids; mNumHours = numHours; mPointValue = numKids * numHours * 2;
   }

   public Request(String date, String requester, int numKids, int numHours, String accepter) {
      mDate = date; mRequester = requester; mAccepter = accepter; mPointValue = numKids * numHours * 2;
      mNumKids = numKids; mNumHours = numHours;
   }

   public boolean setAccepter(String accepter) {
      if(mAccepter == null) {
         mAccepter = accepter;
         return true;
      }
      else {
         return false;
      }
   }

   public String getString() {
      return mDate + " " + mRequester + " " + mNumKids + " " + mNumHours;
   }

   public String getDate() {
      return mDate;
   }

   public String getRequester() {
      return mRequester;
   }

   public String getNumKids() {
      return String.valueOf(mNumKids);
   }

   public String getNumHours() {
      return String.valueOf(mNumHours);
   }

   public String getFullDate() {
      return mDate + " " + mTime;
   }

   public String getAccepter() {
      return mAccepter;
   }

   public int getPointValue() {
      return mPointValue;
   }



}