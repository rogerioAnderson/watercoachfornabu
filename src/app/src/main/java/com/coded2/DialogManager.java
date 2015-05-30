package com.coded2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.coded2.nabuwatercoach.Constants;

public class DialogManager
{
	private static ProgressDialog loadingDialog;
	
	public static final void showLoading(Context context, boolean modal)
	{
		showLoading(context, "Loading...",modal);
	}
	
	public static final void showLoading(Context context, String text,boolean modal)
	{
		loadingDialog = ProgressDialog.show(context, "", text, true, true);
		loadingDialog.setCancelable(!modal);
	}

	public static final void hideLoading()
	{
		try{
			if(loadingDialog!=null)
				loadingDialog.cancel();
		}catch(Exception e){
			Log.d(Constants.APPLICATION_TAG,(e.getMessage()==null)?"Null pointer":e.getMessage());
		}
	}

	
	public static final void toast(String text, Context context) {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast.getYOffset() / 2);
		toast.show();
	}
	
	public static final void alert(String text, Context context) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// set title
		alertDialogBuilder.setTitle("Alert");

		// set dialog message
		alertDialogBuilder
		.setMessage(text)
		.setCancelable(false)
		.setPositiveButton("OK",new OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
					}
				  });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	public static final AlertDialog.Builder confirm(Context context, String title, String msg, String cancel, String confirm, OnClickListener confirmListener, OnClickListener cancelListener)
	{
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle(title);
		alert.setMessage(msg);
		
		alert.setPositiveButton(confirm, confirmListener);
		alert.setNegativeButton(cancel, cancelListener);
		
		alert.show();
		
		return alert;
	}
}


