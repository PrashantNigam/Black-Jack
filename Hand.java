
import java.util.Stack;

public class Hand {
	
	private Stack<Card> hand = new Stack<>();
	
	public boolean addCard(Card card) {
		
		if (card != null) {
			return hand.add(card);
		}
		return false;
	}
	
	public int getValue() {
		return Deck.getValue(hand);
	}

	public Card peekTop() {
		return hand.peek();
	}
	
	@Override
	public String toString() {
		
		StringBuilder display = new StringBuilder();
		for(Card aCard : hand) {
		
			display.append("\n\t")
				   .append(aCard);
		}
		return display.toString();
	}
}
