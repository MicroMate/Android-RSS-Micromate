package com.micromate.micromatereader;


import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {

	private Button uaktualnijBazeButton; 
	private	ListView listView;	
	private List<Article> articles; 
	private ArticleListAdapter articleListAdapter;     //TAK MASZ DEKLAROWAC - POZMIENIAJ WSZYSTKIE
	private DBoperacje baza;
	private RssSaxParserTask rssSaxParserTask;	
	private MyDialogFragment dialogPobierz;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView)findViewById(R.id.listView1);
		uaktualnijBazeButton = (Button)findViewById(R.id.button1);
		//TextView title = (TextView)findViewById(R.id.textView2); 
		//title.setText(myRSSHandler.getChannelTitle()); //TYTUL MOZNA ZAPISAC W USTAWIENIACH PROGRAMU
		
		articles = new ArrayList<Article>();
		baza = new DBoperacje(this);   //BAZA DANYCH
		dialogPobierz = new MyDialogFragment();
		
		baza.open(); 					//OTWIERANIE BAZY
		pobierzDaneBazy(listView); 		//POBIERANIE ARTYKULOW Z BAZY
			
			
		uaktualnijBazeButton.setOnClickListener(new View.OnClickListener() {   //AKTUALIZACJA LISTY	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//   "http://feeds.feedburner.com/frogermcs?format=xml"
				//   "http://feeds.feedburner.com/blogspot/hsDu?format=xml"		
				//   "http://android.com.pl/feed/"
				//String url = "http://android.com.pl/feed/";			
				
				String url = "http://www.micromate.bl.ee/?feed=rss2";
				//RssReaderTask rssReaderTask = new RssReaderTask(); //URUCHOMIENIE WATKA - PARSER SAX kanalu Rss
				//rssReaderTask.execute(url,url,url,url,url,url,url,url,url,url); //jest 10, wiec po 10%
				
				rssSaxParserTask = new RssSaxParserTask(articleListAdapter, baza, dialogPobierz, MainActivity.this);
				rssSaxParserTask.execute(url,url,url,url,url,url,url,url,url,url); //jest 10, wiec po 10%	
			}
		});
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {   //WYBIERANIE POZYCJI Z LISTY

		        @Override
		        public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		        	
		            Intent myIntent = new Intent(getApplicationContext(), ArticleActivity.class);
		            
		            //myIntent.putExtra("feedTitle", myRSSHandler.getChannelTitle());
		            myIntent.putExtra("articleTitle",articles.get(pos).getTitle());
		            myIntent.putExtra("articleContent",articles.get(pos).getDescription());
		            //myIntent.putExtra("articleUrl", RSSHandler.articles.get(pos).getUrl().toString());
		            myIntent.putExtra("articleUrl", articles.get(pos).getUrl());
		            
		            startActivity(myIntent);
		        }
		});
		
	}
	
	 @Override
	  protected void onResume() {
	    baza.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    baza.close();
	    super.onPause();
	  }
	  
	  
	  private void pobierzDaneBazy(ListView list) {
		     
			articles = baza.getAllData(); //POBIERANIE ARTICLES z BAZY DANYCH
		    
		    //articleListAdapter = new ArticleListAdapter(getApplicationContext(), R.layout.activity_main, articles);   //Context mozn przekazac teý tak
			//articleListAdapter = new ArticleListAdapter(MainActivity.this, R.layout.activity_main, articles);         //w metodzie: setOnClickListener nawet trzeba tak
		    articleListAdapter = new ArticleListAdapter(this, R.layout.activity_main, articles);
			list.setAdapter(articleListAdapter);
		
		}

	
}
