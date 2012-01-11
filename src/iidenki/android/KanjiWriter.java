package iidenki.android;


import java.io.InputStream;

import iidenki.android.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.PadView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class KanjiWriter extends Activity implements OnClickListener{
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    setContentView(R.layout.kanjiwriter);
	    
	    Button b = (Button) findViewById(R.id.button1);
    	b.setOnClickListener(this);
    	b = (Button) findViewById(R.id.button2);
    	b.setOnClickListener(this);
	    
	    run();
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
		Button clearbutton = (Button) findViewById(R.id.button2);
		
		ViewGroup baseView = (ViewGroup) findViewById(R.id.buttonsArea);
    	baseView.removeAllViews();
		
		if (src == clearbutton){
			PadView padView = (PadView) findViewById(R.id.padView);
	    	padView.clear();
		}
		if (src == donebutton){
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
            				selectedKanji(b.getText().charAt(0));
        				}
        			}
        		});    	
        		
        	}
		}
	}
	
	private void selectedKanji(char k){
		
	}
	
}