package com.jelly.callassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.jelly.callassistant.R;

public class OutgoingCallDetection extends BroadcastReceiver
{ 
	private static final String TAG = "OutGoingCallDetection";
	
	private class OutgoingConnectDetection implements LogcatObserver{
		private int times = 0;
		
		@Override
		public boolean handleNewLine(String line) {
			if (line.contains("stopRing()... (OFFHOOK state)")) {
				times++;
				Log.d(TAG, "Outgoing Call times: " + times);
				if (times == 6){
					Log.d(TAG, "Outgoing Call connected.");
					IncomingCallDetection.notifyOutgoingConnected();
					return true;
				}
			}
			return false;
		}
		
	}
	
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
        Log.d(TAG, "Outgoing Call start.");

        LogcatScanner.startScanLogcatInfo(new OutgoingConnectDetection());
	}
}
