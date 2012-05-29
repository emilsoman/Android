package com.learning.demo;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService {
	Twitter twitter ; 
	YambaApp app ;
	static final String TAG = "RefreshService" ;
	
	public RefreshService() {
		super(TAG);
	}
	
	@Override
	public void onCreate() {
		app = (YambaApp)getApplication() ;
		twitter = app.twitter ;
		Log.d(TAG , "onCreate" );
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG , "onDestroy");
		super.onDestroy();
	}



	@Override
	protected void onHandleIntent(Intent intent) {
		try {

			List<Twitter.Status> timeline = twitter.getPublicTimeline();
			for (Twitter.Status status : timeline) {
				Log.d(TAG, String.format("%s : %s", status.user.name, status.text));
			}
		} catch (TwitterException e) {
			Log.d(TAG, "Service not started due to network error") ;
			e.printStackTrace();
		}
		
		
	}

}
