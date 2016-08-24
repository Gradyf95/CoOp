package com.coop.grady.coop;

/**
 * Created by user on 6/29/2016.
 */
public class RequestHolder {

   private static Request mRequest;

   public RequestHolder(Request request) {
      mRequest = request;
   }

   public boolean setAccepter(String accepter) {
      return mRequest.setAccepter(accepter);
   }

   public static Request getInstance() {
      return mRequest;
   }

}
