
/**
 * The Hand class is a subclass of the CardList class and is used to model a hand of cards
 */
public abstract class Hand extends CardList {
	
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * @param player the player who plays this hand
	 * @param cards the list of cards in this hand
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player=player;
		for (int i=0;i<cards.size();i++) {
			this.addCard((BigTwoCard) cards.getCard(i));
		}
	}
	
	private CardGamePlayer player;
	
	
	/**
	 * a method for retrieving the player of this hand
	 * @return the player of this hand
	 */
	public CardGamePlayer getPlayer() {return player;}
	
	/**
	 * a method for retrieving the top card of this hand
	 * @return the top card of this hand
	 */
	public Card getTopCard() {         // require modification: Full House, Quad
		this.sort();
		return this.getCard(this.size()-1);
	}
	
	/**
	 * a method for checking if this hand beats a specified hand
	 * @param hand the hand to be compared with
	 * @return a boolean indicating if this hand beats the specified hand provided
	 */
	public boolean beats(Hand hand) {     // require modification: all 5-card hands
		if (this.player==hand.player)
			return true;
		if (this.size()==hand.size()) {
			BigTwoCard topcardd = (BigTwoCard) this.getTopCard();
			if (topcardd.compareTo(hand.getTopCard())>0)
				return true;
		}
	    return false;
	}
	
	
	/**
	 * a method for checking if this is a valid hand
	 * @return a boolean indicating if this hand is valid
	 */
	public abstract boolean isValid();
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return a string specifying the type of this hand
	 */
	public abstract String getType() ;
	
	
}
