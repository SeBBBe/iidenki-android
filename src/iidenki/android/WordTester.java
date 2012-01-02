package iidenki.android;

import iidenki.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class WordTester extends Activity implements OnClickListener{
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.vocabtest);
	    
	    Button okbutton = (Button) findViewById(R.id.button1);
    	okbutton.setOnClickListener(this);
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.word_classes, android.R.layout.simple_spinner_item );
    	adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
    	Spinner s = (Spinner) findViewById( R.id.spinner1);
    	s.setAdapter( adapter );
	}

	public void onClick(View arg0) {
		 Toast.makeText(getApplicationContext(), "click!",
		          Toast.LENGTH_SHORT).show();
	}
}