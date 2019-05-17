class NodeDisplayConsole {
	int posx;
	int posy;
	boolean left;
	int nbbranche;
	
	Node node;
	
	NodeDisplayConsole(Node node, int posx, int posy, int nbbranche, boolean left) {
		this.node = node;
		this.posx = posx;
		this.posy = posy;
		this.nbbranche = nbbranche;
		this.left = left;
	}	
	
	int sizeValueConsole() {
		if (nbbranche == 0)
			return node.sizeValueConsole();
		else
			return 1;
	}
}
