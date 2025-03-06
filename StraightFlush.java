
/**
 * The StraightFlush class is a subclass of the Hand class and is used to model a hand of straight flush
 */
public class StraightFlush extends Hand {
	
	/**
	 * a constructor for building a hand of straight flush with the specified player and list of cards
	 * @param player the player who plays this hand
	 * @param cards the list of cards in this hand
	 */
	public StraightFlush(CardGamePlayer player, CardList cards){
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
		if (this.size()==hand.size()) {
			if (hand.getType()=="StraightFlush") {
				return super.beats(hand);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * a method for checking if this is a valid hand of straight flush
	 * @return a boolean indicating if this is a valid hand of straight flush
	 */
	public boolean isValid() {
		if (this.size()==5) {		
			final char[] order= {'l','m','a','b','c','d','e','f','g','h','i','j','k'};
			this.sort();
			
			for (int i=0;i<4;i++) {
				if (order[this.getCard(i).rank]+1 != order[this.getCard(i+1).rank] || this.getCard(i).suit != this.getCard(i+1).suit) 
					return false;
			}
			return true;
		}
	      return false;
	}
		
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return a string specifying the type of this hand ("StraightFlush")
	 */
	public String getType() {
		return "StraightFlush";
	}

}
