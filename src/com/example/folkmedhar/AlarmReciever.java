package com.example.folkmedhar;

import com.example.folkmedhar.pantanir.bokun.Skref3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmReciever extends BroadcastReceiver{
	 @Override
     public void onReceive(Context context, Intent intent)
     {	
		 Log.e("hey", "hey");
		 Uri sound = Uri.parse("android.resource://" + "com.example.folkmedhar" + "/" + R.raw.sound);	
		 	NotificationCompat.Builder mBuilder =
		            new NotificationCompat.Builder(context)
		            .setSmallIcon(R.drawable.ic_greida5)
		            .setContentTitle("Fólk með hár")
		            .setSound(sound)
		            .setContentText("Þú átt tíma á morgun klukkan: "+Skref3.time_reminder);

		        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		        mNotificationManager.notify(1, mBuilder.build());
		        
      }
}