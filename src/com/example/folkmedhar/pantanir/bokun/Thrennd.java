package com.example.folkmedhar.pantanir.bokun;

/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem býr til hlut sem heldur utan um bókaða tíma
 */
public class Thrennd
{
	public String timi;
	public int lengd;
	public String id;
	
	/**
	 * Býr til nýjan hlut af taginu Tvennd
	 * @param t
	 * @param l
	 */
	public Thrennd( String t, int l, String i )
	{
		timi = t;
		lengd = l;
		id = i;
	}
}