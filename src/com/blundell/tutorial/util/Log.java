package com.blundell.tutorial.util;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Message;

/**
 * A simple Log wrapper so that we have more control</br>
 * Here we can turn off logging for a live build or do other custom logging things
 * @author paul.blundell
 */
public class Log {

	private static final boolean live = false;
	
	private static final String TAG = "UserFeedYouTubeTut";

	public static void d(String msg){
		d(msg, null);
	}
	
	public static void d(String msg, Throwable e){
		if(!live)
			android.util.Log.d(TAG, Thread.currentThread().getName() +"| "+ msg, e);
	}
	
	public static void i(String msg){
		i(msg, null);
	}
	
	public static void i(String msg, Throwable e){
		if(!live)
			android.util.Log.i(TAG, Thread.currentThread().getName() +"| "+ msg, e);
	}
	
	public static void e(String msg){
		e(msg, null);
	}
	
	public static void e(String msg, Throwable e){
		if(!live)
			android.util.Log.e(TAG, Thread.currentThread().getName() +"| "+ msg, e);
	}

	public static String identifyMessage(Resources res, Message msg) {
		try{ 
			return res.getResourceEntryName(msg.what); 
		} 
		catch(NotFoundException ignore){ 
			return "not found";
		}
	}	
}