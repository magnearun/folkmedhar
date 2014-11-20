/**
 * @author: Eva Dögg Steingrímsóttir og Magnea Rún Vignisdóttir
 * @since: 19.11.2014
 * Klasinn sér um að gefa breytunum sem halda utan um tímasetningu bókunar gildi. Hann sér 
 * auk þess um að sækja lausa tíma á valinni dagsetningu úr gagnagrunni.
 */
package com.example.folkmedhar.pantanir;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Spinner;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.pantanir.bokun.Bokadir;
import com.example.folkmedhar.pantanir.bokun.BokadirLausir;
import com.example.folkmedhar.pantanir.bokun.Lausir;
import com.example.folkmedhar.pantanir.bokun.Skref2;

public class FerlaBokun {
	
	// Heldur utan um bókaða og lausa tíma fyrir alla
	// starfsmenn
	private static BokadirLausir[] bokadirStarfsmenn;
	
	// Auðkenni allra starfsmanna
	// Sækja frá öðrum stað + randomize MISSING
	private static String[] starfsmenn = {"BOB","PIP","ODO","MRV","EDK","BIP","DOR"};
	
	// Heldur utan um hvaða tímar eru lausir og hjá hvaða starfsmanni
	private static Lausir[] lausirTimarHeild;
	
	// Fjöldi lausra tíma
	private static int lausirNum;
		
	// Tímasetning lausra tíma
	private static String[] lausirString;
	
	private static String pontunarId; // Auðkenni pöntunar
	private static String pontunText = ""; // Upplýsingar um pöntun
	private static String manudur, dagur, ar; // Dagsetning pöntunar
	
	/**
	 * Skilar true ef að tíminn timi er liðinn miðað við daginn
	 * í dag
	 * @param timi
	 * @return
	 */
	public static boolean timiLidinn(String timi) {
		
		Locale locale = new Locale("IS");
		
		SimpleDateFormat format = new SimpleDateFormat("HHmm",locale);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timiNuna = format.format(Calendar.getInstance().getTime());
		
		// Gefa starfsmönnum nógu langan fyrirvara
		String timeSpinner = timi.substring(0,2) + timi.substring(3);
		

		format = new SimpleDateFormat("yyyMMdd",locale);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dagurNuna = format.format(Calendar.getInstance().getTime());
		
		String dagurSpinner = MainActivity.getDate();
		dagurSpinner = dagurSpinner.substring(0,4) + dagurSpinner.substring(5,7) +
				dagurSpinner.substring(8);

		if(dagurSpinner.equals(dagurNuna)) {
			// Skilað false ef tíminn er liðinn eða ef fyrirvarinn er ekki nógu langur
			int timeTo = Integer.parseInt(timeSpinner)- Integer.parseInt(timiNuna);
			if(timeTo < 0 || timeTo < 60) {
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

		// Hér þarf að sækja frekar lengdina á því sem heldur utan um alla
		// starfsmenn, MISSING
		bokadirStarfsmenn = new BokadirLausir[7];
		for (int i = 0; i<7; i++) {
			bokadirStarfsmenn[i] = new BokadirLausir(new Bokadir[18],new Lausir[18]);
		}
		
		
		// Hvert sæti í bokadirStarfsmenn (eitt fyrir hvern starfsmann) er 
		// fyllt af Bokadir hlut sem heldur utan um auðkenni tiltekins starfsmanns
		// og tímasetningu frá 9:00 - 17:30. Allir tímar eru upphafsstilltir sem lausir
		for (int j = 0; j<7; j++) {
			bokadirStarfsmenn[j].laust[0] = new Lausir("09:00",true,starfsmenn[j]);
			bokadirStarfsmenn[j].laust[1] = new Lausir("09:30", true,starfsmenn[j]);
			int b = 9;
					
			for(int i = 2; i<bokadirStarfsmenn[j].laust.length-1; i = i+2) {
				b = b+1;
				String s = b + ":00";
				bokadirStarfsmenn[j].laust[i] = new Lausir(s, true,starfsmenn[j]);
				s = b + ":30";
				bokadirStarfsmenn[j].laust[i+1]  = new Lausir(s,true,starfsmenn[j]);
			}
			
		}
		
		lausirString = new String[18];
		lausirTimarHeild = new Lausir[18];
		
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
		
		Log.e("Bókaðir", bokadir.length()+"");
		
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
	public static void lausirTimar(Bokadir[] a, Lausir[] b) {

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
	public static void setLausirTimar(Lausir[] lausirTimar) {
		int timaLengd = Integer.parseInt(MainActivity.getLengd());
		
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
						lausirString[lausirNum] = lausirTimar[i].getTimi();	
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
    	if(MainActivity.getDate()==null || MainActivity.getTime().equals("Timi")) {
    		return false;
    	} 
    	
    	else {
    		setDateInfo();
    		// Notandanum er sama um hver starfsmaðurinn er
    		if(MainActivity.getStaffId().equals("000")) {
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
		String date = MainActivity.getDate();
		String lengd = MainActivity.getLengd();

		MainActivity.setTime(time);
		
		int timaLengd = Integer.parseInt(lengd);
		String endTime ="";
		
		// Reikna klukkan hvað tíminn endar miðað við valda aðgerð
		for(int j = 0; j<lausirTimarHeild.length; j++) {
			// Fann tímann
			if(time.equals(lausirTimarHeild[j].getTimi())) {
				// Bæti tímalengdina við tímann og fæ endatíma
				if(j+timaLengd >= 17) {
					endTime = "18:00";
				}
				else {
					endTime = lausirTimarHeild[j+timaLengd].getTimi();
				}
			}
		}
		
		MainActivity.setStartEndDate(date + " " + time, date + " " +  endTime);

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
				if(lausirTimarHeild[i].getTimi().equals(MainActivity.getTime())) {
					
					// Uppfæri hvaða starfsmanni var úthlutaður tíminn
					MainActivity.setStaffId(lausirTimarHeild[i].getId());
					MainActivity.setStarfsmadur(MainActivity.getStarfsmadur(MainActivity.getStaffId()));
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
		if(!MainActivity.getStaffId().equals("000")){
			// Finn lausa tíma starfsmannsins út frá bókuðum tímum hans
			lausirTimar(bokadirStarfsmenn[0].bokad,bokadirStarfsmenn[0].laust);
			// Birti lausa tíma svo notandinn geti valið úr þeim
			setLausirTimar(bokadirStarfsmenn[0].laust);
		}
		
		else {
			// Notandanum er sama um hver stafsmaðurinn er
			// Finn lausa tíma allra stafsmanna út frá bókuðum tímum þeirra
			for (int i = 0; i< 7; i++) {
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
			spinnerTimar[i] = lausirString[i];
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
		return starfsmenn[i];
	}
	
	/**
	 * Skilar bókuðum tímum starfsmanns i
	 * @param i
	 * @return
	 */
	public static Bokadir[] getBokadirStarfsmenn(int i ) {
		return bokadirStarfsmenn[i].bokad;
	}
	
	/**
	 * Skilar lausa tímum starfsmanns i
	 * @param i
	 * @return
	 */
	public static Lausir[] getLausirStarfsmenn(int i) {
		return bokadirStarfsmenn[i].laust;
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
	 * Skilar tímasetningu lausa tímans i
	 * @param i
	 * @return
	 */
	public static String getLausirString(int i) {
		return lausirString[i];
	}
	
	/**
	 * Skilar fylki sem heldur utan um hvaða tímar eru lausir 
	 * og hjá hvaða starfsmanni
	 * @return
	 */
	public static Lausir[] getLausirTimarHeild() {
		return lausirTimarHeild;
	}
	
	/**
     * Breytan sem heldur utanum auðkenni valins starfsmanns
     * fær rétt gildi
     */
    public static void setStaffId() {
    	
    	String starfsmadur = MainActivity.getStarfsmadur();
    	
    	switch(starfsmadur) {
    	
    	case "Hver sem er":
    		MainActivity.setStaffId("000");
    		break;
		case "Bambi": 
			MainActivity.setStaffId("BOB");
			break;
		case "Perla" : 
			MainActivity.setStaffId("PIP");
			break;
		case "Oddur" : 
			MainActivity.setStaffId("ODO");
			break;
		case "Magnea" : 
			MainActivity.setStaffId("MRV");
			break;
		case "Eva" :
			MainActivity.setStaffId("EDK");
			break;
		case "Birkir" : 
			MainActivity.setStaffId("BIP");
			break;
		case "Dagný" : 
			MainActivity.setStaffId("DOR");
			break;
		default: MainActivity.setStaffId("ERR");
		
    	}
    }
    
    /**
     * Breytan sem heldur utan um tímalengd aðgerðar fær rétt gildi
     * 
     */
    public static void setTimaLengd() {
    	
    	String adgerd = MainActivity.getAdgerd();
    	
    	switch(adgerd) {
    	case "Dömuklipping":
    		MainActivity.setLengd("2");
    		break;
		case "Herraklipping": 
			MainActivity.setLengd("1");
			break;
		case "Barnaklipping" : 
			MainActivity.setLengd("1");
			break;
		case "Heillitun" : 
			MainActivity.setLengd("2");
			break;
		case "Litur í rót" : 
			MainActivity.setLengd("2");
			break;
		case "Strípur" :
			MainActivity.setLengd("3");
			break;
		case "Litun og strípur" : 
			MainActivity.setLengd("3");
			break;
		case "Dömuklipping og litun/strípur" : 
			MainActivity.setLengd("4");
			break;
		case "Herraklipping og litun/strípur" : 
			MainActivity.setLengd("3");
			break;
		case "Greiðsla" : 
			MainActivity.setLengd("2");
			break;
		case "Permanett" : 
			MainActivity.setLengd("4");
			break;
		case "Blástur" : 
			MainActivity.setLengd("2");
			break;
		default: MainActivity.setLengd("ERR");
		}
    }
    
    /**
	 * Breyturnar fyrir fyrir hárlengd, aðgerð og starfsmann fá
	 * valin gildi og breytur sem halda utan um staðsetningu þeirra í
	 * Spinner viðmótshluti einnig
	 */
	public static void setBokunarUpplysingar(Spinner sSpinner, Spinner aSpinner, Spinner hSpinner ) {
		
		// Valinn starfsmaður
		MainActivity.setStarfsmadur(sSpinner.getSelectedItem().toString());
    	MainActivity.setStarfsmadurPos(sSpinner.getSelectedItemPosition());
    	
    	// Valin aðgerð
    	MainActivity.setAdgerd(aSpinner.getSelectedItem().toString());
		MainActivity.setAdgerdPos(aSpinner.getSelectedItemPosition());
		
		// Valin hárlengd
		MainActivity.setHarlengd(hSpinner.getSelectedItem().toString());
		MainActivity.setHarlengdPos(hSpinner.getSelectedItemPosition());
	} 
	
	/**
	 * Skilar mánuði á forminu "Jan" fyrir steng á forminu "01"
	 * @param s
	 * @return
	 */
	public static String parseManudur(String s){
		String manudur;
		switch(s) {
    	
			case "01": 
				manudur = "JAN";
				break;
			
			case "02" : 
				manudur = "FEB";
				break;
			
			case "03" : 
				manudur = "MAR";
				break;
			
			case "04" : 
				manudur = "APR";
				break;
			
			case "05" :
				manudur = "MAY";
				break;
			
			case "06" : 
				manudur = "JUN";
				break;
			
			case "07" : 
				manudur = "JUL";
				break;
				
			case "08" : 
				manudur = "AUG";
				break;
				
			case "09" : 
				manudur = "SEP";
				break;
				
			case "10" : 
				manudur = "OKT";
				break;
				
			case "11" : 
				manudur = "NOV";
				break;
			
			case "12" : 
				manudur = "DES";
				break;
				
			default: manudur = "Error";
			
		}
		return manudur;
		
	}
	
	/**
	 * Skilar upplýsingum um pöntun notandans
	 * @return
	 */
	public static String getPontun() {
		return pontunText;
	}
	
	/**
	 * Skilar árinu sem pöntunin var bókuð á
	 * @return
	 */
	public static String getAr() {
		return ar;
	}
	
	/**
	 * Skilar mánuðinum sem pöntunin var bókuð á
	 * @return
	 */
	public static String getManudur() {
		return manudur;
	}
	
	/**
	 * Skilar deginum sem pöntunin var bókuð á
	 * @return
	 */
	public static String getDagur() {
		return dagur;
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
	 * Gefur breytu sem heldur utan um hvaða ár pöntunin var bókuð á
	 * gildið a
	 * @param a
	 */
	public static void setAr(String a) {
		ar = a;
	}
	
	/**
	 * Gefur breytu sem heldur utan um mánuðinn sem pöntunin var bókuð á
	 * gildið m
	 * @param m
	 */
	public static void setManudur(String m) {
		manudur = parseManudur(m);
	}
	
	/**
	 * Gefur breytu sem heldur utan um daginn sem pöntunin var bókuð á
	 * gildið d
	 * @param d
	 */
	public static void setDagur(String d) {
		dagur = d;
	}
	
	/**
	 * Gefur breytu sem heldur utan um auðkenni pöntunar
	 * gildið id
	 * @param id
	 */
	public static void setID(String id) {
		pontunarId = id;
	}
	
	public static void setPontunText(String text) {
		pontunText = text;
	}
}