package iidenki.android;

import iidenki.android.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class iidenki extends Activity implements OnClickListener {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    setContentView(R.layout.mainmenu);
	    
	    Button b = (Button) findViewById(R.id.button1);
    	b.setOnClickListener(this);
    	b = (Button) findViewById(R.id.hangmanButton);
    	b.setOnClickListener(this);
    	b = (Button) findViewById(R.id.button2);
    	b.setOnClickListener(this);
    	b = (Button) findViewById(R.id.button4);
    	b.setOnClickListener(this);
    	b = (Button) findViewById(R.id.button5);
    	b.setOnClickListener(this);
    	b = (Button) findViewById(R.id.button6);
    	b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Button src = (Button)v;
		Button vocabbutton = (Button) findViewById(R.id.button1);
		Button kanjibutton = (Button) findViewById(R.id.button2);
		Button hangmanbutton = (Button) findViewById(R.id.hangmanButton);
		Button kanjiviewbutton = (Button) findViewById(R.id.button4);
		Button wordviewbutton = (Button) findViewById(R.id.button5);
		Button kanjiwritebutton = (Button) findViewById(R.id.button6);
		if (src == vocabbutton){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, WordTestMenu.class.getName());
			startActivity(intent);
		}
		if (src == hangmanbutton){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, FileLoader.class.getName());
			intent.putExtra("next","Hangman");
			startActivity(intent);
		}
		if (src == kanjibutton){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, KanjiTestMenu.class.getName());
			startActivity(intent);
		}
		if (src == kanjiviewbutton){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, FileLoader.class.getName());
			intent.putExtra("next","KanjiViewer");
			startActivity(intent);
		}
		if (src == wordviewbutton){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, FileLoader.class.getName());
			intent.putExtra("next","WordViewer");
			startActivity(intent);
		}
		if (src == kanjiwritebutton){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, KanjiWriteMenu.class.getName());
			startActivity(intent);
		}
	}
}