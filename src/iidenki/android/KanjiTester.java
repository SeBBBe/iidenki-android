package iidenki.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

import vocab.DynamicTest;
import vocab.Kanji;
import vocab.LatestTest;
import vocab.SimpleTest;
import vocab.Tester;
import vocab.Word;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiTester extends Activity implements OnClickListener{
	
	private TranslateAnimation ta;
	private Kanji currentkanji;
	private Tester<Kanji> test;
	private ArrayList<Kanji> list; 
	private int correct;
	private int total;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.kanjitester);
	    setClickListener();
	    
	    correct = 0;
	    total = 0;
	    boolean error = false;
	    
	    String fil = getIntent().getStringExtra("file");
	    String testtype = getIntent().getStringExtra("test type");
		String reset = getIntent().getStringExtra("reset list");
		String num = getIntent().getStringExtra("number");
    	
    	File readfrom = new File(fil);
    	list = null;
    	
    	try{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(readfrom));
			list =(ArrayList<Kanji>)in.readObject();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Invalid file!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "You need to select an iidenki kanji list file", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "Download such a file or create one using iidenki on your computer", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "then place it on the root of your phone memory card", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			finish();
			error = true;
		}
    	
    	if (!error){
	    	if (reset.equals("true")){
	    		resetList(list);
	    	}
	    	
	    	if (testtype.equals("Test all kanji")){
	    		test = new SimpleTest<Kanji>(list);
	    	}
	    	if (testtype.equals("Test the most difficult kanji")){
	    		test = new DynamicTest<Kanji>(list, num);
	    	}
	    	if (testtype.equals("Test the latest kanji")){
	    		test = new LatestTest<Kanji>(list, num);
	    	}
	    	
	    	newRound();
    	}
	    
	}

	private void newRound() {
		currentkanji = test.getNext();
		TextView word = (TextView) findViewById(R.id.textView1);
		word.setText(currentkanji.translation);
		startAnimation(R.id.rain01);
 	    startAnimation(R.id.rain02);
 	    startAnimation(R.id.rain03);
 	    startAnimation(R.id.rain04);
	}

	private void startAnimation(int v){
		Random generator = new Random();
		int index = generator.nextInt(list.size());
		String text = list.get(index).kanji;
		if (generator.nextInt(8) == 0){
			text = currentkanji.kanji;
		}
		
		TextView test = (TextView) findViewById(v);
		test.setText(text);
		
		Display display = getWindowManager().getDefaultDisplay();
		int y =  display.getHeight();
		
		Random rand = new Random();
	    ta = new TranslateAnimation(0, 0, 0, y);
	    ta.setDuration(3000 + rand.nextInt(3000));
	    ta.setFillAfter(true);
	    
	    ta.setAnimationListener(new AnimListen(this, v));


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
	
	/**
	 * Reset score
	 *
	 * @param newlist the list to reset
	 */
	private void resetList(ArrayList<Kanji> newlist) {
		for (Object w : newlist.toArray()){
			((Word)w).right = 0;
			((Word)w).wrong = 0;
		}
		Toast.makeText(getApplicationContext(), "scores have been reset", Toast.LENGTH_SHORT).show();
	}
	
	private class AnimListen implements AnimationListener{

		private KanjiTester kt;
		private int v;
		public AnimListen(KanjiTester kt, int v){
			this.kt = kt;
			this.v = v;
		}
		
		public void onAnimationEnd(Animation animation) {
			kt.repeat(v);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
	}

	public void repeat(int v) {
		startAnimation(v);
	}
}