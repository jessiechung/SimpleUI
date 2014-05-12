package com.example.simpleui;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.PushService;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        Parse.initialize(this, "CgPkaJbiKHrCUgyeaYOmrHOyRxFjc75xHstT7ngm", "G5tkkwn4QN8r0m6hxkhqgbApvDLHzmPkW1T5t0wf");
        PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground(); // Save the current Installation to Parse.
        PushService.subscribe(this, "all", MainActivity.class); // When users indicate they are Giants fans, we subscribe them to that channel.
             
        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
    	
    	private Button button;
    	private EditText editText;
    	private CheckBox checkBox;
    	private SharedPreferences sp;
		private SharedPreferences.Editor editor;

        public PlaceholderFragment() {
        }
        
        private void send() {
        	String text = editText.getText().toString(); // get editText text value
        	if (checkBox.isChecked())
        		text = "***********";
			Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show(); // show editText text value on the screen
			editText.getText().clear(); // clear editText text value
			//editText.setText(""); // clear editText text value
			
			// Sending Pushes to Channels
			ParsePush push = new ParsePush();
			push.setChannel("all");
			push.setMessage(text);
			push.sendInBackground();
			
			Intent intent = new Intent();
			intent.setClass(getActivity(), MessageActivity.class);
			intent.putExtra("text", text);
			intent.putExtra("checkBox", checkBox.isChecked());
			getActivity().startActivity(intent);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            sp = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
            editor = sp.edit();
            
            checkBox = (CheckBox) rootView.findViewById(R.id.checkBox1);
            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					editor.putBoolean("checkBox", isChecked);
					editor.commit();
				}
			});
            
            button = (Button) rootView.findViewById(R.id.button1);
            editText = (EditText) rootView.findViewById(R.id.editText1);
            editText.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					Log.d("debug", "keyCode:" + keyCode + ", KeyEvent:" + event.getAction());
					
					if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
						send();
						return true;
					} else {
						editor.putString("text", editText.getText().toString());
						editor.commit();
					}
					return false;
				}
			});
            
            button.setText("Send");
            button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					send();
				}
            });
            
            editText.setText(sp.getString("text", ""));
			checkBox.setChecked(sp.getBoolean("checkBox", false));

            return rootView;
        }
    }
    
//    public void send(View view) {
//    	Log.d("debug", "click test!!");
//    }

}
