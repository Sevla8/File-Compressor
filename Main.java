import java.io.File;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		File file = new File("file.txt");
		// SortedLinkedList<Node> list = Huffman.occCount(file);
		// for (Node node : list) {
		// 	System.out.println(node.toString());
		// }

		// Node node = Huffman.createNode(file);
		// System.out.println(node.toString());

		HashMap<Byte, boolean[]> hashMap = Huffman.getCodage(Huffman.createNode(file));
		for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
			System.out.println((char)entry.getKey().byteValue());
			for (boolean bool : entry.getValue()) {
				if (bool)
					System.out.print("1");
				else 
					System.out.print("0");
			}
			System.out.println("#");
		}

		// File test = new File("test.txt");
		// Huffman.write(test, Huffman.getCodage(Huffman.createNode(file)));
	}
}