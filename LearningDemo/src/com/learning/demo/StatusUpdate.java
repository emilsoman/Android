package com.learning.demo;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusUpdate extends Activity {
	static final String TAG = "StatusUpdate" ;
	EditText editText;
	Button buttonUpdate;
	YambaApp app ;

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
		
        super.onCreate(savedInstanceState);
        app = (YambaApp) getApplication() ;
        setContentView(R.layout.main);
        
        editText = (EditText) findViewById(R.id.editText) ;
        buttonUpdate = (Button) findViewById(R.id.updateStatus) ;
        Debug.startMethodTracing("Yamba");
        class PostToTwitter extends AsyncTask<String, Void , String> {
        	
        	@Override
        	protected String doInBackground(String... params) {
        		String updateString = params[0] ;
        		try {
        			Twitter twitter = app.twitter ;
        			twitter.updateStatus(updateString);
        			return StatusUpdate.this.getString(R.string.toastStatusUpdated) ; 
        		} catch (TwitterException e) {
        			Log.e(TAG , "Died" , e ) ;
        			e.printStackTrace();
        			return StatusUpdate.this.getString(R.string.toastStatusNotUpdated) ;
        		}
        		
        	}
        	
        	@Override
        	protected void onPostExecute(String result) {
        		Toast.makeText(StatusUpdate.this , result , Toast.LENGTH_LONG).show();
        		super.onPostExecute(result);
        	}
        	
        	
        }
        
        buttonUpdate.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String updateString = editText.getText().toString() ;
				Log.d(TAG , "status to be updated = " + updateString);
				new PostToTwitter().execute(updateString) ;
			}
		}) ;
        
        
    }
	
	@Override
	protected void onStop() {
		Debug.stopMethodTracing();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu) ;
		return true ;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Intent intent = new Intent(this , UpdaterService.class);
		final Intent refreshIntent = new Intent(this, RefreshService.class) ;
		switch(item.getItemId())
		{
			case R.id.start_service:
				startService(intent);
				return true ;
			case R.id.stop_service:
				stopService(intent);
				return true ;
			case R.id.refresh_updates:
				startService(refreshIntent);
				return true ;
			default :
				return false ;
		}
		
	}
	
	
}