/**
 * @author: http://www.androidhub4you.com/2012/10/custom-calendar-in-android.html
 * @since: 15.10.2014
 * Klasinn sér um að birta dagatal fyrir val á dagsetningu bókunar. Klasinn er 
 * að miklu leiti byggður á tutorial af netinu en breytingar voru gerðar á honum
 * og aðferðum og breytum bætt við
 */
package com.example.folkmedhar.pantanir.bokun;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.folkmedhar.MainActivity;
import com.example.folkmedhar.R;


@SuppressLint("SimpleDateFormat") public class CalendarActivity extends Activity implements OnClickListener {

	private TextView currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar calendar;
	private int month, year;
	private long fyrri, seinni;
	
	private int backMonth, forwardMonth;

	private static final String dateTemplate = "MMMM yyyy";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		Locale locale = new Locale("IS");
		calendar = Calendar.getInstance(locale);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);
		
		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);
		backMonth = 0;
		forwardMonth = 6;
		

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		
	}

	/**
	 * 
	 * @param month
	 * @param year
	 */
	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		calendar.set(year, month - 1, calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate,
				calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		
		if (v == prevMonth && backMonth >0) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			backMonth--;
			forwardMonth++;

			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth && forwardMonth > 0) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			backMonth++;
			forwardMonth--;

			setGridCellAdapterToDate(month, year);
		}
		

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		//private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				//"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"dd-MMM-yyyy");
		

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId,
				int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
	
			Calendar calendar = Calendar.getInstance();
			TimeZone tz = TimeZone.getTimeZone("GMT");
		    calendar.setTimeZone(tz);
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentMonth(calendar.get(Calendar.MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		/* Ekki að nota þetta? MISSING
		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}
		*/

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {

			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			
			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;


			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				list.add(String
						.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
								+ i)
						+ "-GREY"
						+ "-"
						+ getMonthAsString(prevMonth)
						+ "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
			
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-"
							+ getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				list.add(String.valueOf(i + 1) + "-GREY" + "-"
						+ getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
				int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);

			// ACCOUNT FOR SPACING

			
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources()
						.getColor(R.color.lightgray02));
			}
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(
						R.color.darkgray));
			}
			if (day_color[1].equals("BLUE")) {
				gridcell.setTextColor(getResources().getColor(R.color.orrange));
			}
			int manudur = 0;
			switch(themonth) {
			case "January":
				manudur = 0;
				break;
			case "February":
				manudur = 1;
	    	case "October":
	    		manudur = 9;
	    		break;
			case "November": 
				manudur = 10;
				break;
			case "December":
	    		manudur = 11;
	    		break;
			case "September": 
				manudur = 8;
				break;
			
			default: manudur = 0;
			}
			Calendar c = Calendar.getInstance();
			TimeZone tz = TimeZone.getTimeZone("GMT");
		    c.setTimeZone(tz);
			c.set(Integer.parseInt(theyear), manudur, Integer.parseInt(theday));
			fyrri = c.getTimeInMillis();
			c = Calendar.getInstance();
		    c.setTimeZone(tz);
			seinni = c.getTimeInMillis();
		
			if(fyrri-seinni<0) {
				//gridcell.setTextColor(getResources().getColor(R.color.lightgray));
				
			}
			return row;
		}

		@Override
		public void onClick(View view) {
			
			String date_month_year = (String) view.getTag();
			Date parsedDate = null;
		
			try {
				parsedDate = dateFormatter.parse(date_month_year);
	

			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(parsedDate);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			int day = cal.get(Calendar.DATE);
			String dagur = String.valueOf(day);
			cal = Calendar.getInstance();
			
			if(day<10) {
				dagur = "0"+day;
			}
			String buttonDagur = dagur+month+year;
			if( !buttonDagur.equals(""))
			if((cal.get(Calendar.DATE)<=day && month==cal.get(Calendar.MONTH)) || (month>cal.get(Calendar.MONTH) ||
					year>cal.get(Calendar.YEAR)))
			{
				MainActivity.setStringDate(dagur + "-" + (month+1) + "-" + year);
				MainActivity.setDate(year + "-" + (month+1) + "-" + dagur);
				finish();
			}
			
		}
		
		public int getCurrentMonth() {
			return currentMonth;
		}

		private void setCurrentMonth(int currentMonth) {
			this.currentMonth = currentMonth;
		}
		
		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}
}
