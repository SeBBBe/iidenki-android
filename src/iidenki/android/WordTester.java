package iidenki.android;

import java.util.ArrayList;

import iidenki.android.R;
import iidenki.android.system.SimpleTest;
import iidenki.android.system.Tester;
import iidenki.android.system.Word;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    	
    	ArrayList<Word> temp = new ArrayList<Word>();
    	temp.add(new Word("car","kuruma","",""));
    	temp.add(new Word("fish","sakana","",""));
    	temp.add(new Word("rice","gohan","",""));
    	temp.add(new Word("desk","tsukue","",""));
    	temp.add(new Word("chair","isu","",""));
    	test = new SimpleTest<Word>(temp);
    	
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