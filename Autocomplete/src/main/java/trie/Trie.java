package trie;

import java.util.*;
/**
 * {@code Trie} является  модифицированной имплементацией префиксного дерева. <a href="https://ru.wikipedia.org/wiki/%D0%9F%D1%80%D0%B5%D1%84%D0%B8%D0%BA%D1%81%D0%BD%D0%BE%D0%B5_%D0%B4%D0%B5%D1%80%D0%B5%D0%B2%D0%BE">Префиксное дерево</a>
 *  Каждый узел {@code TrieNode} содержит массив индексов строк из файла с таким же ключом
 */
public class Trie{
    private int size;
    public Trie(int size) {
        this.size = size;
    }

    public Trie(){

    }
    static class TrieNode {
        Map<Character, TrieNode> children = new TreeMap<Character, TrieNode>();
        ArrayList<Integer> indexes = new ArrayList<>();
    }
    TrieNode root = new TrieNode();



    public void put(String s, int index) {
        if(s.length() > size) s = s.substring(0,size);
        TrieNode v = root;
        for (char ch : s.toLowerCase().toCharArray()) {
            if (!v.children.containsKey(ch)) {
                v.children.put(ch, new TrieNode());
            }
            v.indexes.add(index);
            v = v.children.get(ch);
        }
    }

    public ArrayList<Integer> find(String s) {
        int i = 0;
        if(s.length() >= size) s = s.substring(0,size-1);
        TrieNode v = root;
        for (char ch : s.toLowerCase().toCharArray()) {
            if (!v.children.containsKey(ch)) {
                break;
            } else {
                v = v.children.get(ch);
            }
            i++;
        }
        if (v.equals(root) || i != s.length()) return null;
        return v.indexes;
    }

}