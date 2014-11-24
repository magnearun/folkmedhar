/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem býr til hlut sem heldur utan um lausa tíma
 */


package com.example.folkmedhar.pantanir.bokun;

public class Lausir
{
	private String timi; 	// Tímasetning á forminu 09:00
	private boolean laus;   // Hefur gildið false ef tíminn er ekki laus
	private String id; 		// Auðkenni starfsmanns
	
	 /**
	  * Býr til nýjan hlut af taginu Lausir
	  * @param t
	  * @param b
	  * @param i
	  */
	 public Lausir( String t, boolean b, String i ) {
		timi = t;
		laus= b;
		id = i;
	 }
	 
	 
	 /**
	  * Skilar gildi breytu sem heldur utan um tímasetningu á forminu
	  * 9:00
	  * @return
	  */
	 public String getTimi() {
		 return timi;
	 }
	 
	 
	 /**
	  * Gefir breytu sem heldur utan  um tímasetningu á forminu 9:00 gildi
	  * @return
	  */
	 public void setTimi(String t) {
		 timi = t;
	 }
	 
	 
	 /**
	  * Skilar gildi breytu sem heldur utan um auðkenni starfsmanns
	  * @return
	  */
	 public String getId() {
		 return id;
	 }
	 
	 /**
	  * Gefir breytu sem heldur utan um auðkenni starfsmanns gildi
	  * @return
	  */
	 public void setId(String i) {
		 id = i;
	 }
	 
	 /**
	  * Skilar gildi sem er true ef tiltekinn tími er laus
	  * @return
	  */
	 public boolean isLaus() {
		 return laus;
	 }
	 
	 /**
	  * Gefir breytu sem heldur utan um hvort að tiltekinn tími sé laus
	  * gildi
	  * @return
	  */
	 public void setLaus(boolean b) {
		 laus = b;
	 }
 
}
