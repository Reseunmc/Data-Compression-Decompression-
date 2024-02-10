import java.util.*;
import java.io.*;


public class Huffman {
	
	 /**
	  * Method for generating a frequency table of the characters that appear within a file
	  * 
	  * @param filename - the name of the file as a String
	  * @return a map representing the frequency table
	  */
	 private static Map<Character, Integer> genFreqTable(String filename) {
		 //Initialize a map for the purpose of representing the frequency table
	     Map<Character, Integer> freq_table = new HashMap<Character, Integer>();
	     //Declaration of BufferedReader input variable split from try-catch
	     BufferedReader input;
	        
	     //Try-catch the attempt to open the file
	     try {
	    		input = new BufferedReader(new FileReader(filename));
	     } 
	     
	     catch (FileNotFoundException e) {
	         System.err.println("Cannot open file.\n" + e.getMessage());
	         return freq_table;
	     }
	        
	     //Try-catch the loop that is used in order to read the file
	     try {
	        int c;
	        //While there remain characters to read
	        while ((c = input.read()) != -1) {
	        	char ch = (char) c;
	        		
	        	//If the frequency table does not contain the character key
	        	if (!freq_table.containsKey(ch)) {
	        			freq_table.put(ch, 1);	//Initialize a map entry with key - character and freq - 1
	        	}
	        	//If the frequency table does contain the character key
	        	else {
	        		int frequency = freq_table.get(ch);
	        		freq_table.put(ch, ++frequency);	//Increment the frequency
	        	}
	        }
	     }
	     
	     catch (IOException e) {
	    	 System.err.println("IO error while reading.\n" + e.getMessage());
	     }
	        
	     //Try-Catch the closing of the file
	     try {
			input.close();
		 }
	     
		 catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		 }
			
	     return freq_table;
	 }
	 
	 /**
	  * Method for creating a priority queue for the purpose of assembling the huffman_tree
	  * Can be thought of as a helper method for the tree creation method below
	  * abstracts the setting up of the priority queue
	  * 
	  * @param map - takes the frequency table
	  * @return a priorityqueue 
	  */
	 private static PriorityQueue<BinaryTree<HuffNode>> createPQueue(Map<Character, Integer> freq_table) {
		 //Initialize a PriorityQueue, pass in TreeComparator
		 PriorityQueue<BinaryTree<HuffNode>> priorityqueue = new PriorityQueue<BinaryTree<HuffNode>>(freq_table.size(), new TreeComparator());
		 
		 //For each entry in the frequency table
		 for (Map.Entry<Character, Integer> entry : freq_table.entrySet()) {
			 //Create a new HuffNode given the entry's character and frequency
			 HuffNode node = new HuffNode(entry.getKey(), entry.getValue());
			 
			 //Create a new Binary tree setting its data as the HuffNode
			 BinaryTree<HuffNode> codetree = new BinaryTree<HuffNode>(node);
			 priorityqueue.add(codetree);	//Add to tthe priority queue
		 }
		 
		 return priorityqueue;
	 }
	 
	 /**
	  * Method for the purpose of Tree Creation given a priority queue
	  * This method in itself can also be seen as a helper method for creating the code map!
	  * 
	  * @param map - frequency table
	  * @return a Complete Huffman Tree
	  */
	 private static BinaryTree<HuffNode> createTree(Map<Character, Integer> freq_table) {
		 //Use createPQueue to create a priority queue by passing in the frequency table
		 PriorityQueue<BinaryTree<HuffNode>> newPQueue = createPQueue(freq_table);
		 
		 //While there is more than 1 tree in the queue
		 while (newPQueue.size() > 1) {
			 //Remove two trees
			 BinaryTree<HuffNode> tree1 = newPQueue.remove();
			 BinaryTree<HuffNode> tree2 = newPQueue.remove();
			 
			 //Create a root HuffNode and a parent binary tree with its data as the root
			 HuffNode root = new HuffNode('/', tree1.getData().getFrequency() + tree2.getData().getFrequency());
			 BinaryTree<HuffNode> parent = new BinaryTree<HuffNode>(root, tree1, tree2);
			 
			 //Add this combined tree back to the priority queue
			 newPQueue.add(parent);
		 }
		 return newPQueue.peek();
	 }
	 
	 /**
	  * Method for obtaining a code map - mapping characters to their codes determined by the tree
	  * @param map - the code map
	  * @return
	  */
	 private static Map<Character, String> createCodeMap(Map<Character, Integer> freq_table) {
		 //Obtain the Huffman Tree through the createTree method passing in the frequency table
		 BinaryTree<HuffNode> final_codetree = createTree(freq_table);
		 Map<Character, String> CodeMap = new HashMap<Character, String>();	//Initialize the code-map
		 String s = "";	//Initialize the empty string that is going to represent the code
		 
		 createCodeMap_helper(final_codetree, CodeMap, s);
		 
		 return CodeMap;
	 }
	 
	 /**
	  * Helper method for making the code-map, does the recursive calls for createCodeMap which initializes the code and map
	  * 
	  * @param tree
	  * @param CodeMap
	  * @param s
	  */
	 private static void createCodeMap_helper(BinaryTree<HuffNode> huff_tree, Map<Character, String> CodeMap, String s) {
		 //If the tree is a leaf, put the tree's data's character in the CodeMap and associate it with s
		 if (huff_tree.isLeaf()) {
			 CodeMap.put(huff_tree.getData().getCharacter(), s);
		 }
		 
		 //If the tree has a left child, recursively call again, pass in the left child, the CodeMap, and the string + "0"
		 if (huff_tree.hasLeft()) {
			 createCodeMap_helper(huff_tree.getLeft(), CodeMap, s+"0");
		 }
		 
		//If the tree has a right child, recursively call again, pass in the right child, the CodeMap, and the string + "1"
		 if (huff_tree.hasRight()) {
			 createCodeMap_helper(huff_tree.getRight(), CodeMap, s+"1");
		 }
	 }
	 
	 /**
	  * Compression Method that reads a plain text file, compresses, and writes bits to a new file
	  * @param filename - original file name
	  * @param compFile - compressed file's name
	  * @param codeMap - the Code map
	  */
	 private static void Compression(String filename, String compFile, Map<Character, String> CodeMap) {
		 char mapchar;
		 BufferedReader input = null;
		 BufferedBitWriter bitOutput = null;
		 
		 //Try-catch the opening of the files
		 try {
			 input = new BufferedReader(new FileReader(filename));
			 bitOutput = new BufferedBitWriter(compFile);
		 }
		 
		 catch (FileNotFoundException e) {
	         System.err.println("Cannot open file.\n" + e.getMessage());
		 }
		 
		 //Try-catch the reading of the plain text file and writing of the compressed file
		 try {
			 int c;
			 //While there remain characters to be read
			 while ((c = input.read()) != -1) {
				 char ch = (char) c;
				 //For loop through the length of each character's associated code 
				 for (int j = 0; j < CodeMap.get(ch).length(); j++) {
					 mapchar = CodeMap.get(ch).charAt(j);
					 
					 //If the character at the given index is 0, writeBit false
					 if (mapchar == '0') {
						 bitOutput.writeBit(false);
					 }
					 //If the character at the given index is 1, writeBit true
					 else if (mapchar == '1') {
						 bitOutput.writeBit(true);
					 }
				 }
			 }
		 }
		 
		 catch (IOException e) {
	    	 System.err.println("IO error while reading.\n" + e.getMessage());
		 }
		 
		 //Try-catch the closing of the files
		 try {
			 input.close();
			 bitOutput.close();
		 }
		 
		 catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		 }
	 }
	 
	 /**
	  * Method for decompressing a compressed file, that is reading bits and writing them to a plain text file
	  * @param compFile
	  * @param decompFile
	  * @param huffman_tree
	  */
	 public static void Decompression(String compFile, String decompFile, BinaryTree<HuffNode> huffman_tree) {
		 BinaryTree<HuffNode> huffman_complete = huffman_tree;
		 BufferedBitReader bitInput = null;
		 BufferedWriter Output = null;
		 
		 //Try-catch the opening of the files
		 try {
			 bitInput = new BufferedBitReader(compFile);
			 Output = new BufferedWriter(new FileWriter(decompFile));
		 }
		 
		 catch (IOException e) {
			 System.err.println("Cannot open file.\n" + e.getMessage());
		 }
		 
		 //Try-catch the reading and writing of the files
		 try {
			 //While there are bits to read
			 while (bitInput.hasNext()) {
				 //Create a temporary huffman tree which we can set to left or right
				 BinaryTree<HuffNode> mutable_huff_tree = huffman_complete;
				 
				 //While the temporary tree is not a leaf
				 while (!mutable_huff_tree.isLeaf()) {
					 //Read the bit
					 boolean bit = bitInput.readBit();
					 //If its true set the temporary tree to its right
					 if (bit) {
						 mutable_huff_tree = mutable_huff_tree.getRight();
					 }
					//If its false set the temporary tree to its left
					 else if (!bit) {
						 mutable_huff_tree = mutable_huff_tree.getLeft();
					 }
				 }
				 //Write the character to the plain text file
				 Output.write(mutable_huff_tree.getData().getCharacter());
			 }
		 }
		 
		 catch (IOException e) {
	    	 System.err.println("IO error while reading.\n" + e.getMessage());
		 }
		 
		 //Try-catch the closing of the files
		 try {
			 bitInput.close();
			 Output.close();
		 }
		 catch (IOException e) {
			System.err.println("Cannot close file.\n" + e.getMessage());
		 }
	 }
	 
	 //Main method with test cases hard coded in
	 public static void main(String[] args) {
		 Map<Character, Integer> freq_table = genFreqTable("Inputs/test1.txt");
		 Map<Character, String> code_map = createCodeMap(freq_table);
		 BinaryTree<HuffNode> code_tree = createTree(freq_table);
		 Compression("Inputs/test1.txt", "Inputs/test1_compressed.txt", code_map);
		 Decompression("Inputs/test1_compressed.txt", "Inputs/test1_decompressed.txt", code_tree);
		 
		 
		 Map<Character, Integer> freq_table2 = genFreqTable("Inputs/USConstitution.txt");
		 Map<Character, String> code_map2 = createCodeMap(freq_table2);
		 BinaryTree<HuffNode> code_tree2 = createTree(freq_table2);
		 Compression("Inputs/USConstitution.txt", "Inputs/USConstitution_compressed.txt", code_map2);
		 Decompression("Inputs/USConstitution_compressed.txt", "Inputs/USConstitution_decompressed.txt", code_tree2);
		 
		 
		 Map<Character, Integer> freq_table3 = genFreqTable("Inputs/WarAndPeace.txt");
		 Map<Character, String> code_map3 = createCodeMap(freq_table3);
		 BinaryTree<HuffNode> code_tree3 = createTree(freq_table3);
		 Compression("Inputs/WarAndPeace.txt", "Inputs/WarAndPeace_compressed.txt", code_map3);
		 Decompression("Inputs/WarAndPeace_compressed.txt", "Inputs/WarAndPeace_decompressed.txt", code_tree3);
		 
	 }
}
	 
