package codepath.apps.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity {

	private Spinner spinner1, spinner2, spinner3;
	private Button btnSettings;
	private EditText etSite;
	Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	public void addListenerOnButton() {

//		btnSettings.setOnClickListener(new OnClickListener(){
	}	
			
			public void onClick(View v) {
				// pass back variables to the previous screen
				spinner1 = (Spinner) findViewById(R.id.spinner1);
				spinner2 = (Spinner) findViewById(R.id.spinner2);
				spinner3 = (Spinner) findViewById(R.id.spinner3);
				etSite = (EditText) findViewById(R.id.etSite);
				
				btnSettings = (Button) findViewById(R.id.btnSettings);
				i = new Intent(getBaseContext(), SearchActivity.class);
				String site_string = etSite.getText().toString(); 
				i.putExtra("image", spinner1.getSelectedItem().toString());
				i.putExtra("color", spinner2.getSelectedItem().toString()); 
				i.putExtra("type", spinner3.getSelectedItem().toString());
				i.putExtra("site", site_string);
				Toast.makeText(this, "Settings Saved. Go ahead and search!", Toast.LENGTH_SHORT).show();
				startActivity(i);
			}
//		});
		
		
	}

