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
		
		//to jest dla manifestu w metoda onPostExecute - sprawdzam czy zosta∏y pobrane dane
		//z null tez dzial ale ∏atwiej i w∏asciwie uzyc metode isEmty()
		//articles = null;               //LISTA DOMYSLNIE NIE JEST null jest PUSTA, znajdujace sie w niej elemnty sa null
 
		for (int i = 0; i < urls.length; i++) { //Petla dla progresu w dialogu
			try {
				
				articles = parseXml(urls[0]); 
				
				publishProgress((int) (((i+1) / (float) urls.length) * 100));  //aktualizowanie pasku postepu 
      
	  
			} catch (ParserConfigurationException pce){
				Log.e("SAX XML", "sax parse error", pce);
				
			} catch (SAXException se){
				Log.e("SAX XML", "sax error", se);
			
			} catch (IOException e) {
				Log.e("SAX XML", "Exception", e);
			}
		}
  
		return articles;
	}

	//Wywo∏anie metody publishProgress - prowadzi do wywolania metody onProgressUpdate
	//przyjmuje argumentu typu integer (mozna tez zastosowac typ String)
	@Override
	protected void onProgressUpdate(Integer... values) {          
		dialogPobierz.setPostep(values[0]);  //DIALOG POSTEPU
	}



	//Metoda ta s∏uzy do aktualizowania elemntów interfejsu uzytkowanika, 
	//dlatego nalezy wyswietlic wynik za pomoca adaptera widoku ListView.
	@Override
	protected void onPostExecute(List<Article> articles) {

		try{
			dialogPobierz.dismiss();
		}catch(Exception e){
			Log.d("RssSaxParserTask", "BLAD ZAMYKANIA DIALOGU: "+e);
		}
		
		if (articles.isEmpty()) {
			 Log.d("SAX XML ARTICLES", "ARTICLES ARE EMPTY !!!");
	       
	         //Toast.makeText(getApplicationContext(),    // w MainActivity
			 Toast.makeText(activity,                     // gdy pobierane activity (kontekst) w konstrukotrze
	         	 "Plik rss nie móg∏ zostaç przeczytany.", Toast.LENGTH_SHORT).show();
	         return;
	    }
		
		aktualizacjaBazy(articles);	
		
		aktualizacjaListCategory();

		
	}




	private List<Article> parseXml(String strUrl) throws ParserConfigurationException,SAXException,IOException {
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
	 
		baza.deleteAll();
	
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
		//tak bylo dla zwyklego adaptera
		//podczas dodawania metoda addAll nie trzeba konwertowaç typu Set do List, argument metody jest typu Collection 
		//categoryListAdapter.addAll(baza.getCategoryColumn()); 
		//categoryListAdapter.add("All"); //adding all categories to the list
		
		List<String> categoryNames = new ArrayList<String>(baza.getCategoryColumn());  
		categoryNames.add("All"); //adding all categories to the list
		
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

