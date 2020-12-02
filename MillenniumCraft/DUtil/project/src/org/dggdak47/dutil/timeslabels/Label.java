package org.dggdak47.dutil.timeslabels;

import java.time.LocalDateTime;

public class Label {
	private LocalDateTime dateTimeOfExpiring;
	private String name;
	
	public String getName() {
		return this.name;
	}
	public LocalDateTime getDateTimeOfExpiring() {
		return this.dateTimeOfExpiring;
	}
	
	public Label(String name, LocalDateTime ldt){
		this.name = name;
		dateTimeOfExpiring = ldt;
	}
}
