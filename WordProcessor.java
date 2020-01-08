import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A Simple Word Processing class
 *
 * <p>Purdue University -- CS25100 -- Fall 2019 -- Tries</p>
 */
public class WordProcessor implements Serializable {

    private Node wordTrie;  //Root Node of the Trie
    private static final long serialVersionUID = 59483759347598437L;
    private List<String> list;

    /**
     * A simple Node class representing each
     * individual node of the trie
     */
    public class Node implements Serializable {

        protected char c;
        protected Node left, equal, right;
        protected boolean isEnd;
        private static final long serialVersionUID = 59483759347598437L;

        /**
         * Constructor for Node class
         *
         * @param ca Character assigned to the node
         */
        public Node(char ca) {
            this.c = ca;
            this.equal = null;
            this.left = null;
            this.right = null;
            this.isEnd = false;
        }
    }

    /**
     * Defualt constructor for the WordProcessor class
     */
    public WordProcessor() {

        wordTrie = null;
    }

    /**
     * Method to add a string to the trie
     *
     * @param s String to be added
     */
    public void addWord(String s) {
        wordTrie = AddTrie(wordTrie, s, 0);
    }


    /**
     * Method to add an array of strings to the trie
     *
     * @param s Array of strings to be added
     */
    public void addAllWords(String[] s) {
        for (String value : s) { //For each word
            addWord(value); //Add the word to the trie
        }
    }

    /**
     * Method to check of a string exists in the trie
     *
     * @param s String to be checked
     * @return true if string exists
     */
    public boolean wordSearch(String s) {
        int lenOfWord = s.length(); //Length of word
        int index = 0;
        Node node = wordTrie; //Set the current node to be root
        while (true) { //Continuously run loop
            if (index < lenOfWord && node == null) { //If the word being searched has extra characters after the word present in Trie
                return false; //Return false
            }
            if (s.charAt(index) < node.c) { //If the char at index is less than char of current node
                node = node.left; //Change the node to be the left child
                if (node == null && index != lenOfWord) { //If node becomes null & index isn't the position of last char
                    return false; //Return false
                }
            } else if (s.charAt(index) > node.c) { //If the char at index is greater than char of current node
                node = node.right; //Change the node to be the right child
                if (node == null && index != lenOfWord) { //If node becomes null & index isn't the position of last char
                    return false; //Return false
                }
            } else if (s.charAt(index) == node.c) { //If the char at index is equal to char of current node
                index++; //Increase index
                if (index == lenOfWord) { //If the index becomes the position of the last char of the word
                    return node.isEnd; //Return true if isEnd is true, else return false
                }
                node = node.equal; //Change the node to be the equal child
            }
        }
    }


    /**
     * Method to check if the trie if empty
     * (No stirngs added yet)
     *
     * @return
     */
    public boolean isEmpty() {
        return wordTrie == null;
    }

    /**
     * Method to empty the trie
     */
    public void clear() {
        wordTrie = null;
    }


    /**
     * Getter for wordTire
     *
     * @return Node wordTrie
     */
    public Node getWordTrie() {
        return wordTrie;
    }


    /**
     * Method to search autocomplete options
     *
     * @param s Prefix string being searched for
     * @return List of strings representing autocomplete options
     */
    public List<String> autoCompleteOptions(String s) {
        int len = s.length(); //Word length
        int index = 0;
        Node node = wordTrie; //Set node to be the root
        list = new LinkedList<>(); //Create a new list of Strings
        if (s == null || s.equals("")) {
            return list;
        }
        while (true) { //Continuously run loop
            if (s.charAt(index) < node.c) { //If the current char is less than node's char
                node = node.left; //Change the node to be the left child
                if (node == null) { //If node becomes null, return an empty list
                    return list; //This is because node would become null only when the word isn't present in the trie
                }
            } else if (s.charAt(index) > node.c) { //If the current char is greater than node's char
                node = node.right; //Change the node to be the right child
                if (node == null) { //If node becomes null, return an empty list
                    return list; //This is because node would become null only when the word isn't present in the trie
                }
            } else if (s.charAt(index) == node.c) { //If the current char equals node's char
                index++; //Increase index
                if (index == len) {
                    if (node.isEnd) {
                        return list;
                    }
                    else {
                        break;
                    }
                }
                node = node.equal; //Change the node to be the equal child
            }
        }

        List<String> retList = new LinkedList<>();
        Node track = node.equal; //A node that stores from where we need to start the inorder traversal
        traversal(track, ""); //Call helper method to add the suggestions to the list
        for (String value : list) { //For each suggestion
            String ret = s + value; //Add the prefix to each suggestion
            retList.add(ret); //Add ret to the list
        }
        return retList;

    }

    public void traversal(Node node, String w) {
        if (node.isEnd) {
            list.add(w+node.c); //Add the string to the list
        }

        if (node.left != null) { //If the left child isn't null
            traversal(node.left, w); //Call the method on the left child
        }

        if (node.equal != null) { //If the equal child isn't null
            traversal(node.equal, w + node.c); //Call the method on the equal child, but add the char to the string
        }

        if (node.right != null) { //If the right child isn't null
            traversal(node.right, w); //Call the method on the right child
        }
    }

    public Node AddTrie(Node x, String s, int d) {
        char c = s.charAt(d);
        if (x == null) {
            x = new Node(c);
        }
        if (c < x.c) {
            x.left = AddTrie(x.left, s, d);
        }
        else if (c > x.c) {
            x.right = AddTrie(x.right, s, d);
        }
        else if (d < s.length() - 1) {
            x.equal = AddTrie(x.equal, s, d+1);
        }
        else {
            x.isEnd = true;
        }
        return x;
    }
}
