import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {

	Stack<Card> deck = new Stack<>();

	public static Deck newShuffledDeck(boolean test) {

		Deck deck = new Deck();
		if (test) {
			deck.testDeck();
		} else {
			deck.createDeck();
			deck.shuffle();
		}
		return deck;
	}
	
	public void createDeck() {

		/* Generate 13 Cards for each of the four suites*/ 
		for (Card.Suit cardSuit : Card.Suit.values()) { 
			for(Card.Rank cardValue : Card.Rank.values()) {
				
				Card card = new Card(cardSuit, cardValue);
				deck.add(card);
			}
		}
	}
	
	public void testDeck() {
		
		deck.add(new Card(Card.Suit.CLUB, Card.Rank.KING));
		deck.add(new Card(Card.Suit.DIAMOND, Card.Rank.FIVE));
		
		deck.add(new Card(Card.Suit.CLUB, Card.Rank.QUEEN));		
		deck.add(new Card(Card.Suit.SPADE, Card.Rank.FIVE));
		
		deck.add(new Card(Card.Suit.DIAMOND, Card.Rank.SIX));
		
		deck.add(new Card(Card.Suit.CLUB, Card.Rank.ACE));
		
		deck.add(new Card(Card.Suit.DIAMOND, Card.Rank.ACE));
		
		deck.add(new Card(Card.Suit.DIAMOND, Card.Rank.SEVEN));
		
		Collections.reverse(deck);
	}

	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	public Card hit() {
		return deck.pop();
	}
	
	/** @return value of deck*/
	public static int getValue(List<Card> cards) {
		
		int handValue = 0;
		int aceCount = 0;
		for(Card card : cards) {
			
			Card.Rank rank = card.getRank();
			if (rank == Card.Rank.ACE) {
				aceCount++;
			} else {
				handValue += rank.getValue();
			}
		}

		/* Calculate hand value. Aces are worth 1 or 11 - if 11 would go over 21 make it worth 1*/
		
		/* If hand value is over 10 getting an ace (value = 11) would put them up to 22, so make ace worth one*/
		for (int i = 0; i < aceCount; i++) {				
			handValue += (handValue > 10) ? 1 : 11;
		}
		return handValue;
	}
	
	@Override
	public String toString() {
		
		StringBuilder display = new StringBuilder();
		for(Card aCard : deck) {
		
			display.append("\n")
				   .append(aCard);
		}
		return display.toString();
	}
}
