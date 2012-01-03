package iidenki.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import vocab.SimpleTest;
import vocab.Tester;
import vocab.Word;
import iidenki.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WordTester extends Activity implements OnClickListener{
	
	private Word currentword;
	private Tester<Word> test;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.vocabtest);
	    
	    Button okbutton = (Button) findViewById(R.id.button1);
    	okbutton.setOnClickListener(this);
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.word_classes, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	Spinner s = (Spinner) findViewById(R.id.spinner1);
    	s.setAdapter(adapter);
    	
    	File root = Environment.getExternalStorageDirectory(); //fetch the external storage dir
    	File readfrom = new File(root.toString() + "/words");
    	
    	try{
			ObjectInputStream in=new ObjectInputStream(new FileInputStream(readfrom));
			ArrayList<Word> temp =(ArrayList<Word>)in.readObject();
			test = new SimpleTest<Word>(temp);
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "File read error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
    	
    	Toast.makeText(getApplicationContext(), readfrom.toString(), Toast.LENGTH_SHORT).show();
    	
    	chooseWord();
	}

	private void chooseWord() {
		TextView engword = (TextView) findViewById(R.id.textView2);
		currentword = test.getNext();
		engword.setText(currentword.toString());
	}

	/**
	 * Check the answer
	 */
	public void onClick(View arg0) {
		EditText textbox = (EditText) findViewById(R.id.editText1);
		String answer = textbox.getText().toString();
		
		Spinner spin = (Spinner) findViewById(R.id.spinner1);
		String wordclass = (String) spin.getSelectedItem();
		
		if (currentword.check(answer, wordclass, true)){
			Toast.makeText(getApplicationContext(), "correct!", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), "wrong!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "correct answer: " + currentword.getRomaji(), Toast.LENGTH_SHORT).show();
		}
		
		chooseWord();
		
	}
}