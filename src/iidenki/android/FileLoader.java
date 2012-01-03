package iidenki.android;

import java.io.File;

import android.app.ListActivity;
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

public class FileLoader extends ListActivity implements OnItemClickListener{
	
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  File root = Environment.getExternalStorageDirectory(); //fetch the external storage dir
	  File[] listOfFiles = root.listFiles();

	  setListAdapter(new ArrayAdapter<File>(this, R.layout.list_item, listOfFiles));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);
	  lv.setOnItemClickListener(this);
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String fil = (String) ((TextView) arg1).getText();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClassName(this, WordTester.class.getName());
	    intent.putExtra("file",fil);
		startActivity(intent);
	}
}
