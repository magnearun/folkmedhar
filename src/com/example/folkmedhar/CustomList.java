package com.example.folkmedhar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

	public class CustomList extends ArrayAdapter<String>{
		private final Activity context;
		private final String[] titleFylki;
		private final String[] txtFylki;
	
		public CustomList(Activity context,
		String[] nafn, String[] lysing) {
		super(context, R.layout.list_custom_item, nafn);
		this.context = context;
		this.titleFylki = nafn;
		this.txtFylki = lysing;

	}
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