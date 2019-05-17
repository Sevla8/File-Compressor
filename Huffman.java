import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.io.FileNotFoundException;
import java.util.ArrayList;


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

	public static void getCodingRec(Node tree, HashMap<Byte, boolean[]> hashMap, boolean[] tab, boolean bool) {
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
			while (currentByte != -1) {
				byte character = currentByte;
				ArrayList<Byte> binary = new ArrayList<Byte>();
				currentByte = (byte)fileR.read();
				while (currentByte != ' ' && currentByte != -1) {
					binary.add(currentByte);
					currentByte = (byte)fileR.read();
				}
				boolean[] bool = new boolean[binary.size()];
				for (int i = 0; i < binary.size(); i += 1)
					bool[i] = binary.get(i) == '1' ? true : false;
				hashMap.put(character, bool);
				currentByte = (byte)fileR.read();
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

		try {
			FileInputStream fileR = new FileInputStream(file);

			ArrayList<boolean[]> list = new ArrayList<boolean[]>();
			byte currentByte = (byte)fileR.read();
			while (currentByte != -1) {
				list.add(hashMap.get(currentByte));
				currentByte = (byte)fileR.read();
			}
			fileR.close();

			int length = 0;
			for (boolean[] bool : list) {
				length += bool.length;
			}
			if (length%8 != 0)
				length += 8-length%8;

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

			FileOutputStream fileW = new FileOutputStream(fileOut);

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

	public static void decode(String fileName, String fileNameOut) {

	}
}