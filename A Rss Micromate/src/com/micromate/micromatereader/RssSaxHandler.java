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
	
	private StringBuilder builderText; //dla przechwytywania znak�w w metodzie charakters
	
	//flaga dla przechwytywania aktualnej pozycji w dokumencie
	//flagi pozycji musza byc zastosowane gdy dokument posiada znaczniki o tej samej nazwie
	private boolean inItem = false;     
	
	private boolean currentElement = false; //pomiedzy znacznikami moga byc znaki dlatego trzeba stosowac flage
	
	private DBoperacje baza;
	private String latestArticleDate ="0000-00-00 00:00:00";
	private List<String> newArticles;
	
	
	/*Constructor*/
	public RssSaxHandler(DBoperacje baza){
		this.baza = baza;
	}
	
	public List<Article> getArticles() {
		return articles;
	}
	
	public List<String> getNewArticles() {
		return newArticles;
	}
	
	/**/
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		
		builderText = new StringBuilder();
		//articles = new ArrayList<Article>();
		latestArticleDate = baza.getLatestArticleDate();
		newArticles = new ArrayList<String>();
		
		Log.d("RssSaxHandler", "startDOCUMENT");
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
	            Log.d("RssSaxHandler", "startElement - if in Item");
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
        	//Log.d("RssSaxHandler", "characters - if currentElemnet");
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
				Log.d("RssSaxHandler", "endElement - if TITLE");
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
				
			//if new article add name of category to the list
			if (latestArticleDate.compareTo(article.getDate()) < 0) { 
				newArticles.add(article.getCategory());				
				Log.d("RssSaxHandler", "DODANIE DO LISTY NOWEGO ARTYKU�U");
				articles.add(article);
				inItem = false;
			}
			else
				throw new NoNewArticlesException();
				//throw new SAXException();
			
			
			}	
		}
		
		builderText.setLength(0); 
	} 
	
	
}

//Own Exception
class NoNewArticlesException extends SAXException {}
