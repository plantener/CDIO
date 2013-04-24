package Objects;

public class Block {
	
	private int x;
	private int y;
	private int h;
	private int l;
	
	public Block(int x, int y, int h, int l) {
		this.x = x;
		this.y = y;
		this.h = h;
		this.l = l;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}
}
