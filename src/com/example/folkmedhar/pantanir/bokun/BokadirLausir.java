/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem býr til hlut sem heldur utan um bókaða og lausa tíma
 */

package com.example.folkmedhar.pantanir.bokun;

public class BokadirLausir
{
	public Bokadir[] bokad;  // Heldur utan um bókaða tíma
	public Lausir[] laust;   // Heldur utan um lausa tíma
	
	/**
	 * Býr til nýjan hlut af taginu BokadirLausir
	 * @param b
	 * @param l
	 */
	public BokadirLausir( Bokadir [] b, Lausir[] l )
	{
		bokad = b;
		laust = l;
	}	
}