package com.example.huffman;

import java.io.Serializable;

public class Node<T extends Comparable<Data>> implements Comparable<Node<Data>> , Serializable {
    T data;
    Node left;
    Node right;
    public Node(T data) { this.data = data; }
    public void setData(T data) { this.data=data; }
    public T getData() { return data; }
    public Node getLeft() { return left; }
    public void setLeft(Node left) { this.left = left; }
    public Node getRight() { return right; }
    public void setRight(Node right) { this.right = right;}
    public boolean isLeaf(){ return (left==null && right==null); }
    public boolean hasLeft(){ return left!=null; }
    public boolean hasRight(){ return right!=null; }
    public String toString() { return "[" + data + "]"; }

    @Override
    public int compareTo(Node<Data> o) {
        return 0;
    }
}


