package com.coop.grady.coop;

/**
 * Created by mUser on 6/21/2016.
 */
public class UserHolder {
   private static User mUser;

   public UserHolder(User user) {
      mUser = user;
   }

   public String getFirstName() {
      return mUser.getFirstName();
   }

   public String getLastName() {
      return mUser.getLastName();
   }

   public String getFullName() {
      return mUser.getFullName();
   }

   public String getEmail() {
      return mUser.getEmail();
   }

   public String getAddress() {
      return mUser.getAddress();
   }

   public int getNumKids() {
      return mUser.getNumKids();
   }

   public int getPoints() {
      return mUser.getPoints();
   }
   public String getPassword() {
      return mUser.getPassword();
   }

   public static void setAddress(String address) {
      mUser.setAddress(address);
   }

   public static void setEmail(String email) {
      mUser.setEmail(email);
   }

   public static void setFirstName(String firstName) {
      mUser.setFirstName(firstName);
   }

   public static void setLastName(String lastName) {
      mUser.setLastName(lastName);
   }

   public static void setNumKids(int numKids) {
      mUser.setNumKids(numKids);
   }

   public static void setPassword(String password) {
      mUser.setPassword(password);
   }
   public static void setPoints(int points) {
      mUser.setPoints(points);
   }

   public static User getInstance() {
      return mUser;
   }
}
