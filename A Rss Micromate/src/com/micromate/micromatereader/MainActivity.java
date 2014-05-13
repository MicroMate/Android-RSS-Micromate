/*
 *  1. Podzia¸ artyku¸—w na kategorie 
 *  1.1  Nowe Activity - ArticlesListActivity
 *  ca¸y kod z MainActivity przenios¸em do nowego activty
 *  W MainActivity Utworzy¸em liste kategori artyku¸—w.
 *  1.2  Dodanie intencji - przekazywanie danych pomiedzy aktywnosciami
 *  1.3  Dodanie do tabeli kolumny cotegory
 *  1.4  Dodanie do ziarna zmienej category
 *   
 */

package com.micromate.micromatereader;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	
	
	private ArrayAdapter<String> adapter_listy;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		String[] elementy_listy = {
				"Android", 
				"Linux", 
				"AVR", 
				"All" };
		
		
		adapter_listy = new ArrayAdapter<String>(this, R.layout.category_list_item,elementy_listy);
		setListAdapter(adapter_listy);
		
		
		ListView lv = getListView();
		
		// listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
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
		
	}
	
	

	
}
