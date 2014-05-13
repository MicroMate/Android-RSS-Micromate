package com.micromate.micromatereader;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class RssSaxHandler extends DefaultHandler {

	private Article article;
	private List<Article> articles = new ArrayList<Article>(); 
	
	private StringBuilder builderText; //dla przechwytywania znak—w w metodzie charakters
	
	//flaga dla przechwytywania aktualnej pozycji w dokumencie
	//flagi pozycji musza byc zastosowane gdy dokument posiada znaczniki o tej samej nazwie
	private boolean inItem = false;     
	
	private boolean currentElement = false; //pomiedzy znacznikami moga byc znaki dlatego trzeba stosowac flage
	
	public List<Article> getArticles() {
		return articles;
	}
	
	/**/
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		
		builderText = new StringBuilder();
		//articles = new ArrayList<Article>();
		
		Log.d("Micromate Reader", "startDOCUMENT");
	}
	
	
	/**/
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		
		 currentElement = true;
		
		 if(localName.equalsIgnoreCase("item")) { 
			 	inItem = true;						//
	            article = new Article();			//
	            Log.d("Micromate Reader", "startElement - inItem");
	     }
	}

	
	/**/
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	
		//if (inItem == true)
		if(currentElement) {
			builderText.append(ch, start, length);     //
        	Log.d("Micromate Reader", "characters - cuurentElemnet");
		}
	}

	
	/**/
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		
		currentElement = false;
		
		
		if(inItem) { 
	       
			if(localName.equalsIgnoreCase("title")) {
				article.setTitle(builderText.toString().trim());
				Log.d("Micromate Reader", "endElement - TITLE");
			}	
			else if(localName.equalsIgnoreCase("link"))
				article.setUrl(builderText.toString().trim());
			else if(localName.equalsIgnoreCase("description"))
				article.setDescription(builderText.toString().trim());
			else if(localName.equals("pubDate"))
				article.setPubDate(builderText.toString());
			else if(localName.equals("category"))
				article.setCategory(builderText.toString());
		
			else if(localName.equalsIgnoreCase("item")) {  //
				articles.add(article);
				inItem = false;
			}	
		}
		
		builderText.setLength(0); 
	} 
	
	
	
	
}
