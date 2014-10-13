package com.meximas.wcr.sports;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sport {

	private String discipline;
	private Date date;
	private Map<String, ArrayList<String>> listOfSportEventsLists = new HashMap<String, ArrayList<String>>();
	private List<String> userDisciplines = new ArrayList<String>();
	private List<String> eventsToDisplay = new ArrayList<String>();
	private Set<String> userEvents = new HashSet<String>();

	public void setUserDisciplines(List<String> userDisciplines) {
		this.userDisciplines = userDisciplines;
	}

	public List<String> getUserDisciplines() {
		return userDisciplines;
	}

	public void setListOfSportEventsLists(
			Map<String, ArrayList<String>> listOfSportEventsLists) {
		this.listOfSportEventsLists = listOfSportEventsLists;
	}

	public Map<String, ArrayList<String>> getListOfSportEventsLists() {
		return listOfSportEventsLists;
	}

	public void setEventsToDisplay(List<String> eventsToDisplay) {
		this.eventsToDisplay = eventsToDisplay;
	}

	public List<String> getEventsToDisplay() {
		return eventsToDisplay;
	}

	public void setUserEvents(Set<String> choosedByUserEvents) {
		this.userEvents = choosedByUserEvents;
	}

	/**
	 * @return the userEvents
	 */
	public Set<String> getUserEvents() {
		return userEvents;
	}

}
