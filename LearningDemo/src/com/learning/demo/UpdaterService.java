package com.learning.demo;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
//import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService" ;
	protected static final long DELAY = 5; // in seconds
	boolean running = false ;
	Twitter twitter ;
	YambaApp app ;
	
	@Override
	public void onCreate() {
		app = (YambaApp)getApplication() ;
		twitter = app.twitter ;
		Log.d(TAG , "onCreate" );
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/*// This part was written as Async Task but Services never stop. Hence , using a normal Thread 
		class GetPublicTimeline extends AsyncTask<Twitter, Void, String>{

			@Override
			protected String doInBackground(Twitter... params) {
				twitter = params[0] ;
				try {
					List<Twitter.Status> timeline = twitter.getPublicTimeline() ;
					for(Twitter.Status status : timeline)
					{
						Log.d(TAG , String.format("%s : %s", status.user.name , status.text));
					}
					return getString(R.string.toastServiceStarted) ;
				} catch (TwitterException e) {
					e.printStackTrace();
					return getString(R.string.toastServiceNotStarted) ;
				}
			}

			@Override
			protected void onPostExecute(String result) {
				Toast.makeText( getApplicationContext() , result, Toast.LENGTH_LONG).show() ;
				super.onPostExecute(result);
			}

			
		}
		new GetPublicTimeline().execute(twitter);
		*/
		running = true ;
		new Thread() {
			public void run() {
				while (running) {
					try {

						List<Twitter.Status> timeline = twitter
								.getPublicTimeline();
						for (Twitter.Status status : timeline) {
							Log.d(TAG, String.format("%s : %s",
									status.user.name, status.text));
						}
						Thread.sleep(DELAY * 1000);
					} catch (TwitterException e) {
						Log.d(TAG, "Service not started due to network error") ;
						e.printStackTrace();
					} catch (InterruptedException e) {
						Log.d(TAG, "Updater Service Interrupted");
						e.printStackTrace();
					}
				}

			}

		}.start();
		Log.d(TAG , "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		running = false ;
		Toast.makeText(getApplicationContext(), getString(R.string.toastServiceStopped), Toast.LENGTH_LONG).show();
		Log.d(TAG , "onDestroy");
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG , "onBind");
		return null;
	}
	
}
