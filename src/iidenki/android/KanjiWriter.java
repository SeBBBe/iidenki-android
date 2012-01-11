package iidenki.android;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import vocab.DynamicTest;
import vocab.Kanji;
import vocab.LatestTest;
import vocab.SimpleTest;
import vocab.Tester;
import vocab.Word;

import iidenki.android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.PadView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiWriter extends Activity implements OnClickListener{
	
	private Kanji currentkanji;
	private Tester<Kanji> test;
	private ArrayList<Kanji> list; 
	private int correct;
	private int total;
	private File readfrom;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    setContentView(R.layout.kanjiwriter);
	    
	    String fil = getIntent().getStringExtra("file");
	    String testtype = getIntent().getStringExtra("test type");
		String reset = getIntent().getStringExtra("reset list");
		String num = getIntent().getStringExtra("number");
    	
    	readfrom = new File(fil);
    	list = null;
    	
    	boolean error = false;
    	try{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(readfrom));
			list =(ArrayList<Kanji>)in.readObject();
			in.close();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Invalid file!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "You need to select an iidenki kanji list file", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "Download such a file or create one using iidenki on your computer", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "then place it on the root of your phone memory card", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			finish();
			error = true;
		}
	    
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
    	
	    Button b = (Button) findViewById(R.id.button1);
    	b.setOnClickListener(this);
    	b = (Button) findViewById(R.id.button2);
    	b.setOnClickListener(this);
    	b = (Button) findViewById(R.id.button3);
    	b.setOnClickListener(this);
	    
	    run();
	    newRound();
	}
	
	private void newRound() {
		currentkanji = new Kanji("temp");
		int i = 0;
		while (currentkanji.toString().length() != 1){
			currentkanji = test.getNext();
			i++;
			if (i > list.size()){
				currentkanji = null;
				break;
			}
		}
		
		if (currentkanji == null){
			try{
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(readfrom));
				out.writeObject(list);
			}catch(Exception f){
				Log.e("", "", f);
				Toast.makeText(getApplicationContext(), "File error! Scores not saved.", Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(getApplicationContext(), "end of quiz!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), correct + " correct out of " + total, Toast.LENGTH_SHORT).show();
			finish();
		}else{
			TextView exp = (TextView) findViewById(R.id.textView2);
			exp.setText("meaning: " + currentkanji.translation + " reading: " + currentkanji.reading);
		}
	}
	
	 public void run() {
         // Inits the strokes dictionaries
	        InputStream dataFile = getResources().openRawResource(R.raw.jdata);
	        //InputStream dataFile = getResources().openRawResource(R.raw.jdata_test);
	        if (dataFile!=null) {
	        	PadView.initDictionaries(dataFile);
	        } else {
	        }
	        
	        if (PadView.getPaintbush() == null) {
		        Drawable paintbrush = getResources().getDrawable(R.drawable.paintbrush);
		        if (paintbrush instanceof BitmapDrawable) {
		        	PadView.setPaintbush((BitmapDrawable) paintbrush);
		        }	
	        }
     }

	public void onClick(View arg0) {
		Button src = (Button)arg0;
		
		Button donebutton = (Button) findViewById(R.id.button1);
		Button skipbutton = (Button) findViewById(R.id.button3);
		
		ViewGroup baseView = (ViewGroup) findViewById(R.id.buttonsArea);
    	baseView.removeAllViews();
		
		if (src == donebutton){
			baseView = (ViewGroup) findViewById(R.id.buttonsArea);
			PadView padView = (PadView) findViewById(R.id.padView);
	    	char result[] = padView.search();
	    	
	    	for (int i=0; i<result.length; i++) {
        		Button button = new Button(this.getBaseContext());
        		button.setTextSize(20.0F);
        		button.setTypeface(Typeface.create("Serif", Typeface.NORMAL));
        		button.setText(result, i, 1);
        		baseView.addView(button);
        		
        		button.setOnClickListener( new View.OnClickListener() {			
        			public void onClick(View v) {
        				if (v instanceof Button) {
        					Button b = (Button) v;
            				guess(b.getText().charAt(0));
        				}
        			}
        		});    	
        		
        	}
		}
		
		if (src == skipbutton){
			showAnswerDialog();
			newRound();
		}
		
		clearPad();
	}

	private void clearPad() {
		PadView padView = (PadView) findViewById(R.id.padView);
		padView.clear();
	}
	
	private void guess(char k){
		String answer = k + "";
		
		ViewGroup baseView = (ViewGroup) findViewById(R.id.buttonsArea);
    	baseView.removeAllViews();
    	clearPad();
		
		if (currentkanji.check(answer)){
			Toast.makeText(getApplicationContext(), "correct!", Toast.LENGTH_SHORT).show();
			test.success();
			correct++;
			total++;
			newRound();
		}else{
			showAnswerDialog();
			test.fail();
			total++;
			newRound();
		}
	}

	private void showAnswerDialog() {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setMessage("the answer was\n" + currentkanji.toString() + "\nreading: " + currentkanji.reading + "\nmeaning: " + currentkanji.translation);
		alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface arg0, int arg1) {
		    }
		});
		alertbox.show();
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
	
}