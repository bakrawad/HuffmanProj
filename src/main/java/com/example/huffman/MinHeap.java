package com.example.huffman;

public class MinHeap {
    private TNode<Data>[] heap;
    private int size;
    private int capacity;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new TNode [capacity];
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int leftChild(int i) {
        return 2 * i + 1;
    }
    private int rightChild(int i) {
        return 2 * i + 2;
    }


    public void insert(TNode<Data> value) {
        if (size >= capacity) {
            System.out.println("Heap is full. Cannot insert more elements.");
            return;
        }

        int currentIndex = size;
        heap[currentIndex] = value;
        size++;

        while (currentIndex != 0 && heap[parent(currentIndex)].data.freq > heap[currentIndex].data.freq) {
            swap(currentIndex, parent(currentIndex));
            currentIndex = parent(currentIndex);
        }
    }

    public Data extractMin() {
        if (size < 0) {
            throw new IllegalStateException("Heap is empty");
        }

        if (size == 1) {
            size--;
            return heap[0].data;
        }

        Data root = heap[0].data;
        heap[0].data = heap[size - 1].data;
        size--;
        minHeapify(0);

        return root;
    }
    public TNode<Data> removeMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        TNode<Data> removedElement = heap[0];
        heap[0] = heap[size - 1];
        size--;
        minHeapify(0);

        return removedElement;

    }


    public void heapSort() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            minHeapify(i);
        }

        for (int i = size - 1; i > 0; i--) {
            swap(0, i);
            minHeapifyDown(0, i);
        }
        // Reverse the sorted array to get descending order
        int start = 0;
        int end = size - 1;
        while (start < end) {
            swap(start, end);
            start++;
            end--;
        }
    }


    private void minHeapifyDown(int i, int heapSize) {
        int smallest = i;
        int leftChild = leftChild(i);
        int rightChild = rightChild(i);

        if (leftChild < heapSize && heap[leftChild].data.freq < heap[smallest].data.freq) {
            smallest = leftChild;
        }

        if (rightChild < heapSize && heap[rightChild].data.freq < heap[smallest].data.freq) {
            smallest = rightChild;
        }

        if (smallest != i) {
            swap(i, smallest);
            minHeapifyDown(smallest, heapSize);
        }
    }

    private void minHeapify(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        int smallest = i;

        if (left < size && heap[left].data.freq < heap[smallest].data.freq) {
            smallest = left;
        }

        if (right < size && heap[right].data.freq < heap[smallest].data.freq) {
            smallest = right;
        }

        if (smallest != i) {
            swap(i, smallest);
            minHeapify(smallest);
        }
    }

    private void swap(int i, int j) {
        TNode<Data> temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    public Data get(int i) {
        if (this.heap[i] != null) {
            return this.heap[i].data;
        } else {

            return null;
        }
    }
    public TNode<Data> getNode(int i) {
        if (this.heap[i] != null) {
            return this.heap[i];
        } else {

            return null;
        }
    }
    public TNode<Data> setNode(int i, TNode<Data> node) {
        if (this.heap[i] != null) {
            this.heap[i] = node;
            return this.heap[i];
        } else {

            return null;
        }
    }
    public int getlength(){
        return heap.length;
    }
    public int getSize(){
        return size;
    }
    public static TNode<Data> buildTree(char[] preorder) {
        int[] index = {0}; // Using an array to pass index by reference

        return buildTreeHelper(preorder, index);
    }

    private static TNode<Data> buildTreeHelper(char[] preorder, int[] index) {
        // Base case: empty preorder or reached end of preorder
        if (index[0] >= preorder.length - 1 || preorder[index[0]] == 'ඕ') {
            index[0]++; // Move index to next character
            return null;
        }

        // Create a new node with the current character
        TNode<Data> node = new TNode<Data>(new Data(preorder[index[0]]));
        index[0]++; // Move index to next character



        // Construct left and right subtrees
        node.left = buildTreeHelper(preorder, index);
        node.right = buildTreeHelper(preorder, index);

        return node;
    }
}
