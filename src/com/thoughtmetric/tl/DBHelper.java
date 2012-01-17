/*
 * Copyright 2010, 2011 Ali Piccioni
 *
 * This program is distributed under the terms of the GNU General Public License
 *
 *  This file is part of Team Liquid Android App.
 *
 *  Team Liquid Android App is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Team Liquid Android App is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Team Liquid Android App.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.thoughtmetric.tl;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper {
	private SQLiteDatabase db;
	private OpenHelper helper;
	private Context context;
	private static String TAG = "DBHelper";
	
	public DBHelper(Context context) {
		this.context = context;
		helper = new OpenHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public void close(){
		helper.close();
		db.close();
	}
	
	public void insertUser(String username, String password){
		deleteUser();
		String sql = String.format("INSERT INTO user VALUES (\"%s\", \"%s\", 0)", username, password);
		db.execSQL(sql);
	}
	
	public void deleteUser(){
		String sql;
		sql = "DELETE FROM user";
		db.execSQL(sql);
	}
	
	public void validateUser(){
		setUserValidStatus(1);
	}
	
	public void invalidateUser(){
		setUserValidStatus(0);
	}
	
	private void setUserValidStatus(int status){
		String sql = String.format("UPDATE user SET valid=%d", status);
		db.execSQL(sql);
	}
	
	public Cursor getUser(){
		String sql = "SELECT * FROM user";
		return db.rawQuery(sql, null);
	}
	
	public void insertForum(String name, String url){
		String sql = String.format("INSERT INTO forums VALUES (NULL, \"%s\", \"%s\")", name, url);
		db.execSQL(sql);
	}
	
	
	public Cursor getForums(){
		String sql = "SELECT * FROM forums";
		return db.rawQuery(sql, null);
	}
	
	private class OpenHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "teamliquid";
		private static final int DATABASE_VERSION = 5;

		private static final String CREATE_FORUMS_TABLE = "CREATE TABLE forums (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, url TEXT);";
		private static final String CREATE_USER_TABLE = "CREATE TABLE user (username TEXT, password TEXT, valid INTEGER)";
		
		public OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_FORUMS_TABLE);
			db.execSQL(CREATE_USER_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String sql = "DELETE FROM forums";
			if (oldVersion <= 2){
				db.execSQL(CREATE_USER_TABLE);
			}
			db.execSQL(sql);
		}
	}

	public void clear() {
		String sql = "DELETE FROM forums"; 
		db.execSQL(sql);
	}
}
