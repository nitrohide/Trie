package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null,new TrieNode(new Indexes(0,(short) 0,(short)(allWords[0].length()-1)),null,null),null);
		for(int i = 1; i < allWords.length; i++) {
			TrieNode prev = root;
			TrieNode ptr = root.firstChild;
			int start = 0;
			boolean end = false;
			String word = allWords[i];
			while(!end) {
				if(ptr == null) {
					TrieNode Sibling = new TrieNode(new Indexes(i , (short) start, (short) (word.length()-1)), null, null);
					prev.sibling = Sibling;
					break;
				}
				else {
					if(word.charAt(start) == allWords[ptr.substr.wordIndex].charAt(ptr.substr.startIndex)) {
						if(ptr.firstChild == null) {
							int counter = check(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex), word.substring(start));
							TrieNode Add = new TrieNode(new Indexes(i, (short)(start + counter)  , (short)(word.length()-1)) , null, null);;
							TrieNode Replace = new TrieNode(new Indexes(ptr.substr.wordIndex, (short)(ptr.substr.startIndex + counter), (short)(ptr.substr.endIndex)) , null, Add);
							TrieNode Double = new TrieNode(new Indexes(ptr.substr.wordIndex, (short) (ptr.substr.startIndex), (short)(ptr.substr.startIndex + counter-1)), Replace,ptr.sibling );
							if(isParent(prev, ptr)) {
								prev.firstChild = Double;
								break;
							}
							else {
								prev.sibling = Double;
								break;
							}
						}
						else{
							if (leaf(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex,ptr.substr.endIndex+1), word.substring(start)) ) {
								start = start + ptr.substr.endIndex - ptr.substr.startIndex+1;
								prev = ptr;
								ptr = ptr.firstChild;
							}
							else {
								int counter = check(allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex), word.substring(start));
								TrieNode Add = new TrieNode(new Indexes(i,(short) (start + counter),(short) (word.length()-1)), null, null); 
								TrieNode Replace = new TrieNode(new Indexes(ptr.substr.wordIndex,(short) (ptr.substr.startIndex+counter),(short) (ptr.substr.endIndex)), ptr.firstChild, Add);
								TrieNode Double = new TrieNode(new Indexes(ptr.substr.wordIndex,(short) (ptr.substr.startIndex),(short) (ptr.substr.startIndex+counter-1)), Replace, ptr.sibling);
								if(isParent(prev, ptr)) {
									prev.firstChild = Double;
									break;
								}
								else {
									prev.sibling = Double;
									break;
								}
							}
							
						}
					}
					else {
						prev = ptr;
						ptr = ptr.sibling;
					}
				}
			}
		}
		return root;
	}
	
	private static int check(String word, String ptr) {
		int counter = 0;
		for(int i = 0; i<word.length(); i++) {
			if(word.charAt(i) == ptr.charAt(i)) {
				counter++;
			}
			else {
				break;
			}
		}
		return counter;
	}

	private static boolean isParent(TrieNode prev , TrieNode ptr) {
		if(prev.firstChild == null) {
			return false;
		}
		else if(prev.firstChild.equals(ptr)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static boolean leaf(String ptr, String sub) {
		if(sub.length() <= ptr.length()) {
			return false;
		}
		else if(ptr.equals(sub.substring(0,ptr.length()))) {
			return true; 
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		TrieNode ptr = root.firstChild;
		int start = 0;
		boolean end = false;
		while(!end) {
			if(ptr == null) {
				return null;
			}
			else {
				String ptr1 = allWords[ptr.substr.wordIndex];
				String pre = prefix.substring(start);
				if(prefix.charAt(start) == ptr1.charAt(ptr.substr.startIndex)) {
					int preL = pre.length();
					int ptrL = ptr.substr.endIndex - ptr.substr.startIndex + 1;
					if(ptr.firstChild == null) {
						if(preL <= ptrL && prefix.substring(start).equals(ptr1.substring(ptr.substr.startIndex, ptr.substr.startIndex + prefix.length() - start))) {
							list.add(ptr);
							return list;
						}
						return null;
						
					}
					else {				
						if(preL == ptrL && pre.equals(ptr1.substring(ptr.substr.startIndex, ptr.substr.endIndex + 1))) {
							break;
						}
						
						else if(preL < ptrL && pre.equals(ptr1.substring(ptr.substr.startIndex, ptr.substr.startIndex + preL))) {
							break;
							
						}
						
						else if(preL > ptrL && pre.substring(0, ptr.substr.endIndex - ptr.substr.startIndex + 1).equals(ptr1.substring(ptr.substr.startIndex, ptr.substr.endIndex + 1))) {
							start = start + ptr.substr.endIndex - ptr.substr.startIndex + 1;
							ptr = ptr.firstChild;
						}
						
						else {
							return null;
						}
					}
				}
				else {
					ptr = ptr.sibling;
				}
			}	
		}
		ArrayList<TrieNode> answer = leafs(ptr, allWords);
		return shorten(answer);
	}
	
	private static ArrayList<TrieNode> leafs(TrieNode root, String[] words) {
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		ArrayList<TrieNode> a = new ArrayList<TrieNode>();
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			if(ptr.firstChild == null) {
				list.add(ptr);
				a.addAll(list);
			}
			a.addAll(leafs(ptr, words));
		}
		return a;	
	}
	
	private static ArrayList<TrieNode> shorten(ArrayList<TrieNode> list){
		ArrayList<TrieNode> shorter = new ArrayList<TrieNode>();
		int index = 0;
		while(index < list.size()) {
			if(!shorter.contains(list.get(index))) {
				shorter.add(list.get(index));
			}
			index++;
		}
		return shorter;
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
