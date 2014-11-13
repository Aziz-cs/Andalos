package iLighTech.Dr_Ragheb.Andalos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
	Button sera, contactUs;
    SharedPreferences AndalosFinishedVideos;
    Intent intent;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	
	
	sera = (Button) findViewById(R.id.btn_sera);
	contactUs = (Button) findViewById(R.id.btn_contact);
	
	sera.setOnClickListener(this);
	contactUs.setOnClickListener(this);
	
	//================



    


}
public void onClick(View buttonClicked) {
	// TODO Auto-generated method stub
	switch (buttonClicked.getId()) {
	case R.id.btn_sera:
		intent = new Intent(this, AndalosList.class);
		startActivity(intent);
		break;
	case R.id.btn_contact:
		intent = new Intent(this, About.class);
		startActivity(intent);
		break;
	default:
		break;
	}
}



@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	menu.add("مسح الذاكرة").setIcon(android.R.drawable.ic_menu_recent_history);
	return super.onCreateOptionsMenu(menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	new AlertDialog.Builder(this)
	.setTitle("مسح ذاكرة البرنامج")
	.setIcon(android.R.drawable.ic_dialog_alert)
	.setMessage(
			"سيتم مسح ذاكرة الدروس المستمعة والمحملة، هل تريد الإستمرار؟")
	.setPositiveButton("نعم",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int which) {
					// continue with delete
					AndalosFinishedVideos = getSharedPreferences("andalos", 0);
					SharedPreferences.Editor editor = AndalosFinishedVideos.edit();
					editor.clear();
					editor.commit();
					
					AndalosFinishedVideos = getSharedPreferences("listened_lessons", 0);
					editor = AndalosFinishedVideos.edit();
					editor.clear();
					editor.commit();

					AndalosFinishedVideos = getSharedPreferences("downloadedLessons", 0);
					editor = AndalosFinishedVideos.edit();
					editor.clear();
					editor.commit();
					Toast.makeText(Main.this, "تم مسح الذاكرة بنجاح", Toast.LENGTH_LONG).show();
				}
			})
	.setNegativeButton("إلغاء",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int which) {
					// do nothing
				}
			}).show();
	
	
	return super.onOptionsItemSelected(item);
}


}
