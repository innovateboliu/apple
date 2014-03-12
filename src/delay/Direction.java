package delay;

public enum Direction {
	
	TIMBUKTUTOTONKA(1),
	TONKATOTIMBUKTU(-1);
	
	private int direction; 
	private Direction(int direction) {
		this.direction = direction;
	}
}
