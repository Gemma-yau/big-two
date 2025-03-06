import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The BigTwoGUI class implements the CardGameUI interface. It is used to build a GUI for the Big Two card game and handle all user actions.
 */
public class BigTwoGUI implements CardGameUI{
	
	//CONSTRUCTOR
	/**
	 * a constructor for creating a BigTwoGUI
	 * @param game a reference to a Big Two card game associated with this GUI.
	 */
	public BigTwoGUI(BigTwo game) { 
		this.game=game;
		initialize();
		disable();
	}
	
	//INSTANCE VARIABLES
	private BigTwo game;
	private boolean[] selected;
	private int playerid;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea chatArea;
	private JTextField chatInput;
	private JMenuItem connect;
	
	//INTERFACE METHODS
	/**
	 * a method for setting the index of the active player
	 * @param activePlayer an integer indicating the index of the active player
	 */
	public void setActivePlayer(int activePlayer) { 
		this.activePlayer=activePlayer;
	}
	
	/**
	 * a method for setting the index of the local player
	 * @param id an integer indicating the index of the local player
	 */
	public void setPlayerID(int id) {
		this.playerid=id;
	}
	/**
	 * a method for repainting the GUI
	 */
	public void repaint() {
		frame.repaint();
	}
	
	/**
	 * a method for printing the specified string to the message area of the GUI
	 * @param msg the string of message to be printed 
	 */
	public void printMsg(String msg) {
		this.msgArea.append(msg);
	}
	
	/**
	 * a method for printing the specified string to the chat area of the GUI
	 * @param msg the string of message to be printed 
	 */
	public void printChat(String msg) {
		this.chatArea.append(msg);
	}
	
	/**
	 * a method for clearing the message area of the GUI
	 */
	public void clearMsgArea() {
		this.msgArea.setText(null);
	}
	
	/**
	 * a method for resetting the GUI
	 */
	public void reset() {
		selected=new boolean[game.getPlayerList().get(activePlayer).getNumOfCards()];
		clearMsgArea();
		this.chatArea.setText(null);
		enable();
	}
	
	/**
	 * a method for enabling user interactions(play and pass buttons) with the GUI
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}
	
	/**
	 * a method for disabling user interactions(play and pass buttons) with the GUI
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}
	
	/**
	 * a method for disabling the menu item "connect" in the GUI
	 */
	public void disableconnect() {
		connect.setEnabled(false);
	}

	/**
	 * a method for prompting the active player to select cards and make his/her move
	 */
	public void promptActivePlayer() {
		if (playerid==activePlayer) {
			msgArea.append("Your turn: \n");
			enable();
		}
		else {
			msgArea.append(game.getPlayerList().get(activePlayer).getName()+ "'s turn: \n");
			disable();
		}
		selected=new boolean[game.getPlayerList().get(playerid).getNumOfCards()];
	}
	
	
	//INNER CLASS
	/**
	 * an inner class that extends the JPanel class and implements the MouseListener interface to draw the card game table and  handle mouse click events
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		
		/**
		 * a constructor for creating a BigTwoPanel
		 */
		public BigTwoPanel() {
			addMouseListener(this);
		}
		
		/**
		 * a method to draw the card game table
		 * @param g a Graphics object to draw things on the JPanel
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			ArrayList<CardGamePlayer> players=game.getPlayerList();
			for (int i=0;i<players.size();i++) {
				if (players.get(i).getName() != "") {
					String iconpath="images/player"+i+".PNG";
					Image image=new ImageIcon(iconpath).getImage();	
					g.drawImage(image,10,130*i+20,140,140, this);
					if (i!=playerid) {
						g.drawString(players.get(i).getName(),10,130*i+35);
					}
				    else { 
						g.drawString("You",10,130*i+35);
				    }
				}
				if (game.getStarted()) {
					if (i!=playerid)
						drawback(i,g);
					else drawface(i,g);	
				}	
			}
				drawcardplayed(g);
		}
		
		/**
		 * a method to draw the faces of cards of the active player
		 * @param i an integer indicating the index of the active player
		 * @param g a Graphics object to draw things on the JPanel
		 */
		public void drawface(int i,Graphics g){
			if (game.getPlayerList().get(i).getNumOfCards()==0) 
				return;
			CardList cardsinhand=game.getPlayerList().get(i).getCardsInHand();
			char[] RANKS = { 'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K' };
			char[] SUITS= {'d','c','h','s'};
			for (int j=0;j<cardsinhand.size();j++) {
				Card card=cardsinhand.getCard(j);
				String path="images/" + RANKS[card.getRank()] + SUITS[card.getSuit()]+".jpg";
				Image image=new ImageIcon(path).getImage();
				if (selected[j]) {
					g.drawImage(image,j*22+150,i*135+30-12,77,110, bigTwoPanel);
				}
				else { g.drawImage(image,j*22+150,i*135+30,77,110, bigTwoPanel);}
			}
		}
		
		/**
		 * a method to draw the back of cards of the other players
		 * @param i an integer indicating the index of the player
		 * @param g a Graphics object to draw things on the JPanel
		 */
		public void drawback(int i,Graphics g) {
			Image image=new ImageIcon("images/back.jpg").getImage();
			for (int j=0;j<game.getPlayerList().get(i).getCardsInHand().size();j++) 
				g.drawImage(image,j*22+150,i*135+30,77,110, bigTwoPanel);
		}
		
		/**
		 * a method to draw the last hand of cards played on the table
		 * @param g a Graphics object to draw things on the JPanel
		 */
		public void drawcardplayed(Graphics g) {
			if (game.getHandsOnTable().size()!=0) {
				char[] RANKS = { 'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K' };
				char[] SUITS= {'d','c','h','s'};
				
				Hand lasthand=game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
				int lastplayer=game.getPlayerList().indexOf(lasthand.getPlayer());
				g.drawString(game.getPlayerList().get(lastplayer).getName(),10,555);
				
				for (int j=0;j<lasthand.size();j++) {
					Card card=lasthand.getCard(j);
					String path="images/" + RANKS[card.getRank()] + SUITS[card.getSuit()]+".jpg";
					Image image=new ImageIcon(path).getImage();
					g.drawImage(image,j*82+150,570,77,110, bigTwoPanel);
				}
			}
		}
			
		/**
		 * a method to handle mouse click events
		 * @param e a MouseEvent
		 */
		public void mouseReleased(MouseEvent e) {
		
				Point p=e.getPoint();
					for (int i=game.getPlayerList().get(playerid).getCardsInHand().size()-1;i>=0;i--) {
						int h=selected[i]?-12:0;
						if (i*22+150 <= p.x && p.x <= i*22+150+77 && playerid*135+30+h <= p.y && p.y <= playerid*135+30+110+h) {
							selected[i]=!selected[i];
							this.repaint();
							break;
						}
					}
		}
		
		/**
		 * method not being overridden but has to be included due to the implementation
		 * @param e a MouseEvent
		 */
		public void mouseClicked(MouseEvent e) {}
		/**
		 * method not being overridden but has to be included due to the implementation
		 * @param e a MouseEvent
		 */
		public void mousePressed(MouseEvent e) {}
		/**
		 * method not being overridden but has to be included due to the implementation
		 * @param e a MouseEvent
		 */
		public void mouseEntered(MouseEvent e) {}
		/**
		 * method not being overridden but has to be included due to the implementation
		 * @param e a MouseEvent
		 */
		public void mouseExited(MouseEvent e) {}
	}
		
	
	/**
	 * an inner class that implements the ActionListener interface to handle button-click events for the “Play” button
	 */
	class PlayButtonListener implements ActionListener{
		/**
		 * a method to handle button-click events for the “Play” button
		 * @param e an ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			boolean containtrue=false;
			ArrayList<Integer> cardidx=new ArrayList<Integer>() ;
				for (int i=0;i<selected.length;i++) {
					if (selected[i]) {
						containtrue=true;
						cardidx.add(i);
					}
				}
			if (containtrue) {
				int[] idx=new int[cardidx.size()];
				for (int i=0;i<cardidx.size();i++)
					idx[i]=cardidx.get(i);
				game.makeMove(activePlayer,idx);
			}
		}
    }
	
	/**
	 * an inner class that implements the ActionListener interface to handle button-click events for the “Pass” button
	 */
	class PassButtonListener implements ActionListener{
		/**
		 * a method to handle button-click events for the “Pass” button
		 * @param e an ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			game.makeMove(activePlayer,null);
		}
	}
	
	/**
	 *  an inner class that implements the ActionListener interface to handle menu-item-click events for the “Connect” menu item
	 */
	class ConnectMenuItemListener implements ActionListener{
		/**
		 * a method to handle menu-item-click events for the “Connect" menu item
		 * @param e an ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			game.callConnect();
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface to handle menu-item-click events for the “Quit” menu item
	 */
	class QuitMenuItemListener implements ActionListener{
		/**
		 * a method to handle menu-item-click events for the “Quit” menu item
		 * @param e an ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			game.callQuit();
		}
	}
	
	/**
	 * an inner class that implements the ActionListener interface to handle enter-key-pressed events for the text field 
	 */
	class ChatItemListener implements ActionListener {
		/**
		 * a method to handle enter-key-pressed events for the text field
		 * @param e an ActionEvent
		 */
		public void actionPerformed(ActionEvent e) {
			game.sendChatMessage(chatInput.getText()+"\n");
			chatInput.setText("");
		}
	}
	
	
	//PRINTING THE MAIN OF THE GAME
	/**
	 * a method for the initialization of the GUI 
	 */
	public void initialize() {
		frame=new JFrame("Big Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		playButton=new JButton("Play");
		passButton=new JButton("Pass");
		msgArea=new JTextArea(5,22);
		chatArea=new JTextArea();
		chatInput=new JTextField(22);
		
		//MENU BAR
		JMenuBar menubar=new JMenuBar();
		JMenu menu=new JMenu("Game");
		JMenu menu2=new JMenu("Message");
		connect=new JMenuItem("Connect");
		connect.addActionListener(new ConnectMenuItemListener());
		JMenuItem quit=new JMenuItem("Quit");
		quit.addActionListener(new QuitMenuItemListener());
		menu.add(connect);
		menu.add(quit);
		menubar.add(menu);
		menubar.add(menu2);
		frame.setJMenuBar(menubar);
		
		bigTwoPanel=new BigTwoPanel();
		bigTwoPanel.setBackground(new Color(102, 153, 204));
		frame.add(bigTwoPanel,BorderLayout.CENTER);
		
		//RIGHTSIDE
	    JPanel right=new JPanel();
	    frame.add(right,BorderLayout.EAST);    
	    JScrollPane upscroller = new JScrollPane(msgArea); 
	    msgArea.setLineWrap(true);   
	    msgArea.setEditable(false);  
	    JScrollPane downscroller = new JScrollPane(chatArea); 
	    chatArea.setLineWrap(true);
	    chatArea.setEditable(false);
	    chatInput.addActionListener(new ChatItemListener());
	    
	    right.setLayout(new GridBagLayout());
		GridBagConstraints r=new GridBagConstraints();
		r.fill=GridBagConstraints.BOTH;
	    r.weightx=1;
		r.weighty=0.5;
		r.insets=new Insets(5,5,5,5);
	    right.add(upscroller,r); 
	    r.gridy=1;
	    r.insets=new Insets(0,5,0,5);
		right.add(downscroller,r);
		
		//BOTTOMPART
		JPanel bottom=new JPanel();
		frame.add(bottom,BorderLayout.SOUTH);
		
		bottom.setLayout(new GridBagLayout());
		GridBagConstraints b=new GridBagConstraints();
		
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());	
	    bottom.add(playButton);
		bottom.add(passButton);
		
		b.anchor=GridBagConstraints.LINE_END;
	    b.weightx=1;
	    JPanel text=new JPanel();
	    bottom.add(text,b);
		text.setLayout(new GridBagLayout());
		GridBagConstraints t=new GridBagConstraints();
		text.add(new JLabel("Type:"));
		t.weightx=1;
		text.add(chatInput);
		
		frame.setSize(900, 770);
		frame.setVisible(true);	
	}
}
