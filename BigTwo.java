import java.util.ArrayList;

/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card game.
 */
public class BigTwo implements CardGame {
	
	/**
	 * a constructor for creating a Big Two card game
	 */
	public BigTwo() {
		CardGamePlayer player1=new CardGamePlayer("");
		CardGamePlayer player2=new CardGamePlayer("");
		CardGamePlayer player3=new CardGamePlayer("");
		CardGamePlayer player4=new CardGamePlayer("");
		playerList=new ArrayList<CardGamePlayer>();
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		ui=new BigTwoGUI(this); 
		
		client=new BigTwoClient(this,this.ui);
	}
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable=new ArrayList<Hand>();
	private int currentPlayerIdx;
	private BigTwoGUI ui;
	private BigTwoClient client;
	private boolean started;
	
	/**
	 * a method for getting the number of players
	 *@return the number of players
	 */
	public int getNumOfPlayers() {return numOfPlayers;}
	
	/**
	 * a method for retrieving the deck of cards being used
	 * @return the deck of cards being used
	 */
	public Deck getDeck() {return deck;}
	
	/**
	 * a method for retrieving the list of players
	 * @return the list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {return playerList;}
	
	/**
	 * a method for retrieving the list of hands played on the table
	 * @return the list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable() {return handsOnTable;}
	
	/**
	 * a method for retrieving the index of the current player
	 * @return the index of the current player
	 */
	public int getCurrentPlayerIdx() {return currentPlayerIdx;}
	
	/**
	 * a method for updating the name of a player in the player list
	 * @param i index of the player to be updated
	 * @param name name of the player 
	 */
	public void addmember(int i,String name) {
		playerList.set(i,new CardGamePlayer(name));
	}
	
	/**
	 * a method to set the name of a player to an empty string (indicating his/her quitting)
	 * @param i index of the player who quits
	 */
	public void deletemember(int i) {
		playerList.set(i,new CardGamePlayer(""));
	}
	
	/**
	 * a method to call the sendMessage method in the client class when a player wants to send a chat message
	 * @param msg the chat message
	 */
	public void sendChatMessage(String msg) {
		client.sendMessage(new CardGameMessage(7,-1,msg));
	}
	
	/**
	 * a method to call the connect method in the client class
	 */
	public void callConnect() {
		client.connect();
	}
	
	/**
	 * a method to call the quit method in the client class
	 */
	public void callQuit() {
		client.quit();
	}
	
	/**
	 * a method to return the boolean started
	 * @return a boolean indicating if the game has started or not
	 */
	public boolean getStarted() {return started;}
	
	/**
	 * a method for starting/restarting the game with a given shuffled deck of cards
	 * @param deck a deck of cards to be used in the game
	 */
	public void start(Deck deck) {
		started=true;
		
		//(i) remove all the cards from the players as well as from the table
		for (CardGamePlayer player : playerList)
			player.removeAllCards();
		handsOnTable=new ArrayList<Hand>();
		
		//(ii) distribute the cards to the players
		for (int i=0;i<13;i++) {
			for (int j=0;j<4;j++)
			playerList.get(j).addCard(deck.getCard(4*i+j));
		}
		
		// to sort their cards
		for (CardGamePlayer player : playerList)
			player.sortCardsInHand();
	
		//(iii) identify the player who holds the Three of Diamonds
		//(iv) set both the currentPlayerIdx of the BigTwo object and the activePlayer of the BigTwoUI object to the index of the player who holds the Three of Diamonds
		for (CardGamePlayer player : playerList) {
			if (player.getCardsInHand().contains(new Card(0,2))) {
				this.currentPlayerIdx=playerList.indexOf(player);
				ui.setActivePlayer(this.currentPlayerIdx);
				break;
			}
		}
		//(v) call the repaint() method of the BigTwoUI object to show the cards on the table
		ui.repaint();
		
		//(vi) call the promptActivePlayer() method of the BigTwoUI object to prompt user to select cards and make his/her move
		ui.promptActivePlayer();
	}
	
	/**
	 * a method for making a move by a player with the specified index using the cards specified by the list of indices
	 * @param playerIdx index of the player who made the move
	 * @param cardIdx list of index specifying the cards being played
	 */
	public void makeMove(int playerIdx,int[] cardIdx) {
		this.client.sendMessage(new CardGameMessage(6,-1,cardIdx));
	}
	
	/**
	 * a method for checking a move made by a player
	 * @param playerIdx index of the player who made the move
	 * @param cardIdx list of index specifying the cards being played
	 */
	public void checkMove(int playerIdx, int[] cardIdx) {
		String validity="false";      //validity can be "true","false" or "pass"
		CardList cards;
		
		if (cardIdx==null) { //pass or typed incorrect number
			if (handsOnTable.size()!=0 && handsOnTable.get(handsOnTable.size()-1).getPlayer()!=playerList.get(playerIdx))   //the first player can't pass his/her turn
				validity="pass";}
		
		else { 					//the player indeed played some cards
			cards=playerList.get(playerIdx).play(cardIdx);
			Hand hand= composeHand(playerList.get(playerIdx),cards);
			
			if (hand!=null){   //the cards can form a hand
				if (handsOnTable.size()==0) {
					if (cards.contains(new Card(0,2))) 
						validity="true";   //the first player plays diamond3
				}
				//this isn't the first hand
				else if (hand.beats(handsOnTable.get(handsOnTable.size()-1)))
			  		validity="true";
			
				if (validity=="true") {
					ui.printMsg("{"+ hand.getType()+"} "+ cards.toString()+"\n");
					handsOnTable.add(hand);
					hand.getPlayer().removeCards(cards);
					if (!endOfGame()) {
						currentPlayerIdx=(currentPlayerIdx+1)%4;
						ui.setActivePlayer(currentPlayerIdx);
					}
				}
		    }
		}
		
		if (validity=="pass") {
			ui.printMsg("Pass"+"\n");
			currentPlayerIdx=(currentPlayerIdx+1)%4;
			ui.setActivePlayer(currentPlayerIdx);
		}
		
		 //validity==false
		else if (validity=="false") {
			ui.printMsg("Not a legal move!!!"+"\n");
		}
		
		if (!endOfGame()) {
			ui.promptActivePlayer();
		}
		else { 
			ui.printMsg("Game ends"+"\n");
			ui.disable();
			
			String sentence="";
			for (CardGamePlayer player : playerList) {
				if (player.getNumOfCards()!=0)
					sentence= sentence + player.getName()+ " has "+player.getNumOfCards()+" cards in hand."+ "\n";
				else sentence=sentence+ player.getName()+" wins the game."+"\n" ;
				}
			this.client.end(sentence);
		}
		ui.repaint();
	}
	
	
	/**
	 * a method for checking if the game ends
	 * @return a boolean indicating if the game ends
	 */
	public boolean endOfGame() {
		for (CardGamePlayer player : playerList) {
			if (player.getNumOfCards()==0)
				return true;
		}
		return false;
	}
	
	/**
	 * a method for starting a Big Two card game
	 */
	public static void main(String[] args) {
		BigTwo game=new BigTwo();
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player
	 * @param player the player who made this move
	 * @param cards the list of cards being played
	 * @return a valid hand from the specified list of cards of the player. Returns null if no valid hand can be composed from the specified list of cards.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		
		Hand handn;
		
		if (cards.size()==1) {
			handn=new Single(player,cards);
			if (handn.isValid()) {
				return handn;}
			else return null;
		}
		
		else if (cards.size()==2) {
			handn=new Pair(player,cards);
			if (handn.isValid())
				return handn;
			else return null;
		
		}
		
		else if (cards.size()==3) {
			handn=new Triple(player,cards);
			if (handn.isValid()) 
				return handn;
			else return null;
		}
		
		else if (cards.size()==5) {
			
			handn=new StraightFlush(player,cards);
			if (handn.isValid())
				return handn;
			
			handn=new Quad(player,cards);
			if (handn.isValid())
				return handn;
			
			handn=new FullHouse(player,cards);
			if (handn.isValid())
				return handn;
			
			handn=new Flush(player,cards);
			if (handn.isValid())
				return handn;
			
			handn=new Straight(player,cards);
			if (handn.isValid())
				return handn;
		}
		
		return null;
	}
	
}
