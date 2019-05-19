import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Huffman {

	public static SortedLinkedList<Node> occurenceCount(File file) {
		SortedLinkedList<Node> list = new SortedLinkedList<Node>();
		HashMap<Byte, Integer> hashMap = new HashMap<Byte, Integer>();

		FileInputStream fileR = null;
		try {
			fileR = new FileInputStream(file);
			long length = file.length();
			long it = 0;
			int character;
			while (length > it) {
				it += 1;
				character = fileR.read();
				byte currentByte = (byte) character;
				if (hashMap.get(currentByte) != null)
					hashMap.put(currentByte, hashMap.get(currentByte)+1);
				else 
					hashMap.put(currentByte, 1);
			}
			fileR.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		for (Map.Entry<Byte, Integer> entry : hashMap.entrySet()) {
			list.sortedAdd(new Node (entry.getKey(), entry.getValue(), null, null));
		}

		return list;
	}

	public static Node createNode(File file) {
		SortedLinkedList<Node> list = Huffman.occurenceCount(file);

		while (list.size() >= 2) {
			Node rightNode = list.remove();
			Node leftNode = list.remove();
			Node node = new Node((byte)0, rightNode.weight + leftNode.weight, rightNode, leftNode);
			list.sortedAdd(node);
		}
		return list.getFirst();	
	}

	private static void getCodingRec(Node tree, HashMap<Byte, boolean[]> hashMap, boolean[] tab, boolean bool) {
		boolean[] tmp = new boolean[tab.length+1];
		for (int i = 0; i < tab.length; i += 1)
			tmp[i] = tab[i];
		tmp[tab.length] = bool;
		if (tree.leftNode == null && tree.rightNode == null)
			hashMap.put(tree.character, tmp);
		else {
			Huffman.getCodingRec(tree.rightNode, hashMap, tmp, true);
			Huffman.getCodingRec(tree.leftNode, hashMap, tmp, false);
		}
	}
	
	public static HashMap<Byte, boolean[]> getCoding(Node tree) {
		HashMap<Byte, boolean[]> hashMap = new HashMap<Byte, boolean[]>();
		Huffman.getCodingRec(tree.rightNode, hashMap, new boolean[0], true);
		Huffman.getCodingRec(tree.leftNode, hashMap, new boolean[0], false);
		return hashMap;
	}

	public static void write(File file, HashMap<Byte, boolean[]> hashMap) {
		try {
			FileOutputStream fileW = new FileOutputStream(file);
			fileW.write(hashMap.size());
			for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
				fileW.write(entry.getKey());
				for (boolean bool : entry.getValue()) {
				 	if (bool)
				 		fileW.write('1');
				 	else 
				 		fileW.write('0');
				}
				fileW.write(' ');
			}
			fileW.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<Byte, boolean[]> read(File file) {
		HashMap<Byte, boolean[]> hashMap = new HashMap<Byte, boolean[]>();
		try {
			FileInputStream fileR = new FileInputStream(file);
			int length = fileR.read();
			byte currentByte = (byte)fileR.read();
			while (length != 0) {
				byte character = currentByte;
				ArrayList<Byte> binary = new ArrayList<Byte>();
				currentByte = (byte)fileR.read();
				while (currentByte != ' ') {
					binary.add(currentByte);
					currentByte = (byte)fileR.read();
				}
				boolean[] bool = new boolean[binary.size()];
				for (int i = 0; i < binary.size(); i += 1)
					bool[i] = binary.get(i) == '1' ? true : false;
				hashMap.put(character, bool);
				currentByte = (byte)fileR.read();
				length -= 1;
			}
			fileR.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return hashMap;
	}

	public static void encode(String fileName, String fileNameOut) {
		File file = new File(fileName);
		File fileOut = new File(fileNameOut);
		Node node = Huffman.createNode(file);
		HashMap<Byte, boolean[]> hashMap = Huffman.getCoding(node);
		Huffman.write(fileOut, hashMap);
		try {
			FileInputStream fileR = new FileInputStream(file);

			ArrayList<boolean[]> list = new ArrayList<boolean[]>();
			byte[] buffer = new byte[10];
			int n = fileR.read(buffer);
			while (n != -1) {
				for (int k = 0; k < n; k += 1){
					list.add(hashMap.get(buffer[k]));
				}
				buffer = new byte[10];
				n = fileR.read(buffer);
			}
			fileR.close();

			int length = 0;
			for (boolean[] bool : list)
				length += bool.length;

			boolean[] group = new boolean[length];
			int i = 0;
			for (int j = 0; j < list.size(); j += 1) {
				for (int k = 0; k < list.get(j).length; k += 1) {
					group[i] = list.get(j)[k];
					i += 1;
				}
			}
			while (i < length) {
				group[i] = false;
				i += 1;
			}

			FileOutputStream fileW = new FileOutputStream(fileOut, true); // to write after EOF

			for (int j = 0; j < group.length/8; j += 1) {
				byte b = 0;
				for (int k = 0; k < 8; k += 1) {
					if (group[j*8+k])
						b = (byte)(b + (byte)(1 << k));
				}
				fileW.write(b);
			}
			fileW.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getTreeRec(byte character, boolean[] bool, Node node) {
		for (int i = 0; i < bool.length-1; i += 1) {
			if (bool[i]) {
				if (node.rightNode == null)
					node.rightNode = new Node((byte)0, 0, null, null);
				node = node.rightNode;
			}
			else {
				if (node.leftNode == null)
					node.leftNode = new Node((byte)0, 0, null, null);
				node = node.leftNode;
			}
		}
		if (bool[bool.length-1])
			node.rightNode = new Node(character, 0, null, null);
		else 
			node.leftNode = new Node(character, 0, null, null);
	}

	public static Node getTree(HashMap<Byte, boolean[]> hashMap) {
		Node node = new Node((byte)0, 0, null, null);
		for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
			Huffman.getTreeRec(entry.getKey(), entry.getValue(), node);
		}
		return node;
	}

	public static void decode(String fileName, String fileNameOut) {
		File file = new File(fileName);
		File fileOut = new File(fileNameOut);
		HashMap<Byte, boolean[]> hashMap = read(file);
		Node tree = getTree(hashMap);
		ArrayList<Boolean> list = new ArrayList<Boolean>();

		try {
			FileInputStream fileR = new FileInputStream(file);
			int length = fileR.read();
			byte currentByte = (byte)fileR.read();
			while (length != 0) {
				currentByte = (byte)fileR.read();
				while (currentByte != ' ') {
					currentByte = (byte)fileR.read();
				}
				currentByte = (byte)fileR.read();
				length -= 1;
			}

			for (int i = 0; i < 8; i += 1) {
				if ((byte)((currentByte >> i) & 0x1) == 0)
					list.add(false);
				else 
					list.add(true);
			}
			byte[] buffer = new byte[10];
			int n = fileR.read(buffer); 		// obligé de lire dans un buffer car si on lit 
			while (n != -1) {					// le byte 11111111 (-1) la boucle s'arrete
				for (int k = 0; k < n; k += 1) {
					for (int i = 0; i < 8; i += 1) {
						if ((byte)((buffer[k] >> i) & 0x1) == 0)
							list.add(false);
						else 
							list.add(true);
					}
				}
				buffer = new byte[10];
				n = fileR.read(buffer);
			}
			fileR.close();

			Node node = tree;
			FileOutputStream fileW = new FileOutputStream(fileOut);
			for (int i = 0; i < list.size(); i += 1) {
				if (node.rightNode != null && node.leftNode != null) {
					if (list.get(i))
						node = node.rightNode;
					else 
						node = node.leftNode;
				}
				else {
					fileW.write(node.character);
					node = list.get(i) ? tree.rightNode : tree.leftNode;
				}
			}
			fileW.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
