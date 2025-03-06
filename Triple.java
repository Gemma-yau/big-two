
/**
 * The Triple class is a subclass of the Hand class and is used to model a hand of triple
 */
public class Triple extends Hand{
	
	/**
	 * a constructor for building a hand of triple with the specified player and list of cards
	 * @param player the player who plays this hand
	 * @param cards the list of cards in this hand
	 */
	public Triple(CardGamePlayer player, CardList cards){
		super(player,cards);
	}
	
	/**
	 * a method for checking if this is a valid hand of triple
	 * @return a boolean indicating if this is a valid hand of triple
	 */
	public boolean isValid() {          
		if (this.size()==3) {
			Card c1=this.getCard(0);
			Card c2=this.getCard(1);
			Card c3=this.getCard(2);
			if (c1.rank==c2.rank && c2.rank==c3.rank) 
				return true;
			}
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return a string specifying the type of this hand (""Triple)
	 */
	public String getType() {
		return "Triple";
	}

}
