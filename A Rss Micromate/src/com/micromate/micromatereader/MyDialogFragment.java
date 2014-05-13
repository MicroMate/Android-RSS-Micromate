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
    
}