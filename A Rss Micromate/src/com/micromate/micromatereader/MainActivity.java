

package com.micromate.micromatereader;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	
	private ListView categoryListView; //lista kategorii artyku¸—w bloga
	private ArrayAdapter<String> categoryListAdapter;
	private DBoperacje baza;
	private List<String> categories;
	private Button uaktualnijBazeButton;
	private RssSaxParserTask rssSaxParserTask;
	private MyDialogFragment dialogPobierz;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		categoryListView = (ListView)findViewById(R.id.categoryList);
		uaktualnijBazeButton = (Button)findViewById(R.id.button1);
		
		dialogPobierz = new MyDialogFragment();
		
		baza = new DBoperacje(this);
		baza.open();
		
		//Convert HasSet to ArrayList
		categories = new ArrayList<String>(baza.getCategoryColumn()); 
		categories.add("All"); //adding all categories to the list
		
		categoryListAdapter = new ArrayAdapter<String>(this, R.layout.category_list_item, categories);
		categoryListView.setAdapter(categoryListAdapter);		
		
		// listening to single list item on click
        categoryListView.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
               
              // selected item 
              String category = ((TextView) view).getText().toString();
               
              // Launching new Activity on selecting single List Item
              Intent i = new Intent(getApplicationContext(), ArticlesListActivity.class);
             
              // sending data to new activity
              i.putExtra("category", category);
              startActivity(i);
             
          }
        });
        
    	//AKTUALIZACJA BAZY 	
		uaktualnijBazeButton.setOnClickListener(new View.OnClickListener() {   
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
							
				String url = "http://www.micromate.bl.ee/?feed=rss2";
				
				rssSaxParserTask = new RssSaxParserTask(categoryListAdapter,baza, dialogPobierz, MainActivity.this);
				rssSaxParserTask.execute(url,url,url,url,url,url,url,url,url,url); //jest 10, wiec po 10%	
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

	
}
