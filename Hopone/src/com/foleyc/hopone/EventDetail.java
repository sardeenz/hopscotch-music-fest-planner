package com.foleyc.hopone;


import java.io.IOException;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EventDetail extends ListActivity {
	private static final int UPDATE_FREQUENCY = 500;
	private static final int STEP_VALUE = 4000;
	
	private MediaCursorAdapter mediaAdapter = null;
	private TextView selectedFile = null;
	private SeekBar seekbar = null;
	private MediaPlayer player = null;
	private ImageButton playButton = null;
	private ImageButton prevButton = null;
	private ImageButton nextButton = null;
	private String idTag = "";
	
	private boolean isStarted = true;
	private String currentFile = "";
	private boolean isMoveingSeekBar = false;
	
	private final Handler handler = new Handler();
	private EventDbAdapter mDbHelper;
	Cursor image = null;
	
	private final Runnable updatePositionRunnable = new Runnable() {
		public void run() {
			updatePosition();
		}
	};
		
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
   	   setContentView(R.layout.music_main);

       /*
        * Start MusicListActivity
        */       
       selectedFile = (TextView)findViewById(R.id.selectedfile);
       seekbar = (SeekBar)findViewById(R.id.seekbar);
       playButton = (ImageButton)findViewById(R.id.play);
       prevButton = (ImageButton)findViewById(R.id.prev);
       nextButton = (ImageButton)findViewById(R.id.next);
       
       player = new MediaPlayer();
       
       player.setOnCompletionListener(onCompletion);
       player.setOnErrorListener(onError);
       seekbar.setOnSeekBarChangeListener(seekBarChanged);
       
       idTag = getIntent().getStringExtra("_id");
       //String idArtist = getIntent().getStringExtra("event");
       mDbHelper = new EventDbAdapter(this);	              
   }
   
   public void onHomeClick(View v) {
   	Log.i("inside onHomeClick","inside onHomeClick");
       UIUtils.goHome(this);
   }

   public void onSearchClick(View v) {
   	Log.i("inside onSearchClick","inside onSearchClick");
       UIUtils.goSearch(this);
   }
   
   public void drawUI(){ 	  
	   
	    // separate lists into sections helper -- usage: adapter.addsection("name", adapter)
	    SeparatedListAdapter adapter = new SeparatedListAdapter(this);
	   
	    // get all the event data for one event/row
	    Cursor details = mDbHelper.fetchEventDetailsCur(idTag);
	    startManagingCursor(details);
	    
	    // get all the song data for an event
	    Cursor songs = mDbHelper.fetchEventSongsCur(idTag);
	    startManagingCursor(songs);
	    
	    // get all the image data for an event
	    image = mDbHelper.fetchEventImageCur(idTag);
	    startManagingCursor(image);
	    
	    // image and artist section
	    String[] imageFields = new String[] {"visual", "event", "location", "date"};
	    int[] imageViews = new int[] {R.id.image, R.id.event, R.id.location, R.id.time};       
	    //String[] imageFields = new String[] {"visual", "event"};
        //int[] imageViews = new int[] {R.id.image, R.id.event};       
        adapter.addSection("Artist", new CustomImageAdapter(this, R.layout.list_image, image, imageFields, imageViews));   

        // mp3 streaming song section
   	    mediaAdapter = new MediaCursorAdapter(this, R.layout.list_musicitem, songs);
        adapter.addSection("Listen", mediaAdapter);
       
        // mp3 button listeners
        playButton.setOnClickListener(onButtonClick);
        nextButton.setOnClickListener(onButtonClick);
        prevButton.setOnClickListener(onButtonClick);
       
	    // Add CheckBox Date Section
    	String[] displayFields = new String[] {"date", "event", "location"};
        int[] displayViews = new int[] {R.id.time, R.id.event, R.id.location};
	   	CheckboxDetailCursorAdapter cboxAdapter = new CheckboxDetailCursorAdapter(this, R.layout.check_list_detail, details, displayFields, displayViews);	   	
	   	adapter.addSection("Schedule", cboxAdapter);
	   	
		// Add Venue Section
//    	String[] displayLocFields = new String[] {"location", "address1", "address2"};
//        int[] displayLocViews = new int[] {R.id.location, R.id.address1, R.id.address2};
//	   	SimpleDetailAdapter simpleDetailAdapter = new SimpleDetailAdapter(this, R.layout.list_complex, details, displayLocFields, displayLocViews);	   	
//	   	adapter.addSection("Venue", simpleDetailAdapter);
	   	
	   	// Add to Google Calendar
	   	// ...
	   	
	   	// Add Bio Section
	   	//adapter.addSection("Bio", new DetailCursorAdapter(this, listBio));
	   	String[] displayBioFields = new String[] {"description"};
        int[] displayBioViews = new int[] {R.id.description};
        SimpleBioAdapter simpleBioAdapter = new SimpleBioAdapter(this, R.layout.detailadapter, details, displayBioFields, displayBioViews);	   	
	   	adapter.addSection("Bio", simpleBioAdapter);	   		   	
	   	
		ListView lv = (ListView) findViewById(android.R.id.list);
		lv.setAdapter(adapter);		
	   
   }
      
   @Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		
		currentFile = (String) view.getTag();
		
		startPlay(currentFile);
	}
   
   public static boolean nullCheck(String dbVal) {				
		return (dbVal == null || dbVal.equals(""))? true:false; 				
	}
   
	private void startPlay(String file) {
		//Log.i("Selected: ", file);
		
		//TODO: find just mp3 part of the url string via substring or something
		//selectedFile.setText(file.substring(file.length()-10));
		String fileName = file.replace("%20", " ");
		fileName = fileName.replace("http://hopscotchmusicfest.com/media/songs2011/"," ");
		selectedFile.setText(fileName);				
		seekbar.setProgress(0);				
		player.stop();
		player.reset();
		
		try {
			player.setDataSource(file);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		seekbar.setMax(player.getDuration());
		playButton.setImageResource(android.R.drawable.ic_media_pause);
		
		updatePosition();
		
		isStarted = true;
	}
	
	private void stopPlay() {
		player.stop();
		player.reset();
		playButton.setImageResource(android.R.drawable.ic_media_play);
		handler.removeCallbacks(updatePositionRunnable);
		seekbar.setProgress(0);		
		isStarted = false;
	}
	
	private void updatePosition(){
		handler.removeCallbacks(updatePositionRunnable);
		
		seekbar.setProgress(player.getCurrentPosition());
		
		handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
	}
   
	private class MediaCursorAdapter extends SimpleCursorAdapter{

		public MediaCursorAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c, 
				new String[] { "filename" },
				new int[] { R.id.media1 });
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			//TextView title = (TextView)view.findViewById(R.id.title);
			TextView name = (TextView)view.findViewById(R.id.media1);
			//TextView duration = (TextView)view.findViewById(R.id.duration);
			if (CheckboxCursorAdapter.nullCheck(cursor.getString(cursor.getColumnIndex("title")))) {
				name.setText("No songs available");
			} else {
				name.setText(cursor.getString(cursor.getColumnIndex("title")));		
			}	
			//name.setText(cursor.getString(cursor.getColumnIndex("title")));
			
//			title.setText(cursor.getString(
//					cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));
//
//			long durationInMs = Long.parseLong(cursor.getString(
//					cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)));
//			
//			double durationInMin = ((double)durationInMs/1000.0)/60.0;
//			
//			durationInMin = new BigDecimal(Double.toString(durationInMin)).setScale(2, BigDecimal.ROUND_UP).doubleValue(); 
//
//			duration.setText("" + durationInMin);
			
			//view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));
			view.setTag(cursor.getString(cursor.getColumnIndex("filename")));
			// Added for song name display in player
			//songName = cursor.getString(cursor.getColumnIndex("title"));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.list_musicitem, parent, false);
			
			bindView(v, context, cursor);
			
			return v;
		}
    }
	
	private View.OnClickListener onButtonClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case R.id.play:
				{
					if(player.isPlaying())
					{
						handler.removeCallbacks(updatePositionRunnable);
						player.pause();
						playButton.setImageResource(android.R.drawable.ic_media_play);
					}
					else
					{
						if(isStarted)
						{
							player.start();
							playButton.setImageResource(android.R.drawable.ic_media_pause);
							
							updatePosition();
						}
						else
						{
							startPlay(currentFile);
						}
					}
					
					break;
				}
				case R.id.next:
				{
					int seekto = player.getCurrentPosition() + STEP_VALUE;
					
					if(seekto > player.getDuration())
						seekto = player.getDuration();
					
					player.pause();
					player.seekTo(seekto);
					player.start();
					
					break;
				}
				case R.id.prev:
				{
					int seekto = player.getCurrentPosition() - STEP_VALUE;
					
					if(seekto < 0)
						seekto = 0;
					
					player.pause();
					player.seekTo(seekto);
					player.start();
					
					break;
				}
			}
		}
	};
	
	private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			stopPlay();
		}
	};
	
	private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// returning false will call the OnCompletionListener
			return false;
		}
	};
	
	private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			isMoveingSeekBar = false;
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			isMoveingSeekBar = true;
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
			if(isMoveingSeekBar)
			{
				player.seekTo(progress);
			
				Log.i("OnSeekBarChangeListener","onProgressChanged");
			}
		}
	};

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("EventDetail onResume!!!!!!!!!!!!!!!!!!!!!!!!","onResume");
	    mDbHelper.open();
	    drawUI();
    }
    
	@Override
	protected void onDestroy() {		
		Log.i("EventDetail onDestroy!!!!!!!!!!!!!!!!!!!!!!!!","onDestroy");
		handler.removeCallbacks(updatePositionRunnable);
		player.stop();
		player.reset();
		player.release();
		player = null;
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		Log.i("EventDetail onPause!!!!!!!!!!!!!!!!!!!!!!!!","onPause");
//		handler.removeCallbacks(updatePositionRunnable);
//		player.stop();
//		player.reset();
//		player.release();
//		player = null;
		super.onPause();
	    //mDbHelper.close();
	    //TODO: If you get a phone call while music is playing will adding player.stop here stop it?  Thor found a bug.
	}
	
    /** Handle "image click" action. */
    public void onImageClick(View v) {
    	Log.i("image clicked","image clicked");
    	//Cursor c = mDbHelper.fetchEventImageCur(idTag);
	    //startManagingCursor(c);
	    ImageView iv = (ImageView)v.findViewById(R.id.image);
		//Bitmap bmImage = null;
		String imageUrl = (image.getString(image.getColumnIndex("visualbig")));	
		//iv.setImageBitmap(bmImage);
		ImageDownloader imgDl = new ImageDownloader();
		imgDl.download(imageUrl, iv);
    	//CustomImageAdapter ci = new CustomImageAdapter();
    	//ci.onImageClick(v);
    }
	
}