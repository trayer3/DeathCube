package com.projectreddog.deathcube.utility;

import java.util.List;
import java.util.Random;

public class RandomChoice {

	public static Object chooseRandom(List<Object> choicesList) {
		int length = choicesList.size();
		Random rand = new Random();
		
		int randomIndex = rand.nextInt(length);
		
		return choicesList.get(randomIndex);
	}
	
	public static Object chooseRandom(Object[] choicesArray) {
		int length = choicesArray.length;
		Random rand = new Random();
		
		int randomIndex = rand.nextInt(length);
		
		return choicesArray[randomIndex];
	}
	
	public static Object chooseRandomExcluding(List<Object> choicesList, List<Object> exclusionList){
		Object randomChoice = new Object();
		boolean notFound = true;
		while(notFound){
			randomChoice = chooseRandom(choicesList);
			
			if(!exclusionList.contains(randomChoice)){
				notFound = false;
			}
		}
		
		return randomChoice;
	}
	
	public static Object chooseRandomExcluding(Object[] choicesArray, Object[] exclusionArray){
		Object randomChoice = new Object();
		boolean notFound = true;
		boolean inExclusionArray = false;
		while(notFound){
			randomChoice = chooseRandom(choicesArray);
			
			inExclusionArray = false;
			for(int i = 0; i < exclusionArray.length; i++){
				if(randomChoice.equals(exclusionArray[i])){
					inExclusionArray = true;
				}
			}
			
			if(!inExclusionArray){
				notFound = false;
			}
		}
		
		return randomChoice;
	}
}
