package com.micromate.micromatereader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryListAdapter extends ArrayAdapter<Category>{

	private Context context;
	
	private List<Category> category = new ArrayList<Category>();
	
	public CategoryListAdapter(Context context, int textViewResourceId, List<Category> category) {
		super(context, textViewResourceId, category);
		  this.context = context;
		  this.category = category;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 1. Create inflater 
	    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    // 2. Get custom row layout from inflater
	    View rowView = inflater.inflate(R.layout.category_list_item, parent, false);

	    // 3. Get the two text views from the custom row layout xml file  
	    TextView labelView = (TextView) rowView.findViewById(R.id.category_text);
	    TextView valueView = (TextView) rowView.findViewById(R.id.newArticleView);

	    // 4. Set the text for textView 
	    labelView.setText(category.get(position).getName());
	    valueView.setText(String.valueOf(category.get(position).getNewArticle()));

	    // 5. retrn rowView
	    return rowView;
	}
	

}

