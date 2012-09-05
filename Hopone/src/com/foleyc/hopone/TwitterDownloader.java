package com.foleyc.hopone;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class TwitterDownloader extends Activity {	
		ProgressDialog dialog = null;
		private String searchTerm = "hopscotchfest";
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.feedmain);
	        String searchUrl = "http://search.twitter.com/search.json?q=@" 
					+ searchTerm + "&rpp=100&page=" + 1;
	        new DownloadTwitterTask().execute(searchUrl);
	    }
		
		private void runOnPostExecute(ArrayList<Tweet> tweets) {
			ListView listView = (ListView) findViewById(R.id.ListViewId);
	        listView.setAdapter(new TweetItemAdapter(this, R.layout.feedslistitem, tweets));
		}
		
		private class DownloadTwitterTask extends AsyncTask<String, Void, ArrayList<Tweet>> {
//			public TwitterDownloader activity;
//			public DownloadTwitterTask(TwitterDownloader a){
//				 activity = a;
//			}
		     protected ArrayList<Tweet> doInBackground(String... urls) {
		         return loadTweetsFromNetwork(urls[0]);
		     }

		     protected void onPostExecute(ArrayList<Tweet> tweets) {
		         Log.i("onPostExecute","do the page layout here");
		         runOnPostExecute(tweets);
//		         ListView listView = (ListView) findViewById(R.id.ListViewId);
//		         listView.setAdapter(new TweetItemAdapter(this, R.layout.feedslistitem, tweets));
		     }
		 }
		
		private ArrayList<Tweet> loadTweetsFromNetwork(String url) {
			ArrayList<Tweet> tweets = new ArrayList<Tweet>();
			
			HttpClient client = new  DefaultHttpClient();
			HttpGet get = new HttpGet(url);
		      
			ResponseHandler<String> responseHandler = new BasicResponseHandler();

			String responseBody = null;
			try{
				responseBody = client.execute(get, responseHandler);
				Log.i("responseBody = ",responseBody); 
			}catch(Exception ex) {
				Log.v("responseBody","Exception: " + ex.getMessage());
				ex.printStackTrace();
			}

			JSONObject fullResponseJSONObject = null;
			JSONArray resultsJSONArray = null;
			//JSONParser parser=new JSONParser();		
			
			try {
				fullResponseJSONObject = new JSONObject(responseBody);	
				resultsJSONArray = fullResponseJSONObject.getJSONArray("results");
				if (resultsJSONArray != null && resultsJSONArray.length() != 0){
					Log.i("results = are not null","results are not null"); 
				} else {
					Log.i("results = are null","results are null"); 
				}
				//jsonObject = (JSONObject) new JSONTokener(responseBody).nextValue();
				//results = jsonObject.getJSONArray("results");
				
				//Object obj = parser.parse(responseBody);
				//jsonObject=(JSONObject)obj;
				//String query = jsonObject.getString("query");			
				//Log.i("results = ", ""+results + "jsonObject = " + results.toString()); 
			}catch(Exception ex){
				Log.v("results","Exception: " + ex.getMessage());
			}
			
			//JSONArray arr = null;
			
//			try {
//				Object j = jsonObject.get("results");
//				results = (JSONArray)j;
//			}catch(Exception ex){
//				Log.v("TEST","Exception: " + ex.getMessage());
//			}
			int num_results = resultsJSONArray.length();
			for(int k=0; k< num_results; k++) {
				Tweet tweet;
				try {
					tweet = new Tweet (
							resultsJSONArray.getJSONObject(k).getString("from_user"),
							resultsJSONArray.getJSONObject(k).getString("text"),
							resultsJSONArray.getJSONObject(k).getString("profile_image_url")
						);

				tweets.add(tweet);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			for(Object t : results) {
//				Tweet tweet = new Tweet(
//						((JSONObject)t).get("from_user").toString(),
//						((JSONObject)t).get("text").toString(),
//						((JSONObject)t).get("profile_image_url").toString()
//						);
//				tweets.add(tweet);
//			}
			
			return tweets;
		}
		
		public class Tweet {
			public String username;
			public String message;
			public String image_url;
			
			public Tweet(String username, String message, String url) {
				this.username = username;
				this.message = message;
				this.image_url = url;
			}
		}
		
		public void onHomeClick(View v) {
			   	Log.i("inside onHomeClick","inside onHomeClick");
			       UIUtils.goHome(this);
		}

		public void onSearchClick(View v) {
			   	Log.i("inside onSearchClick","inside onSearchClick");
			       UIUtils.goSearch(this);
		}

		@Override
		protected void onResume() {

			super.onResume();
		}
		
}
