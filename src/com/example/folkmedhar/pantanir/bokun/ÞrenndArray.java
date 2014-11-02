package com.example.folkmedhar.pantanir.bokun;

/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem býr til hlut sem heldur utan um bókaða tíma
 */
public class ÞrenndArray
{
	public Þrennd[] bokad;
	public Timar[] laust;
	
	/**
	 * Býr til nýjan hlut af taginu Tvennd
	 * @param t
	 * @param l
	 */
	public ÞrenndArray( Þrennd [] b, Timar[] l )
	{
		bokad = b;
		laust = l;
	}
}