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

	ArticleListAdapter articleListAdapter;
	DBoperacje baza;
	List<Article> articles;
	RssSaxHandler rssSaxHandler;
	MyDialogFragment dialogPobierz;
	FragmentActivity activity; //context
	
	//Konstruktor
	public RssSaxParserTask( 
			ArticleListAdapter articleListAdapter, 
			DBoperacje baza,
			MyDialogFragment dialogPobierz,
			FragmentActivity activity) {
	
		this.articleListAdapter = articleListAdapter;
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
		
		//to jest dla manifestu w metodei onPostExecute - sprawdzam czy zosta∏y pobrane dane
		//z null tez dzial ale ∏atwiej i w∏asciwie uzyc metode isEmty()
		//articles = null;               //LISTA DOMYSLNIE NIE JEST null jest PUSTA, znajdujace sie w niej elemnty sa null
 
		for (int i = 0; i < urls.length; i++) { //Petla dla progresu w dialogu
			try {
				
				articles = parseXml(urls[0]); 
				
				publishProgress((int) (((i+1) / (float) urls.length) * 100));     //OKNO DIALOGOWE 4  aktualizowanie pasku postepu      // 3
      
	  
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

	//OKNO DIALOGOWE 5  - wywo∏anie metody publishProgress - prowadzi do wywolania metody onProgressUpdate
	//przyjmuje argumentu typu integer (mozna tez zastosowac typ String)
	@Override
	protected void onProgressUpdate(Integer... values) {          
		dialogPobierz.setPostep(values[0]);  //DIALOG POSTEPU
	}



	//Metoda ta s∏uzy do aktualizowania elemntów interfejsu uzytkowanika, 
	//dlatego nalezy wyswietlic wynik za pomoca adaptera widoku ListView.
	@Override
	protected void onPostExecute(List<Article> articles) {

		dialogPobierz.dismiss();
		
		if (articles.isEmpty()) {
			 Log.d("SAX XML ARTICLES", "ARTICLES ARE EMPTY !!!");
	       
	         //Toast.makeText(getApplicationContext(),    // w MainActivity
			 Toast.makeText(activity,                     // gdy pobierane activity (kontekst) w konstrukotrze
	         	 "Plik rss nie móg∏ zostaç przeczytany.", Toast.LENGTH_SHORT).show();
	         return;
	    }
		
		aktualizacjaBazy(articles);	
		aktualizacjaAdaptera(); //czyli listy

		
	}




private List<Article> parseXml(String strUrl) throws ParserConfigurationException,SAXException,IOException {
//private List<Article> parseXml(String strUrl) throws Exception {
	  
        URL xmlUrl = new URL(strUrl);
	        
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxFactory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
	        
        rssSaxHandler = new RssSaxHandler();
        reader.setContentHandler(rssSaxHandler);
	       
        InputSource inputSource = new InputSource(xmlUrl.openStream());
        reader.parse(inputSource);
	        
        return rssSaxHandler.getArticles(); 
}



//private void aktualizacjaBazy() {
private void aktualizacjaBazy(List<Article> articles) {
	 
	baza.deleteAll();
	
	for (Article article : articles)
		baza.addToDatabase(new Article(article.getTitle(),article.getDescription(), article.getUrl(), article.getPubDate()));
	    
}

private void aktualizacjaAdaptera() {

	//Tak jest ZLE nie aktualizuje listy
	//articles.clear();
	//articles = new ArrayList<Article>();
	//articles = baza.getAllContacts(); //Pobranie z bazy artyku∏ów	
		  		    
	//Gdy uzywamy wlasny adapter tak wyglada poprawna aktualizacja listy
	articleListAdapter.clear();
	articleListAdapter.addAll(baza.getAllData());
	articleListAdapter.notifyDataSetChanged();
}


}

