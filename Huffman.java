import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.io.FileNotFoundException;


public class Huffman {

	public static SortedLinkedList<Node> occCount(File file) {
		SortedLinkedList<Node> sortedLinkedList = new SortedLinkedList<Node>();
		HashMap<Byte, Integer> hashMap = new HashMap<Byte, Integer>();

		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		long length = file.length();
		long it = 0;
		int character;
		while (length > it) {
			it += 1;
			try {
				character = fileInputStream.read();
				byte currentByte = (byte) character;
				if (hashMap.get(currentByte) != null)
					hashMap.put(currentByte, hashMap.get(currentByte)+1);
				else 
					hashMap.put(currentByte, 1);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (Map.Entry<Byte, Integer> entry : hashMap.entrySet()) {
			sortedLinkedList.sortedAdd(new Node (entry.getKey(), entry.getValue(), null, null));
		}

		return sortedLinkedList;
	}

	public static Node createNode(File file) {
		SortedLinkedList<Node> list = Huffman.occCount(file);

		while (list.size() >= 2) {
			Node rightNode = list.remove();
			Node leftNode = list.remove();
			Node node = new Node((byte)0, rightNode.weight + leftNode.weight, rightNode, leftNode);
			list.sortedAdd(node);
		}
		return list.getFirst();	
	}

	public static void getCodageRec(Node tree, HashMap<Byte, boolean[]> hashMap, boolean[] tab, boolean bool) {
		tmp = new boolean[tab.length];
		for (int i = 0; i < tab.length; i += 1)
			tmp[i] = tab[i];
		tab[tab.length] = bool;
		if (tree.leftNode == null && tree.rightNode == null) {
			hashMap.put(tree.character, tmp);
		}
		if (tree.leftNode != null) {
			Huffman.getCodageRec(tree.leftNode, hashMap, tmp, false);
		}
		if (tree.rightNode != null) {
			Huffman.getCodageRec(tree.rightNode, hashMap, tmp, true);
		}
	}
	
	public static HashMap<Byte, boolean[]> getCodage(Node tree) {
		HashMap<Byte, boolean[]> hashMap = new HashMap<Byte, boolean[]>();
		boolean[] tabTrue = new boolean[1];
		tabTrue[0] = true;
		boolean[] tabFalse = new boolean[1];
		tabFalse[0] = false;

		Huffman.getCodageRec(tree, hashMap, tabTrue, true);
		Huffman.getCodageRec(tree, hashMap, tabFalse, true);
		return hashMap;
	}

	public static void write(File file, HashMap<Byte, boolean[]> hashMap) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(hashMap.size());
			for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
				fileOutputStream.write(entry.getKey());
				for (boolean bool : entry.getValue()) {
				 	if (bool)
				 		fileOutputStream.write('1');
				 	else 
				 		fileOutputStream.write('0');
				}
			}
			fileOutputStream.write(' ');
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public HashMap<Byte, boolean[]> read(File file) {
	// 	FileInputStream fileInputStream = null;
	// 	try {
	// 		fileInputStream = new FileInputStream(file);

	// 	}
	// 	catch (FileNotFoundException e) {
	// 		e.printStackTrace();
	// 	}
	// }
}