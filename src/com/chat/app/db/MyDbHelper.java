package com.chat.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
	
	public static final String DB_NAME = "mood_db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "mood";
	
	private SQLiteDatabase db;

	public MyDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
     * 获取数据库操作对象
     * @param isWrite 是否可写
     * @return
     */
    public synchronized SQLiteDatabase getDatabase(boolean isWrite) {

        if(db == null || !db.isOpen()) {
            if(isWrite) {
                try {
                    db=getWritableDatabase();
                } catch(Exception e) {
                    // 当数据库不可写时
                    db=getReadableDatabase();
                    return db;
                }
            } else {
                db=getReadableDatabase();
            }
        }
        // } catch (SQLiteException e) {
        // // 当数据库不可写时
        // mDb = getReadableDatabase();
        // }
        return db;
    }
    
    public int delete(String table, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return db.delete(table, whereClause, whereArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        getDatabase(true);
        return db.insertOrThrow(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return db.update(table, values, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        getDatabase(false);
        return db.rawQuery(sql, selectionArgs);
    }

    public void execSQL(String sql) {
        getDatabase(true);
        db.execSQL(sql);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
    // final
        String orderBy) {
        getDatabase(false);
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }


}
