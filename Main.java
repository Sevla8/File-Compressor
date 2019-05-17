import java.io.File;
import java.util.Map;
import java.util.HashMap;

public class Main {
	public static void main(String[] args) {
		File file = new File("file.txt");
		File test = new File("test.txt");
		
		// SortedLinkedList<Node> list = Huffman.occurenceCount(file);
		// for (Node node : list) {
		// 	System.out.println(node.toString());
		// }

		// Node node = Huffman.createNode(file);
		// node.display();

		// HashMap<Byte, boolean[]> hashMap = Huffman.getCoding(Huffman.createNode(file));
		// for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
		// 	System.out.println((char)entry.getKey().byteValue());
		// 	for (boolean bool : entry.getValue())
		// 		System.out.print(bool+" ");
		// 	System.out.println("");
		// }

		// Huffman.write(test, Huffman.getCoding(Huffman.createNode(file)));

		// HashMap<Byte, boolean[]> hashMap = Huffman.read(test);
		// for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
		// 	System.out.println((char)entry.getKey().byteValue());
		// 	for (boolean bool : entry.getValue())
		// 		System.out.print(bool+" ");
		// 	System.out.println("");
		// }

		Huffman.encode("file.txt", "test.txt");
	}
}