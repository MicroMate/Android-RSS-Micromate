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
	private String date;
	private String category;
	
	public Article(String title, String description, String url, String date, String category) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.date = date;
		this.category = category;
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

	public String getDate() {
		return date;
	}
	
	public void setDate(String date){
		this.date = date;
	}

	public void setPubDate(String pubDate) {
		//wzor daty z pliku xml
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZZ",Locale.UK);
		//moj wzor daty
		SimpleDateFormat mojformater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK);
		
		Date date = null;
	    String strDate = "0000-00-00 00:00:00";
		try {
			date = formatter.parse(pubDate);
			strDate =  mojformater.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("ArticleDateParser",
				    "Wyst�pi� problem z konwersj� pubDate: " + e.toString());
		}	  
		
		this.date = strDate;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
	//W�asny Format Daty i Godziny, dla wyswietlania na liscie
	public String getListDate() {
		//wzor daty z pliku xml
		//SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZZ",Locale.UK);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK);
		//moj wzor daty
		SimpleDateFormat mojformater = new SimpleDateFormat("dd-MM-yy   HH:mm:ss",Locale.UK);
	    
		Date date = null;
	    String strDate = "0000-00-00 00:00:00";
		try {
			date = formatter.parse(this.date);
			strDate =  mojformater.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("ArticleDateParser",
				    "Wyst�pi� problem z konwersj� daty: " + e.toString());
		}	  
		
		return strDate;
	}
	
	/*
	//Konwertowanie daty do typu Date
	public Date getDateDate() {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK);
		Date date = null;
	    
		try {
			date = formatter.parse(this.date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("ArticleDateParser",
				    "Wyst�pi� problem z konwersj� daty: " + e.toString());
		}	  
		
		return date;
	}
	*/

}
