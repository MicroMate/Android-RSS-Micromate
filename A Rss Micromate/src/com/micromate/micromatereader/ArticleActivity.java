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
		
		
		TextView feedTitle = (TextView)findViewById(R.id.textView1);
		TextView articleTitle = (TextView)findViewById(R.id.textView2);
		TextView articleContent = (TextView)findViewById(R.id.textView3);
		Button btnArticleUrl = (Button)findViewById(R.id.button1);
		
		
		final Intent intent = getIntent();
		feedTitle.setText(intent.getStringExtra("feedTitle"));
		articleTitle.setText(intent.getStringExtra("articleTitle"));
		articleContent.setText(intent.getStringExtra("articleCategory"));
			
		
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
