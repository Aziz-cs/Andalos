package iLighTech.Dr_Ragheb.Andalos;

import iLighTech.Dr_Ragheb.Andalos.Adapter.AndalosAdapter;
import iLighTech.Dr_Ragheb.Andalos.Adapter.AndalosAdapterDL;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class AndalosList extends Activity {
	ListView AndalosListView, AndalosListViewDL;
	public static final String KEY_TITLE = "title";
	public static final String KEY_DURATION = "duration";
	public static String lessonName = "Andalos Lesson";
	public static String[] lessonURLs, lessonSizes;
	ArrayList<HashMap<String, String>> AndalosList;
	ArrayList<HashMap<String, String>> AndalosListDL;
	
	public static int notDownloadedYetColor, downloadedColor;
	
	AndalosAdapter andalosAdapter;
	AndalosAdapterDL andalosAdapterDL;

	TabHost th;
	int currentTab, sharedLessonNumber;
	static SharedPreferences downloadedLessons, listenedLessons;
	public static boolean[] andalosFinished;
	// =====For download
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private Button startBtn;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_siralist);

		setupVariablsAndViews();
		prepareTabs();
		prepareAndalosLists();

		AndalosListViewDL.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				playAudioFile(position);

			}
		});

		AndalosListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				HashMap<String, String> lessonHash = AndalosList.get(position);
				String lessonName = lessonHash.get(KEY_TITLE);

				Intent playAudioIntent = new Intent(AndalosList.this,
						PlayAudio.class);
				playAudioIntent.putExtra("URL", lessonURLs[position]);
				playAudioIntent.putExtra("LESSON_NAME", lessonName);
				playAudioIntent.putExtra("LIST_POSITION", position);
				playAudioIntent.putExtra("TAB", 2);

				if (andalosFinished[position] == true) {
					playAudioIntent.putExtra("VIDEOWATCHED", true);
				} else {
					playAudioIntent.putExtra("VIDEOWATCHED", false);
				}
				startActivity(playAudioIntent);
			}
		});
	}

	/*
	 * Create each listview row content, then adding it to it's ArrayList.
	 */
	public void createAndalosRow(String name, String duration) {
		// looping through all song nodes <song>
		HashMap<String, String> map = new HashMap<String, String>();
		// adding each child node to HashMap key => value
		map.put(KEY_TITLE, name);
		map.put(KEY_DURATION, duration);

		AndalosList.add(map);

	}

	public void createAndalosDLRow(String name, String duration) {
		// looping through all song nodes <song>
		HashMap<String, String> map = new HashMap<String, String>();
		// adding each child node to HashMap key => value
		map.put(KEY_TITLE, name);
		map.put(KEY_DURATION, duration);

		AndalosListDL.add(map);

	}

	/*
	 * Create listview content then setup it with the Adapter
	 */
	public void prepareAndalosLists() {

		// Andalos series
		createAndalosRow("الطريق إلى الأندلس", "54:45");
		createAndalosRow("عهد الفتح الإسلامي", "56:16");
		createAndalosRow("عهد الولاة", "56:54");
		createAndalosRow("عبد الرحمن الداخل صقر قريش", "59:13");
		createAndalosRow("الإمارة الأموية", "51:36");
		createAndalosRow("الخلافة الأموية", "55:22");
		createAndalosRow("عهد الدولة العامرية والفتنة", "56:58");
		createAndalosRow("عهد ملوك الطوائف", "59:59");
		createAndalosRow("دولة المرابطين", "01:11:37");
		createAndalosRow("بين المرابطين والموحدين", "57:16");
		createAndalosRow("دولة الموحدين", "01:10:32");
		createAndalosRow("سقوط الأندلس", "01:16:15");

		// Andalos series
		createAndalosDLRow("الطريق إلى الأندلس", "54:45");
		createAndalosDLRow("عهد الفتح الإسلامي", "56:16");
		createAndalosDLRow("عهد الولاة", "56:54");
		createAndalosDLRow("عبد الرحمن  وصقر قريش", "59:13");
		createAndalosDLRow("الإمارة الأموية", "51:36");
		createAndalosDLRow("الخلافة الأموية", "55:22");
		createAndalosDLRow("عهد الدولة العامرية والفتنة", "56:58");
		createAndalosDLRow("عهد ملوك الطوائف", "59:59");
		createAndalosDLRow("دولة المرابطين", "01:11:37");
		createAndalosDLRow("بين المرابطين والموحدين", "57:16");
		createAndalosDLRow("دولة الموحدين", "01:10:32");
		createAndalosDLRow("سقوط الأندلس", "01:16:15");

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences tabShared = getSharedPreferences("TAB", 0);
		SharedPreferences.Editor editor = tabShared.edit();
		editor.putInt("CURRENT_TAB", th.getCurrentTab());
		editor.commit();
	}

	/*
	 * method check if mp3 exist 1st then to play audio files directly from sd
	 * card if existed:
	 */
	public void playAudioFile(final int lessonNumber) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"Andalos/" + getLessonName(lessonNumber) + ".mp3");

		if (file.exists() && isLessonCompletelyDownloaded(lessonNumber) == true) {
			// Do action

			intent.setDataAndType(Uri.fromFile(file), "audio/*");
			startActivity(intent);
		} else {

			new AlertDialog.Builder(this)
					.setTitle("يجب تحميل الدرس")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage(
							"يبدو أن الدرس لم يتم تحميله بعد، هل تريد تحميله الآن؟")
					.setPositiveButton("نعم",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
									if (isOnline()) {
										startDownload(lessonNumber);
									} else {
										Toast.makeText(
												AndalosList.this,
												"من فضلك تأكد من الاتصال بالانترنت",
												Toast.LENGTH_LONG).show();
									}
								}
							})
					.setNegativeButton("ربما لاحقا",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).show();
		}

	}

	// For download task:
	private void startDownload(int lessonNumber) {
		// save this lesson number to variable to check if it's completed
		sharedLessonNumber = lessonNumber;
		// get lesson name to save it on sd card
		lessonName = getLessonName(lessonNumber);
		// start download
		new DownloadFileAsync().execute(lessonURLs[lessonNumber]);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("جاري التحميل...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {

				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();
				int lenghtOfFile = conexion.getContentLength();


				File cacheDir = new File(
						android.os.Environment.getExternalStorageDirectory(),
						"Andalos");
				if (!cacheDir.exists()) {
					cacheDir.mkdir();

				}

				File f = new File(cacheDir, lessonName + ".mp3");
				InputStream input = new BufferedInputStream(url.openStream());
				FileOutputStream output = new FileOutputStream(f);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;

		}

		protected void onProgressUpdate(String... progress) {

			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			Toast.makeText(AndalosList.this, "تم التحميل بنجاح",
					Toast.LENGTH_LONG).show();
			// save in sharedpref that this lesson is succefuly been downloaded
			// completetly:
			downloadedLessons = getSharedPreferences("downloadedLessons", 0);
			SharedPreferences.Editor editor = downloadedLessons.edit();
			editor.putBoolean("" + sharedLessonNumber, true);
			editor.commit();
			finish();
			startActivity(getIntent());
		}
	}

	/*
	 * setup variables, making findviewbyids, all here..
	 */
	public void setupVariablsAndViews() {
		AndalosListView = (ListView) findViewById(R.id.lv_andalos);
		AndalosListViewDL = (ListView) findViewById(R.id.lv_andalos_dl);

		AndalosList = new ArrayList<HashMap<String, String>>();
		AndalosListDL = new ArrayList<HashMap<String, String>>();

		downloadedLessons = getSharedPreferences("downloadedLessons", 0);

		
		lessonURLs = new String[12];
		lessonURLs[0] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/13/13.mp3";
		lessonURLs[1] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/14/14.mp3";
		lessonURLs[2] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/15/15.mp3";
		lessonURLs[3] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/16/16.mp3";
		lessonURLs[4] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/17/17.mp3";
		lessonURLs[5] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/18/18.mp3";
		lessonURLs[6] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/19/19.mp3";
		lessonURLs[7] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/20/20.mp3";
		lessonURLs[8] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/21/21.mp3";
		lessonURLs[9] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/22/22.mp3";
		lessonURLs[10] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/23/23.mp3";
		lessonURLs[11] = "http://audio2.islamweb.net/lecturs/rajeb_alserjani/24/24.mp3";
		
/*		lessonURLs[0] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[1] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[2] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[3] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[4] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[5] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[6] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[7] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[8] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[9] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[10] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";
		lessonURLs[11] = "http://oringz.com/ringtone/what-friends-are-for/sounds-942-what-friends-are-for/?download";*/
//
		lessonSizes = new String[12];
		lessonSizes[0] = "25MB";
		lessonSizes[1] = "26MB";
		lessonSizes[2] = "26MB";
		lessonSizes[3] = "27MB";
		lessonSizes[4] = "23MB";
		lessonSizes[5] = "25.5MB";
		lessonSizes[6] = "26MB";
		lessonSizes[7] = "27MB";
		lessonSizes[8] = "33MB";
		lessonSizes[9] = "26MB";
		lessonSizes[10] = "32MB";
		lessonSizes[11] = "35MB";

		andalosFinished = new boolean[12];
		listenedLessons = getSharedPreferences("listened_lessons", 0);
		fillFinishedVideosArray(listenedLessons, "listened_lessons", andalosFinished);
		
		andalosAdapter = new AndalosAdapter(this, AndalosList,
				R.drawable.andalos_lv);
		AndalosListView.setAdapter(andalosAdapter);

		andalosAdapterDL = new AndalosAdapterDL(this, AndalosListDL,
				R.drawable.andalos_lv);
		AndalosListViewDL.setAdapter(andalosAdapterDL);
		
		//setting colors:
		notDownloadedYetColor = getResources().getColor(R.color.notDownloaded);
		downloadedColor = getResources().getColor(R.color.defaultColor);

	}

	/*
	 * prepare tabs
	 */
	public void prepareTabs() {

		th = (TabHost) findViewById(R.id.tabhost);
		th.setup();
		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.tab1);
		specs.setIndicator("تحميل السلسلة",
				getResources().getDrawable(android.R.drawable.ic_menu_save));
		th.addTab(specs);

		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.tab2);
		specs.setIndicator("السماع من الإنترنت", getResources()
				.getDrawable(android.R.drawable.ic_media_play));
		th.addTab(specs);

		SharedPreferences tabShared = getSharedPreferences("TAB", 0);
		th.setCurrentTab(tabShared.getInt("CURRENT_TAB", 1));
	}

	/*
	 * method that takes lessonNumber and returns the lesson name
	 */
	public static String getLessonName(int lessonNumber) {
		lessonName = "";
		switch (lessonNumber) {
		case 0:
			lessonName = "الطريق إلى الأندلس";
			break;
		case 1:
			lessonName = "عهد الفتح الإسلامي";
			break;
		case 2:
			lessonName = "عهد الولاة";
			break;
		case 3:
			lessonName = "عبد الرحمن الداخل صقر قريش";
			break;
		case 4:
			lessonName = "الإمارة الأموية";
			break;
		case 5:
			lessonName = "الخلافة الأموية";
			break;
		case 6:
			lessonName = "عهد الدولة العامرية والفتنة";
			break;
		case 7:
			lessonName = "عهد ملوك الطوائف";
			break;
		case 8:
			lessonName = "دولة المرابطين";
			break;
		case 9:
			lessonName = "بين المرابطين والموحدين";
			break;
		case 10:
			lessonName = "دولة الموحدين";
			break;
		case 11:
			lessonName = "سقوط الأندلس";
			break;
		default:
			break;
		}
		return lessonName;
	}

	public static boolean isFileExist(int lessonNumber) {
		File file = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"Andalos/" + getLessonName(lessonNumber) + ".mp3");
		return file.exists();
	}

	/*
	 * Checks if the device has Internet connection.
	 * 
	 * @return <code>true</code> if the phone is connected to the Internet.
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/*
	 * this method check for that lesson number is it's completely downloaded or
	 * not
	 */
	public static boolean isLessonCompletelyDownloaded(int lessonNumber) {
		boolean isCompletelyDownloaded = downloadedLessons.getBoolean(""
				+ lessonNumber, false);

		return isCompletelyDownloaded;
	}
	
	/*
	 * fill finished
	 */
	public void fillFinishedVideosArray(SharedPreferences sharedPref,String prefName, boolean[] array){
	    sharedPref = getSharedPreferences(prefName, 0);
	    for(int i=0; i<array.length; i++){
	        array[i] = sharedPref.getBoolean("Lesson" + i, false);
	    }
	}
@Override
protected void onRestart() {
	// TODO Auto-generated method stub
	super.onRestart();
	fillFinishedVideosArray(listenedLessons, "listened_lessons", andalosFinished);
}

}
