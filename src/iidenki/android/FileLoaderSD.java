package iidenki.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import control.GameHandler;

import vocab.Word;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.TabActivity;
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

public class FileLoaderSD extends ListActivity implements OnItemClickListener{
	
	private String next;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		next = getIntent().getStringExtra("next");
		  
		File root = Environment.getExternalStorageDirectory(); //fetch the external storage dir
		File[] listOfFiles = root.listFiles();
	
		boolean error = false;
		try{
			setListAdapter(new ArrayAdapter<File>(this, R.layout.list_item, listOfFiles));
		}catch (Exception e){
			Toast.makeText(getApplicationContext(), "You need to insert an SD card to load files!", Toast.LENGTH_SHORT).show();
			error = true;
			finish();
		}
	
		if (!error){
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setOnItemClickListener(this);
		}
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProgressDialog dialog = ProgressDialog.show(this, "", 
                "Loading. Please wait...", true);
		String fil = (String) ((TextView) arg1).getText();
		
		if (next.equals("WordTester")){
			String testtype = getIntent().getStringExtra("test type");
			String wordclass = getIntent().getStringExtra("test word class");
			String reset = getIntent().getStringExtra("reset list");
			String num = getIntent().getStringExtra("number");
			
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, WordTester.class.getName());
		    intent.putExtra("file",fil);
		    intent.putExtra("test type",testtype);
			intent.putExtra("test word class",wordclass);
			intent.putExtra("reset list",reset);
			intent.putExtra("number",num);
			startActivity(intent);
			finish();
		}else if(next.equals("Hangman")){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, Hangman.class.getName());
		    intent.putExtra("file",fil);
			startActivity(intent);
			finish();
		}else if(next.equals("KanjiTester")){
			String testtype = getIntent().getStringExtra("test type");
			String reset = getIntent().getStringExtra("reset list");
			String num = getIntent().getStringExtra("number");
			String speed = getIntent().getStringExtra("speed");
			
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, KanjiTester.class.getName());
		    intent.putExtra("file",fil);
		    intent.putExtra("test type",testtype);
			intent.putExtra("reset list",reset);
			intent.putExtra("number",num);
			intent.putExtra("speed",speed);
			startActivity(intent);
			finish();
		}else if(next.equals("KanjiWriter")){
			String testtype = getIntent().getStringExtra("test type");
			String reset = getIntent().getStringExtra("reset list");
			String num = getIntent().getStringExtra("number");
			String speed = getIntent().getStringExtra("speed");
			
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, KanjiWriter.class.getName());
		    intent.putExtra("file",fil);
		    intent.putExtra("test type",testtype);
			intent.putExtra("reset list",reset);
			intent.putExtra("number",num);
			intent.putExtra("speed",speed);
			startActivity(intent);
			finish();
		}else if(next.equals("KanjiViewer")){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, KanjiViewer.class.getName());
		    intent.putExtra("file",fil);
			startActivity(intent);
			finish();
		}else if(next.equals("WordViewer")){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClassName(this, WordViewer.class.getName());
		    intent.putExtra("file",fil);
			startActivity(intent);
			finish();
		}
		
	}
}
