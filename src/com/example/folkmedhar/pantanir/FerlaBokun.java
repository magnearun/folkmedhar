/**
 * @author: Eva Dögg Steingrímsóttir og Magnea Rún Vignisdóttir
 * @since: 25.11.2014
 * Klasinn sér um alla virkni bókunarferlisins. Hann gefur breytum sem halda utan um upplýsingar
 * bókunar gildi og kallar á klasa sem sér um að sækja lausa tíma á valinni dagsetningu úr gagnagrunni.
 */
package com.example.folkmedhar.pantanir;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Spinner;

import com.example.folkmedhar.AlarmReceiver;
import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.notendur.UserFunctions;
import com.example.folkmedhar.pantanir.bokun.Bokadir;
import com.example.folkmedhar.pantanir.bokun.BokadirLausir;
import com.example.folkmedhar.pantanir.bokun.Lausir;
import com.example.folkmedhar.pantanir.bokun.Skref2;

public class FerlaBokun {
	
	// Upplýsingar um bókun
	private static String nafn, simi, adgerd, harlengd, email;
		
	// Tímasetning pöntunar:
	//    bokunarDagur er á forminu "26-11-2014", bokunarDate á forminu "2014-11-26" og
	//    startDate og endDate á forminu "2014-11-26 09:00"
	private static  String bokunarTimi, bokunarDagur, lengdAdgerdar, 
	bokunarDate, startDate, endDate;
	
	// Upplýsingar um starfsmann
	private static String staff_id, starfsmadur;
	
	// Fjoldi starfsmanna sem vinna á stofunni
	// Athugasemd: Æskilegra væri að sækja fjölda starfsmanna úr gagnagrunni
	private static int fjoldiStarfsmanna = 7; 
	
	// Fjöldi tímalota („timeslot“) sem stofan býður upp á þar sem hver lota
	// er 30 mínútur
	// Athugasemd: Æskilegra væri að sækja fjölda tímalota úr gagnagrunni
	private static int fjoldiTima = 18;
	
	// Staðsetning vals í Spinner viðmótshlut
	private static int starfsmadurPos, adgerdPos, harlengdPos;
	
	// Breytan er notuð til að halda utan hvort pöntun hafi verið
    // bókuð rétt áður en ýtt er á "back" takkann
	private static boolean bokudPontun;
		
	// Heldur utan um bókaða og lausa tíma fyrir alla
	// starfsmenn
	private static BokadirLausir[] bokadirStarfsmenn;
	
	// Auðkenni allra starfsmanna
	// Athugasemd: Æskilegra væri að sækja auðkenni starfsmanna úr
	//             gagnagrunni
	private static String[] allirStarfsmenn = {"BOB","PIP","ODO","MRV","EDK","BIP","DOR"};
	
	// Heldur utan um hvaða tímar eru lausir og hjá hvaða starfsmanni
	private static Lausir[] lausirTimarHeild;
	
	// Fjöldi lausra tíma
	private static int lausirNum;
		
	// Tímasetning lausra tíma
	private static String[] lausirTimi;
	
	private static String pontunarId; // Auðkenni pöntunar
	private static String pontunText = ""; // Upplýsingar um pöntun
	
	// Dagsetning næstu pöntunar
	private static String naestaPontunManudur, naestaPontunAr, naestaPontunDay; 
	
	// Heldur utan um aðgerð og tímalengd hennar
	private static HashMap<String, String> mapLengd = new HashMap<String, String>();
	
	// Heldur utan um mánuð á forminu "01" og samsvarandi streng á forminu "Jan"
	private static HashMap<String, String> mapManudur = new HashMap<String, String>();
	
	// Dagsetning og tími áminningar
	private static int reminderAr, reminderManudur, reminderDagur;
	private static String reminderTime;
	
	/**
	 * Breytur sem halda utan um notendaupplýsingar er gefið gildi sem sótt 
	 * er úr gagnagrunni símans og tilviksbreytur upphafsstilltar.
	 */
	public FerlaBokun () {
		
		setLengdMap();
		setManudurMap();
		
		 // Sækja upplýsingar um notanda úr gagnagrunni símans
     	nafn = UserFunctions.getUserName(MainActivity.getContext());
     	simi = UserFunctions.getUserPhone(MainActivity.getContext());
     	email = UserFunctions.getUserEmail(MainActivity.getContext());
        
     	bokudPontun = false;
        bokunarTimi = "";
	}
	
	/**
	 * Býr til hakkatöflu þar sem hvert hólf heldur utan um tegund aðgerðar
	 * og tímalengd hennar
	 */
	private void setLengdMap() {
		
		// Athugasemd: Æskilegra væri að sækja aðgerð og tímalengd
		//             úr gagnagrunni
		mapLengd.put("Dömuklipping", "2");
		mapLengd.put("Herraklipping", "1");
		mapLengd.put("Barnaklipping", "1");
		mapLengd.put("Heillitun", "2"); 
		mapLengd.put("Litur í rót", "2"); 
		mapLengd.put("Dömuklipping og litun/strípur", "4"); 
		mapLengd.put("Herraklipping og litun/strípur", "3");
		mapLengd.put("Greiðsla", "2");
		mapLengd.put("Permanett", "4");
		mapLengd.put("Blástur", "2");
		mapLengd.put("Strípur", "3");
		mapLengd.put("Litun og strípur", "3");
	}
	
	/**
	 * Býr til hakkatöflu þar sem hvert hólf heldur utan um mánuð á forminu
	 * "01" og samsvarandi streng á forminu "Jan"
	 */
	private void setManudurMap() {
		
		mapManudur .put("01", "JAN");
		mapManudur .put("02", "FEB");
		mapManudur .put("03", "MARS");
		mapManudur .put("04", "APRÍL"); 
		mapManudur .put("05", "MAÍ"); 
		mapManudur .put("06", "JÚNÍ"); 
		mapManudur .put("07", "JÚLÍ");
		mapManudur .put("08", "ÁGÚST");
		mapManudur .put("09", "SEPT");
		mapManudur .put("10","OKT");
		mapManudur .put("11","NÓV");
		mapManudur .put("12","DES");
	}
	
	/**
	 * Skilar true ef að tíminn timi er liðinn miðað við daginn
	 * í dag
	 * @param timi
	 * @return
	 */
	private static boolean timiLidinn(String timi) {
		
		Locale locale = new Locale("IS");
		
		SimpleDateFormat format = new SimpleDateFormat("HHmm",locale);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timiNuna = format.format(Calendar.getInstance().getTime());
		
		String timeSpinner = timi.substring(0,2) + timi.substring(3);
		
		format = new SimpleDateFormat("yyyyMMdd",locale);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dagurNuna = format.format(Calendar.getInstance().getTime());
		
		String dagurSpinner = getDate();
		dagurSpinner = dagurSpinner.substring(0,4) + dagurSpinner.substring(5,7) +
				dagurSpinner.substring(8);

		if(dagurSpinner.equals(dagurNuna)) {
			// Skilað false ef tíminn er liðinn eða ef fyrirvarinn er ekki nógu langur
			int timeTo = Integer.parseInt(timeSpinner)- Integer.parseInt(timiNuna);
			if(timeTo < 0 || timeTo < 60) {
				// Athugasemd: Æskilegra væri að sækja lengd lágmarks fyrirvara
				//             úr gagnagrunni
				return true;
			}
			
		}
		return false;	
	}
	
	/**
	 * Upphafsstillir breytur sem halda utan um lausa og bókaða tíma
	 * starfsmanna
	 */
	public static void setTimar() {
		
		bokadirStarfsmenn = new BokadirLausir[fjoldiStarfsmanna];
		for (int i = 0; i<fjoldiStarfsmanna; i++) {
			bokadirStarfsmenn[i] = new BokadirLausir(new Bokadir[fjoldiTima],new Lausir[fjoldiTima]);
		}
		
		
		// Hvert sæti í bokadirStarfsmenn (eitt fyrir hvern starfsmann) er 
		// fyllt af Bokadir hlut sem heldur utan um auðkenni tiltekins starfsmanns
		// og tímasetningu frá 9:00 - 17:30. Allir tímar eru upphafsstilltir sem lausir
		for (int j = 0; j<fjoldiStarfsmanna; j++) {
			// Athugasemd: Æskilegra væri að sækja tímasetningar 
			//			   úr gagnagrunni
			bokadirStarfsmenn[j].laust[0] = new Lausir("09:00",true,allirStarfsmenn[j]);
			bokadirStarfsmenn[j].laust[1] = new Lausir("09:30", true,allirStarfsmenn[j]);
			int b = 9;
					
			for(int i = 2; i<bokadirStarfsmenn[j].laust.length-1; i = i+2) {
				b = b+1;
				String s = b + ":00";
				bokadirStarfsmenn[j].laust[i] = new Lausir(s, true,allirStarfsmenn[j]);
				s = b + ":30";
				bokadirStarfsmenn[j].laust[i+1]  = new Lausir(s,true,allirStarfsmenn[j]);
			}
			
		}
		
		lausirTimi = new String[fjoldiTima];
		lausirTimarHeild = new Lausir[fjoldiTima];
		
		// Höfum ekki enn fundið neina lausa tíma svo öll sætin í lausirTimarHeild
		// eru fyllt af Lausir hlut sem inniheldur null í öllum tilviksbreytum
		for (int i = 0; i<lausirTimarHeild.length; i++) {	
			lausirTimarHeild[i]  = new Lausir(null,true,null);
		}
	}
	
	/**
	 * Sækir allar bókaða tíma á valinni dagsetningu úr gagnagrunni
	 * og setur þá í fylki bókaðra tíma
	 * @param id
	 * @param bokadir
	 * @param num
	 * @throws JSONException
	 */
	public static void getBokadirTimar(String id, JSONArray bokadir, int num) throws JSONException {
		
		
		// Ákveðinn starfsmaður var valinn, sæki bókaða tíma hans
		// úr gagnagrunni. Auðkenni hans er í breytunni MainActivity.staff_id
		if(num==-1){
			for(int i = 0; i < bokadir.length(); i++){
				JSONObject timi = bokadir.getJSONObject(i);
				bokadirStarfsmenn[0].bokad[i] = 
						new Bokadir(timi.getString("time"), timi.getInt("lengd"),timi.getString("staff_id"));	
			}
		}
		
		else {
			int j = 0;
			for(int i = 0; i < bokadir.length(); i++){
				JSONObject timi = bokadir.getJSONObject(i);
				// Sæki bókaða tíma úr gagnagrunni fyrir þann starfsmann sem
				// sem á auðkennið sem kallaði á aðerðina
				if (timi.getString("staff_id").equals(id)){
					bokadirStarfsmenn[num].bokad[j] = 
							new Bokadir(timi.getString("time"), timi.getInt("lengd"),timi.getString("staff_id"));
					j++;
				}
			}
		}
		
	}
	
	/**
	 * Þeir tímar sem eru bókaðir fá gildið false í fylki lausra tíma sem merkir
	 * að þeir séu ekki lausir
	 * @param a
	 * @param b
	 */
	private static void lausirTimar(Bokadir[] a, Lausir[] b) {

		for(int i = 0; i<a.length; i++) {
			for(int j = 0; j<b.length; j++) {
				if(a[i]!=null){
					// Tíminn er bókaður
					if(a[i].getTimi().equals(b[j].getTimi())) {
						// Skrái samliggjandi tíma sem bókaða miðað
						// við tímalengd aðgerðar
						for(int k = 0; k<a[i].getLengd(); k++) {
							b[j+k].setLaus(false);
							}
						}
					}
				}
			}
		}
	
	/**
	 * Setur lausa tíma sem valmöguleika í Spinner viðmótshlut
	 * @param lausirTimar
	 */
	private static void setLausirTimar(Lausir[] lausirTimar) {
		int timaLengd = Integer.parseInt(getLengd());
		
		int n = lausirTimar.length;

		for(int i = 0; i<n; i++) {
			
			// Tíminn er laus og hefur ekki áður verið skráður laus hjá öðrum
			// starfsmanni
			if(lausirTimar[i].isLaus() && lausirTimarHeild[i].getTimi()==null) {
				// tíminn er ekki liðinn og að minnsta kosti 30 mínútur
				// eru í hann
				if(!FerlaBokun.timiLidinn(lausirTimar[i].getTimi())) {
					boolean laust = true;
					int j = 0;
					// Athuga hvort að fjöldi samliggjandi lausra tíma sé nógu mikill
					// fyrir tímalengd aðgerðar
					if( (i+timaLengd) > n) {
						break;
					}
					while(j<timaLengd && (j+i) < n) {
						if (!lausirTimar[j+i].isLaus()) {
							laust = false;
							break;
						}
						j++;
					}
					
					// Fjöldi samliggjandi lausra tíma var nógu mikill fyrir
					// tímalengd aðgerðar og uppfæri fjölda lausra tíma
					if (laust) {
						
						// Skrái tímann sem lausan ásamt auðkenni starfsmannsins
						lausirTimarHeild[i].setTimi(lausirTimar[i].getTimi());
						lausirTimarHeild[i].setId(lausirTimar[i].getId());
						lausirTimi[lausirNum] = lausirTimar[i].getTimi();	
						lausirNum++;
					
					}
				}
			}
		}
	}
	
	/**
	 * Uppfærir breytur sem halda utan um staðsetningu tímans 
	 * sem var valinn í Spinnar viðmótshlut. Kallar á aðferð sem að uppfærir auðkenni starfsmanns
	 * ef enginn sérstakur starfsmaður var valinn.
	 */
	public static boolean bokun() {
		// Engin dagsetning valin
    	if(getDate()==null || getTime().equals("Veldu tíma")) {
    		return false;
    	} 
    	
    	else {
    		setDateInfo();
    		// Notandanum er sama um hver starfsmaðurinn er
    		if(getStaffId().equals("000")) {
    			staffId(); // Sækja auðkenni starfsmannsins sem var úthlutað
    			              // tímanum sem var valinn
    		}
    		return true;
    	}
	}
	
	/**
	 * Gefur breytum sem halda utan um þann tíma sem notandinn valdi
	 * rétt gildi
	 */
	private static void setDateInfo() {
		Lausir[] lausirTimarHeild = FerlaBokun.getLausirTimarHeild();
		Spinner timiSpinner = Skref2.getTimiSpinner();
		String time = timiSpinner.getSelectedItem().toString();
		String date = getDate();
		String lengd = getLengd();

		setTime(time);
		
		int timaLengd = Integer.parseInt(lengd);
		String endTime ="";
		
		// Reikna klukkan hvað tíminn endar miðað við valda aðgerð
		for(int j = 0; j<lausirTimarHeild.length; j++) {
			// Fann tímann
			if(time.equals(lausirTimarHeild[j].getTimi())) {
				// Bæti tímalengdina við tímann og fæ endatíma
				if(j+timaLengd >= fjoldiTima-1) {
					endTime = "18:00";
					// Athugasemd: Æskilegra væri að sækja endTime
					//             úr gagnagrunni
				}
				else {
					endTime = lausirTimarHeild[j+timaLengd].getTimi();
				}
			}
		}
		
		setStartEndDate(date + " " + time, date + " " +  endTime);

	}
	
	/**
	 * Gefur auðkenni starfsmanns rétt gildi ef enginn sérstakur starfsmaður var valinn,
	 * það er að segja auðkenni starfsmannsins sem á tímann sem var valinn
	 */	
	private static void staffId() {
		Lausir[] lausirTimarHeild = FerlaBokun.getLausirTimarHeild();
		
		for(int i = 0; i<lausirTimarHeild.length; i++) {
			if(lausirTimarHeild[i].getTimi()!=null){
				
				// Fann tímann
				if(lausirTimarHeild[i].getTimi().equals(getTime())) {
					
					// Uppfæri hvaða starfsmanni var úthlutaður tíminn
					setStaffId(lausirTimarHeild[i].getId());
					setStarfsmadur(getStarfsmadur(getStaffId()));
					}
				}
			}
		}
	
	/**
	 * Finnur lausa tíma þess starfsmanns sem var valinn, ef enginn
	 * starfsmaður var valinn eru lausir tímar allra starfsmanna fundnir
	 */
	public static void finnaAllaLausaTima() {
		// Ákveðinn starfsmaður var valinn
		if(!getStaffId().equals("000")){
			// Finn lausa tíma starfsmannsins út frá bókuðum tímum hans
			lausirTimar(bokadirStarfsmenn[0].bokad,bokadirStarfsmenn[0].laust);
			// Birti lausa tíma svo notandinn geti valið úr þeim
			setLausirTimar(bokadirStarfsmenn[0].laust);
		}
		
		else {
			// Notandanum er sama um hver stafsmaðurinn er
			// Finn lausa tíma allra stafsmanna út frá bókuðum tímum þeirra
			for (int i = 0; i< fjoldiStarfsmanna; i++) {
				lausirTimar(bokadirStarfsmenn[i].bokad,bokadirStarfsmenn[i].laust);
				setLausirTimar(bokadirStarfsmenn[i].laust);	
			}
		}
					
	}
	
	/**
	 * Skilar fylki af lausum tímum
	 * @return
	 */
	public static String[] getTimeSpinner() {
		String[] spinnerTimar;
		spinnerTimar = new String[lausirNum];
		for (int i = 0; i<spinnerTimar.length; i++) {
			spinnerTimar[i] = lausirTimi[i];
		}
		
		java.util.Arrays.sort(spinnerTimar);
		return spinnerTimar;
	}
	
	/**
	 * Skilar id starfsmanns i
	 * @param i
	 * @return
	 */
	public static String getStarfsmenn(int i) {
		return allirStarfsmenn[i];
	}

	
	/**
	 * Breyta sem heldur utan um fjölda lausra tíma
	 * fær gildið num
	 * @param num
	 */
	public static void setLausirNum(int num) {
		lausirNum = num;
	}
	
	/**
	 * Skilar fjölda lausra tima
	 * @return
	 */
	public static int getLausirNum() {
		return lausirNum;
	}
	
	/**
	 * Skilar fylki sem heldur utan um hvaða tímar eru lausir 
	 * og hjá hvaða starfsmanni
	 * @return
	 */
	private static Lausir[] getLausirTimarHeild() {
		return lausirTimarHeild;
	}
	
	/**
     * Breytan sem heldur utan um auðkenni valins starfsmanns
     * fær rétt gildi
     * Athugasemd: Æskilegra væri að sækja auðkenni og nöfn starfsmanna
     *             úr gagnagrunni
     */
    public static void setStaffId() {
    	
    	String starfsmadur = getStarfsmadur();
    	
    	switch(starfsmadur) {
    	case "Hver sem er":
    		setStaffId("000");
    		break;
		case "Bambi": 
			setStaffId("BOB");
			break;
		case "Perla" : 
			setStaffId("PIP");
			break;
		case "Oddur" : 
			setStaffId("ODO");
			break;
		case "Magnea" : 
			setStaffId("MRV");
			break;
		case "Eva" :
			setStaffId("EDK");
			break;
		case "Birkir" : 
			setStaffId("BIP");
			break;
		case "Dagný" : 
			setStaffId("DOR");
			break;
		default: setStaffId("ERR");
		
    	}
    }
    
    /**
     * Breytan sem heldur utan um tímalengd aðgerðar fær rétt gildi
     * 
     */
    public static void setTimaLengd() {
    	String adgerd = getAdgerd();
    	setLengd(mapLengd.get(adgerd));
    }
    
    /**
	 * Breyturnar fyrir fyrir hárlengd, aðgerð og starfsmann fá
	 * valin gildi og breytur sem halda utan um staðsetningu þeirra í
	 * Spinner viðmótshluti einnig
	 */
	public static void setBokunarUpplysingar(Spinner sSpinner, Spinner aSpinner, Spinner hSpinner ) {
		
		// Valinn starfsmaður
		setStarfsmadur(sSpinner.getSelectedItem().toString());
    	setStarfsmadurPos(sSpinner.getSelectedItemPosition());
    	
    	// Valin aðgerð
    	setAdgerd(aSpinner.getSelectedItem().toString());
		setAdgerdPos(aSpinner.getSelectedItemPosition());
		
		// Valin hárlengd
		setHarlengd(hSpinner.getSelectedItem().toString());
		setHarlengdPos(hSpinner.getSelectedItemPosition());
	} 
	
	/**
	 * Skilar mánuði á forminu "Jan" fyrir steng á forminu "01"
	 * @param s
	 * @return
	 */
	private static String parseManudur(String s){
		return mapManudur.get(s);
	}
	
	/**
	 * Skilar upplýsingum um pöntun notandans
	 * @return
	 */
	public static String getPontun() {
		return pontunText;
	}
	
	/**
	 * Skilar árinu sem næsta pöntun var bókuð á
	 * @return
	 */
	public static String getNaestaPontunAr() {
		return naestaPontunAr;
	}
	
	/**
	 * Skilar mánuðinum sem næsta pöntun var bókuð á
	 * @return
	 */
	public static String getNaestaPontunManudur() {
		return naestaPontunManudur;
	}	
	
	/**
	 * Skilar auðkenni pöntunar
	 * @return
	 */
	public static String getID() {
		return pontunarId;
	}
	
	/**
	 * Gefur breytu sem heldur utan um upplýsingar um pöntun
	 * notandans gildið text
	 * @param text
	 */
	public static void setPontun(String pontun) {
		pontunText = pontun;
	}
	
	/**
	 * Gefur breytu sem heldur utan um hvaða ár næsta pöntun var bókuð á
	 * gildið ar
	 * @param ar
	 */
	public static void setNaestaPontunAr(String ar) {
		naestaPontunAr = ar;
	}
	
	/**
	 * Gefur breytu sem heldur utan um mánuðinn sem næsta pöntun var bókuð á
	 * gildið m
	 * @param m
	 */
	public static void setNaestaPontunManudur(String m) {
		naestaPontunManudur = parseManudur(m);
	}
	
	
	/**
	 * Gefur breytu sem heldur utan um auðkenni pöntunar
	 * gildið id
	 * @param id
	 */
	public static void setID(String id) {
		pontunarId = id;
	}

	/**
	 * Gefur breytu sem heldur utan um hvort nýlega hafi verið bókuð pöntun
	 * gildi
	 * @param b
	 */
	public static void setBokudPontun(boolean b) {
		bokudPontun = b;
	}
	
	/**
	 * /**
	 * Skilar gildi breytu sem heldur utan um hvort nýlega hafi verið bókuð pöntun
	 * @return boolean
	 */
	public static boolean getBokudPontun() {
		return bokudPontun;
	}
	
	/**
	 * Skilar nafni þess starfsmanns sem á auðkennið s
	 * @param s
	 * @return String
	 * Athugasemd: Æskilegra væri að sækja nöfn og auðkenni starfsmanna
	 *             úr gagnagrunni
	 */
	public static String getStarfsmadur(String s) {
		
		String starfsmadur;
		
		switch(s) {
			case "BOB": 
				starfsmadur = "Bambi";
				break;
			
			case "PIP" : 
				starfsmadur = "Perla";
				break;
			
			case "ODO" : 
				starfsmadur = "Oddur";
				break;
			
			case "MRV" : 
				starfsmadur= "Magnea";
				break;
			
			case "EDK" :
				starfsmadur = "Eva";
				break;
			
			case "BIP" : 
				starfsmadur = "Birkir";
				break;
			
			case "DOR" : 
				starfsmadur = "Dagný";
				break;
			
			default: starfsmadur = "Error";
			
		}
		return starfsmadur;
	}
	
	/**
	 * Skilar nafni notandans
	 * @return String
	 */
	public static String getName() {
		return nafn;
	}
	/**
	 * Skilar síma notandans
	 * @return String
	 */
	public static String getSimi() {
		return simi;
	}
	/**
	 * Skilar netfangi notandans
	 * @return String
	 */
	public static String getEmail() {
		return email;
	}
	
	/**
	 * Gefur breytunni sem heldur utan um tíma bókunar gildi
	 * @param t
	 */
	public static void setTime(String t) {
		bokunarTimi = t;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um tíma bókunar gildi
	 * @return String
	 */
	public static String getTime() {
		bokunarTimi = Skref2.getTimiSpinner().getSelectedItem().toString();
		return bokunarTimi;
	}
	
	/**
	 * Gefur breytunum sem halda utan um dagsetningu bókunar
	 * gildi
	 * @param b
	 * @param e
	 */
	public static void setStartEndDate(String b, String e) {
		startDate = b;
		endDate = e;
	}
	
	/**
	 * Skilar dagsetningu bókunar á forminu "2014-11-17"
	 * @return
	 */
	public static String getDate() {
		return bokunarDate;
	}
	
	/**
	 * Gefur breytu sem heldur utan um dagsetningu bókunar á
	 * forminu "2014-11-17" gildi
	 * @param d
	 */
	public static void setDate(String d) {
		bokunarDate = d;
	}
	
	/**
	 * Gefur breytu sem heldur utan um dagsetningu bókunar á
	 * forminu "17-11-2014" gildi
	 * @param d
	 */
	public static void setStringDate(String d) {
		bokunarDagur = d;
	}
	
	/**
	 * Gefur breytu sem heldur utan um dag næstu pöntunar
	 * gildið d
	 * @param d
	 */
	public static void setNaestaPontunDay(String d) {
		naestaPontunDay = d;
	}
	
	/**
	 * Skilar dagsetningu bókunar á forminu "17-11-2014"
	 * @return String
	 */
	public static String getStringDate() {
		return bokunarDagur;
	}
	
	/**
	 * Skilar degi næstu pöntunar
	 * @return String
	 */
	public static String getNaestaPontunDay() {
		return naestaPontunDay;
	}
	
	/**
	 * Skilar byrjunar dagsetningu bókunar á forminu "2014-11-17 09:00"
	 * @return String
	 */
	public static String getStartDate() {
		return startDate;
	}
	
	/**
	 * Skilar enda dagsetningu bókunar á forminu "2014-11-17 09:00"
	 * @return String
	 */
	public static String getEndDate() {
		return endDate;
	}
	
	/**
	 * Skilar tímalengd aðgerðarinnar sem var valin
	 * @return String
	 */
	public static String getLengd() {
		return lengdAdgerdar;
	}
	
	/**
	 * Gefur breytu sem heldur utan um tímalengd 
	 * aðgerðar gildi
	 * @param l
	 */
	public static void setLengd(String l) {
		lengdAdgerdar = l;
	}
	
	/**
	 * Gefur breytu sem heldur utan um hvaða starfsmaður 
	 * var valinn gildi
	 * @param s
	 */
	public static void setStarfsmadur(String s) {
		starfsmadur = s;
	}
	
	/**
	 * Gefur breytu sem heldur utan um auðkenni valins stafsmanns
	 * @param id
	 */
	public static void setStaffId(String id) {
		staff_id = id;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um auðkenni valins stafsmanns
	 * @return String
	 */
	public static String getStaffId() {
		return staff_id;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um hvaða starfsmaður 
	 * var valinn gildi
	 * @return String
	 */
	public static String getStarfsmadur() {
		return starfsmadur;
	}
	
	/**
	 * Gefur breytu sem heldur utan um hvaða aðgerð 
	 * var valinn gildi
	 * @param a
	 */
	public static void setAdgerd(String a) {
		adgerd = a;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um hvaða aðgerð
	 * var valinn gildi
	 * @return String
	 */
	public static String getAdgerd() {
		return adgerd;
	}
		
	/**
	 * Skilar gildi breytu sem heldur utan um hvaða hárlengd
	 * var valinn gildi
	 * @return String
	 */
	public static String getHarlengd() {
		return harlengd;
	}
	
	/**
	 * Gefur breytu sem heldur utan um hvaða hárlengd 
	 * var valinn gildi
	 * @param l
	 */
	public static void setHarlengd(String l) {
		harlengd = l;
	}
	
	/**
	 * Gefur breytu sem heldur utan um staðsetningu valins 
	 * stafsmanns í Spinner viðmótshlut
	 * @param i
	 */
	public static void setStarfsmadurPos(int i) {
		starfsmadurPos = i;
	}
	
	/**
	 * Gefur breytu sem heldur utan um staðsetningu valinnar 
	 * aðgerðar í Spinner viðmótshlut
	 * @param i
	 */
	public static void setAdgerdPos(int i) {
		adgerdPos = i;
	}
	
	/**
	 * Gefur breytu sem heldur utan um staðsetningu valinnar 
	 * hárlengdar í Spinner viðmótshlut
	 * @param i
	 */
	public static void setHarlengdPos(int i) {
		harlengdPos = i;
	}
	
	
	/**
	 * Skilar gildi breytu sem heldur utan um staðsetningu valins 
	 * stafsmanns í Spinner viðmótshlut
	 * @return int
	 */
	public static int getStarfsmadurPos() {
		return starfsmadurPos;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um staðsetningu valinnar 
	 * aðgerðar í Spinner viðmótshlut
	 * @return int
	 */
	public static int getAdgerdPos() {
		return adgerdPos;
	}
	
	/**
	 * Skilar gildi breytu sem heldur utan um staðsetningu valinnar 
	 * hárlengdar í Spinner viðmótshlut
	 * @return int
	 */
	public static int getHarlengdPos() {
		return harlengdPos;
	}
	
	/**
	 * Skilar true ef year er hlaupár, false annars
	 * @param year
	 * @return
	 */
	public static boolean leapYear(int year )
	{
	    if( year%400==0 ) return true;
	    if( year%100==0 ) return false;
	    if( year%4==0 ) return true;
	    return false;
	}
	
	/**
	 * Uppfærir breytur sem halda utan um mánaðardag bókunar sem áminning
	 * er skráð á ef mánaðardagur bókunar er fyrsti mánaðarins
	 */
	public static void erFyrstiManadarins(){
        if(reminderDagur==01){
        	if(reminderManudur==01){
        		reminderAr=reminderAr-1;
        		reminderManudur=12;
        	}
        	if(reminderManudur==4 || reminderManudur==6 || reminderManudur==9 || reminderManudur==11){
        		reminderDagur=30;
        	}
        	else if(reminderManudur==2 && leapYear(reminderAr)){
        		reminderDagur=29;
        	}
        	else if(reminderManudur==2 && !leapYear(reminderAr)){
        		reminderDagur=28;
        	}
        	else{
        		reminderDagur=31;
        	}
        	if(reminderManudur!=12)
        	reminderManudur--;
        }
        else{
        	reminderDagur--;
        }
    }
	
	/**
	 * Býr til nýja áminningu fyrir bókunina
	 */
	public static void scheduleReminder(Context context)
    {

        Calendar myAlarmDate = Calendar.getInstance();
        myAlarmDate.setTimeInMillis(System.currentTimeMillis());
        erFyrstiManadarins();
        myAlarmDate.set(reminderAr, reminderManudur, 11, 18, 00, 0);
        Long time = myAlarmDate.getTimeInMillis();
     
        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(context,1,  
        		intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            
    }
	
	/**
	 * Gefur tímasetningu áminningar gildið
	 * time
	 * @param bokunarTimi
	 */
	public static void setTimeReminder(String bokunTimi, Context context) {
		SharedPreferences prefs = context.getSharedPreferences("BokunTimi", 0);
		prefs.edit().putString("bokunTimi", bokunTimi).commit();
	}
	
	/**
	 * Gefur breytu sem heldur utan um á hvaða degi áminning
	 * pöntunar á að koma
	 * gildið d
	 * @param d
	 */
	public static void setReminderDagur(int d) {
		reminderDagur = d;
	}
	
	/**
	 * Gefur breytu sem heldur utan um í hvaða mánuði 
	 * áminnngin pöntunar á að koma
	 * gildið m
	 * @param m
	 */
	public static void setReminderManudur(int m) {
		reminderManudur = m;
	}
	
	/**
	 * Gefur breytu sem heldur utan um á hvaða ári áminning pöntunar á 
	 * að koma
	 * gildið a
	 * @param a
	 */
	public static void setReminderAr(int a) {
		reminderAr = a;
	}
	
	/**
	 * Skilar þeim degi sem pöntun var bókuð á
	 * @return
	 */
	public static int getReminderDagur() {
		return reminderDagur;
	}
	
	/**
	 * Skilar þeim mánuði sem áminning pöntunar á að koma í
	 * @return
	 */
	public static int getReminderManudur() {
		return reminderManudur;
	}
	
	/**
	 * Skilar því ári sem áminning pöntunar á að koma á
	 * @return
	 */
	public static int getReminderAr() {
		return reminderAr;
	}
	
	/**
	 * Skilar þeim tíma sem áminning hefur verið skráð
	 * á
	 * @return
	 */
	public static String getReminderTime() {
		return reminderTime;
	}
	
	/**
	 * Gefur breytu sem heldur utan tímann sem áminning er skráð á gildið
	 * t
	 * @param t
	 */
	public static void setReminderTime(String t) {
		reminderTime = t;
	}
}
