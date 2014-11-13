package iLighTech.Dr_Ragheb.Andalos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		 Thread timer = new Thread(){
		     	@Override
		     	public void run() {
		     		try{
		     			sleep(3000);
		     		}catch(InterruptedException e){
		     			e.printStackTrace();
		     		}finally{
		     			startActivity(new Intent(Splash.this, Main.class));
		     		}
		     	}

		     };
		     timer.start();
		     
		 }
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}
	


}
