package com.example.simpleui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {

	private TextView textView;
	private ProgressBar progressBar;
	private static final String FILE_NAME = "text.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		textView = (TextView) findViewById(R.id.textView1);
		progressBar  = (ProgressBar) findViewById(R.id.progressBar1);

		String text = getIntent().getStringExtra("text");
		boolean isChecked = getIntent().getBooleanExtra("checkBox", false); 
		
		saveData(text, isChecked);

		//textView.setText(text);
		
		//writeFile(text);
	}

	private void loadData() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> msgList, ParseException e) {
				if (e == null) {
					String content = "";
					for (ParseObject msg : msgList)
						content += msg.getString("text") + "\n";
					textView.setText(content);
					progressBar.setVisibility(View.GONE);  // View.INVISIBLE, View.VISIBLE  
				} else
					e.printStackTrace();
			}
		});
	}

	private void saveData(String text, boolean isChecked) {
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
				loadData();
			}
		});
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
