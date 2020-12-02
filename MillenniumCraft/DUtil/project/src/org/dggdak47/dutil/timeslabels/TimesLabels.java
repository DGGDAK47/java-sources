package org.dggdak47.dutil.timeslabels;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.dggdak47.dutil.timelabels.exceptions.NoSuchLabelException;
import org.dggdak47.dutil.timelabels.exceptions.SuchLabelAlreadyExistsException;

public class TimesLabels {
	private Long years;
	private Long months;
	private Long days;
	private Long hours;
	private Long minutes;
	private Long seconds;
	private ArrayList<Label> labels = new ArrayList<Label>();
	
	public boolean hasLabel(String labelName) {
		for(Label l: labels){
			if(l.getName().equals(labelName)){
				return true;
			}
		}
		
		return false;
	}
	public LocalDateTime expiryLabelDate(String labelName) {
		for(Label l: labels){
			if(l.getName().equals(labelName)){
				return l.getDateTimeOfExpiring();
			}
		}
		
		return null;
	}
	public boolean hasTimeExpiredForLabel(String labelName){
		for(Label l: labels){
			if(l.getName().equals(labelName)){
				LocalDateTime now = LocalDateTime.now();
				
				if( now.compareTo(l.getDateTimeOfExpiring()) >= 0){
					return true;
				}else{
					return false;
				}
			}
		}
		
		return false;
	}
	public void addLabel(String labelName){
		if(hasLabel(labelName)){
			return;
		}
		
		labels.add(new Label(labelName, addDateTime(LocalDateTime.now())));
	}
	public void removeLabel(String labelName){
		Label label = null;
		
		for(Label l: labels){
			if(l.getName().equals(labelName)){
				label = l;
				break;
			}
		}
		
		if(label == null){
			return;
		}else{
			labels.remove(label);
		}
	}
	private LocalDateTime addDateTime(LocalDateTime toAdd) {
		LocalDateTime toReturn = toAdd.plusYears(years);
		toReturn = toReturn.plusMonths(months);
		toReturn = toReturn.plusDays(days);
		toReturn = toReturn.plusHours(hours);
		toReturn = toReturn.plusMinutes(minutes);
		toReturn = toReturn.plusSeconds(seconds);
		
		return toReturn;
	}
	
	public TimesLabels(Long years, Long months, Long days, Long hours, Long minutes, Long seconds){
		this.years = years;
		this.months = months;
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
}