package com.example.simpleui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {

	private TextView textView;
	private static final String FILE_NAME = "text.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		textView = (TextView) findViewById(R.id.textView1);

		String text = getIntent().getStringExtra("text");
		boolean isChecked = getIntent().getBooleanExtra("checkBox", false); 
		
		ParseObject testObject = new ParseObject("Message");
		testObject.put("text", text);
		testObject.put("checkBox", isChecked);
		testObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					Log.d("debug", "OK");
				} else {
					e.printStackTrace();
				}
				exeDone();
			}
		});

		
		textView.setText(text);
		
		//writeFile(text);
	}
	
	private void exeDone() {
		Toast.makeText(this, "done.", Toast.LENGTH_SHORT).show();
	}
	
	private void writeFile(String text) {
		text += "\n";
		
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
			fos.write(text.getBytes());
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
