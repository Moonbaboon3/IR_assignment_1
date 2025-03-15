/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package invertedIndex;

import java.io.*;

import static java.lang.Math.log10;
import static java.lang.Math.sqrt;

import java.util.*;

/**
 *
 * @author ehab
 */
public class Index5 {

    //--------------------------------------------
    int N = 0;
    public Map<Integer, SourceRecord> sources;  // integer -> document_id , sourceRecord -> file information including name.

    public HashMap<String, DictEntry> index; // THe inverted index  string -> term ,dictEntry -> hashSet with frequency and posting list)
    //--------------------------------------------

    public Index5() { // constructor 
        sources = new HashMap<Integer, SourceRecord>();
        index = new HashMap<String, DictEntry>();
    }

    public void setN(int n) { 
        N = n;
    }


    //---------------------------------------------
    public void printPostingList(Posting p) {
        // Iterator<Integer> it2 = hset.iterator();
        System.out.print("[");
        while (p != null) {
            /// -4- **** complete here ****
            // fix get rid of the last comma 

            //if the next node is not null it adds a comma after printing the docid
            if(p.next != null){
            System.out.print("" + p.docId + "," );
            p = p.next;
            }
            else{
                //if its null it adds the docid and doesnt add the comma
                System.out.print("" + p.docId);
                p = p.next;
            }
        }
        System.out.println("]");
    }

    //---------------------------------------------
    public void printDictionary() {
        for (Map.Entry<String, DictEntry> stringDictEntryEntry : index.entrySet()) {//for each index entry
            Map.Entry pair = (Map.Entry) stringDictEntryEntry;// get next term(key) dictentry(value) pair in index
            DictEntry dd = (DictEntry) pair.getValue();//extract dictEntry from index entry
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "]       =--> ");//output string of term and doc frequency
            printPostingList(dd.pList);// print posting list of term
        }
        System.out.println("------------------------------------------------------");
        System.out.println("*** Number of terms = " + index.size());
    }
 
    //-----------------------------------------------
    public void buildIndex(String[] files) {
        // Set the path to the collection directory and get the list of files if it exists
        int fid = 0;// holds the file id first document is 0
        for (String fileName : files) {// loop over every file
            File file = new File(fileName); // create a file object
            if (!file.exists() || !file.isFile()) { //make sure file exists and if not then skip
                System.out.println("File " + fileName + " not found or is not a file. Skip it.");
                continue;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {// buffer reader object reads file through file name
                if (!sources.containsKey(fileName)) {// if the collection does not contain this file 
                    sources.put(fid, new SourceRecord(fid, fileName, fileName, "notext"));// create a sourceRecord of the file with its information and add it to sources
                }

                String line;//holds current line
                int flen = 0; // holds number of words in line
                while ((line = reader.readLine()) != null) {// there are lines to itterate on
                    flen += indexOneLine(line, fid);// add word to index and or document id to posting list and update frequency
                }
                sources.get(fid).length = flen;

            } catch (IOException e) {
                System.out.println("Error reading file " + fileName + ": " + e.getMessage());
            }
            fid++;
        }
        printDictionary();
    }

    //----------------------------------------------------------------------------  
    public int indexOneLine(String ln, int fid) {
        int flen = 0; // holds number of words in a line

        String[] words = ln.split("\\W+");
      //   String[] words = ln.replaceAll("(?:[^a-zA-Z0-9 -]|(?<=\\w)-(?!\\S))", " ").toLowerCase().split("\\s+");
      //tokenizes ie extracts all words 
        flen += words.length; // adds number of words found in line to flen
        for (String word : words) { // each word in line
            word = word.toLowerCase(); //make word lower case
            if (stopWord(word)) { // if stopWord (a word the occurs alot and is useless to index) skip
                continue;
            }
            word = stemWord(word);
            // check to see if the word is not in the dictionary
            // if not add it
            if (!index.containsKey(word)) {
                index.put(word, new DictEntry());
            }
            // add document id to the posting list
            if (!index.get(word).postingListContains(fid)) {// if posting list of word doesn't contain document id
                index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term 
                if (index.get(word).pList == null) { // if posting list of word is null
                    // create a new posting node and add it to index for word
                    index.get(word).pList = new Posting(fid); 
                    index.get(word).last = index.get(word).pList; 
                } else { // posting list of word exists
                    //create node and add to posting list for word
                    index.get(word).last.next = new Posting(fid);
                    index.get(word).last = index.get(word).last.next;
                }
            } else { // word and document id exist in index 
                index.get(word).last.dtf += 1; //update dtf to show another instance of the word
            }
            //set the term_fteq in the collection
            index.get(word).term_freq += 1; // update global term frequncy in collection
            if (word.equalsIgnoreCase("lattice")) { // assumption: if word lattice found print debugging information

                System.out.println("  <<" + index.get(word).getPosting(1) + ">> " + ln);
            }

        }
        return flen;
    }

//----------------------------------------------------------------------------  
    boolean stopWord(String word) {// returns true if word is a stopWord or less than 2 letters otherwise false 
        return List.of("a", "an", "and", "are", "as", "at", "be", "by", "for", "if", "in", "is", "it", "its", "of", "on", "that", "the", "to", "was", "were", "will", "with").
                contains(word.toLowerCase()) || word.length() < 2;
    }
//----------------------------------------------------------------------------  

    String stemWord(String word) { //skip for now
        return word;
//        Stemmer s = new Stemmer();
//        s.addString(word);
//        s.stem();
//        return s.toString();
    }

    //----------------------------------------------------------------------------  
 Posting intersect(Posting pL1, Posting pL2) { // Given two posting lists, the goal is to merge(intersect) those two lists.
///****  -1-   complete after each comment ****
//   INTERSECT ( p1 , p2 )
//          1  answer ←      {}

        // sets posting node answer and last equal to null
        Posting answer = null;
        Posting last = null;
//      2 while p1  != NIL and p2  != NIL

    // if both posting lists entered as parameters are not null it will check if theyre equa
    while(pL1 != null && pL2 != null){
    if(pL1.docId ==pL2.docId){
        //its its equal we only want to add it once so making a new node and and add it to answer list
       Posting newNode = new Posting(pL1.docId);
    
       if(answer == null){
        answer = newNode;
       }
       //if answer list is null it adds it as the first node else it sets the it at the end of the list
       else{
        last.next = newNode;
       }
       last = newNode;
       //iterates though both posting lists
       pL1 = pL1.next;
       pL2 = pL2.next;
    }
    //if its not equal it will check which one is smaller and itreate through it
    else if(pL1.docId < pL2.docId){
        pL1 = pL1.next;
    }
    else{
        pL2 = pL2.next;
    }
    }
     
//          3 do if docID ( p 1 ) = docID ( p2 )
 
//          4   then ADD ( answer, docID ( p1 ))
                // answer.add(pL1.docId);
 
//          5       p1 ← next ( p1 )
//          6       p2 ← next ( p2 )
 
 //          7   else if docID ( p1 ) < docID ( p2 )
            
//          8        then p1 ← next ( p1 )
//          9        else p2 ← next ( p2 )
 
//      10 return answer
        return answer;
    }


    public String find_24_01(String phrase) { // any mumber of terms non-optimized search 
        //parameter is a phrase ex: [any sentence works]
        String result = ""; //  result holds a string with information about all files that contain all words from phrase
        String[] words = phrase.split("\\W+"); // split the phrase into seperate words
        int len = words.length; // holds number of words
        
        //fix this if word is not in the hash table will crash...

        //added an if condition to fix the exception by ensuring that the  key its trying to access even exists 
        //before trying to compare it  to the table
        if(index.containsKey(words[0].toLowerCase())){ // make word lowercase and make sure it is in the index
        Posting posting = index.get(words[0].toLowerCase()).pList; // get the posting list of this word
        int i = 1; // start with all remaining words
        while (i < len) { //  while word is within list of words
            
            posting = intersect(posting, index.get(words[i].toLowerCase()).pList); // find the intersection of documents for both words
            i++;
        }
    
        while (posting != null) { // loop over posting list with all intersected documents
            //System.out.println("\t" + sources.get(num));
            
            //extract information from current source record and add it to result
            result += "\t" + posting.docId + " - " + sources.get(posting.docId).title + " - " + sources.get(posting.docId).length + "\n";
            posting = posting.next;
        }
    }
        return result;
    }

    
    
    //---------------------------------
    String[] sort(String[] words) {  //bubble sort
        boolean sorted = false; //initilized sorted flag
        String sTmp;
        //-------------------------------------------------------
        while (!sorted) { //while not sorted
            sorted = true; //assume sorted is true
            for (int i = 0; i < words.length - 1; i++) {//for the number of words
                int compare = words[i].compareTo(words[i + 1]); // compare each word with next word
                if (compare > 0) { // if not in place ie next word should alphabetically come first
                    //swap word positions
                    sTmp = words[i]; 
                    words[i] = words[i + 1];
                    words[i + 1] = sTmp;
                    sorted = false; // pass lead to at least 1 swap so not sorted
                }
            }
        }
        return words;
    }

     //---------------------------------

    public void store(String storageName) {
        try {
            String pathToStorage = System.getProperty("user.dir") + "\\rl\\"+storageName; //get path to collection
            Writer wr = new FileWriter(pathToStorage); // create a writer object to this location
            for (Map.Entry<Integer, SourceRecord> entry : sources.entrySet()) { // for each source document
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().URL + ", Value = " + entry.getValue().title + ", Value = " + entry.getValue().text);
                //write source record details to document
                wr.write(entry.getKey().toString() + ","); 
                wr.write(entry.getValue().URL.toString() + ",");
                wr.write(entry.getValue().title.replace(',', '~') + ",");
                wr.write(entry.getValue().length + ","); //String formattedDouble = String.format("%.2f", fee );
                wr.write(String.format("%4.4f", entry.getValue().norm) + ",");
                wr.write(entry.getValue().text.toString().replace(',', '~') + "\n");
            }
            wr.write("section2" + "\n");

            Iterator it = index.entrySet().iterator(); // create an iterator over index
            while (it.hasNext()) { // while there are entries in the index
                Map.Entry pair = (Map.Entry) it.next(); // get next source document
                DictEntry dd = (DictEntry) pair.getValue(); // get next dicEntry
                //  System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
                //write DictEntry details to document
                wr.write(pair.getKey().toString() + "," + dd.doc_freq + "," + dd.term_freq + ";");
                Posting p = dd.pList;
                //write posting list
                while (p != null) {
                    //    System.out.print( p.docId + "," + p.dtf + ":");
                    wr.write(p.docId + "," + p.dtf + ":");
                    p = p.next;
                }
                wr.write("\n");
            }
            wr.write("end" + "\n");
            wr.close(); 
            System.out.println("=============EBD STORE=============");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//========================================= 
    //checks if file exists true if it does else false   
    public boolean storageFileExists(String storageName){
        java.io.File f = new java.io.File(System.getProperty("user.dir") + "\\rl"+storageName);
        return f.exists() && !f.isDirectory();

    }
//----------------------------------------------------    
    public void createStore(String storageName) {
        try {
            //create path to storage 
            String pathToStorage = System.getProperty("user.dir") + "\\rl"+storageName;
            Writer wr = new FileWriter(pathToStorage); // create writer to this path
            wr.write("end" + "\n");
            wr.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//----------------------------------------------------      
     //load index from hard disk into memory
    public HashMap<String, DictEntry> load(String storageName) {
        try {
            String pathToStorage = System.getProperty("user.dir") + "\\rl"+storageName;         
            sources = new HashMap<Integer, SourceRecord>(); // create sources hashmap
            index = new HashMap<String, DictEntry>(); //create index hashmap
            BufferedReader file = new BufferedReader(new FileReader(pathToStorage)); // create buffer reader for files in storage location
            String ln = ""; 
            int flen = 0; // number of words in the line
            //add all sources 
            while ((ln = file.readLine()) != null) { // while there are lines to read
                if (ln.equalsIgnoreCase("section2")) { // if reach part of document with section 2 then break/ completed
                    break;
                }
                String[] ss = ln.split(","); // split line at ,
                int fid = Integer.parseInt(ss[0]); // retrieve id 
                try {
                    System.out.println("**>>" + fid + " " + ss[1] + " " + ss[2].replace('~', ',') + " " + ss[3] + " [" + ss[4] + "]   " + ss[5].replace('~', ','));
                    //create source record
                    SourceRecord sr = new SourceRecord(fid, ss[1], ss[2].replace('~', ','), Integer.parseInt(ss[3]), Double.parseDouble(ss[4]), ss[5].replace('~', ','));
                    //   System.out.println("**>>"+fid+" "+ ss[1]+" "+ ss[2]+" "+ ss[3]+" ["+ Double.parseDouble(ss[4])+ "]  \n"+ ss[5]);
                    sources.put(fid, sr); // add source 
                } catch (Exception e) {

                    System.out.println(fid + "  ERROR  " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // add all index entries
            while ((ln = file.readLine()) != null) { // while there are lines
                //     System.out.println(ln);
                if (ln.equalsIgnoreCase("end")) { // when reach 'end' then done
                    break;
                }
                String[] ss1 = ln.split(";"); // split at ;
                String[] ss1a = ss1[0].split(","); // split ss1 at , for index entry
                String[] ss1b = ss1[1].split(":");// split ss1 at : for posting lst
                index.put(ss1a[0], new DictEntry(Integer.parseInt(ss1a[1]), Integer.parseInt(ss1a[2]))); // add index entry
                String[] ss1bx;   //posting list
                for (int i = 0; i < ss1b.length; i++) { // for each 
                    ss1bx = ss1b[i].split(",");
                    if (index.get(ss1a[0]).pList == null) { // if term has no posting list
                        //create posting list and add term and document to posting list
                        index.get(ss1a[0]).pList = new Posting(Integer.parseInt(ss1bx[0]), Integer.parseInt(ss1bx[1]));
                        index.get(ss1a[0]).last = index.get(ss1a[0]).pList;
                    } else {
                        //add to posting list
                        index.get(ss1a[0]).last.next = new Posting(Integer.parseInt(ss1bx[0]), Integer.parseInt(ss1bx[1]));
                        index.get(ss1a[0]).last = index.get(ss1a[0]).last.next;
                    }
                }
            }
            System.out.println("============= END LOAD =============");
            //    printDictionary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }
}

//=====================================================================
