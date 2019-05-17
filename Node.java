import java.lang.Comparable;
import java.util.LinkedList;

public class Node implements Comparable<Node> {
	public byte character;
	public int weight;
	public Node rightNode;
	public Node leftNode;

	public Node(byte character, int weight, Node rightNode, Node leftNode) {
		this.character = character;
		this.weight = weight;
		this.rightNode = rightNode;
		this.leftNode = leftNode;
	}

	public Node(byte character, int weight) {
		this(character, weight, null, null);	
	}

	public int sizeValueConsole() {
		return Integer.valueOf(character).toString().length()+Integer.valueOf(weight).toString().length();
	}
	
	public int countPlaceConsole() {
		int nb = sizeValueConsole();
		if (leftNode != null)
			nb += 1 + leftNode.countPlaceConsole();
		if (rightNode != null)
			nb += 1 + rightNode.countPlaceConsole();
		
		return nb;
	}	
	
	public int countOffsetLeftConsole() {
		int nb = 0;
		if (leftNode != null) {
			nb += 1 + leftNode.countPlaceConsole();
		}
		
		return nb;
	}
	
	public int countOffsetRightConsole() {
		int nb = 0;
		if (rightNode != null) {
			nb += 1 + rightNode.countPlaceConsole();
		}
		
		return nb;		
	}
		
	public void display() {
		LinkedList<NodeDisplayConsole> list = new LinkedList<NodeDisplayConsole>();
		
		list.add(new NodeDisplayConsole(this, this.countOffsetLeftConsole(), 0, 0, true));
		int cur_offset = 0;		
		int lastposy = 0;
		while (!list.isEmpty()) {
			NodeDisplayConsole n = list.poll();
			if (n.posy != lastposy) {
				System.out.println("");
				cur_offset = 0;
				lastposy = n.posy;
			}
			if (n.left)
				n.posx = n.posx - (n.sizeValueConsole()-1)/2;
			else
				n.posx = n.posx - n.sizeValueConsole()/2;
			
			int tmp = cur_offset;
			for (int i = 0; i < n.posx - tmp; ++i) {
				System.out.print(" ");
				cur_offset++;
			}
			
			if (n.nbbranche == 0) {
				System.out.print((char)n.node.character+" "+n.node.weight);				
				if (n.node.leftNode != null) {
					int nbbranche = n.node.leftNode.countOffsetRightConsole()+Math.max(n.node.leftNode.sizeValueConsole()/2, 1);
					list.add(new NodeDisplayConsole(n.node.leftNode, n.posx - 1, n.posy+1, nbbranche, true));
				}
				
				if (n.node.rightNode != null) {
					int nbbranche = n.node.rightNode.countOffsetLeftConsole()+Math.max(n.node.rightNode.sizeValueConsole()/2, 1);
					list.add(new NodeDisplayConsole(n.node.rightNode, n.posx + n.sizeValueConsole(), n.posy+1, nbbranche, false));
				}
			} else {
				if (n.left) {
					System.out.print("/");
					list.add(new NodeDisplayConsole(n.node, n.posx - 1, n.posy+1, n.nbbranche - 1, n.left));
				} else {
					System.out.print("\\");
					list.add(new NodeDisplayConsole(n.node, n.posx + 1, n.posy+1, n.nbbranche - 1, n.left));
				}				
			}
			cur_offset += n.sizeValueConsole();
		}
		System.out.println("");
	}

	public int compareTo(Node node) {
		if (this.weight < node.weight)
			return -1;
		if (this.weight > node.weight)
			return 1;
		return 0;
	}

	public String toString() {
		String string = new String("");
		string += "character = "+(char)this.character+"\nweight = "+this.weight+"\n";
		if (this.rightNode != null)
			string += this.rightNode.toString();
		if (this.leftNode != null)
			string += this.leftNode.toString();
		return string;
	}
}