# 211. Design Add and Search Words Data Structure

## Design

A regular character follows one branch of the trie. The `.` wildcard must try every child of the current node, so matching uses DFS.

## Java implementation

```java
class WordDictionary {
    private static class Node {
        Node[] children = new Node[26];
        boolean isWord;
    }
    private final Node root = new Node();

    public void addWord(String word) {
        Node node = root;
        for (char ch : word.toCharArray()) {
            int i = ch - 'a';
            if (node.children[i] == null) node.children[i] = new Node();
            node = node.children[i];
        }
        node.isWord = true;
    }

    public boolean search(String word) { return matches(word, 0, root); }

    private boolean matches(String word, int pos, Node node) {
        if (pos == word.length()) return node.isWord;
        char ch = word.charAt(pos);
        if (ch != '.') {
            Node next = node.children[ch - 'a'];
            return next != null && matches(word, pos + 1, next);
        }
        for (Node next : node.children) {
            if (next != null && matches(word, pos + 1, next)) return true;
        }
        return false;
    }
}
```

Adding a word is `O(L)`, and a search without wildcards is `O(L)`. Multiple `.` characters can explore many branches in the worst case. After consuming every character, check `isWord` so a prefix is not mistaken for a complete word.
