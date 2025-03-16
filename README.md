# Inverted Index Implementation
## Goal


The goal of this project is to index the terms appeared in each document so that they are retrieved efficiently. This is done by mapping each term to its corresponding documentId that it appeared in.

## Important Methods





```bash
 public void buildIndex(String[] files) {};
```
This is used to read all the files stored in a specific folder by checking first if they already exist in sources(which is a hashmap that maps fileId to file related info).
Then taking each file to  read line by line to count number of terms occured in each file.

```bash
 indexOneLine(String ln, int fid) {};
```

This function processes a single line of text (ln) from a document with file ID (fid) and updates an inverted index.

## Indexing Steps



- Tokenization (Spliting into words)
- Normalization (Stemming)
- Building the actual Index table.


## Tech Stack

Java
