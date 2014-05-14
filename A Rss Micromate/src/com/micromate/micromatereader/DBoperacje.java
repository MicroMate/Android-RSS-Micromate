package com.micromate.micromatereader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBoperacje {
	
	private SQLiteDatabase db; 	
	private DBopenHelper dbOpenHelper;
	
	
	public DBoperacje(Context context) {  
		  dbOpenHelper = new DBopenHelper(context);
	  }
	
	//Open DataBase
	public void open() throws SQLException {
	    db = dbOpenHelper.getWritableDatabase();
	}

	//Close DataBase
	public void close() {
	    dbOpenHelper.close();
	}
	
	
	 /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
	
    //orginal 
    // Dodawanie wyniku do bazy
    public void addToDatabase(Article article) {
    	
        ContentValues values = new ContentValues();
        values.put(DBopenHelper.NAZWA_KOLUMNY_TITLE, article.getTitle());
        values.put(DBopenHelper.NAZWA_KOLUMNY_DESCRIPTION, article.getDescription()); 
        values.put(DBopenHelper.NAZWA_KOLUMNY_URL, article.getUrl()); 
        values.put(DBopenHelper.NAZWA_KOLUMNY_PUBDATE, article.getPubDate()); 
        values.put(DBopenHelper.NAZWA_KOLUMNY_CATEGORY, article.getCategory()); 
        
        // Inserting Row
        db.insert(DBopenHelper.NAZWA_TABELI, null, values);
        //bazaDanych.close(); // Closing database connection
    }
        
    // Getting All WYNIKI
    public List<Article> getAllData() {
       
    	List<Article> lista = new ArrayList<Article>();
             
        // Select All Query      
        String selectQuery = "SELECT  * FROM " + DBopenHelper.NAZWA_TABELI;
        Cursor kursor = db.rawQuery(selectQuery, null);
        
        //Drugi sposob pobierania danych z bazy
        //segregowanie wedlug kolumny CZAS (najkrotszy bedzie pierwszy)
        //Cursor kursor = bazaDanych.query(OpenHelperWynik.NAZWA_TABELI, wszystkieKolumny, null, null, null, null,OpenHelperWynik.NAZWA_KOLUMNY_CZAS); 
        
        //int licznik = 1; // dla numeru pozycji na liscie
        		
        // looping through all rows and adding to list
        if (kursor.moveToFirst()) {
            do {
                Article wynik = new Article();
                
                //pobieranie danych z bazy danych
                wynik.setId(Integer.parseInt(kursor.getString(0)));
                wynik.setTitle(kursor.getString(1));
                wynik.setDescription(kursor.getString(2));
                wynik.setUrl(kursor.getString(3));
                wynik.setPubDate(kursor.getString(4));
                wynik.setCategory(kursor.getString(5));
                
                //licznik (nr pozycji na liscie - mozna dodac do ziarna)
                //wynik.setNr(licznik++);
                
                // Adding contact to list
                lista.add(wynik);
                
            } while (kursor.moveToNext());
        }
 
        kursor.close();
        // return contact list
        return lista;
    }
    
    // Getting article where category
    public List<Article> getCategory(String category) {
       
    	List<Article> lista = new ArrayList<Article>();
             
        // Select All Query      
        String selectQuery = "SELECT  * FROM " + DBopenHelper.NAZWA_TABELI +" WHERE category = '"+category+"'";
        Cursor kursor = db.rawQuery(selectQuery, null);
        		
        // looping through all rows and adding to list
        if (kursor.moveToFirst()) {
            do {
                Article wynik = new Article();
                
                //pobieranie danych z bazy danych
                wynik.setId(Integer.parseInt(kursor.getString(0)));
                wynik.setTitle(kursor.getString(1));
                wynik.setDescription(kursor.getString(2));
                wynik.setUrl(kursor.getString(3));
                wynik.setPubDate(kursor.getString(4));
                wynik.setCategory(kursor.getString(5));
                
                //licznik (nr pozycji na liscie - mozna dodac do ziarna)
                //wynik.setNr(licznik++);
                
                // Adding contact to list
                lista.add(wynik);
                
            } while (kursor.moveToNext());
        }
 
        kursor.close();
        // return contact list
        return lista;
    }
    
    // Getting Categories by Articles
    public Set<String> getCategoryColumn() {
       
    	Set<String> lista = new HashSet<String>();  //HashSet Nie dubluje element—w
             
        // Select All Query      
        String selectQuery = "SELECT category FROM " + DBopenHelper.NAZWA_TABELI;
        Cursor kursor = db.rawQuery(selectQuery, null);
        		
        // looping through all rows and adding to list
        if (kursor.moveToFirst()) {
            do {
                //Article wynik = new Article();
            	String category;
                
                //pobieranie danych z bazy danych
                category = kursor.getString(0);
                
                // Adding contact to list
                lista.add(category);
                
            } while (kursor.moveToNext());
        }
 
        kursor.close();
        // return contact list
        return lista;
    }
    
    
    
    /* Usuwanie poprawic
    public boolean deleteCountry(long _index) {
        String where = KEY_ID + "=" + _index;
        return db.delete(DB_TABLE, where , null) > 0;
    }
    */
    
    public void deleteAll() { //przypisuje nr id = 1 
    	//bazaDanych.execSQL("delete * from "+ OpenHelperWynik.NAZWA_TABELI);
        //bazaDanych.delete(OpenHelperWynik.NAZWA_TABELI, "1", null);
        db.delete(DBopenHelper.NAZWA_TABELI, null, null);
    }
    
    
}
