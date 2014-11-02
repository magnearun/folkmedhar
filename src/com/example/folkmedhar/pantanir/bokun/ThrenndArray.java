package com.example.folkmedhar.pantanir.bokun;

/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem býr til hlut sem heldur utan um bókaða tíma
 */
public class ThrenndArray
{
	public Thrennd[] bokad;
	public Timar[] laust;
	
	/**
	 * Býr til nýjan hlut af taginu Tvennd
	 * @param t
	 * @param l
	 */
	public ThrenndArray( Thrennd [] b, Timar[] l )
	{
		bokad = b;
		laust = l;
	}
}