package iLighTech.Dr_Ragheb.Andalos;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

public class PlayAudio extends Activity implements  OnPreparedListener, MediaController.MediaPlayerControl  {

    public static final String AUDIO_FILE_NAME = "audioFileName";
    NotificationManager notification;
	Notification notify;
	String lessonName;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String audioFile;
	int currentPosition, duration, list_position, currentTab;
    private Handler handler = new Handler();
    SharedPreferences listenedLessons;

    boolean isVideoWatched;
    //for download
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        prepareVariables();
        //If user not connected to Internet, notify user by that in order to be able to listen online
        if(!isOnline()){
        	Toast.makeText(this, "من فضلك تأكد من الاتصال بالانترنت", Toast.LENGTH_LONG).show();
        }

        try{
        	mediaPlayer.release();
        }catch (Exception e) {
			// TODO: handle exception
		}
        setContentView(R.layout.activity_play);

        Button finish = (Button) findViewById(R.id.btn_finish);
        finish.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try{
				mediaPlayer.release();
				notification.cancelAll();
				}catch (Exception e) {
					// TODO: handle exception
				}finally{
					PlayAudio.this.finish();
				}
			}
		});
        ImageView videoWatchedMark = (ImageView) findViewById(R.id.iv_videoWatched);
        TextView videoWatchedText = (TextView) findViewById(R.id.tv_videoWatched);


        TextView title = (TextView) findViewById(R.id.tv_title);
        

        Intent intent = getIntent();
        audioFile = intent.getExtras().getString("URL");
        list_position = intent.getExtras().getInt("LIST_POSITION");
        
        lessonName = intent.getExtras().getString("LESSON_NAME");
        title.setText(lessonName);
        isVideoWatched = intent.getExtras().getBoolean("VIDEOWATCHED");
        
        new DownloadFileAsync().execute();  
        
        if(isVideoWatched){
        	videoWatchedMark.setVisibility(0); //visible
        	videoWatchedText.setVisibility(0);
        	Log.i("INTERNET", "in if");
        }else{
        	videoWatchedMark.setVisibility(4); //invisible - 8 for gone
        	videoWatchedText.setVisibility(4);
        	Log.i("INTERNET", "in else");
        }
        
                 
    }


        
        

    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	mediaController.show();
    	return false;
    }
    
   
/*    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	mediaPlayer.pause();
    	duration = mediaPlayer.getDuration();
    	currentPosition = mediaPlayer.getCurrentPosition();
        mediaController.hide();
    }*/

    
    public void finishVideo(SharedPreferences sharedPref, String prefName, int LessonNumber){
        sharedPref = getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Lesson" + LessonNumber, true);
        editor.commit();

    }

    public void showNotification(){
      notification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	notify = new Notification(R.drawable.ic_launcher, getResources().getString(R.string.notify_bar_words), System.currentTimeMillis());
    	
    	
    	Context context = this;
    	CharSequence title = "سلسلة الأندلس";
    	CharSequence details = lessonName;
    	Intent notifyIntent = new Intent(Intent.ACTION_MAIN);
    	notifyIntent.setClass(this, PlayAudio.class);
    	
    	PendingIntent pending = PendingIntent.getActivity(context, 0, notifyIntent, 0);


    	notify.setLatestEventInfo(context, title, details, pending);
    	notification.notify(0, notify);
    }

	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		 mediaController.setMediaPlayer(this);
		    mediaController.setAnchorView(findViewById(R.id.main_audio_view));

		    handler.post(new Runnable() {
		      public void run() {
		        mediaController.setEnabled(true);
		        mediaController.show();
		      }
		    });
		  
	}

	 public void start() {
	        mediaPlayer.start();
	      }

	      public void pause() {
	        mediaPlayer.pause();
	      }

	      public int getDuration() {
	        return mediaPlayer.getDuration();
	      }

	      public int getCurrentPosition() {
	        return mediaPlayer.getCurrentPosition();
	      }

	      public void seekTo(int i) {
	        mediaPlayer.seekTo(i);
	      }

	      public boolean isPlaying() {
	        return mediaPlayer.isPlaying();
	      }

	      public int getBufferPercentage() {
	        return 0;
	      }

	      public boolean canPause() {
	        return true;
	      }

	      public boolean canSeekBackward() {
	        return true;
	      }

	      public boolean canSeekForward() {
	        return true;
	      }
	 /*
     * Checks if the device has Internet connection.
     */
    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    

	  	@Override
	  	protected Dialog onCreateDialog(int id) {
	  		switch (id) {
	  		case DIALOG_DOWNLOAD_PROGRESS:
	  			mProgressDialog = new ProgressDialog(this);
	  			mProgressDialog.setMessage("جاري التحميل...");
	  			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	  			mProgressDialog.setCancelable(false);
	  			mProgressDialog.show();
	  			return mProgressDialog;
	  		default:
	  			return null;
	  		}
	  	}
	  	
	      //Async Task
	      class DownloadFileAsync extends AsyncTask<String, String, String> {

	  		@Override
	  		protected void onPreExecute() {
	  			super.onPreExecute();
	  			showDialog(DIALOG_DOWNLOAD_PROGRESS);
	  		}

	  		@Override
	  		protected String doInBackground(String... aurl) {
	  			 Uri myUri = Uri.parse(audioFile); // initialize Uri here
		  			mediaPlayer = new MediaPlayer();

	  	        mediaPlayer.setOnPreparedListener(PlayAudio.this);
	  	        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	  	        try {
	  				mediaPlayer.setDataSource(getApplicationContext(), myUri);
	  				mediaPlayer.prepare();
	  		        mediaPlayer.start();
	  			} catch (Exception e) {
	  				// TODO Auto-generated catch block

	  				} 
	  	        
	  	        //on complete
	  	      mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	  			
	  			public void onCompletion(MediaPlayer mp) {
	  				//  
	  	        	Log.i("INTERNET", "inside in completetion");
	  				mediaPlayer.release();
	  				try{

	  				notification.cancelAll();
	  	        	Log.i("INTERNET", "inside in completetion");
	  				}catch (Exception e) {
	  					// TODO: handle exception
	  				}

	  					finishVideo(listenedLessons, "listened_lessons", list_position);

	  				PlayAudio.this.finish();
	  	        	Log.i("INTERNET", "inside in close activty");
	  			}
	  		});

	      

	  			return null;
	  		}

	  		@Override
	  		protected void onPostExecute(String unused) {
	  			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);

	  		}
	  	}
	      
	      /*
	       */
	     private void prepareVariables(){
	    	 mediaController = new MediaController(PlayAudio.this);
	     }

	      @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
	    	   Intent setIntent = new Intent(Intent.ACTION_MAIN);
	    	   setIntent.addCategory(Intent.CATEGORY_HOME);
	    	   setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	   showNotification();
	    	   startActivity(setIntent);
	    }
}
