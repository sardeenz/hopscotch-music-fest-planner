package com.foleyc.contentprovider;

import com.foleyc.hopone.HopScotchDB;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class HopscotchContentProvider extends ContentProvider {

	private HopScotchDB mDB;
	
	private static final String AUTHORITY = "com.foleyc.contentprovider.HopscotchContentProvider";
	public static final int EVENTS = 165;
	public static final int EVENT_ID = 330;
	 
	private static final String EVENTS_BASE_PATH = "events";
	private static final String SONGS_BASE_PATH = "songs";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	        + "/" + EVENTS_BASE_PATH);
	 
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	        + "/hs-events";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	        + "/hs-events";
	
	private static final UriMatcher sURIMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, EVENTS_BASE_PATH, EVENTS);
	    sURIMatcher.addURI(AUTHORITY, EVENTS_BASE_PATH + "/#", EVENT_ID);
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		mDB = new HopScotchDB(getContext());
        return true;		
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	    queryBuilder.setTables(HopScotchDB.TABLE_EVENTS);
	 
	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    case EVENT_ID:
	        queryBuilder.appendWhere(HopScotchDB.ID + "="
	                + uri.getLastPathSegment());
	        break;
	    case EVENTS:
	        // no filter
	        break;
	    default:
	        throw new IllegalArgumentException("Unknown URI");
	    }
	 
	    Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
	            projection, selection, selectionArgs, null, null, sortOrder);
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);
	    return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
