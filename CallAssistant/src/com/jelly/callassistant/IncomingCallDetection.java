package com.jelly.callassistant;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.jelly.callassistant.R;

public class IncomingCallDetection extends BroadcastReceiver {
	private static final String TAG = "OutGoingCallDetection";
	
	// IDLE -> OFFHOOK : outgoing call        -- IdleState -> RingState           
	// RINGING -> OFFHOOK : incoming call     -- RingState -> CallingState
	// OFFHOOK -> IDLE : call ended           -- CallingState -> IdleState
	// RINGING -> IDLE : incoming call ended without accept  -- RingState -> IdleState
	
	private static State state = new IncomingCallDetection().new IdleState(false);
	
	public static void notifyOutgoingConnected(){
		IncomingCallDetection detection = new IncomingCallDetection();
		detection.setState(detection.new CallingState());
	}
	
	private void setState(State state) {
		Log.d(TAG, "$$$$ setState from " + IncomingCallDetection.state.toString() + "\n to " + state.toString());
		IncomingCallDetection.state = state;
	}
	
	private void notifyCallConnected(){
		Log.d(TAG, "@@@@ notifyCallConnected");
		CallDetection.getInstance().notifyCallConnected();
	}
	
	private void notifyCallEnded(){
		Log.d(TAG, "@@@@ notifyCallEnded");
		CallDetection.getInstance().notifyCallEnded();
	}
	
	interface State {
		public void recvEvent(String event);
	}
	
	class IdleState implements State {
		
		public IdleState(boolean notify){
			if (notify) {
				IncomingCallDetection.this.notifyCallEnded();
			}
		}
		
		public void recvEvent(String event)
		{
			Log.d(TAG, "$$$$ IdleState recvEvent " + event);
			if (event.equals("OFFHOOK")) // Outgoing call start
			{
				IncomingCallDetection.this.setState(new RingState());
			}
			else if (event.equals("RINGING")) // Incoming call start
			{
				IncomingCallDetection.this.setState(new RingState());
			}
		}
	}
	
	class RingState implements State {
		public void recvEvent(String event)
		{
			Log.d(TAG, "$$$$ RingState recvEvent " + event);
			if (event.equals("OFFHOOK")) // Incoming call connected
			{
				IncomingCallDetection.this.setState(new CallingState());
			}
			else if (event.equals("IDLE")) // Outgoing/Incoming call ended at RingState
			{
				IncomingCallDetection.this.setState(new IdleState(false));
			}
		}
	}
	
	class CallingState implements State {
		public CallingState(){
			IncomingCallDetection.this.notifyCallConnected();
		}
		public void recvEvent(String event)
		{
			Log.d(TAG, "$$$$ CallingState recvEvent " + event);
			if (event.equals("IDLE")) // Outgoing/Incoming call ended at CallingState
			{
				IncomingCallDetection.this.setState(new IdleState(true));
			}
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.d(TAG, "Incomming Call state : " + intent.getStringExtra("state"));
		
		state.recvEvent(intent.getStringExtra("state"));
	}

}
