package com.example.chat.Activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FriendsDateBaseHelper 
{
	private FriendsDataBase friendsDB;
	private SQLiteDatabase db;
	private String name;
	
	public void getInstance(Context context,String name)
	{
		friendsDB=new FriendsDataBase(context, "friends.db", null, 1);
		db=friendsDB.getWritableDatabase();
		this.name=name;
	}
	
	public Cursor search()
	{
		Cursor cursor = db.rawQuery("select * from"+ this.name, null);// 执行完会自动关闭吗？
		return cursor;
	}
	
	public boolean search(String str)
	{
		Cursor cursor = db.rawQuery("select fname from"+ this.name, null);
		if(cursor.getCount()==0)
			return false;
		return true;
	}
	
	public void insert(String str )
	{
		db.execSQL("insert into"+this.name+"values(null,?)",
				new String[] {str});
	}
	
	public void delete(String str)
	{
		db.delete(this.name, "fname=?",
				new String[] {str});
	}
	public void deleteAll()
	{
		db.delete(name, null, null);
	}
	
	public void closeDB()
	{
		if (friendsDB != null) 
		{
			friendsDB.close();
		}
	}

}
