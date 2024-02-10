import java.util.*;

/*
* each key item pair and create a tree, each tree has a character and a tree
* code tree class
* contains character and frequency
* 
* A ball
* map -> (a,2),(b,1),(l,2)
* from this you would make three trees and 
* 
* create min priority queue tree, use a priority queue, some data structure
* 
* put all trees in a queue, queue has a method that can take out the tree with the lowest frequency
* 
* more frequent characters closer to the top of the tree
* 
* use to make an encoded bitmap
* 
* have whole tree, root, left gives 0 bit, right gives 1 bit
* 
* is the tree im looking at a leaf, if it is then return the code,
* 
* otherwise you have to recurse down the left and right child and add 0 or 1 to the code
* 
* buffered reader to read the file which generates the character and frequency relation, use all of those to create a huffman tree
* 
* add back mashed up huffman tree to priority queue
* 
* comparator - compares trees, implements comparator imported from java.util
* thats the class you want to be pushing into ur min pririoty queue
* 
* compares sizes of the frequency returns -1 0 1
* 
* angled brackets talk about objects that are contained in the bigger object
* 
* treecomp imp comparator<binary tree<codeTree>>
* 
* priority queue of binary trees containing code tree elements
*pass in the size of your map and also the tree comparator	
*
*every time you iterate through the current vertex youre pushin out of the stack, you pick a random number based on the number of out vertices
*
*
*/

public class TreeComparator implements Comparator<BinaryTree<HuffNode>>{
	
	@Override
	public int compare(BinaryTree<HuffNode> tree1, BinaryTree<HuffNode> tree2) {
		int tree1_frequency = tree1.getData().getFrequency();
		int tree2_frequency = tree2.getData().getFrequency();
		
		if (tree1_frequency < tree2_frequency) {
			return -1;
		}
		
		if (tree1_frequency > tree2_frequency) {
			return 1;			
		}
		
		else {
			return 0;
		}
	}
}
