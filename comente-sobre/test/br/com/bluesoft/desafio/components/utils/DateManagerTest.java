package br.com.bluesoft.desafio.components.utils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import static org.junit.Assert.*;

import com.ibm.icu.util.Calendar;



public class DateManagerTest {
	@Test
	public void shouldUpdateToTheLastSecondOfTheDay(){
		Date date1, date2;
		GregorianCalendar calendar1, calendar2;

		calendar1 = new GregorianCalendar();
		calendar2 = new GregorianCalendar();
		date1 = new Date();
		date2 = new Date();
		
		date1 = DateManager.updateToTheLastSecondOfTheDay(date1);
		
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		
		assertTrue(calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE));
		assertTrue(calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH));
		assertTrue(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR));
		assertTrue(calendar1.get(Calendar.HOUR)+(calendar1.get(Calendar.AM_PM)*12) == 23);
		assertTrue(calendar1.get(Calendar.MINUTE) == 59);
		assertTrue(calendar1.get(Calendar.SECOND) == 59);
	}
	
	@Test
	public void shouldCalculateTheDifferenceBetweenDatesInSeconds(){
		Date dateFirst, dateLast;
		int differenceInMiliseconds = 1000000;
		int differenceInSeconds = differenceInMiliseconds/1000;
		dateFirst = new Date();
		dateLast = new Date();
		
		dateFirst.setTime(dateLast.getTime()-differenceInMiliseconds);
		assertTrue(differenceInSeconds == DateManager.differenceBetweenDatesInSeconds(dateFirst,dateLast));
	}

	@Test
	public void shouldCalculateTheDifferenceBetweenAnOldDateAndANowDateInSeconds(){
		Date dateFirst;
		int differenceInMiliseconds = 1000000;
		int differenceInSeconds = differenceInMiliseconds/1000;
		dateFirst = new Date();
		
		dateFirst.setTime(dateFirst.getTime()-differenceInMiliseconds);
		assertTrue(differenceInSeconds == DateManager.differenceBetweenDatesInSeconds(dateFirst,null));
	}
	
	@Test
	public void shoulParseStringToDateOnFormat_PT_BR(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		NumberFormat idFormat_PT_BR, idFormatReturned;   
		GregorianCalendar calendar = new GregorianCalendar();
		String strDate = "01/01/2012";
		Date date = new Date();
		
		idFormat_PT_BR = dateFormat.getNumberFormat();
		
		date = DateManager.parseStringToDate_PT_BR(strDate);
		calendar.setTime(date);
		dateFormat.setCalendar(calendar);
		idFormatReturned = dateFormat.getNumberFormat(); 
		
		assertTrue(idFormatReturned == idFormat_PT_BR);
	}
	
	@Test
	public void shoulNotParseStringIfItIsNullOrEmpty(){
		String strDate = "";
		Date date;
		date = DateManager.parseStringToDate_PT_BR(strDate);
		assertNull(date);
	}

}
