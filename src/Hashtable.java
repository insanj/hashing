//Created by Julian Weiss for CSC172
//Part of the Hashing and Sorting project
import java.util.*;
public class Hashtable{
	int capacity;
	PlottedWord[][] table;
	ArrayList<String> keys;
	
	public Hashtable(int givenCapacity){
		capacity = (givenCapacity>0)?givenCapacity:73;
		keys = new ArrayList<String>();
		
		table = new PlottedWord[capacity][];
	}//end constructor
	
	public int nextPrime(){
		int toPrime = 0;
		for(int i = capacity * 100; toPrime == 0; i++)
			if(isPrime(i))
				toPrime = i;
		
		return toPrime;
	}//end method

	public boolean isPrime(int n) {
		if(n%2==0)
			return false;
		for(int i=3; i*i <= n; i+=2)
			if(n%i == 0)
				return false;
		
		return true;
	}//end method
	
	public PlottedWord[] get(PlottedWord word){
		int index = word.word.compareTo("") % capacity;
		for( ; table[index] != null && !table[index][0].word.equals(word.word); index = (index+4)%capacity);
		return table[index];
	}
	
	public int add(PlottedWord word){
		if(keys.size() > table.length/2)
			return reloadTableWithCapacity(nextPrime(), word);
		int index = word.word.compareTo("") % capacity;

		for( ; table[index] != null && !table[index][0].word.equals(word.word); index = (index+4)%capacity);
		if(table[index] == null){
			PlottedWord[] currWordList = new PlottedWord[1];
			currWordList[0] = (word);
			table[index] = currWordList;
			keys.add(word.word);
		}//end if
		
		else{
			PlottedWord[] currWordList = table[index];
			PlottedWord[] expandedWordList = Arrays.copyOf(currWordList, currWordList.length + 1);
			expandedWordList[currWordList.length] = word;
			table[index] = expandedWordList;
		}//end else
		
		return index;
	}//end method
	
	public int reloadTableWithCapacity(int altCapacity, PlottedWord becauseOfWord){
		capacity = altCapacity;
		Hashtable altTable = new Hashtable(altCapacity);
		for(PlottedWord[] pwl : table)
			if(pwl != null)
				for(PlottedWord pw : pwl)
					if(pw != null)
						altTable.add(pw);
		
		keys = altTable.keys;
		table = altTable.table;
		
		return altTable.add(becauseOfWord);
	}//end method
}//end class