
public class HuffNode {
	private char character;
	private int frequency;
	
	public HuffNode(char character,int frequency) {
		this.character = character;
		this.frequency = frequency;
	}

	public char getCharacter() {
		return character;
	}
	
	public int getFrequency() {
		return frequency;
	}
}
