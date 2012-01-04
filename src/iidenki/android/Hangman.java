package iidenki.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import control.GameHandler;

import vocab.Word;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Hangman extends Activity implements OnClickListener{
	
	GameHandler gh;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    setContentView(R.layout.hangman);
	    int level = 1;
	    
	    String fil = getIntent().getStringExtra("file");
	    File readfrom = new File(fil);
	    ArrayList<Word> temp = null;
	    boolean error = false;
    	
    	try{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(readfrom));
			temp =(ArrayList<Word>)in.readObject();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Invalid file!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "You need to select an iidenki vocabulary list file", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "Download such a file or create one using iidenki on your computer", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "then place it on the root of your phone memory card", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			finish();
			error = true;
		}
    	
    	if (!error){
    		gh = new GameHandler(temp);
		    ImageView img = (ImageView) findViewById(R.id.imageView1);
		    img.setImageResource(R.drawable.hangman1);
		    
		    TextView box = (TextView) findViewById(R.id.textView1);
		    box.setText(gh.partialText());
		    
		    Button b = (Button) findViewById(R.id.button1);
		    b.setOnClickListener(this);
    	}
	}
	
	/**
	 * Make a guess
	 */
	public void onClick(View arg0) {
		EditText textbox = (EditText) findViewById(R.id.editText1);
		String answer = textbox.getText().toString();
		textbox.setText("");
		gh.guess(answer);
		TextView box = (TextView) findViewById(R.id.textView1);
	    box.setText(gh.partialText());
	    box = (TextView) findViewById(R.id.textView3);
	    box.setText(gh.wrongText());
	}
}