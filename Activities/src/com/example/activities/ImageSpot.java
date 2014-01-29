package com.example.activities;

import java.text.SimpleDateFormat;
import android.net.Uri;

public class ImageSpot {
	
	private double longitude;
	private double latitude;
	private String imgUri;
	private String timestamp;
	
    public ImageSpot(double longitude, double latitude, String imgUri, String timestamp) {
       this.longitude = longitude;
       this.latitude = latitude;
       this.imgUri = imgUri;
       this.timestamp = timestamp;
    }



	public double getLongitude() {
		return longitude;
	}



	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}



	public double getLatitude() {
		return latitude;
	}



	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}



	public String getImgUri() {
		return imgUri;
	}

	public void setImgUri(String imgUri) {
		this.imgUri = imgUri;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void String(String timestamp) {
		this.timestamp = timestamp;
	}
    
    
    
}

