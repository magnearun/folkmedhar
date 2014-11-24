/**
 * @author: Birkir Pálmason
 * @since: 20.11.2014
 * Klasinn sér um að búa til áminningu fyrir bókun notandans. Tími hennar er sóttur
 * úr gagnagrunni símans.
 */

package com.example.folkmedhar;

import com.example.folkmedhar.R;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver{
	
	 @Override
	 /**
	  * Býr til nýja áminningu
	  */
     public void onReceive(Context context, Intent intent)
     {	
		 // Sæki tímasetningu bókunar úr gagnagrunni símans
		 SharedPreferences prefs = context.getSharedPreferences("BokunTimi", 0);
		 String bokunTimi = prefs.getString("bokunTimi", "");
		 
		 Uri sound = Uri.parse("android.resource://" + "com.example.folkmedhar" + "/" + R.raw.sound);	
		 	NotificationCompat.Builder mBuilder =
		            new NotificationCompat.Builder(context)
		            .setSmallIcon(R.drawable.ic_greida5)
		            .setContentTitle("Fólk með hár")
		            .setSound(sound)
		            .setContentText("Þú átt tíma á morgun klukkan: "+bokunTimi);

		        NotificationManager mNotificationManager = 
		        		(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		        mNotificationManager.notify(1, mBuilder.build());
		        
      }
}