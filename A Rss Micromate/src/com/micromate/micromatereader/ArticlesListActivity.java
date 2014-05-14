package com.micromate.micromatereader;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ArticlesListActivity extends FragmentActivity {

	private	ListView listView;	
	private List<Article> articles; 
	private ArticlesListAdapter articleListAdapter;   
	private DBoperacje baza;
	private Intent intent;
	private String category;
	private TextView categoryTextView;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articles_list);
		
		listView = (ListView)findViewById(R.id.listView1);
		categoryTextView = (TextView)findViewById(R.id.categoryTextView); 
		
		articles = new ArrayList<Article>();
		baza = new DBoperacje(this);   //BAZA DANYCH
	
		//odebranie danej z nadrzednej aktywnosci
		intent = getIntent();
	    category = intent.getStringExtra("category");
		
	    categoryTextView.setText(category);
	    
		baza.open(); 	
		
		if (category.equals("All"))
			pobierzAllData(); 		
		else 
			pobierzCategoryData();
			
		
		//WYBIERANIE POZYCJI Z LISTY
		listView.setOnItemClickListener(new OnItemClickListener() {   
 
		        @Override
		        public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
		        	
		            Intent myIntent = new Intent(getApplicationContext(), ArticleActivity.class);
		            
		            myIntent.putExtra("articleCategory",articles.get(pos).getCategory());
		            myIntent.putExtra("articleTitle",articles.get(pos).getTitle());
		            myIntent.putExtra("articleDescription", articles.get(pos).getDescription());
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
