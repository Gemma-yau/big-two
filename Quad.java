
/**
 * The Quad class is a subclass of the Hand class and is used to model a hand of quad
 */
public class Quad extends Hand {
	
	/**
	 * a constructor for building a hand of quad with the specified player and list of cards
	 * @param player the player who plays this hand
	 * @param cards the list of cards in this hand
	 */
	public Quad(CardGamePlayer player, CardList cards){
		super(player,cards);
	}
	
	/**
	 * a method for retrieving the top card of this hand
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		this.sort();
		if (this.getCard(0).rank==this.getCard(1).rank)
			return this.getCard(3);
		else return this.getCard(4);
	}
	
	/**
	 * a method for checking if this hand beats a specified hand
	 * @param hand the hand to be compared with
	 * @return a boolean indicating if this hand beats the specified hand provided
	 */
	public boolean beats(Hand hand) {
		if (this.getPlayer() == hand.getPlayer())
			return true;
		if (hand.getType()=="Straight" || hand.getType()=="Flush" || hand.getType()=="FullHouse")
			return true;
		else if (hand.getType()=="Quad") {
			return super.beats(hand);
		}
		return false;
	}
	
	/**
	 * a method for checking if this is a valid hand of quad
	 * @return a boolean indicating if this is a valid hand of quad
	 */
	public boolean isValid() {
		if (this.size()==5) {
			this.sort();
			int num0=this.getCard(0).rank;
			int num1=this.getCard(1).rank;
			int num2=this.getCard(2).rank;
			int num3=this.getCard(3).rank;
			int num4=this.getCard(4).rank;
			
			if (num1==num2 && num2==num3)
				return num0==num1 || num3==num4;
		}
		return false;
	}
	
	/**
	 * a method for returning a string specifying the type of this hand
	 * @return a string specifying the type of this hand ("Quad")
	 */
	public String getType() {
		return "Quad";
	}

}
