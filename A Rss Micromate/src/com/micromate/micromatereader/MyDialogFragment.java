package com.micromate.micromatereader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
//import android.app.DialogFragment; //DialogFragment zosta∏ wprowadzony od wersji Api level 11 , Android 3.0
// aby dzia∏a∏ na ni˝szych wersjach nale˝y importowaç ta biblioteke (dzia∏a od Androida 1.6)

public class MyDialogFragment extends DialogFragment {
 
	ProgressDialog mojProgressDialog;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        
		 mojProgressDialog = new ProgressDialog(getActivity());
	     
		 mojProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);         // 2
	     mojProgressDialog.setMessage("Pobieranie receptur...");                       // 3
	     mojProgressDialog.setCancelable(false);          // 4  wylaczenie przycisku back
	     
	     return mojProgressDialog;
	     
	}
		
	public void setPostep(int values) {
		
		mojProgressDialog.setProgress(values);
		
	}
		
    /* DLA PRZYCISKU OK CANCEL
     * 	// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setMessage("My Message")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
        */
    
}