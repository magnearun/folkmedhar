package com.example.folkmedhar.pantanir.bokun;

/**
 * @author: Eva Dögg Steingrímsdóttir
 * @since: 15.10.2014
 * Klasinn sem býr til hlut sem heldur utan um bókaða tíma
 */
public class Bokadir
{
	private String timi;   // Tímasetning á forminu 9:00
	private int lengd;     // Tímalengd bókunar
	private String id;     // Auðkenni starfsmanns
	
	/**
	 * Býr til nýjan hlut af taginu Bokadir
	 * @param t
	 * @param l
	 * @param i
	 */
	public Bokadir( String t, int l, String i )
	{
		timi = t;
		lengd = l;
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
	  * Gefir breytu sem heldur utan um tímasetningu á forminu 9:00 gildi
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
	  * Skilar gildi breytu sem heldur utan um tímalengd bókunar
	  * @return
	  */
	 public int getLengd() {
		 return lengd;
	 }
	 
	 /**
	  * Gefir breytu sem heldur utan um tímalengd bókunar gildi
	  * gildi
	  * @return
	  */
	 public void setLengd(int l) {
		 lengd = l;
	 }
}