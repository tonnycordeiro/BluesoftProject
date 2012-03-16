package br.com.bluesoft.desafio.components.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

public class DateManager {

    static final Integer secondsByDay = 86400; 
    static final Integer milisecondToSecond = 1000; 	
	
	public static Date updateToTheLastSecondOfTheDay(Date date){
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		date = calendar.getTime();
		return date; 
	}
	
	public static long differenceBetweenDatesInSeconds(Date first,Date last){
		if (last == null)
			last = new Date(); /*Current date*/

		return (long)(last.getTime()-first.getTime())/1000;
	}

	public static Date parseStringToDate_PT_BR(String strDate) {
		Date date = null;
		DateFormat dateFormatPT_BR;  
		
		if(strDate == null || strDate.isEmpty())
			return null;
		
    	dateFormatPT_BR = new SimpleDateFormat("dd/MM/yy");
    	
    	try {
			date = (Date)dateFormatPT_BR.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        return date;
	}
}
