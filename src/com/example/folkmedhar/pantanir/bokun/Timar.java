package com.example.folkmedhar.pantanir.bokun;

/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem býr til hlut sem heldur utan um lausa tíma
 */
public class Timar
{
	public String timi;
	public boolean laus;
 /**
  * Býr til nýjan hlut af taginu Timar
  * @param t
  * @param b
  */
 public Timar( String t, boolean b )
	{
		timi = t;
		laus= b;
	}
}
