package iidenki.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileLoaderInt extends ListActivity implements OnItemClickListener{
	
	private String next;
	public static String[] listOfFiles = {"Genki I vocabulary list","Genki I kanji list"};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		next = getIntent().getStringExtra("next");
	
		boolean error = false;
		try{
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, listOfFiles));
		}catch (Exception e){
			Toast.makeText(getApplicationContext(), "Internal read error!", Toast.LENGTH_SHORT).show();
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
