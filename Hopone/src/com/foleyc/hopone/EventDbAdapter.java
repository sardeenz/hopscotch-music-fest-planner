/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.foleyc.hopone;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.OverlayItem;

/**
 * Simple events database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all events as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class EventDbAdapter {

    public static final String EVENTDATE = "date";
    //public static final String EVENTTIME = "time";
    public static final String KEY_ROWID = "_id";
    public static final String EVENT = "event";
    public static final String LOCATION = "location";
    public static final String ADDRESS1 = "address1";
    public static final String ADDRESS2 = "address2";
    public static final String PHONE = "phone";
    public static final String DESCRIPTION = "description";
    public static final String CHECKED = "checked";
    public static final String FILENAME = "filename";
    public static final String ARTIST = "artist";
    public static final String TITLE = "title";
    public static final String VISUAL = "visual";
    public static final String VISUALBIG = "visualbig";
    public static final String VERSIONID = "versionid";
    public static final String LOCALVERSIONID = "localversionid";
    
    private static final String TAG = "EventDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table events (_id integer primary key autoincrement, " +
        		"date datetime not null, " +
        		//"time text not null, " +
        		"event text not null," +
        		"location text not null, " +
        		"address1 text not null, " +
        		"address2 text not null, " +
        		"phone text not null, " +
        		"description text not null, " +
        		"checked text not null)" +
        		";";
     private static final String DATABASE_CREATE1 =
    	"create table songs (_id integer primary key autoincrement, " +
				"filename text not null, " +
				"artist text not null, " +
				"title text not null," +
				"visual text not null," +
				"visualbig text not null)" +
				";";
     
     private static final String DATABASE_CREATE2 =
     	"create table version (versionid text," +
     	"localversionid text)" +
 				";";

    private static final String DATABASE_NAME = "eventdb";
    private static final String DATABASE_TABLE = "events";
    private static final String DATABASE_TABLE1 = "songs";
    private static final String DATABASE_TABLE2 = "version";
    private static final int DATABASE_VERSION = 2;
	private static final String DB_PATH = "/data/data/com.foleyc.hopone/databases/";
	

    private final Context mCtx;

    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

//            db.execSQL(DATABASE_CREATE);
//            db.execSQL(DATABASE_CREATE1);
//            db.execSQL(DATABASE_CREATE2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS events");
            db.execSQL("DROP TABLE IF EXISTS songs");            
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public EventDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the events database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public EventDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public EventDbAdapter openRead() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    public SQLiteDatabase getDb() {
        return this.mDb;
     }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed
     */
    public long createEvents(String date, String event, String location, String address1, String address2,
    		String phone, String description, String checked) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(EVENTDATE, date);
        //initialValues.put(EVENTTIME, time);
        initialValues.put(EVENT, event);        
        initialValues.put(LOCATION, location);
        initialValues.put(ADDRESS1, address1);
        initialValues.put(ADDRESS2, address2);
        initialValues.put(PHONE, phone);
        initialValues.put(DESCRIPTION, description);
        initialValues.put(CHECKED, "0");

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public long createSongs(String filename, String artist, String title, String visual, String visualbig) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(FILENAME, filename);
        initialValues.put(ARTIST, artist);
        initialValues.put(TITLE, title);        
        initialValues.put(VISUAL, visual);
        initialValues.put(VISUALBIG, visualbig);
 
        return mDb.insert(DATABASE_TABLE1, null, initialValues);
    }
    
    public long createVersion(String versionid, String localversionid) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(VERSIONID, versionid);
        initialValues.put(LOCALVERSIONID, localversionid);
        return mDb.insert(DATABASE_TABLE2, null, initialValues);
    }
    
    public long updateLocalVersion(String localversionid) {
        ContentValues args = new ContentValues();
        //args.put(VERSIONID, versionid);
        args.put(LOCALVERSIONID, localversionid);
        return mDb.update(DATABASE_TABLE2, args, null,null);
    }
    
    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteAll() {
    	mDb.delete(DATABASE_TABLE1, null, null);
        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }

    /**
     * Return a Cursor over the list of all events in the database
     * 
     * @return Cursor over all events
     */
    public Cursor fetchAllEventsByDate(String date) {    	
    	Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, DESCRIPTION, CHECKED},
        		"date LIKE ?" , new String[] {"%"+date.substring(0, 10)+"%"}, null, null, "event");    	
   		if (mCursor != null) {
   			mCursor.moveToFirst();
         }
        return mCursor;
    }
    
    /**
     * Return a Cursor over the list of all events in the database
     * 
     * @return Cursor over all events
     */
    public Cursor fetchAllEventsByDateSortTime(String date) {    	
    	Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, DESCRIPTION, CHECKED},
        		"date LIKE ?" , new String[] {"%"+date.substring(0, 10)+"%"}, null, null, "date");
    	//"group_concat(event) as event"
   		if (mCursor != null) {
   			mCursor.moveToFirst();
         }
        return mCursor;
    }
    
    /**
     * Return a Cursor over the list of all events in the database
     * 
     * @return Cursor over all events
     */
    public Cursor fetchAllEventsByQuery(String mQuery) {    	
    	Cursor bandCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, DESCRIPTION, CHECKED},
        		"event LIKE ?" , new String[] {"%"+mQuery+"%"}, null, null, null);
   		if (bandCursor != null) {
   			bandCursor.moveToFirst();
         }
        return bandCursor;
    }
    
    /**
     * Return a Cursor over the list of all events in the database
     * 
     * @return Cursor over all events
     */
    public Cursor fetchAllEvents() {    	
    	Cursor mCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, DESCRIPTION, CHECKED},
        		null, null, null, null, "event");
   		if (mCursor != null) {
   			mCursor.moveToFirst();
         }
        return mCursor;
    }
    
	public Cursor fetchAllVenues() {
    	//final String MY_QUERY = "SELECT distinct location, address1, address2, phone FROM events order by location";
    	//Cursor cur = mDb.rawQuery(MY_QUERY, null); 
    	Cursor cur = mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, "location", "address1", "address2", "phone"}, null, null, "location", null, "location", null);
    	//Cursor cur = mDb.query(true, DATABASE_TABLE, new String[]{"event"}, null, null, null, null, "event", null);
  		
    	if (cur != null) {
  			cur.moveToFirst();  
         }
  		//Log.i("cur.getString(0); = " , cur.getString(0));
  		return cur;
	}
    
    /**
     * Return a Cursor over the list of all events in the database
     * @return Cursor over all events
     */
    public Cursor fetchAllCheckedEventsByDate(String date) {
    	//private static String WHERE = "date=? ";
    	Cursor fCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, DESCRIPTION, CHECKED},
        		"date LIKE ?"+ " and " + "checked=?" , new String[] {"%"+date.substring(0, 10)+"%", "1"}, null, null, "event");    			
   		if (fCursor != null) {
   			fCursor.moveToFirst();
         }

        return fCursor;
    }
    
    /**
     * Return a Cursor over the list of all events in the database
     * @return Cursor over all events
     */
    public Cursor fetchAllCheckedEventsByDateSortByDate(String date) {
    	//private static String WHERE = "date=? ";
    	Cursor fCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, DESCRIPTION, CHECKED},
        		"date LIKE ?"+ " and " + "checked=?" , new String[] {"%"+date.substring(0, 10)+"%", "1"}, null, null, "date");    			
   		if (fCursor != null) {
   			fCursor.moveToFirst();
         }

        return fCursor;
    }
    
    /**
     * Return a Cursor over the list of all events in the database
     * @return Cursor over all events
     */
    public Cursor fetchAllCheckedEvents() {
    	//private static String WHERE = "date=? ";
    	Cursor fCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, DESCRIPTION, CHECKED},
        		"checked=?" , new String[] {"1"}, null, null, "event");
   		if (fCursor != null) {
   			fCursor.moveToFirst();
         }

        return fCursor;
    }
    
    /**
     * Return a Cursor over the list of all events in the database
     * @return Cursor over all events
     */
    public Cursor fetchAllCheckedEventsDateSort() {
    	//private static String WHERE = "date=? ";
    	Cursor fCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, DESCRIPTION, CHECKED},
        		"checked=?" , new String[] {"1"}, null, null, "date");
   		if (fCursor != null) {
   			fCursor.moveToFirst();
         }

        return fCursor;
    }
    
    /**
     * Return a Cursor over the list of all events in the database
     * @return Cursor over all events
     */
    public Cursor fetchEventDetailsCur(String rowid) {
    	Cursor cursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
        		EVENT, LOCATION, ADDRESS1, ADDRESS2, DESCRIPTION, CHECKED},
        		"_id=?", new String[] {rowid}, null, null, null);
   		if (cursor != null) {
   			cursor.moveToFirst();
         }
         return cursor;

    }
    
//    public List<String> fetchEventDetails(String rowid) {
//    	List<String> list = new ArrayList<String>();
//    	//private static String WHERE = "date=? ";
//    	Cursor cursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, EVENTDATE,
//        		EVENTTIME, EVENT, LOCATION, DESCRIPTION, XCORD , YCORD ,CHECKED ,IMAGE ,MEDIA1 ,MEDIA2},
//        		"_id=?", new String[] {rowid}, null, null, null);
//    	if (cursor.moveToFirst()) {
//            do {            
//               list.add(cursor.getString(0));
//               list.add(cursor.getString(1));
//               list.add(cursor.getString(3));
//               list.add(cursor.getString(4));
//               list.add(cursor.getString(5));
//               list.add(cursor.getString(9));
//
//            } while (cursor.moveToNext());
//         }
//         if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//         }
//
//         return list;
//    }
    
//    public List<OverlayItem> selectOverlays(String date) {
//	      List<OverlayItem> list = new ArrayList<OverlayItem>();
//	      Cursor cursor = this.mDb.query(DATABASE_TABLE, new String[] { "date, item, location, xcord, ycord" }, "date=?" , new String[] { date }, null, null, "date desc");
//	      if (cursor.moveToFirst()) {
//	         do {
//	            list.add(new OverlayItem(getPoint(cursor.getDouble(3),cursor.getDouble(4)),	cursor.getString(2), "Location Description")); 
//	         } while (cursor.moveToNext());
//	      }
//	      if (cursor != null && !cursor.isClosed()) {
//	         cursor.close();
//	      }
//	      return list;
//	}
//    
//    private GeoPoint getPoint(double lat, double lon) {
//		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
//	}
    
    /**
     * Return a Cursor over the list of all songs in the database
     * @return Cursor over all songs
     */ 
    public Cursor fetchEventSongsCur(String rowid) {   	
    	final String MY_QUERY = "SELECT b._id, a.artist, a.filename, a.visual, a.visualbig, a.title, b.event, b.location, b.date FROM songs a INNER JOIN events b ON a.artist=b.event WHERE b._id=?";
    	Cursor cur = mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(rowid)});
    	
  		if (cur != null) {
  			cur.moveToFirst();
         }
         return cur;
    }
    
    public Cursor fetchEventImageCur(String rowid) {   	
    	final String MY_QUERY = "SELECT distinct b._id, a.artist, a.visual, a.visualbig, b.event, b.location, b.date FROM songs a INNER JOIN events b ON a.artist=b.event WHERE b._id=?";
    	//Cursor cur = mDb.rawQuery(MY_QUERY, new String[]{String.valueOf(rowid)});
    	Cursor cur = mDb.rawQuery(MY_QUERY, new String[]{rowid});    	  		
  		if (cur != null) {
  			cur.moveToFirst();  
         }
        return cur;
    }
    
    public int selectEventCnt() {
    	int i = 0;
	    Cursor cursor = mDb.rawQuery("SELECT COUNT(*) FROM events", null);	      
	    if(cursor.moveToFirst()) {
            i =  cursor.getInt(0);
        } else {
            i = cursor.getInt(0);
        }
	    cursor.close();
	 return i;   
    }
    
    public String getJSONVersion() {
    	String version = "99";
	    Cursor cursor = mDb.rawQuery("SELECT versionid from version", null);	      
	    if(cursor.moveToFirst()) {
	    	version =  cursor.getString(0);
        } else {
        	version = "99";
        }
	    cursor.close();
	 return version;   
    }
    
    public String getLocalVersion() {
    	String version = "99";
	    Cursor cursor = mDb.rawQuery("SELECT localversionid from version", null);	      
	    if(cursor.moveToFirst()) {
	    	version =  cursor.getString(0);
        } else {
        	version = "99";
        }
	    cursor.close();
	 return version;   
    }
    
    public int selectChk(int rowid) {
    	int i = 0;
    	String _rowid = Integer.toString(rowid);
    	Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { CHECKED },
        		"_id=?" , new String[] { _rowid }, null, null, null);
       	if(cursor.moveToFirst()) { 
            i = cursor.getInt(0); 
        } else {
        	i = cursor.getInt(0);	
        }
        cursor.close();
        return i;
	}


    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateChecked(String rowId, String checked) {
        ContentValues args = new ContentValues();
        args.put(CHECKED, checked);
        Log.i("inside updateChecked", "inside updateChecked, checked =" + checked);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DATABASE_NAME;
    		//checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, 0);
    	}catch(SQLiteException e){
    		Log.i("db not open", "db not open -------++++++++++++++++");
    	}
 
    	if(checkDB != null){
    		Log.i("checkDB != null","checkDB != null");
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    public void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = mCtx.getAssets().open(DATABASE_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DATABASE_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
    	
    	open();
		try {
    		Log.i("copyDataBase", "copying DataBase -------++++++++++++++++");
			copyDataBase();
		} catch (IOException e) {
    		throw new Error("Error copying database");
    	}
 
//    	boolean dbExist = checkDataBase();
// 
//    	if(dbExist){
//    		open();
//    		int cnt = selectEventCnt();
//    		Log.i("DB COunt -------","DB Count = "+cnt);
//    		if (cnt == 171){
//    			
//    			try {
//            		Log.i("copyDataBase", "OLD DB Exists - copying DataBase -------++++++++++++++++");
//        			copyDataBase();
//     
//        		} catch (IOException e) {
//     
//            		throw new Error("Error copying database");
//     
//            	}
//    		}
//    		Log.i("do nothing - database already exist", "do nothing - database already exist -------++++++++++++++++");
//    		//do nothing - database already exist
//    		close();
//    	}else{
// 
//    		//By calling this method and empty database will be created into the default system path
//               //of your application so we are gonna be able to overwrite that database with our database.
//    		//this.getReadableDatabase();
//    		open();
//    		//mDbHelper.getReadableDatabase();
// 
//        	try {
//        		Log.i("copyDataBase", "copying DataBase -------++++++++++++++++");
//    			copyDataBase();
// 
//    		} catch (IOException e) {
// 
//        		throw new Error("Error copying database");
// 
//        	}
//    	}
 
    }

}
