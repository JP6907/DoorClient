package com.example.chat.Activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendsDataBase extends SQLiteOpenHelper
{
	private String name;
	public FriendsDataBase(Context context,String name,SQLiteDatabase.CursorFactory cursorFactory,int version)
	{
		super(context, name, cursorFactory, 1);
		this.name=name;
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE" + this.name+"(_id int auto_increment primary key, fname VARCHAR(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("CREATE TABLE" +this.name+"(_id int auto_increment primary key, fname VARCHAR(20))");
	}

}
