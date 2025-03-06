
/**
 * The Pair class is a subclass of the Hand class and is used to model a hand of pair
 */
public class Pair extends Hand {
	
	/**
	 * a constructor for building a hand of pair with the specified player and list of cards
	 * @param player the player who plays this hand
	 * @param cards the list of cards in this hand
	 */
	public Pair(CardGamePlayer player, CardList cards){
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid hand of pair
	 * @return a boolean indicating if this is a valid hand of pair
	 */
	public boolean isValid() {
		if (this.size()==2) {
			Card c1=this.getCard(0);
			Card c2=this.getCard(1);
			if (c1.rank==c2.rank)
				return true;
		}
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return a string specifying the type of this hand ("Pair")
	 */
	public String getType() {
		return "Pair";
	}
}
