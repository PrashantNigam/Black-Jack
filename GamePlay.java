import java.util.InputMismatchException;
import java.util.Scanner;

public class GamePlay {

	private static final int MIN_BET = 1;
	private static final String PUSH = "Push.";
	private static final int BUST_VALUE = 21;
	private static final int HIT = 1;
	private static final int STAND = 2;
	private static final String GAME_OVER = "Game over! You're out of money!";
	private static final String END_OF_HAND = "End of Hand.";
	private static final String DEALER_WIN = "Dealer wins.";
	private static final String PLAYER_WIN = "You win the hand.";
	private static final String HIT_OR_STAND_ERR = "Not a number!";
	private static final String HIT_OR_STAND = "Would you like to (1) Hit or (2) Stand";
	private static final String DEALING = "Now Dealing...";
	private static final String MAX_BET_MSG = "You cannot bet more than you have.";
	private static final String MIN_BET_MSG = "Minimum Bet for each game is $1. Please bet again!";
	private static final String INCORRECT_BET_MSG = "Not a number!";
	private static final double INIT_BET_AMT = 100.0;
	private static final String WELCOME_MSG = "Welcome to Blackjack!";
	private static final String DEALER_BUSTED = "Dealer Busted!";

	public void play() {

		System.out.println(WELCOME_MSG);
		gameLoop();
		System.out.println(GAME_OVER);
	}

	private void gameLoop() {
		
		final Scanner scanner = new Scanner(System.in);
		Double balanceAmt = INIT_BET_AMT;
		/* Each iteration corresponds to a hand*/
		while (balanceAmt > 0) {

			final double betAmt = getBetAmt(balanceAmt, scanner);
			final Deck deck = Deck.newShuffledDeck(false); 
			final Hand playerHand = new Hand();
			final Hand dealerHand = new Hand();
			dealCards(playerHand, dealerHand, deck);
			boolean stand = playRounds(playerHand, dealerHand, deck, scanner, betAmt);
			if (stand) {
				double value = stand(dealerHand, playerHand, deck, betAmt);
				balanceAmt -= value;
			} else {	
				/*busted*/
				balanceAmt -= betAmt; 
			}
			System.out.println(END_OF_HAND);
			System.out.println("-----------------------------------------");			
		}
		scanner.close();
	}
	
	/** @return True if player chose to stand or gets a BlackJack, 
	 * false if the player gets busted */
	private boolean playRounds(final Hand playerHand, final Hand dealerHand, final Deck deck, final Scanner scanner, final double betAmt) {

		/* Draw new cards*/
		while (true) {

			displayMsgs(playerHand, dealerHand);
			if (isBlackJack(playerHand)) {
				return true;
			}
			final int response = hitOrStand(scanner);
			if (response == HIT) {
				
				System.out.println(String.join(" ", 
											"You drew:", 
											hit(playerHand, deck).toString()));
				if (isBlackJack(playerHand)) {
					return true;
				} else if (isBusted(playerHand)) {
					return false;
				}
			} else if (response == STAND) {
				return true;
			} else {
				System.out.println(HIT_OR_STAND_ERR);
			}
		}
	}

	private boolean isBlackJack(final Hand playerHand) {
		
		if (playerHand.getValue() == BUST_VALUE) {
			
			System.out.println("You got a Black Jack!");
			return true; 
		}
		return false;
	}

	/** @param  
	 *  @return positive bet-amount if player loses, negative if player wins, zero if draw/push */
	private double stand(final Hand dealerHand, final Hand playerHand, final Deck deck, final double betAmt) {
		
		/* Dealer reveals cards */
		System.out.println(String.join(" ", 
									"Dealer Cards:", 
									dealerHand.toString()));
		/* Check if dealer has more points than player */
		if (dealerBeatsPlayerByPoints(dealerHand, playerHand)) {
			return betAmt;
		}
		dealerDrawCards(dealerHand, deck);
		return getPenaltyAmt(dealerHand, playerHand, betAmt);
	}

	private double getPenaltyAmt(final Hand dealerHand, final Hand playerHand, final double betAmt) {
		
		if (dealerGotBusted(dealerHand)) {
			System.out.println(PLAYER_WIN);
			return -betAmt;
		} else {
			if (dealerHand.getValue() == playerHand.getValue()) {
				System.out.println(PUSH);
				return 0;
			} else if (playerHand.getValue() > dealerHand.getValue()) {
				System.out.println(PLAYER_WIN);
				return -betAmt;
			} else {
				System.out.println(DEALER_WIN);
				return betAmt;
			}
		}
	}
	
	private boolean dealerBeatsPlayerByPoints(final Hand dealerHand, final Hand playerHand) {
		
		if (dealerHand.getValue() > playerHand.getValue()) {
			System.out.println(String.join(" ", 
										"Dealer beats you", 
										Integer.toString(dealerHand.getValue()), 
										"to",
										Integer.toString(playerHand.getValue())));
			return true;
		}
		return false;
	}
	
	private void dealerDrawCards(final Hand dealerHand, final Deck deck) {
		
		/* Dealer hits at 16 stands at 17*/
		while (dealerHand.getValue() < 17) {
			hit(dealerHand, deck);
			System.out.println(String.join(" ", 
										"Dealer draws:", 
										dealerHand.peekTop().toString()));
		}
		System.out.println(String.join(" ", 
									"Dealers hand value:", 
									Integer.toString(dealerHand.getValue())));
	}

	private boolean dealerGotBusted(final Hand dealerHand) {
		
		if (dealerHand.getValue() > BUST_VALUE) {
			System.out.println(DEALER_BUSTED);
			return true;
		}
		return false;		
	}

	private void dealCards(final Hand playerHand, final Hand dealerHand, final Deck deck) {

		System.out.println(DEALING);
		/* Player gets two cards*/
		playerHand.addCard(deck.hit());
		playerHand.addCard(deck.hit());

		/* Dealer gets two cards*/
		dealerHand.addCard(deck.hit());
		dealerHand.addCard(deck.hit());
	}

	private double getBetAmt(final double balanceAmt, final Scanner scanner) {

		while (true) {

			double betAmt = 0;
			System.out.println("You have $" + balanceAmt + ". What's your bet?");
			try {
				betAmt = scanner.nextDouble();
			} catch (InputMismatchException e) {
				System.out.println(INCORRECT_BET_MSG);
				scanner.next();
				continue;
			}

			if (betAmt < MIN_BET) {
				System.out.println(MIN_BET_MSG);
				continue;
			}

			if (betAmt > balanceAmt) {
				System.out.println(MAX_BET_MSG);
				continue;
			}
			return betAmt;
		}
	}

	private void displayMsgs(final Hand playerHand, final Hand dealerHand) {
		System.out.println(String.join(" ", 
									"Your Hand:", 
									playerHand.toString()));
		System.out.println(String.join(" ", 
									"Your Hand's Value is", 
									Integer.toString(playerHand.getValue())));
		System.out.println(String.join(" ", 
									"Dealer's Hand:", 
									dealerHand.peekTop().toString(), 
									"and [hidden]"));
	}

	/** To 'Hit' is to ask for another card. To 'Stand' is to hold your total and end your turn. 
	 * @param scanner 
	 * @return user's choice to hit or stand */
	private int hitOrStand(Scanner scanner) {

		while (true) {

			System.out.println(HIT_OR_STAND);
			int hitOrStand = 0;
			try {
				hitOrStand = scanner.nextInt();				
			} catch (InputMismatchException e) {
				System.out.println(HIT_OR_STAND_ERR);
				scanner.next();
				continue;
			}
			return hitOrStand;
		}
	}

	private Card hit(final Hand hand, final Deck deck) {
		
		Card hitCard = deck.hit();
		hand.addCard(hitCard);
		return hitCard;
	}

	private boolean isBusted(Hand playerHand) {

		/* Bust if they go over 21*/
		if (playerHand.getValue() > BUST_VALUE) {

			System.out.println("Busted. Currently valued at: " + playerHand.getValue());
			return true;
		}
		return false;
	}
}