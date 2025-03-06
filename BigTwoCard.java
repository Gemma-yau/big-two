
/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a card used in a Big Two card game
 */
public class BigTwoCard extends Card {
	
	/**
	 * A constructor for building a card with the specified suit and rank.
	 * @param suit an integer between 0 and 3
	 * @param rank an integer between 0 and 12
	 */
	public BigTwoCard(int suit,int rank) {
		super(suit,rank);
	}
	
	
	/**
	 * A method for comparing the order of this card with the specified card
	 * @param card a specific card to be compared with
	 * @return a negative integer, zero, or a positive integer when this card is less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card) {
		final char[] order= {'l','m','a','b','c','d','e','f','g','h','i','j','k'};
		if (order[this.rank] > order[card.rank]) 
			return 1;
		else if (order[this.rank]<order[card.rank])
			return -1;
		else if (this.suit>card.suit)
			return 1;
		else if (this.suit<card.suit)
			return -1;
		else return 0;
	}
	
}
