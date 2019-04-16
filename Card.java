

public class Card {

	public enum Rank {

		ACE(1, "Ace"),
		TWO(2, "Two"), 
		THREE(3, "Three"), 
		FOUR(4, "Four"), 
		FIVE(5, "Five"), 
		SIX(6, "Six"), 
		SEVEN(7, "Seven"), 
		EIGHT(8, "Eight"), 
		NINE(9, "Nine"), 
		TEN(10, "Ten"), 
		JACK(10, "Jack"), 
		QUEEN(10, "Queen"), 
		KING(10, "King");
		
		private final int value;
		private final String display;
		
		private Rank(int value, String display) {	
			this.value = value;
			this.display = display;
		}
		
		public int getValue() {
			return value;
		}
		
		public String getDisplay() {
			return display;
		}

		@Override
		public String toString() {
			return getDisplay();
		}
	}

	public enum Suit {
		
		HEART("Heart"), 
		SPADE("Spade"), 
		DIAMOND("Diamond"), 
		CLUB("Club");
		
		private final String display;
		
		private Suit(String display) {
			this.display = display;
		}
		
		public String getDisplay() {
			return display;
		}

		@Override
		public String toString() {
			return getDisplay();
		}
	}
	
	private Suit suit;
	private Rank rank;
	
	public Card(Suit suit, Rank value){	
		this.suit = suit;
		this.rank = value;
	}
	
	public Rank getRank(){
		return rank;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	@Override
	public String toString(){
		return rank + " of " + suit;
	}
}
