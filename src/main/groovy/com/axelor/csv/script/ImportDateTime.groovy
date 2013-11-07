/**
 * Copyright (c) 2012-2013 Axelor. All Rights Reserved.
 *
 * The contents of this file are subject to the Common Public
 * Attribution License Version 1.0 (the “License”); you may not use
 * this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://license.axelor.com/.
 *
 * The License is based on the Mozilla Public License Version 1.1 but
 * Sections 14 and 15 have been added to cover use of software over a
 * computer network and provide for limited attribution for the
 * Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 *
 * Software distributed under the License is distributed on an “AS IS”
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is part of "Axelor Business Suite", developed by
 * Axelor exclusively.
 *
 * The Original Developer is the Initial Developer. The Initial Developer of
 * the Original Code is Axelor.
 *
 * All portions of the code written by Axelor are
 * Copyright (c) 2012-2013 Axelor. All Rights Reserved.
 */
package com.axelor.csv.script

import java.util.Map;
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime

import com.axelor.apps.base.service.administration.GeneralService;
import com.google.common.base.Strings;
import com.google.inject.Inject;


class ImportDateTime {
	String pat = "((\\+|\\-)?[0-9]{1,%s}%s)"
	String dt = "[0-9]{4}-[0-9]{2}-[0-9]{2}"
	
	public LocalDateTime updateYear(LocalDateTime dateTime, String year){
		if(!Strings.isNullOrEmpty(year)){
			def years = Integer.parseInt(year.find("[0-9]{1,4}"))
			if(year.startsWith("+"))
				dateTime = dateTime.plusYears(years)
			else if(year.startsWith("-"))
				dateTime = dateTime.minusYears(years)
			else
				dateTime = dateTime.withYear(years)
		}
		return dateTime
	}
	
	public LocalDateTime updateMonth(LocalDateTime dateTime, String month){
		if(!Strings.isNullOrEmpty(month)){
			def months = Integer.parseInt(month.find("[0-9]{1,2}"))
			if(month.startsWith("+"))
				dateTime = dateTime.plusMonths(months)
			else if(month.startsWith("-"))
				dateTime = dateTime.minusMonths(months)
			else
				dateTime = dateTime.withMonthOfYear(months)
		}
		return dateTime
	}
	
	public LocalDateTime updateDay(LocalDateTime dateTime, String day){
		if(!Strings.isNullOrEmpty(day)){
			def days = Integer.parseInt(day.find("[0-9]{1,2}"))
			if(day.startsWith("+"))
				dateTime = dateTime.plusDays(days)
			else if(day.startsWith("-"))
				dateTime = dateTime.minusDays(days)
			else
				dateTime = dateTime.withDayOfMonth(days)
		}
		return dateTime
	}
	
	public LocalDateTime updateHour(LocalDateTime dateTime, String hour){
		if(!Strings.isNullOrEmpty(hour)){
			def hours = Integer.parseInt(hour.find("[0-9]{1,2}"))
			if(hour.startsWith("+"))
				dateTime = dateTime.plusHours(hours)
			else if(hour.startsWith("-"))
				dateTime = dateTime.minusHours(hours)
			else
				dateTime = dateTime.withHourOfDay(hours)
		}
		return dateTime
	}
	
	public LocalDateTime updateMinute(LocalDateTime dateTime, String minute){
		if(!Strings.isNullOrEmpty(minute)){
			def minutes = Integer.parseInt(minute.find("[0-9]{1,2}"))
			if(minute.startsWith("+"))
				dateTime = dateTime.plusMinutes(minutes)
			else if(minute.startsWith("-"))
				dateTime = dateTime.minusMinutes(minutes)
			else
				dateTime = dateTime.withMinuteOfHour(minutes)
		}
		return dateTime
	}
	
	public LocalDateTime updateSecond(LocalDateTime dateTime, String second){
		if(!Strings.isNullOrEmpty(second)){
			def seconds = Integer.parseInt(second.find("[0-9]{1,2}"))
			if(second.startsWith("+"))
				dateTime = dateTime.plusSeconds(seconds)
			else if(second.startsWith("-"))
				dateTime = dateTime.minusSeconds(seconds)
			else
				dateTime = dateTime.withSecondOfMinute(seconds)
		}
		return dateTime
	}
	
	public String importDate(String inputDate) {
		
		String patDate = "("+dt+"|TODAY)(\\[("+pat.format(pat,4,"y")+"?"\
											  +pat.format(pat,2,"M")+"?"\
											  +pat.format(pat,2,"d")+"?"\
											  +")\\])?"
		try{
			if(!Strings.isNullOrEmpty(inputDate) && inputDate.matches(patDate)){
				List<String> dates = Arrays.asList(inputDate.split("\\["))
				inputDate = (dates[0] == "TODAY") ? new LocalDateTime().toString("yyyy-MM-dd") : dates[0]
				if(dates.size() > 1){
					LocalDateTime dateTime = new LocalDateTime(inputDate)
					dateTime = updateYear(dateTime, dates[1].find(pat.format(pat,4,"y")))
					dateTime = updateMonth(dateTime,dates[1].find(pat.format(pat,2,"M")))
					dateTime = updateDay(dateTime,dates[1].find(pat.format(pat,2,"d")))
					return dateTime.toLocalDate().toString()
				}else return inputDate
			}else return null
		}catch(Exception e){
			e.printStackTrace()
			return null
		}
	} 
	
	public String importDateTime(String inputDateTime) {
		String tm = "[0-9]{2}:[0-9]{2}:[0-9]{2}"
		String patTime = "("+dt+" "+tm+"|NOW)(\\[(" +pat.format(pat,4,"y")+"?"\
													+pat.format(pat,2,"M")+"?"\
													+pat.format(pat,2,"d")+"?"\
													+pat.format(pat,2,"H")+"?"\
													+pat.format(pat,2,"m")+"?"\
													+pat.format(pat,2,"s")+"?"\
													+")\\])?"
		try{
			if(!Strings.isNullOrEmpty(inputDateTime) && inputDateTime.matches(patTime)){
				List<String> timeList = Arrays.asList(inputDateTime.split("\\["))
				inputDateTime = (timeList[0] == "NOW") ? new LocalDateTime().toString("yyyy-MM-dd HH:mm:ss") : timeList[0]
				if(timeList.size() > 1){
					LocalDateTime dateTime = new LocalDateTime(inputDateTime.replace(" ","T"))
					dateTime = updateYear(dateTime, timeList[1].find(pat.format(pat,4,"y")))
					dateTime = updateMonth(dateTime,timeList[1].find(pat.format(pat,2,"M")))
					dateTime = updateDay(dateTime,timeList[1].find(pat.format(pat,2,"d")))
					dateTime = updateHour(dateTime,timeList[1].find(pat.format(pat,2,"H")))
					dateTime = updateMinute(dateTime,timeList[1].find(pat.format(pat,2,"m")))
					dateTime = updateSecond(dateTime,timeList[1].find(pat.format(pat,2,"s")))
					return dateTime.toString()
				}
				return inputDateTime.replace(" ","T")
			}
			return null
		}catch(Exception e){
			e.printStackTrace()
			return null
		}
	}
		
}
