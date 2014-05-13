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

public class ArticlesListActivity extends FragmentActivity {

	private Button uaktualnijBazeButton; 
	private	ListView listView;	
	private List<Article> articles; 
	private ArticlesListAdapter articleListAdapter;   
	private DBoperacje baza;
	private RssSaxParserTask rssSaxParserTask;	
	private MyDialogFragment dialogPobierz;
	private Intent intent;
	private String category;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articles_list);
		
		listView = (ListView)findViewById(R.id.listView1);
		uaktualnijBazeButton = (Button)findViewById(R.id.button1);
		//TextView title = (TextView)findViewById(R.id.textView2); 
		//title.setText(myRSSHandler.getChannelTitle()); 
		
		articles = new ArrayList<Article>();
		baza = new DBoperacje(this);   //BAZA DANYCH
		dialogPobierz = new MyDialogFragment();
		
		//odebranie danej z nadrzednej aktywnosci
		intent = getIntent();
	    category = intent.getStringExtra("category");
		
		baza.open(); 	
		
		if (category.equals("All"))
			pobierzAllData(); 		
		else 
			pobierzCategoryData();
			
			
		//AKTUALIZACJA LISTY	
		uaktualnijBazeButton.setOnClickListener(new View.OnClickListener() {   
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
							
				String url = "http://www.micromate.bl.ee/?feed=rss2";
				
				rssSaxParserTask = new RssSaxParserTask(articleListAdapter, baza, dialogPobierz, ArticlesListActivity.this);
				rssSaxParserTask.execute(url,url,url,url,url,url,url,url,url,url); //jest 10, wiec po 10%	
			}
		});
		
		
		//WYBIERANIE POZYCJI Z LISTY
		listView.setOnItemClickListener(new OnItemClickListener() {   

		        @Override
		        public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		        	
		            Intent myIntent = new Intent(getApplicationContext(), ArticleActivity.class);
		            
		            //myIntent.putExtra("feedTitle", myRSSHandler.getChannelTitle());
		            myIntent.putExtra("articleTitle",articles.get(pos).getTitle());
		            myIntent.putExtra("articleCategory",articles.get(pos).getCategory());
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
	  
	  
	  private void pobierzAllData() {
		     
			articles = baza.getAllData(); //POBIERANIE ARTICLES z BAZY DANYCH
		    
		    //articleListAdapter = new ArticleListAdapter(getApplicationContext(), R.layout.activity_main, articles);   //Context mozn przekazac teý tak
			//articleListAdapter = new ArticleListAdapter(MainActivity.this, R.layout.activity_main, articles);         //w metodzie: setOnClickListener nawet trzeba tak
		    articleListAdapter = new ArticlesListAdapter(this, R.layout.activity_articles_list, articles);
			listView.setAdapter(articleListAdapter);
		
		}
	  
	  private void pobierzCategoryData() {
		     
			articles = baza.getCategory(category); //POBIERANIE ARTICLES z BAZY DANYCH
		    
		    articleListAdapter = new ArticlesListAdapter(this, R.layout.activity_articles_list, articles);
			listView.setAdapter(articleListAdapter);
		
		}

	
}
