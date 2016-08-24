package com.coop.grady.coop;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by user on 6/8/2016.
 */
public class User {

   private String mFirstName, mLastName, mEmail, mPassword, mAddress;
   private int mPoints, mNumKids;
   private ArrayList<Request> mAccpetedRequests;
   private Bitmap mImage;

   public User(String firstName, String lastName, int numKids, String address, int points) {
      mFirstName = firstName; mLastName = lastName; mNumKids = numKids; mAddress = address; mPoints = points;
      mAccpetedRequests = new ArrayList<Request>();
   }

   public User(String firstName, String lastName, int numKids, String email, String address) {
      mFirstName = firstName; mLastName = lastName; mNumKids = numKids; mEmail = email; mAddress = address;
      mAccpetedRequests = new ArrayList<Request>();
   }

   public String getFirstName() {
      return mFirstName;
   }

   public String getLastName() {
      return mLastName;
   }

   public String getFullName() {
      return mFirstName + " " + mLastName;
   }

   public String getEmail() {
      return mEmail;
   }

   public String getAddress() {
      return mAddress;
   }

   public int getNumKids() {
      return mNumKids;
   }

   public int getPoints() {
      return mPoints;
   }
   public String getPassword() {
      return mPassword;
   }

   public void setAddress(String address) {
      mAddress = address;
   }

   public void setEmail(String email) {
      mEmail = email;
   }

   public void setFirstName(String firstName) {
      mFirstName = firstName;
   }

   public void setLastName(String lastName) {
      mLastName = lastName;
   }

   public void setNumKids(int numKids) {
      mNumKids = numKids;
   }

   public void setPassword(String password) {
      mPassword = password;
   }
   public void setPoints(int points) {
      mPoints = points;
   }

   public void addRequest(Request request) {
      mAccpetedRequests.add(request);
   }

   public void setImage(Bitmap image) {
      mImage = image;
   }

   public Bitmap getImage() {
      return mImage;
   }

   public boolean hasImage() {
      return mImage != null;
   }
}
