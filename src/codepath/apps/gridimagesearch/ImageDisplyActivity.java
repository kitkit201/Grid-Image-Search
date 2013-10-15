package codepath.apps.gridimagesearch;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.loopj.android.image.SmartImageView;

public class ImageDisplyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_disply);
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(result.getFullUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_disply, menu);
		return true;
	}

}
