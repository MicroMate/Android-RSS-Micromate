package com.micromate.micromatereader;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBoperacje {
	
	private SQLiteDatabase db; 	
	private DBopenHelper dbOpenHelper;
	
	//DZIEKI TEMU KONSTRUKTOROWI W MAIN MOZEMY UTWORZYC obiekt ->> Kontakty2Operacje bazaDanych = new Kontakty2Operacje(this);
	public DBoperacje(Context context) {  //CONTEXT ??????
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
	

	
	/* Tablica String—w dla	- Segregowanie wed¸ug kolumny czas
	 private String[] wszystkieKolumny = { OpenHelperWynik.NAZWA_KOLUMNY_ID, 
			 							   OpenHelperWynik.NAZWA_KOLUMNY_TITLE,
			 							   OpenHelperWynik.NAZWA_KOLUMNY_DESCRIPTION
			 							    };
	*/
	
	 /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
	
    //orginal 
    // Dodawanie wyniku do bazy
    public void addToDatabase(Article article) {
    	
        //SQLiteDatabase db = this.getWritableDatabase();
    	//bazaDanych = bazaHelper.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(DBopenHelper.NAZWA_KOLUMNY_TITLE, article.getTitle());
        values.put(DBopenHelper.NAZWA_KOLUMNY_DESCRIPTION, article.getDescription()); 
        values.put(DBopenHelper.NAZWA_KOLUMNY_URL, article.getUrl()); 
        values.put(DBopenHelper.NAZWA_KOLUMNY_PUBDATE, article.getPubDate()); 
 
        // Inserting Row
        db.insert(DBopenHelper.NAZWA_TABELI, null, values);
        //bazaDanych.close(); // Closing database connection
    }
   

/*
	// Dodawanie wyniku do bazy - Segregowanie wed¸ug kolumny czas 
    public void dodajWynik(Article wynik) {
    	
        //SQLiteDatabase db = this.getWritableDatabase();
    	//bazaDanych = bazaHelper.getWritableDatabase();
 
        ContentValues wartosc = new ContentValues();
        wartosc.put(OpenHelperWynik.NAZWA_KOLUMNY_CZAS, wynik.getWynikCzas()); // Contact Name
        wartosc.put(OpenHelperWynik.NAZWA_KOLUMNY_BLEDY, wynik.getBledy()); // Contact Name
        wartosc.put(OpenHelperWynik.NAZWA_KOLUMNY_GODZINA, wynik.getGodzina()); // Contact Phone
        
        //segregowanie wedlug kolumny CZAS (najkrotszy bedzie pierwszy)
        Cursor kursor = bazaDanych.query(OpenHelperWynik.NAZWA_TABELI, wszystkieKolumny, null, null, null, null,OpenHelperWynik.NAZWA_KOLUMNY_CZAS); 
		
        if(kursor.getCount()<10){      //jesli mniej niz 10 wierszy
			// Inserting Row
			bazaDanych.insert(OpenHelperWynik.NAZWA_TABELI, null, wartosc);
		}
        else{
        	//Update ostatni wiersz - czyl z najduzszym czasem 
        	 kursor.moveToLast();
			 long idWiersza = kursor.getLong(0);
			 // insertId = bazaDanych.update(OpenHelperOsoba2.NAZWA_TABELI, wartosc, OpenHelperOsoba2.NAZWA_KOLUMNY_ID + " = ?", new String[] { String.valueOf(rowid) });
			 bazaDanych.update(OpenHelperWynik.NAZWA_TABELI, wartosc, OpenHelperWynik.NAZWA_KOLUMNY_ID + " = " + idWiersza, null );
        	
        }      
        
        //bazaDanych.close(); // Closing database connection
    }
*/    
    
    
    
    // Getting All WYNIKI
    public List<Article> getAllData() {
       
    	List<Article> wynikList = new ArrayList<Article>();
       
       
        //SQLiteDatabase db = this.getWritableDatabase();
        //bazaDanych = bazaHelper.getWritableDatabase();
        
        //Wtym przykladzie mozna podac zapytanie jak w  konsoli
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
               // wynik.setWynikCzas(kursor.getInt(1));
               // wynik.setBledy(kursor.getInt(2));
                wynik.setTitle(kursor.getString(1));
                wynik.setDescription(kursor.getString(2));
                wynik.setUrl(kursor.getString(3));
                wynik.setPubDate(kursor.getString(4));
                
                //licznik (nr pozycji na liscie - mozna dodac do ziarna)
                //wynik.setNr(licznik++);
                
                // Adding contact to list
                wynikList.add(wynik);
                
            } while (kursor.moveToNext());
        }
 
        kursor.close();
        // return contact list
        return wynikList;
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
