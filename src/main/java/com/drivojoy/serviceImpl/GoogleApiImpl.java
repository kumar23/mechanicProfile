package com.drivojoy.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.drivojoy.service.IGoogleApi;

@Component
public class GoogleApiImpl implements IGoogleApi{

	private final Logger logger = Logger.getLogger(GoogleApiImpl.class);
	
	private String readAll(Reader rd) throws IOException 
	{
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1){
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException 
	{
		InputStream is = new URL(url).openStream();
		try{
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		}
		finally{
			is.close();
		}
	}
	
	public double callGoogleDistanceAPI(String fromLocation, double toLatitude, double toLongitude){
		JSONObject json=null;
		int temp=0;
		double distance=0;
		fromLocation=fromLocation.replaceAll(" ", "+");
		try {
			json = readJsonFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?"
					+ "origins="+fromLocation+"&"
					+ "destinations="+toLatitude+","+toLongitude+"&"
					+ "mode=driving&sensor=false&"
					+ "key=AIzaSyABgpi5-dGxywoXYP7RAhU9lpmKaBz7iiE");
			logger.debug(json);
			json.get("rows");
			JSONArray arr=null;
			arr = json.getJSONArray("rows");
			temp=(Integer)arr.getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getInt("value");
			distance=(double)temp/1000;
		}
		catch (JSONException e){
			e.printStackTrace();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
		return distance;

	}
	
}
