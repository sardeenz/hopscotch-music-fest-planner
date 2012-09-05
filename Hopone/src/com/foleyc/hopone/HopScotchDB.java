package com.foleyc.hopone;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.foleyc.hopone.EventDbAdapter.DatabaseHelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HopScotchDB extends SQLiteOpenHelper {
			
	    private static final String DEBUG_TAG = "HopScotchDB";
	    private static final int DB_VERSION = 1;
	    private static final String DB_NAME = "eventdb";
	    private static final String DB_PATH = "/data/data/com.foleyc.hopone/databases/";
	    //private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;
	    public static final String ID = "_id";
	    
	    public static final String COL_TITLE = "title";
	    public static final String COL_URL = "url";
	    
	    public static final String TABLE_EVENTS = "events";
	    public static final String TABLE_SONGS = "songs";	    
	    public static final String EVENTDATE = "date";
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
	    private final Context mCtx;
	    
	    public HopScotchDB(Context context) {
	        super(context, DB_NAME, null, DB_VERSION);
	        this.mCtx = context;
	    }
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	db.execSQL(DB_SCHEMA_EVENTS);
	    	db.execSQL(DB_SCHEMA_SONGS);
	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    }
	    
	    /**
	     * Database creation sql statement
	     */
	    private static final String DATABASE_CREATE_EVENTS =
	        "create table "+ TABLE_EVENTS +" (_id integer primary key autoincrement, " +
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
	     private static final String DATABASE_CREATE_SONGS =
	    	"create table "+ TABLE_SONGS + " (_id integer primary key autoincrement, " +
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

	     private static final String DB_SCHEMA_EVENTS = DATABASE_CREATE_EVENTS;
	     private static final String DB_SCHEMA_SONGS =  DATABASE_CREATE_SONGS;
	     
	     /**
	      * Creates a empty database on the system and rewrites it with your own database.
	      * */
	     public void createDataBase() throws IOException {
	  
	     	boolean dbExist = checkDataBase();
	  
	     	if(dbExist){
//	     		open();
//	     		int cnt = selectEventCnt();
//	     		if (cnt == 125){	     			
//	     			try {
//	             		Log.i("copyDataBase", "OLD DB Exists - copying DataBase -------++++++++++++++++");
//	         			copyDataBase();	      
//	         		} catch (IOException e) {	      
//	             		throw new Error("Error copying database");	      
//	             	}
//	     		}
	     		Log.i("do nothing - database already exist", "do nothing - database already exist -------++++++++++++++++");
	     		//do nothing - database already exist
//	     		close();
	     	}else{
	     		//open();
	     		Log.i("open()", "Do we need to call open() here?");
	         	try {
	         		Log.i("copyDataBase", "copying DataBase -------++++++++++++++++");
	     			copyDataBase();
	  
	     		} catch (IOException e) {
	  
	         		throw new Error("Error copying database");
	  
	         	}
	     	}
	  
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
//	     public HopScotchDB open() throws SQLException {
//	         mDbHelper = new DatabaseHelper(mCtx);
//	         mDb = mDbHelper.getWritableDatabase();
//	         return this;
//	     }
//	     
//	     public HopScotchDB openRead() throws SQLException {
//	         mDbHelper = new DatabaseHelper(mCtx);
//	         mDb = mDbHelper.getReadableDatabase();
//	         return this;
//	     }
//
//	     public void close() {
//	         mDbHelper.close();
//	     }
	     
	     public SQLiteDatabase getDb() {
	         return this.mDb;
	      }
	     
	     /**
	      * Check if the database already exist to avoid re-copying the file each time you open the application.
	      * @return true if it exists, false if it doesn't
	      */
	     public boolean checkDataBase(){
	  
	     	SQLiteDatabase checkDB = null;
	  
	     	try{
	     		String myPath = DB_PATH + DB_NAME;
	     		//checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	     		checkDB = SQLiteDatabase.openDatabase(myPath, null, 0);
	  
	     	}catch(SQLiteException e){	  
	     		Log.i("db not open", "db not open -------++++++++++++++++");
	     	}
	  
	     	if(checkDB != null){	  
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
	     	InputStream myInput = mCtx.getAssets().open(DB_NAME);
	  
	     	// Path to the just created empty db
	     	String outFileName = DB_PATH + DB_NAME;
	  
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

	     
}
