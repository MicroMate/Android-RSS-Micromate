package com.micromate.micromatereader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ArticleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		
		
		TextView categoryView = (TextView)findViewById(R.id.textView1);
		TextView titleView = (TextView)findViewById(R.id.textView2);
		TextView descriptionView = (TextView)findViewById(R.id.textView3);
		Button btnArticleUrl = (Button)findViewById(R.id.visit_site);
		
		
		final Intent intent = getIntent();
		categoryView.setText(intent.getStringExtra("articleCategory"));
		titleView.setText(intent.getStringExtra("articleTitle"));
		descriptionView.setText(intent.getStringExtra("articleDescription"));
			
		
		btnArticleUrl.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        Intent buttonIntent = new Intent(Intent.ACTION_VIEW, 
		                Uri.parse(intent.getStringExtra("articleUrl")));
		        startActivity(buttonIntent);
		    }
		});
		
		
	}



}
