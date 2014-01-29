package com.example.activities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xml.sax.Parser;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseInteraction {
	

	private DatabaseManager mHelper;
	private SQLiteDatabase mDatabase;
	private int currentRouteID = 1 ;
	
	
	 // In welchem Kontext???
	public DatabaseInteraction(Context context) {
		
		mHelper = new DatabaseManager (context);	
	}


	public void closeDBsession () {
		
		mDatabase.close();
	}
	
	
	//Irgendwie noch abfangen ob es eine neue Route ist!
	public boolean addNewRoutePoint (String picture, double latitude, double longitude, String name) {
		
	
	   
       mDatabase = mHelper.getWritableDatabase();
		
		
       //Getting the current timestamp ODER SOLL DIREKT BEIM AUFNEHMEN EINER GENOMMEN WERDEN?
		int time = (int) (System.currentTimeMillis());
		Timestamp tsTemp = new Timestamp(time);
		
		
		
		
		ContentValues route_values = new ContentValues();
	    
		//Zum Testen erstmal alle dem gleichen Record
		route_values.put("_id", currentRouteID);
		route_values.put("timestamp", tsTemp.toString()); // inserting an int
	    route_values.put("picture", picture); // inserting an int
	    route_values.put("latitude", latitude); // inserting a string
	    route_values.put("longitude", longitude);
		
		
		
	
		
		// CurrentRoute greater than the latest one in the DB
		// --> Completely new route!
		if (  currentRouteID > getIDlastRoute() ) {
			
			
			mDatabase.insert("route_points", null , route_values);
			
			//For a completely new route, also the general information, like the name has to be stored		 
			 ContentValues route_info = new ContentValues();
			 route_info.put("_id", currentRouteID);
			 route_info.put("name", name);
			 route_info.put("date", tsTemp.getDate());
			 mDatabase.insert("route_info", null, route_info);
			
			
		} else {
			
		    mDatabase.insert("route_points", null , route_values);
			
		}
		
		
		return true;
		
	}



	private int getIDlastRoute() {
		
		Cursor db_cursor;
		String[] db_columns = {"_ID"};
		int lastRecord;
		
		mDatabase = mHelper.getWritableDatabase();
		
		//Check if an entry exists. Ordered descending to get the latest route
		db_cursor = mDatabase.query("route_points", //table
				db_columns ,  //which column
				null , // select options
				null,  // Using ? in the select options can be replaced here as an array
				null,   // Group by
				null, //Having
				"_id DESC");// order by
		
		
	     if (db_cursor.getCount() != 0 ) {
	    	    
	    	     db_cursor.moveToFirst();
				
				lastRecord =  db_cursor.getInt(db_cursor.getColumnIndex("_id"));
				
			//It is the first time a record is saved	
			} else  {
			
			 lastRecord =  -1;
			
	    	}
	     
	     db_cursor.close();
		return lastRecord;
	}
		

	
	public List<ArrayList<String>> getAllroutesGrouped () {
		List<ArrayList<String>> super2dArray = new ArrayList<ArrayList<String>>();
		
		List<RoutePoint> allRoutes  = new ArrayList <RoutePoint> () ;
		Cursor db_cursor;
		
		
		mDatabase = mHelper.getWritableDatabase();
		
		db_cursor = mDatabase.query("route_points INNER JOIN route_info ON route_points._id = route_info._id", //table
				new String [] { "route_points._id, route_info.name" },  //which column
				null , // select options
				null,  // Using ? in the select options can be replaced here as an array
				"route_points._id, route_info.name",   // Group by ID --> Only the whole routes
				null, //Having
				null);// order by
		

	/*	db_cursor = mDatabase.query("routes", //table
				null ,  //which column
				null , // select options
				null,  // Using ? in the select options can be replaced here as an array
				"_id",   // Group by ID --> Only the whole routes
				null, //Having
				null);// order by
*/		
		
		
//		while (db_cursor.moveToNext()) {
//			
//			// Getting each field
//			int cursor_id = db_cursor.getInt(db_cursor.getColumnIndex("_id"));
//			String cursor_picture = db_cursor.getString(db_cursor.getColumnIndex("picture"));
//			int cursor_longitude = db_cursor.getInt(db_cursor.getColumnIndex("longitude"));
//			int cursor_latitude = db_cursor.getInt(db_cursor.getColumnIndex("latitude"));
//			Timestamp cursor_time = Timestamp.valueOf(db_cursor.getString(db_cursor.getColumnIndex("timestamp")));
//			
//			
//		RoutePoint route_point = 	new RoutePoint(cursor_id, 
//					cursor_time,  //Timestamp class helps us to get the value as timestamp
//					cursor_picture,
//					cursor_latitude,
//					cursor_longitude);
//			
//			allRoutes.add(route_point);
//				
//					
//		}
		
		
		
		while (db_cursor.moveToNext()) {
			
			// Getting each field
			String cursor_id = db_cursor.getString(db_cursor.getColumnIndex("_id"));
			String name = db_cursor.getString(db_cursor.getColumnIndex("name"));
			
			
			
	
			
			ArrayList<String> test = new ArrayList <String> () ;
			test.add(cursor_id);
			test.add(name);
			
			super2dArray.add( test );
			
				
					
		}
		
	     db_cursor.close();

		return super2dArray;
		
		
	}
	
	
	// "1 - n routpoints can be selected"
	//Select needs the values as string and not int
	public List<RoutePoint> getSpecificRoute (String[] ids) {
		
		
		List<RoutePoint> allRoutes  = new ArrayList <RoutePoint> () ;
		Cursor db_cursor;
		
		mDatabase = mHelper.getWritableDatabase();
		

		db_cursor = mDatabase.query("route_points", //table
				null ,  //which column
				"_id = ?" , // select options
				ids,  // Using ? in the select options can be replaced here as an array
				null,   // Group by ID --> Only the whole routes
				null, //Having
				null);// order by
		
		
		
		while (db_cursor.moveToNext()) {
			
			// Getting each field
						int cursor_id = db_cursor.getInt(db_cursor.getColumnIndex("_id"));
						String cursor_picture = db_cursor.getString(db_cursor.getColumnIndex("picture"));
						double cursor_longitude = db_cursor.getDouble(db_cursor.getColumnIndex("longitude"));
						double cursor_latitude = db_cursor.getDouble(db_cursor.getColumnIndex("latitude"));
						Timestamp cursor_time = Timestamp.valueOf(db_cursor.getString(db_cursor.getColumnIndex("timestamp")));
			
			
			RoutePoint route_point = 	new RoutePoint(cursor_id, 
					cursor_time,  //Timestamp class helps us to get the value as timestamp
					cursor_picture,
					cursor_latitude,
					cursor_longitude);
			
			allRoutes.add(route_point);
				
					
		}
		
	     db_cursor.close();
		return allRoutes;
		
		
	}
	
	
	//TEST
	public List<String> getAllRoutesSpecificColumn ( ) {
		
		
		
		List<String> allRoutes  = new ArrayList <String> () ;
		Cursor db_cursor;
		
		mDatabase = mHelper.getWritableDatabase();
		

		db_cursor = mDatabase.query("route_points", //table
				null ,  //which column
				null , // select options
				null,  // Using ? in the select options can be replaced here as an array
				null,   // Group by ID --> Only the whole routes
				null, //Having
				null);// order by
		
		
		
		while (db_cursor.moveToNext()) {
			
			// Getting each field
			String cursor_picture = db_cursor.getString(db_cursor.getColumnIndex("picture"));
			
			allRoutes.add(cursor_picture );
				
					
		}
		
		db_cursor.close();
		
		return allRoutes;
	}
	
	
	
	public List<RoutePoint> getRoutePoint (int id){
		
		 List<RoutePoint> route = new ArrayList <RoutePoint> () ;
		 
		 Cursor db_cursor;
			
			mDatabase = mHelper.getWritableDatabase();
			
			

			db_cursor = mDatabase.query("route_points", //table
					null ,  //which column
					"_id = ?" , // select options
					new String [] { String.valueOf(id)},  // Using ? in the select options can be replaced here as an array
					null,   // Group by ID --> Only the whole routes
					null, //Having
					null);// order by
			
			
			
			while (db_cursor.moveToNext()) {
				
				// Getting each field
							int cursor_id = db_cursor.getInt(db_cursor.getColumnIndex("_id"));
							String cursor_picture = db_cursor.getString(db_cursor.getColumnIndex("picture"));
							int cursor_longitude = db_cursor.getInt(db_cursor.getColumnIndex("longitude"));
							int cursor_latitude = db_cursor.getInt(db_cursor.getColumnIndex("latitude"));
							Timestamp cursor_time = Timestamp.valueOf(db_cursor.getString(db_cursor.getColumnIndex("timestamp")));
				
				
				RoutePoint route_point = 	new RoutePoint(cursor_id, 
						cursor_time,  //Timestamp class helps us to get the value as timestamp
						cursor_picture,
						cursor_latitude,
						cursor_longitude);
				
				route.add(route_point);
					
						
			}
			
		     db_cursor.close();
		
		
		return  route;
	}


	
	public int getCurrentRouteID() {
		return currentRouteID;
	}

	
    //EVERY TIME A NEW ROUTE IS STARTET THIS METHOD HAS TO BE CALLED
	public void registerNewRouteID() {
		
		// -1 means first route ever tracked
		if ( getIDlastRoute() != -1 ) {
		currentRouteID = getIDlastRoute() + 1;
		
		}else {
			
			currentRouteID = 1;
			
		
		}
				
	}
	
	
	
	
	
	
	
	

}
