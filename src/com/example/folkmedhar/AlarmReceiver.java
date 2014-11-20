package com.example.folkmedhar;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.folkmedhar.pantanir.bokun.Skref3;

public class AlarmReceiver extends BroadcastReceiver{
	
	
	 @Override
     public void onReceive(Context context, Intent intent)
     {	
		 SharedPreferences prefs = context.getSharedPreferences("BokunTimi", 0);
		 String bokunTimi = prefs.getString("bokunTimi", "");
		 Uri sound = Uri.parse("android.resource://" + "com.example.folkmedhar" + "/" + R.raw.sound);	
		 	NotificationCompat.Builder mBuilder =
		            new NotificationCompat.Builder(context)
		            .setSmallIcon(R.drawable.ic_greida5)
		            .setContentTitle("Fólk með hár")
		            .setSound(sound)
		            .setContentText("Þú átt tíma á morgun klukkan: "+bokunTimi);

		        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		        mNotificationManager.notify(1, mBuilder.build());
		        
      }
}