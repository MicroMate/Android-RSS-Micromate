package com.micromate.micromatereader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;


public class Article {

	private long id;
	
	private String title ="title";
	private String description ="title";
	//private URL url;
	private String url;
	private String pubDate;
	
	public Article(String title, String description, String url, String pupDate) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.pubDate = pupDate;
	}
	
	public Article() {
		// TODO Auto-generated constructor stub
	}

	//Seters and Geters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	
	//Formatowanie Daty i Godziny
	public String getDate() {
		//wzor daty z pliku xml
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZZ",Locale.UK);
		//moj wzor daty
		SimpleDateFormat mojformater = new SimpleDateFormat("dd-MM-yy   HH:mm:ss",Locale.UK);
	    
		Date date = null;
	    String strDate = "Date";
		  try {
			date = formatter.parse(pubDate);
			strDate =  mojformater.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("ArticleDateParser",
				    "Wystˆpi¸ problem z konwersjˆ pubDate: " + e.toString());
		}	  
		
		return strDate;
	}
	
	
	/*
	//metoda konwertuje typ String do URL
	public void parseToUrl(String str) {
	    try {
	        url = new URL(str);
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
	 }
	 */
	
}
