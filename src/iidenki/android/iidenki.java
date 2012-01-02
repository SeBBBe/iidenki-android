package iidenki.android;

import iidenki.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class iidenki extends Activity implements OnClickListener {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.mainmenu);
	    
	    Button vocabbutton = (Button) findViewById(R.id.button1);
    	vocabbutton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName(this, WordTester.class.getName());
		startActivity(intent);
	}
}