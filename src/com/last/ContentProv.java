package com.last;
import java.util.HashMap;

import com.last.*;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class ContentProv extends ContentProvider{

	private DatabaseContent cp_database = new DatabaseContent(getContext());
	
	public static final Uri cp_uri= Uri.parse("content://edu.buffalo.cse.cse486_586.provider/CP_Database4");
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
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
		SQLiteDatabase db = cp_database.getWritableDatabase();
		long r_id = db.insert(DatabaseContent.TABLE_NAME, null, values);
		if(r_id>0){
			Uri new_uri = ContentUris.withAppendedId(cp_uri, r_id);
			getContext().getContentResolver().notifyChange(new_uri, null);
			return new_uri;
		}
		throw new IllegalArgumentException("Unknown " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		cp_database = new DatabaseContent(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		SQLiteDatabase db = cp_database.getReadableDatabase();
		query.setTables(DatabaseContent.TABLE_NAME);
		query.setProjectionMap(hash_map);
		Cursor qc = query.query(db, projection, selection, selectionArgs, null, null, null);
		qc.setNotificationUri(getContext().getContentResolver(), uri);
		return qc;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private static HashMap<String, String> hash_map;
	static {
		hash_map = new HashMap<String, String>();
		hash_map.put(DatabaseContent.COLUMN_KEY,DatabaseContent.COLUMN_KEY);
		hash_map.put(DatabaseContent.COLUMN_VAL,DatabaseContent.COLUMN_VAL);
	}

}
