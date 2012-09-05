package com.foleyc.hopone;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader {

    public void download(String url, ImageView imageView) {
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
            //Log.i("String url, ImageView imageView", url);
            task.execute(url);
        }
    }

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
	    private String url;
	    private final WeakReference<ImageView> imageViewReference;
	
	    public BitmapDownloaderTask(ImageView imageView) {
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }
	
	    @Override
	    // Actual download method, run in the task thread
	    protected Bitmap doInBackground(String... urls) {
	         // params comes from the execute() call: params[0] is the url.
	    	Bitmap bmImage = null;
			
	       	try {
				URL aURL = new URL(urls[0]); 
				URLConnection conn = aURL.openConnection();
				conn.setUseCaches(true);
				conn.connect(); 				
				InputStream is = conn.getInputStream(); 
				bmImage = BitmapFactory.decodeStream(is); 
				is.close();			   	
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//iv.setImageBitmap(bmImage);
			return bmImage;
	         //return downloadBitmap(params[0]);
	    }
	
	    @Override
	    // Once the image is downloaded, associates it to the imageView
	    protected void onPostExecute(Bitmap bitmap) {
	        if (isCancelled()) {
	            bitmap = null;
	        }
	
	        if (imageViewReference != null) {
	        	Log.i("imageViewReference is not null","imageViewReference is not null");
	            ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}


