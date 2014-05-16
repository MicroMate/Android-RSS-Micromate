package com.micromate.micromatereader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class RssSaxParserTask extends AsyncTask <String, Integer, List<Article>> {

	private CategoryListAdapter categoryListAdapter;
	private List<Category> categories;
	private DBoperacje baza;
	private List<Article> articles;
	private RssSaxHandler rssSaxHandler;
	private MyDialogFragment dialogPobierz;
	private FragmentActivity activity; //context
	private int exceptionNr = 0;
	
	//Konstruktor
	public RssSaxParserTask( 
			CategoryListAdapter categoryListAdapter, 
			List<Category> categories,
			DBoperacje baza,
			MyDialogFragment dialogPobierz,
			FragmentActivity activity) {
	
		this.categoryListAdapter = categoryListAdapter;
		this.categories = categories;
		this.baza = baza;
		this.dialogPobierz = dialogPobierz;
		this.activity = activity;
	}
	
	
	//
	@Override
	protected void onPreExecute() {
		dialogPobierz.show(activity.getSupportFragmentManager(), "missiles"); //DIALOG POSTEPU
	}

	//Implementacji metody doInBackground
	//wynik metody jest przekazywany jako argument do metody onPostExecute(). 
	@Override
	protected List<Article> doInBackground(String... urls) {
  
		articles = new ArrayList<Article>(); 
 
		for (int i = 0; i < urls.length; i++) { //Petla dla progresu w dialogu
			try {
				
				articles = parseXml(urls[0]); 
				
				publishProgress((int) (((i+1) / (float) urls.length) * 100));//update progress bar
      
			//hendles the type of exceptios
			} catch (NoNewArticlesException nae){
				Log.e("RssSaxParserTask", "No New Article");
				exceptionNr = 1;
				
			} catch (ParserConfigurationException pce){
				Log.e("RssSaxParserTask", "sax parse error", pce);
				exceptionNr = 2;
				
			} catch (SAXException se){
				Log.e("RssSaxParserTask", "sax error", se);
				exceptionNr = 3;
			
			} catch (IOException e) {
				Log.e("RssSaxParserTask", "IOException", e);
				exceptionNr = 4;
			}
		
		}
  
		Log.d("RssSaxParserTask", "size of return articles list: "+articles.size());
		return articles;
		
	}

	//Wywo∏anie metody publishProgress - prowadzi do wywolania metody onProgressUpdate
	//przyjmuje argumentu typu integer (mozna tez zastosowac typ String)
	@Override
	protected void onProgressUpdate(Integer... values) {          
		dialogPobierz.setPostep(values[0]);  //DIALOG POSTEPU
	}



	//aktualizacja elemntów interfejsu uzytkowanika, 
	@Override
	protected void onPostExecute(List<Article> list) {

		Log.d("RssSaxParserTask", "RETURN ARTICLES2 rozmiar listy: "+rssSaxHandler.getArticles().size());
		articles = rssSaxHandler.getArticles();
		
		try{
			dialogPobierz.dismiss();
		}catch(Exception e){
			Log.d("RssSaxParserTask", "BLAD ZAMYKANIA DIALOGU: "+e);
		}
		
		//display toast with the type of exception
		if (exceptionNr != 0) {	
			switch(exceptionNr){
			case 1:
				if (articles.isEmpty())
					Toast.makeText(activity,        
						"No new articles.", Toast.LENGTH_SHORT).show();
				else 
					Toast.makeText(activity,        
						articles.size()+" new article", Toast.LENGTH_SHORT).show();	
			 	break;
			case 2:
				Toast.makeText(activity,                   
						"Sax parser error.", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(activity,                   
						"Sax error.", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(activity,                   
						"IOException, Network issue.", Toast.LENGTH_SHORT).show();
				break;
			}
			
	       
	    }
		
		
		if (!articles.isEmpty()) {	
			aktualizacjaBazy(articles);	
			aktualizacjaListCategory();
		}
		
	}




	private List<Article> parseXml(String strUrl) throws 
		 ParserConfigurationException,SAXException,IOException {
	//private List<Article> parseXml(String strUrl) throws Exception {
	  
        URL xmlUrl = new URL(strUrl);
	        
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxFactory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
	        
        rssSaxHandler = new RssSaxHandler(baza);
        reader.setContentHandler(rssSaxHandler);
	       
        InputSource inputSource = new InputSource(xmlUrl.openStream());
        reader.parse(inputSource);
	        
        return rssSaxHandler.getArticles(); 
	}



	//private void aktualizacjaBazy() {
	private void aktualizacjaBazy(List<Article> articles) {
	 
		//po zmianach - dodawanie tylko nowego artykulów bez kasowania danych w bazie
		//baza.deleteAll(); 
	
		for (Article article : articles)
			baza.addToDatabase(new Article(
				article.getTitle(),
				article.getDescription(), 
				article.getUrl(), 
				article.getDate(), 
				article.getCategory()));
	    
	}

	
	private void aktualizacjaListCategory(){
	
		categoryListAdapter.clear();
		//tak bylo dla standardowego adaptera dla jedego wiersza
		//urzywajac metody addAll nie trzeba konwertowaç typu Set do List, argument metody jest typu Collection 
		//categoryListAdapter.addAll(baza.getCategoryColumn()); 
		//categoryListAdapter.add("All"); //adding all categories to the list
		
		List<String> categoryNames = new ArrayList<String>(baza.getCategoryColumn());  
		categoryNames.add("All"); //adding All row to the list for display all articles
		
		categories.clear();
		
		for (String s: categoryNames){
			categories.add(new Category( s, 0));
		}
		
		//if new articles adding quantity number of new articles to list 
		for (String s: rssSaxHandler.getNewArticles()) {
			for (Category c: categories) {
				if (s.equals(c.getName()))			
					c.incrementNewArticle();
				if (c.getName().equals("All")) 
					c.incrementNewArticle(); //increment All category
			}
			
		}
	
		categoryListAdapter.notifyDataSetChanged();
	
	}

	
	
}

