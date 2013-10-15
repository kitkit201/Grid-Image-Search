package codepath.apps.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
    ImageResultArrayAdapter imageAdapter;
    String image = null;
    String color = null;
    String type = null;
    String site = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemCount){
				//onImageSearch(page);
			}
			
		});
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId ){
			   Intent i = new Intent(getApplicationContext(), ImageDisplyActivity.class);
			   ImageResult imageResult = imageResults.get(position);
			   i.putExtra("result", imageResult);
			   startActivity(i);
		        }
		});
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	public abstract class EndlessScrollListener implements OnScrollListener {
		// The minimum amount of items to have below your current scroll position
		// before loading more.
		private int visibleThreshold = 5;
		// The current offset index of data you have loaded
		private int currentPage = 0;
		// The total number of items in the dataset after the last load
		private int previousTotalItemCount = 0;
		// True if we are still waiting for the last set of data to load.
		private boolean loading = true;
		// Sets the starting page index
		private int startingPageIndex = 0;

		public EndlessScrollListener() {
		}

		public EndlessScrollListener(int visibleThreshold) {
			this.visibleThreshold = visibleThreshold;
		}

		public EndlessScrollListener(int visibleThreshold, int startPage) {
			this.visibleThreshold = visibleThreshold;
			this.startingPageIndex = startPage;
			this.currentPage = startPage;
		}

		// This happens many times a second during a scroll, so be wary of the code you place here.
		// We are given a few useful parameters to help us work out if we need to load some more data,
		// but first we check if we are waiting for the previous load to finish.
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// If the total item count is zero and the previous isn't, assume the
			// list is invalidated and should be reset back to initial state
			if (!loading && (totalItemCount < previousTotalItemCount)) {
				this.currentPage = this.startingPageIndex;
				this.previousTotalItemCount = totalItemCount;
				this.loading = true;
			}

			// If it’s still loading, we check to see if the dataset count has
			// changed, if so we conclude it has finished loading and update the current page
			// number and total item count.
			if (loading) {
				if (totalItemCount > previousTotalItemCount) {
					loading = false;
					previousTotalItemCount = totalItemCount;
					currentPage++;
				}
			}
			// If it isn’t currently loading, we check to see if we have breached
			// the visibleThreshold and need to reload more data.
			// If we do need to reload some more data, we execute onLoadMore to fetch the data.
			if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				onLoadMore(currentPage + 1, totalItemCount);
				loading = true;
			}
		}

		// Defines the process for actually loading more data based on page
		public abstract void onLoadMore(int page, int totalItemsCount);

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	}
	
	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		image = getIntent().getStringExtra("image");
		color = getIntent().getStringExtra("color");
		type = getIntent().getStringExtra("type");
		site = getIntent().getStringExtra("site");
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.activity_settings:
				Toast.makeText(this, "Launching Settings", Toast.LENGTH_SHORT).show();
				launchSettings();
				return true;
		default:
			return super.onOptionsItemSelected(item);

		}
		
		
	}
	
	public void launchSettings(){
		Intent i = new Intent(getBaseContext(), Settings.class);
		startActivity(i);
		
	}
	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT)
		     .show();
		AsyncHttpClient client = new AsyncHttpClient();
		//use default settings 
		if(image == null) {
			client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + "start=" + 0 + "&v=1.0&q=" + Uri.encode(query), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					JSONArray imageJsonResults = null;
					try {
						imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
						imageResults.clear();
						imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
						Log.d("DEBUG", imageResults.toString());
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			});	
		}
		// use saved settings
			else{
				
				client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + "start=" + 0 + "&v=1.0&q=" + Uri.encode(query) + "&imgcolor=" + color + "&imgs=" + image + "&imgtype=" + type + "&as_sitesearch=" + site, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						try {
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							imageResults.clear();
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
							Log.d("DEBUG", imageResults.toString());
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
				});
			}
		
	}
	
}
