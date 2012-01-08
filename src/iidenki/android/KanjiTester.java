package iidenki.android;

import java.util.Random;

import iidenki.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiTester extends Activity implements OnClickListener{
	
	private TranslateAnimation ta;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.kanjitester);
	    setClickListener();
	    
	    startAnimation(R.id.rain01);
	    startAnimation(R.id.rain02);
	    startAnimation(R.id.rain03);
	    startAnimation(R.id.rain04);
	}

	private void startAnimation(int v) {
		TextView test = (TextView) findViewById(v);
		
		Random rand = new Random();
	    ta = new TranslateAnimation(0, 0, 0, 800);
	    ta.setDuration(6000 + rand.nextInt(5000));
	    ta.setFillAfter(true);

	    test.startAnimation(ta);
	}

	private void setClickListener() {
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();
		int x = width/4;
		int y =  display.getHeight() * 2;
		
		TextView test = (TextView) findViewById(R.id.rain01);
		test.setLayoutParams(new LinearLayout.LayoutParams(x,y));
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain02);
	    test.setLayoutParams(new LinearLayout.LayoutParams(x,y));
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain03);
	    test.setLayoutParams(new LinearLayout.LayoutParams(x,y));
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain04);
	    test.setLayoutParams(new LinearLayout.LayoutParams(x,y));
	    test.setOnClickListener(this);
	}
	
	/**
	 * Item clicked
	 */
	public void onClick(View arg0) {
		Toast.makeText(getApplicationContext(), ((TextView)arg0).getText(), Toast.LENGTH_SHORT).show();
	}
}