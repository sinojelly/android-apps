package com.jelly.callassistant;

import android.util.Log;
import com.jelly.callassistant.R;

public class CallDetection {
	private static final String TAG = "OutGoingCallDetection";
	
	private CallDetection(){} 

	private static CallDetection instance = new CallDetection(); 

	public static CallDetection getInstance() { 
	       return instance; 
	}
	
	public void notifyCallConnected()
	{
		Log.d(TAG, "^^^^ CallDetection.notifyCallConnected.");
	}
	
	public void notifyCallEnded()
	{
		Log.d(TAG, "^^^^ CallDetection.notifyCallEnded.");
	}	
}
