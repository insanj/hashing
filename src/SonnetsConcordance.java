//Created by Julian Weiss for CSC172
//Part of the Hashing and Sorting project
import java.io.*; import java.util.*;
public class SonnetsConcordance{
	static int amount = 0;
	static Scanner user = new Scanner(System.in);
	
	public static void main(String args[]) throws IOException{			
		System.out.println("Launched sonnet program, pulling sonnets (this might take a while)...");		
		long time = System.currentTimeMillis();
		Hashtable sonnets = new Hashtable(0);
		fillSonnetsHashtable(sonnets, "sonnets.txt");
		
		System.out.println("Done creating sonnet concordance! Took " + (System.currentTimeMillis() - time)/1000.0 + " seconds for " + amount + " words.");
		System.out.println("That means there were " + amount + " words in all of the sonnets combined (with repeats).");
		ArrayList<String> sonnetWords = new ArrayList<String>();
		for(int i = 0; i < sonnets.table.length; i++)
			if(sonnets.table[i] != null)
				sonnetWords.add(sonnets.table[i][0].word);
		
		Collections.sort(sonnetWords);
		System.out.println("Done sorting sonnet concordance, found " + sonnetWords.size() + " unique elements.");
		System.out.println("That means only " + sonnetWords.size() + " words were used in all of Shakespeare's sonnets");		
		
		System.out.print("\nGive me a word and I'll tell you where it is in the sonnets! Type \"stop\" to move on.\n");
		for(String input = user.nextLine(); !input.equalsIgnoreCase("stop"); input = user.nextLine()){
			PlottedWord[] occurences = sonnets.get(new PlottedWord(input.toLowerCase(), 0, 0));
			if(occurences == null)
				System.out.print("Couldn't find word!");
			else{
				System.out.println("Found word at following (line, word) coordinate(s):");
				for(PlottedWord pw : occurences)
					System.out.print("(" + pw.line + ", " + pw.wordNumber + ") ");
			}
			
			System.out.println();
		}//end for
		
		
		System.out.println("\nParsing and pulling common words (this will take way longer)...");
		time = System.currentTimeMillis();
		Hashtable common = new Hashtable(0);
		fillCommonHashtable(common, "mfuew.txt");
		
		System.out.println("Done creating common words concordance! Took " + (System.currentTimeMillis() - time)/1000.0 + " seconds for " + amount + " words.");
		System.out.println("That means the sample I used only had " + amount + " words, total.");
		ArrayList<String> commonWords = new ArrayList<String>();
		for(int i = 0; i < common.table.length; i++)
			if(common.table[i] != null)
				commonWords.add(common.table[i][0].word);
		
		Collections.sort(commonWords);
		System.out.println("Done sorting common concordance, found " + commonWords.size() + " unique elements.");
		System.out.println("That means the sample had " + commonWords.size() + " words without repeats (depends on sample)");
		System.out.println("In comparison to Shakespeare, our most common words are " + (double)commonWords.size()/sonnetWords.size() + " as big!");
		
		time = System.currentTimeMillis();
		ArrayList<String> shakespearean = mergePullingFrom(sonnetWords, commonWords);
		System.out.println("\nDone comparing and retrieving Shakespearean words using common source! Took " + (System.currentTimeMillis() - time)/1000.0 + " seconds to pull " + shakespearean.size() + " words.");
		System.out.println("That means, using the common source, there are apparently " + shakespearean.size() + " Shakespearisms (not used today).");
		
		System.out.println("\nParsing and pulling dictionary words (this will take a while)...");
		//time = System.currentTimeMillis();
		//Hashtable dict = new Hashtable(0);
		//fillDictHashtable(dict);
		Scanner file = new Scanner(new FileReader("linuxdict.txt"));
		HashSet<String> mine = new HashSet<String>();
		while(file.hasNextLine())
			mine.add(file.nextLine().replaceAll("[^a-zA-Z ]", "").toLowerCase());
		
		Object[] dictArray = mine.toArray();
		System.out.println("Done creating dict words table! Took " + (System.currentTimeMillis() - time)/1000.0 + " seconds for " + dictArray.length + " words.");
		ArrayList<String> dictWords = new ArrayList<String>();
		for(Object o : dictArray)
			dictWords.add((String) o);

	/*	ArrayList<String> dictWords = new ArrayList<String>();
		for(int i = 0; i < dict.table.length; i++)
			if(dict.table[i] != null)
				dictWords.add(dict.table[i][0].word);*/
		
		Collections.sort(dictWords);

		time = System.currentTimeMillis();
		shakespearean = mergePullingFrom(sonnetWords, dictWords);
		System.out.println("Done comparing and retrieving Shakespearean words using dictionary! Took " + (System.currentTimeMillis() - time)/1000.0 + " seconds to pull " + shakespearean.size() + " words.");
		System.out.println("That means, using the dictionary, there are actually " + shakespearean.size() + " Shakespearisms (not used today).");
		System.out.println("In comparison to Shakespeare, our full dictionary is " + (double)dictWords.size()/sonnetWords.size() + " as big!");

		PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		for(String s : shakespearean)
			writer.println(s + " ");	
		writer.close();
		
		System.out.println("\nLet's try comparing against a better sample of everyday word usage-- the Barack Obama AMA. Filling table (this will take the longest time)...");
		time = System.currentTimeMillis();
		Hashtable obama = new Hashtable(0);
		fillRedditHashtable(obama, "obama.txt");
		
		System.out.println("Done creating Obama concordance! Took " + (System.currentTimeMillis() - time)/1000.0 + " seconds for " + amount + " words.");
		ArrayList<String> obamaWords = new ArrayList<String>();
		for(int i = 0; i < obama.table.length; i++)
			if(obama.table[i] != null)
				obamaWords.add(obama.table[i][0].word);
		
		Collections.sort(obamaWords);
		System.out.println("Done sorting Obama concordance, found " + obamaWords.size() + " unique elements.");
	
		time = System.currentTimeMillis();
		shakespearean = mergePullingFrom(sonnetWords, obamaWords);
		System.out.println("Done comparing and retrieving Shakespearean words using Obama AMA! Took " + (System.currentTimeMillis() - time)/1000.0 + " seconds to pull " + shakespearean.size() + " words.");
		System.out.println("That means, using the Obama AMA, there are about " + shakespearean.size() + " Shakespearisms (not used today).");
		System.out.println("In comparison to Shakespeare, our Obama-interviewing vocabulary is " + (double)obamaWords.size()/sonnetWords.size() + " as big!");

		System.out.println("\n\nThanks for playing!");
	}//end main
		
	public static void fillSonnetsHashtable(Hashtable table, String path) throws IOException{
		Scanner file = new Scanner(new FileReader(path));
		boolean reachedSonnets = false;
		for(int line = 0; file.hasNextLine(); line++){
			String currLine = file.nextLine();
			if(!reachedSonnets && currLine.trim().equals("THE SONNETS")){
				reachedSonnets = true;
				file.nextLine();
				continue;
			}//end if
			
			ArrayList<String> words = new ArrayList<String>(Arrays.asList(currLine.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+")));
			words.removeAll(Arrays.asList("", null));
			if(reachedSonnets && words.size() > 1){
				if(currLine.trim().equals("End of The Project Gutenberg Etext of Shakespeare's Sonnets"))
					return;
				
				amount += words.size();
				for(int i = 0; i < words.size(); i++)
					table.add(new PlottedWord(words.get(i), line, i));
			}//end if
		}//end for
	}//end method
	
	public static void fillRedditHashtable(Hashtable table, String path) throws IOException{
		Scanner file = new Scanner(new FileReader(path));
		String fileString = file.nextLine();		
		for(amount = 0; file.hasNext(); fileString += file.nextLine());
		
		fileString = fileString.replaceAll("\\<.*?>", " ");
		ArrayList<String> comments = new ArrayList<String>(Arrays.asList(fileString.split("\\s+")));
		
		for(int i = 0; i < comments.size(); i++){
			if(!comments.get(i).isEmpty()){
				table.add(new PlottedWord(comments.get(i), 0, i));
				amount++;
			}
		}//end for
	}//end method
	
	public static void fillCommonHashtable(Hashtable table, String path) throws IOException{
		Scanner file = new Scanner(new FileReader(path));
		String line = file.nextLine();
		for( ; !line.isEmpty() && line.charAt(0) == ' '; line = file.nextLine());
		
		amount = 0;
		for(line = file.nextLine(); file.hasNextLine(); line = file.nextLine()){
			table.add(new PlottedWord(line.replaceAll("[^a-zA-Z ]", "").toLowerCase(), amount, 0));
			amount++;
		}
	}//end method
	
	public static void fillDictHashtable(Hashtable table) throws IOException{
		Scanner file = new Scanner(new FileReader("linuxdict.txt"));
		while(file.hasNextLine())
			table.add(new PlottedWord(file.nextLine().replaceAll("[^a-zA-Z ]", "").toLowerCase(), 0, 0));
	}
	
	public static String[] removeNulls(String[] array){
		List<String> list = new ArrayList<String>(Arrays.asList(array));
		list.removeAll(Collections.singleton(null));
		return list.toArray(new String[list.size()]);
	}//end method
	
	public static ArrayList<String> mergePullingFrom(ArrayList<String> firm, ArrayList<String> comparitor){
		ArrayList<String> remaining = new ArrayList<String>();
		int f = 0, c = 0;
		while(f < firm.size()){
			if(c >= comparitor.size())
				remaining.add(firm.get(f++));
			
			else{
				int comparison = firm.get(f).compareTo(comparitor.get(c));
				if(comparison < 0)
					remaining.add(firm.get(f++));
				
				else if(comparison > 0)
					c++;
				
				else{
					c++;
					f++;
				}
			}//end else
		}//end while
		
		return remaining;
	}//end method
	
	public static void printBigList(ArrayList<String> list){
		for(int i = 0, c = 0; i < list.size(); i++, c += list.get(i-1).compareTo("")){
			System.out.print(list.get(i) + " ");
			if(c >= 50){
				System.out.println();
				c = 0;
			}
		}//end for
	}//end method
}//end class