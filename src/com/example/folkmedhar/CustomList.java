/**
 * @author: Magnea Rún Vignisdóttir
 * @since: 20.11.2014
 * Klasinn útfærir sérsniðinn lista sem notaður er til að birta tilboð 
 * og verðlista.
 */
package com.example.folkmedhar;

import com.example.folkmedhar.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("ViewHolder")
public class CustomList extends ArrayAdapter<String>{
	private final Activity context; 
	private final String[] titleFylki; // Fylki með titlum atriðanna í listanum
	private final String[] txtFylki; // Fylki með texta atriðanna í listanum
	
	/**
	* Nýr sérsniðinn listi með tveim textasvæðum er búin til
	*/
	public CustomList(Activity context, String[] nafn, String[] lysing) {
		super(context, R.layout.list_custom_item, nafn);
		this.context = context;
		this.titleFylki = nafn;
		this.txtFylki = lysing;

		
	}
	
	/**
	* Skilar view, með tveim textasvæðum, fyrir eitt atriði í listanum
	*/
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.list_custom_item, null, true);
		TextView title = (TextView) rowView.findViewById(R.id.title);
		TextView text = (TextView) rowView.findViewById(R.id.txt);
		title.setText(titleFylki[position]);
		text.setText(txtFylki[position]);
		return rowView;
	}
}