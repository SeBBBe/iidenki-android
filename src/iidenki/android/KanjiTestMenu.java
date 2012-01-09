package iidenki.android;

import iidenki.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class KanjiTestMenu extends Activity implements OnClickListener{
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.kanjitestmenu);
	    
	    String fil = getIntent().getStringExtra("file");
	    
	    Button okbutton = (Button) findViewById(R.id.button1);
    	okbutton.setOnClickListener(this);
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kanji_test_types, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	Spinner s = (Spinner) findViewById(R.id.spinner1);
    	s.setAdapter(adapter);
	}
	
	/**
	 * Proceed
	 */
	public void onClick(View arg0) {
		Spinner spin = (Spinner) findViewById(R.id.spinner1);
		String testtype = (String) spin.getSelectedItem();
		CheckBox cb = (CheckBox) findViewById(R.id.checkBox2);
		String reset = cb.isChecked() ? "true" : "false";
		EditText et = (EditText) findViewById(R.id.editText1);
		String num = et.getText().toString();
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName(this, FileLoader.class.getName());
		intent.putExtra("test type",testtype);
		intent.putExtra("reset list",reset);
		intent.putExtra("number",num);
		intent.putExtra("next", "KanjiTester");
		startActivity(intent);
		finish();
	}
}