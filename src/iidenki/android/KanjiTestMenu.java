package iidenki.android;

import iidenki.android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class KanjiTestMenu extends Activity implements OnClickListener{
	
	private int speed;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    setContentView(R.layout.kanjitestmenu);
	    
	    String fil = getIntent().getStringExtra("file");
	    
	    Button okbutton = (Button) findViewById(R.id.button1);
    	okbutton.setOnClickListener(this);
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kanji_test_types, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	Spinner s = (Spinner) findViewById(R.id.spinner1);
    	s.setAdapter(adapter);
    	SeekBar sb = (SeekBar) findViewById(R.id.seekBar1);
    	sb.setOnSeekBarChangeListener(new SeekListen(this));
    	sb.setProgress(50);
    	speed = 50;
    	
    	if (!getNext().equals("KanjiTester")){
    		sb.setVisibility(View.INVISIBLE);
    		TextView t = (TextView) findViewById(R.id.textView3);
    		t.setVisibility(View.INVISIBLE);
    	}
	}
	
	/**
	 * Proceed
	 */
	public void onClick(View arg0) {
		Spinner spin = (Spinner) findViewById(R.id.spinner1);
		String testtype = (String) spin.getSelectedItem();
		//CheckBox cb = (CheckBox) findViewById(R.id.checkBox2);
		//String reset = cb.isChecked() ? "true" : "false";
		String reset = "false";
		EditText et = (EditText) findViewById(R.id.editText1);
		String num = et.getText().toString();
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName(this, FileLoader.class.getName());
		intent.putExtra("test type",testtype);
		intent.putExtra("reset list",reset);
		intent.putExtra("number",num);
		intent.putExtra("speed",speed + "");
		intent.putExtra("next", getNext());
		startActivity(intent);
		finish();
	}
	
	public String getNext(){
		return "KanjiTester";
	}
	
	private class SeekListen implements OnSeekBarChangeListener{
		
		private KanjiTestMenu k;
		public SeekListen(KanjiTestMenu k){
			this.k = k;
		}

		public void onProgressChanged(SeekBar arg0, int value, boolean arg2) {
			k.speed = value;
			TextView text = (TextView) k.findViewById(R.id.textView3);
			String denom = "medium";
			if (value < 20){
				denom = "very slow";
			}else if (value < 40){
				denom = "quite slow";
			}else if (value > 80){
				denom = "very speedy";
			}else if (value > 60){
				denom = "respectably speedy";
			}
			text.setText("Speed: " + denom);
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}