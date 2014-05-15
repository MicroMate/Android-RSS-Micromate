package com.micromate.micromatereader;

public class Category {
	
	private String name;
	private int newArticle =0;
	
	public Category(String name, int newArticle){
		this.name = name;
		this.newArticle = newArticle;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNewArticle() {
		return newArticle;
	}
	public void setNewArticle(int newArticle) {
		this.newArticle = newArticle;
	}
	
	public void incrementNewArticle(){
		newArticle++;
	}
	
	
	
}
