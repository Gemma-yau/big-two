
/**
 * The Single class is a subclass of the Hand class and is used to model a hand of single
 */
public class Single extends Hand {
	
	/**
	 * a constructor for building a hand of single with the specified player and list of cards
	 * @param player the player who plays this hand
	 * @param cards the list of cards in this hand
	 */
	public Single(CardGamePlayer player, CardList cards){
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid hand of single
	 *@return a boolean indicating if this is a valid hand of single
	 */
	public boolean isValid() {
		if (this.size()==1)
		 return true;
		else return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return a string specifying the type of this hand ("Single")
	 */
	public String getType() {
		return "Single";
	}
}
