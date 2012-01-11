package control;

import iidenki.android.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.view.View;

public class FileManager {

	public static ObjectInputStream getInStream(Activity parent, String str){
		if (str.equals("Genki I vocabulary list")){
			try {
				return new ObjectInputStream(parent.getResources().openRawResource(R.raw.genki_vocab));
			} catch (Exception e){
				
			}
		}
		if (str.equals("Genki I kanji list")){
			try {
				return new ObjectInputStream(parent.getResources().openRawResource(R.raw.genki_kanji));
			} catch (Exception e){
				
			}
		}
		try {
			return new ObjectInputStream(new FileInputStream(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
