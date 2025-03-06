
/**
 * The Flush class is a subclass of the Hand class and is used to model a hand of flush
 */
public class Flush extends Hand {
	
	/**
	 * a constructor for building a hand of flush with the specified player and list of cards
	 * @param player the player who plays this hand
	 * @param cards the list of cards in this hand
	 */
	public Flush(CardGamePlayer player, CardList cards){
		super(player,cards);
	}
	
	/**
	 * a method for checking if this hand beats a specified hand
	 * @param hand the hand to be compared with
	 * @return a boolean indicating if this hand beats the specified hand provided
	 */
	public boolean beats(Hand hand) {
		if (this.getPlayer() == hand.getPlayer())
			return true;
		if (hand.getType()=="Straight")
			return true;
		else if (hand.getType()=="Flush") {
			if (this.getTopCard().suit > hand.getTopCard().suit) 
				return true;
			else if (this.getTopCard().suit==hand.getTopCard().suit) 
				return this.getTopCard().rank>hand.getTopCard().rank;
			}
		return false;
	}
	
	/**
	 * a method for checking if this is a valid hand of flush
	 * @return a boolean indicating if this is a valid hand of flush
	 */
	public boolean isValid() {
		if (this.size()==5) {
			for (int i=0;i<4;i++) {
				if (this.getCard(i).suit != this.getCard(i+1).suit)
					return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return a string specifying the type of this hand ("Flush")
	 */
	public String getType() {
		return "Flush";
	}

}
