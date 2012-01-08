package iidenki.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import control.GameHandler;
import vocab.Word;
import iidenki.android.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Hangman extends Activity implements OnClickListener{
	
	private GameHandler gh;
	private int level;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    setContentView(R.layout.hangman);
	    level = 1;
	    
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
		InputMethodManager imm = (InputMethodManager) getSystemService(
			    INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

		EditText textbox = (EditText) findViewById(R.id.editText1);
		String answer = textbox.getText().toString();
		textbox.setText("");
		int lols = gh.guess(answer);
		if (lols == 1){
			Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
		}else if(lols == 0){
			Toast.makeText(getApplicationContext(), "Sorry!", Toast.LENGTH_SHORT).show();
			level++;
			updatePic();
		}
		else if(lols == 3){
			Toast.makeText(getApplicationContext(), "Input exactly one character please!", Toast.LENGTH_SHORT).show();
		}
		TextView box = (TextView) findViewById(R.id.textView1);
	    box.setText(gh.partialText());
	    box = (TextView) findViewById(R.id.textView3);
	    box.setText(gh.wrongText());
	    
	    String p = gh.partialText();
		if (!p.contains("_")){
			Toast.makeText(getApplicationContext(), "Congratulations!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), gh.word() + " means " + gh.meaning(), Toast.LENGTH_SHORT).show();
			disableInput();
		}
	}

	private void updatePic() {
		ImageView img = (ImageView) findViewById(R.id.imageView1);
		switch(level){
			case 1: img.setImageResource(R.drawable.hangman1); break;
			case 2: img.setImageResource(R.drawable.hangman2); break;
			case 3: img.setImageResource(R.drawable.hangman3); break;
			case 4: img.setImageResource(R.drawable.hangman4); break;
			case 5: img.setImageResource(R.drawable.hangman5); break;
			case 6: img.setImageResource(R.drawable.hangman6); break;
			case 7: img.setImageResource(R.drawable.hangman7); break;
			case 8: img.setImageResource(R.drawable.hangman8); break;
			case 9: img.setImageResource(R.drawable.hangman9); break;
			case 10: img.setImageResource(R.drawable.hangman10); 
						disableInput();
						Toast.makeText(getApplicationContext(), "The word was " + gh.word(), Toast.LENGTH_SHORT).show();
						break;
		}
	}

	private void disableInput() {
		Button b = (Button) findViewById(R.id.button1);
		b.setVisibility(View.INVISIBLE);
		EditText textbox = (EditText) findViewById(R.id.editText1);
		textbox.setVisibility(View.INVISIBLE);
	}
}