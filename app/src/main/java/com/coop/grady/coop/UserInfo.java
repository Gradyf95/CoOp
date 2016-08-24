package com.coop.grady.coop;

import android.graphics.Bitmap;

/**
 * Created by user on 6/30/2016.
 */
public class UserInfo {

   private static User user;

   public UserInfo(String firstName, String lastName, int numKids, String address, int points) {
      user = new User(firstName, lastName, numKids, address, points);
   }

   public UserInfo(String firstName, String lastName, int numKids, String address, int points, String email, String password) {
      user = new User(firstName, lastName, numKids, address, points);
      user.setEmail(email);
      user.setPassword(password);
   }

   public String getFirstName() {
      return user.getFirstName();
   }

   public String getLastName() {
      return user.getLastName();
   }

   public String getFullName() {
      return user.getFullName();
   }

   public String getEmail() {
      return user.getEmail();
   }

   public String getAddress() {
      return user.getAddress();
   }

   public int getNumKids() {
      return user.getNumKids();
   }

   public int getPoints() {
      return user.getPoints();
   }
   public String getPassword() {
      return user.getPassword();
   }

   public static void setAddress(String address) {
      user.setAddress(address);
   }

   public static void setEmail(String email) {
      user.setEmail(email);
   }

   public static void setFirstName(String firstName) {
      user.setFirstName(firstName);
   }

   public static void setLastName(String lastName) {
      user.setLastName(lastName);
   }

   public static void setNumKids(int numKids) {
      user.setNumKids(numKids);
   }

   public static void setPassword(String password) {
      user.setPassword(password);
   }
   public static void setPoints(int points) {
      user.setPoints(points);
   }

   public static User getInstance() {
      return user;
   }

   public static void setImage(Bitmap image) {
      user.setImage(image);
   }

   public static Bitmap getImage() {
      return user.getImage();
   }
}
