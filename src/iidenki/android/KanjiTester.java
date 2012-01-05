package iidenki.android;

import java.util.Random;

import iidenki.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
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
	    startAnimation(R.id.rain05);
	    startAnimation(R.id.rain06);
	    startAnimation(R.id.rain07);
	    startAnimation(R.id.rain08);
	}

	private void startAnimation(int v) {
		TextView test = (TextView) findViewById(v);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 2000);
		
		Random rand = new Random();
	    ta = new TranslateAnimation(0, (100-rand.nextInt(200)), 0, 400);
	    ta.setDuration(6000 + rand.nextInt(5000));
	    test.startAnimation(ta);
	    //test.setLayoutParams(lp);
	}

	private void setClickListener() {
		TextView test = (TextView) findViewById(R.id.rain01);
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain02);
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain03);
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain04);
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain05);
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain06);
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain07);
	    test.setOnClickListener(this);
	    test = (TextView) findViewById(R.id.rain08);
	    test.setOnClickListener(this);
	}
	
	/**
	 * Item clicked
	 */
	public void onClick(View arg0) {
		Toast.makeText(getApplicationContext(), "click!", Toast.LENGTH_SHORT).show();
	}
}