package com.example.huffman;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Huffman extends Application {
    static MinHeap heap ;
    StringBuilder preorder = new StringBuilder();
    String pre = "";
    static int[] CountChar = new int[256];
    public static String[] encodings;
    public static ObservableList<HufTable> Hufdata;
    public static int lineinheader;
    public static long size;
    public static String textAr;

    @Override
    public void start(Stage stage) throws IOException {

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #7C93C3;");

        //this for create Button Compress and Decompress
        Button btcomp = new Button("Compress");
        Button btdecomp = new Button("DeCompress");
        btcomp.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        btdecomp.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
        btcomp.setStyle("-fx-background-color: #31569b;-fx-text-fill: white;-fx-padding: 10 20;");
        btcomp.setOnMouseEntered(e -> btcomp.setStyle("-fx-background-color: #010a2a; -fx-text-fill: white; -fx-padding: 10 20;"));
        btcomp.setOnMouseExited(e -> btcomp.setStyle("-fx-background-color: #31569b;-fx-text-fill: white;-fx-padding: 10 20;"));

        btdecomp.setStyle("-fx-background-color: #31569b;-fx-text-fill: white;-fx-padding: 10 20;");
        btdecomp.setOnMouseEntered(e -> btdecomp.setStyle("-fx-background-color: #010a2a; -fx-text-fill: white; -fx-padding: 10 20;"));
        btdecomp.setOnMouseExited(e -> btdecomp.setStyle("-fx-background-color: #31569b;-fx-text-fill: white;-fx-padding: 10 20;"));

        VBox button = new VBox(30);
        button.getChildren().addAll(btcomp, btdecomp);
        button.setLayoutX(100);
        button.setLayoutY(150);
        pane.getChildren().add(button);

        //this for create TextArea
        TextArea textArea = new TextArea();
        textArea.setLayoutX(300);
        textArea.setLayoutY(60);
        textArea.setPrefSize(700, 300);
        textArea.setEditable(false);
        pane.getChildren().add(textArea);

        //this for create TableView

        TableView<HufTable> HufTab = new TableView<>();

        TableColumn<HufTable, Integer> freqColumn = new TableColumn<>("Freq");
        freqColumn.setCellValueFactory(new PropertyValueFactory<>("freq"));
        freqColumn.setMinWidth(100);

        TableColumn<HufTable, Integer> lengthColumn = new TableColumn<>("Length");
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        lengthColumn.setMinWidth(100);

        TableColumn<HufTable, String> huffmanColumn = new TableColumn<>("Huffman");
        huffmanColumn.setCellValueFactory(new PropertyValueFactory<>("huffman"));
        huffmanColumn.setMinWidth(100);

        TableColumn<HufTable, String> asciiColumn = new TableColumn<>("Ascii");
        asciiColumn.setCellValueFactory(new PropertyValueFactory<>("ascii"));
        asciiColumn.setMinWidth(100);

        HufTab.getColumns().addAll(asciiColumn, huffmanColumn, lengthColumn, freqColumn);

        ArrayList<HufTable> hufList = new ArrayList<>();

        Hufdata = FXCollections.observableArrayList(hufList);

        HufTab.setItems(Hufdata);

        HufTab.setLayoutX(420);
        HufTab.setLayoutY(400);
        pane.getChildren().add(HufTab);

        btcomp.setOnAction(e -> {//button to start Compress the file
            heap = new MinHeap(256);
            textAr="";
            textArea.clear();
            textArea.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
            HufTab.getItems().clear();
            Hufdata.clear();

            FileChooser fc = new FileChooser();

            Window stage2 = null;
            File inputFile = fc.showOpenDialog(stage2);

            if (inputFile != null) {
                long filein = inputFile.length();
                String outputFile = ChangeExtension(inputFile.getPath(), "huf");
                try {
                    CountChar = new int[256];//for count the count of chars
                    BufferedInputStream br = new BufferedInputStream(new FileInputStream(inputFile));
                    int line;
                    while ((line = br.read()) != -1) {
                        CountChar[line]++;
                    }
                    for (int i = 0; i < CountChar.length; i++) {//insert the Data into the heap
                        if (CountChar[i] > 0) {
                            heap.insert(new Node<>(new Data(CountChar[i], (char) i)));
                            String A = ((char) i) + "";
                            Hufdata.add(new HufTable(A, "", 0, CountChar[i]));
                        }
                    }

                    heap.heapSort();//to sort the heap
                    encodings = new String[256];
                    System.out.println();

                    BuldTree(heap);// build the tree

                    FileOutputStream out = new FileOutputStream(outputFile, true);
                    BitOutputStream output = new BitOutputStream(new BufferedOutputStream(out));

                    HuffmanCodes(heap.getNode(0), "", encodings);//this method to generate the huffman code
                    HufTab.getItems().clear();

                    FillTable();
                    HufTab.setItems(Hufdata);
                    HufTab.refresh();

                    WriteHeader(getFileExtension(inputFile.getName()), outputFile, filein);//this method to write the header
                    WriteCompressedFile(inputFile.getPath(), output, encodings);//this method to compress the file

                    File outt = new File(outputFile);
                    textAr+="\n"+"Compressed File size: "+outt.length() + "\n";
                    DecimalFormat CompRe = new DecimalFormat();
                    CompRe.setMaximumFractionDigits(2);

                    double CompressF = ((((double) outt.length() /inputFile.length())*100.0)-100);//Compression Ratio

                    textAr+="\n"+"Compression Ratio = "+CompRe.format(CompressF)+"%" + "\n";
                    textArea.setText(textAr);

                    br.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done!");
                    alert.setContentText("The File Compressed!!");
                    alert.show();


                } catch (Exception e1) {

                }
            }
        });
        btdecomp.setOnAction(e -> {
            heap = new MinHeap(256);
            textAr= "";
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("huf files(*.huf)", "*.huf");
            fc.getExtensionFilters().add(extFilter);
            Window stage2 = null;
            File file = fc.showOpenDialog(stage2);
            if (file != null) {

                try {
                    String newFile = ReadHeader(file.getPath());
                    BitInputStream inputStream = new BitInputStream(new BufferedInputStream(new FileInputStream(file)));
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(new File(newFile)));
                    inputStream.skipLines(lineinheader);//to skip the Header
                    DecompressFile(inputStream,output,heap.getNode(0),size);//to Start Decompressing the file
                    textArea.clear();
                    textArea.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 13));
                    textArea.setText(textAr);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Done!");
                    alert.setContentText("The File Has DeCompressed!!");
                    alert.show();
                } catch (Exception e1) {

                }
            }

        });


        Scene scene = new Scene(pane, 1280, 820);
        stage.setTitle("Huffman!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static MinHeap BuldTree(MinHeap heap) {//this method build the huffman tree

        for (int i = 0; i < heap.getlength(); i++) {
            if (heap.getSize() > 1) {
                Node<Data> Z = new Node<>(new Data());
                Node<Data> x = heap.removeMin();
                Node<Data> y = heap.removeMin();
                Z.setLeft(x);
                Z.setRight(y);
                Z.data.freq = (x.data.freq + y.data.freq);
                heap.insert(Z);
            }
        }
        return heap;
    }

    public static String ChangeExtension(String fileName, String newExtension) {//this method to change the Extension file
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            return fileName.substring(0, dotIndex + 1) + newExtension;
        } else {
            return fileName + "." + newExtension;
        }
    }

    public static String getFileExtension(String fileName) {//to get Extension file
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "No extension found";
        }
    }

    private static void HuffmanCodes(Node<Data> root, String code, String[] encodings) {//this method to generate the Huffman Codes
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            encodings[(int) root.data.ch & 0xFF] = code;
        }
        HuffmanCodes(root.left, code + "0", encodings);
        HuffmanCodes(root.right, code + "1", encodings);


    }

    private static void FillTable() {//this method to fill table view
        Hufdata.clear();
        ArrayList<HufTable> hufList = new ArrayList<>();
        for (int i = 0; i < encodings.length; i++) {
            if (encodings[i] != null)
                hufList.add(new HufTable((char) i + "", encodings[i], encodings[i].getBytes().length, CountChar[i]));

        }
        Hufdata = FXCollections.observableArrayList(hufList);
    }

    private void WriteHeader(String fileExtension, String newFilePath, long size) throws IOException {//this Method to write the Header
        pre = "";
        PrintPreOrder();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newFilePath));
        textAr="Header Size: "+pre.length() + "\n";
        out.write((pre.length() + "\n").getBytes());
        textAr+="\n"+"Original File Size: "+size + "\n";
        out.write(String.valueOf(size).getBytes());
        textAr+="\n"+"File Extension: "+fileExtension + "\n";
        String write = "\n" + fileExtension + "\n";
        textAr+="\n"+"Huffman Tree: "+pre + "\n";
        out.write(write.getBytes());
        //out.write((nu+"\n").getBytes());
        write = pre + "\n";
        out.write(write.getBytes());
        out.close();


    }

    private void PrintPreOrder() {//this Helper Method to print PreOrder
        GeneratePreorder(heap.getNode(0), preorder);
        pre = preorder.toString();
        System.out.println(pre);
    }

    private static void GeneratePreorder(Node<Data> node, StringBuilder sb) {//this to Print tree preOrder
        if (node == null) {
            sb.append('ඕ'); // Appending a special character to represent null node in preorder
            return;
        }
        sb.append(node.data.ch);
        GeneratePreorder(node.left, sb);
        GeneratePreorder(node.right, sb);
    }

    private static void WriteCompressedFile(String inputFile, BitOutputStream output, String[] encodings) {//this method to compress the file
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(inputFile));
            int data;
            while ((data = input.read()) != -1) {
                String code = encodings[data & 0xFF];
                for (int i = 0; i < code.length(); i++) {
                    char bit = code.charAt(i);
                    output.write(bit - '0');
                }
            }
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String ReadHeader(String file) {//this Method to Read the Header
        try {
            lineinheader = 0;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String fileNameWithExtension = file.substring(file.lastIndexOf("\\") + 1);
            String path = file.substring(0, file.lastIndexOf("\\"));

            int extensionIndex = fileNameWithExtension.lastIndexOf(".");
            String fileName = fileNameWithExtension.substring(0, extensionIndex);
            String extension = fileNameWithExtension.substring(extensionIndex + 1);
            if (extension.equals("huf")) {//check of extension of file
                int headerSize = Integer.parseInt(reader.readLine());
                size = Long.parseLong(reader.readLine());
                lineinheader++;
                String fileExtension_original = reader.readLine();
                lineinheader++;
                StringBuilder tree = new StringBuilder();
                //lineinheader = Integer.parseInt(reader.readLine());
                int readed = 0;
                while (readed <= headerSize) {
                    readed++;
                    tree.append((char) reader.read());
                }
                textAr="Header Size: "+tree.length() + "\n";
                textAr+="\n"+"Original File Size: "+size + "\n";
                textAr+="\n"+"File Extension: "+fileExtension_original + "\n";
                textAr+="\n"+"Huffman Tree: "+tree + "\n";
                lineinheader+=countLines(tree);


                String newFile = path + "\\" + fileName + "." + fileExtension_original;
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(new File(newFile)));
                System.out.println(tree.toString());
                heap = new MinHeap(1);
                heap.insert(MinHeap.buildTree(tree.toString().toCharArray()));//tree



                reader.close();
                output.close();
            return newFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    return null;
    }
    public static int countLines(StringBuilder stringBuilder) {//this method to count the number of lines in the header
        int count = 1;

        for (int i = 0; i < stringBuilder.length(); i++) {
            if (stringBuilder.charAt(i) == '\n') {
                count++;
            }
        }

        return count;
    }
    private static void DecompressFile(BitInputStream input, BufferedOutputStream output, Node<Data> root, long size) throws IOException {//This method to Decompressing the file
        Node<Data> curr = root;
        int bit;
        int count =0;

        try {
            while ((bit = input.read()) != -1) {
                if (bit==0){
                    curr = curr.left;
                }else {
                    curr = curr.right;
                }if (curr.left==null&&curr.right==null){
                    count++;
                    output.write(curr.data.ch);
                    curr = root;
                    if(count==size){
                        System.out.println("Decompressed done !!");
                        break;
                    }
                }
            }
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}