package com.coop.grady.coop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 6/30/2016.
 */
public class RequestListAdapter extends ArrayAdapter{
   private ArrayList<Request> requestList;
   private Context context;

   public RequestListAdapter(Context context, ArrayList<Request> requestList) {
      super(context, R.layout.request_item, requestList);
      this.requestList = requestList;
      this.context = context;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      View view = convertView;
      ViewHolder holder = null;
      if (view == null) {
         view = LayoutInflater.from(context).inflate(R.layout.request_item, null, false);
         holder = new ViewHolder();
         holder.dateFillSpace = (TextView) view.findViewById(R.id.date_fill_space);
         holder.requesterFillSpace = (TextView) view.findViewById(R.id.requester_fill_space);
         holder.kidsFillSpace = (TextView) view.findViewById(R.id.kids_fill_space);
         holder.hourFillSpace = (TextView) view.findViewById(R.id.hours_fill_space);
         view.setTag(holder);
      }else{
         holder = (ViewHolder) view.getTag();
      }

      Request request = requestList.get(position);
      holder.dateFillSpace.setText(request.getDate());
      holder.requesterFillSpace.setText(request.getRequester());
      holder.kidsFillSpace.setText(request.getNumKids());
      holder.hourFillSpace.setText(request.getNumHours());
      return view;
   }

   public class ViewHolder {
      TextView dateFillSpace, requesterFillSpace, kidsFillSpace, hourFillSpace;
   }
}
