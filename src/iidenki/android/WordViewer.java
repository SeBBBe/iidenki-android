package iidenki.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import control.FileManager;
import control.GameHandler;

import vocab.Kanji;
import vocab.Word;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WordViewer extends ListActivity implements OnItemClickListener{
	
	private String next;
	ArrayList<Word> list;
	private String readfrom;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String fil = getIntent().getStringExtra("file");
	  
		readfrom = fil;
    	list = null;
    	
    	boolean error = false;
    	try{
			ObjectInputStream in=FileManager.getInStream(this, readfrom);
			list =(ArrayList<Word>)in.readObject();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Invalid file!", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "You need to select an iidenki vocabulary list file", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "Download such a file or create one using iidenki on your computer", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(), "then place it on the root of your phone memory card", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			finish();
			error = true;
		}
    	
    	Word[] textlist = null;
    	try{
    		textlist = makeTextlist(list);
    	}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Wrong type of file!", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
    	if (!error){
			setListAdapter(new ArrayAdapter<Word>(this, R.layout.list_item, textlist));
	
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setOnItemClickListener(this);
    	}
	}

	private Word[] makeTextlist(ArrayList<Word> list) {
		Word[] textlist = new Word[list.size()];
		int i = list.size();
		for (Word k : list){
			i--;
			textlist[i] = k;
		}
		return textlist;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String selection = (String) ((TextView) arg1).getText();
		for (Word k : list){
			if (k.toString().equals(selection)){
	            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
	            alertbox.setMessage(k.toString() + "\nkana: " + k.getKana() + "\nkanji: " + k.getKanji() + "\nword class: " + k.type);
	            alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface arg0, int arg1) {
	                }
	            });
	            alertbox.show();
				break;
			}
		}
	}
}
