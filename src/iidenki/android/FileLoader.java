package iidenki.android;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class FileLoader extends TabActivity{
	
	private String next;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.fileloader);
	    next = getIntent().getStringExtra("next");

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, FileLoaderInt.class);
	    transfer(intent);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("artists").setIndicator("Included",
	                      res.getDrawable(R.drawable.int_icon))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, FileLoaderSD.class);
	    transfer(intent);
	    spec = tabHost.newTabSpec("albums").setIndicator("SD card",
	                      res.getDrawable(R.drawable.sd_icon))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}

	private void transfer(Intent intent) {
		intent.putExtra("next", next);
		if (next.equals("WordTester")){
			String testtype = getIntent().getStringExtra("test type");
			String wordclass = getIntent().getStringExtra("test word class");
			String reset = getIntent().getStringExtra("reset list");
			String num = getIntent().getStringExtra("number");
		    intent.putExtra("test type",testtype);
			intent.putExtra("test word class",wordclass);
			intent.putExtra("reset list",reset);
			intent.putExtra("number",num);
		}else if(next.equals("Hangman")){
		}else if(next.equals("KanjiTester")){
			String testtype = getIntent().getStringExtra("test type");
			String reset = getIntent().getStringExtra("reset list");
			String num = getIntent().getStringExtra("number");
			String speed = getIntent().getStringExtra("speed");
		    intent.putExtra("test type",testtype);
			intent.putExtra("reset list",reset);
			intent.putExtra("number",num);
			intent.putExtra("speed",speed);
		}else if(next.equals("KanjiWriter")){
			String testtype = getIntent().getStringExtra("test type");
			String reset = getIntent().getStringExtra("reset list");
			String num = getIntent().getStringExtra("number");
			String speed = getIntent().getStringExtra("speed");
		    intent.putExtra("test type",testtype);
			intent.putExtra("reset list",reset);
			intent.putExtra("number",num);
			intent.putExtra("speed",speed);
		}else if(next.equals("KanjiViewer")){
		}else if(next.equals("WordViewer")){
		}
	}
}
