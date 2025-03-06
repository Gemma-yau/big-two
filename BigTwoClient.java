import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

/**
 * The BigTwoClient class implements the NetworkGame interface.
 * It is used to model a Big Two game client that is responsible for establishing a connection and communicating with the Big Two game server.
 */
public class BigTwoClient implements NetworkGame {
	
	/**
	 * a constructor for creating a Big Two client.
	 * @param game a reference to a BigTwo object associated with this client
	 * @param gui a reference to a BigTwoGUI object associated the BigTwo object.
	 */
	public BigTwoClient(BigTwo game, BigTwoGUI gui) {
		this.game=game;
		this.gui=gui;
		String name=JOptionPane.showInputDialog("What's your name?");
		setPlayerName(name);
		connect();
		}
	
	private BigTwo game;
	private BigTwoGUI gui;
	private Socket sock;
	private ObjectOutputStream oos;
	private int playerID;
	private String playerName;
	private String serverIP="127.0.0.1";
	private int serverPort=2396;
	
	/**
	 * a method for getting the playerID (i.e., index) of the local player
	 * @return the playerID (i.e., index) of the local player
	 */
	public int getPlayerID() {return playerID;}
	/**
	 * a method for setting the playerID (i.e., index) of the local player
	 * @param playerID an integer indicating the index of the local player
	 */
	public void setPlayerID(int playerID) {this.playerID=playerID;}
	/**
	 * a method for getting the name of the local player
	 * @return a string indicating the name of the local player
	 */
	public String getPlayerName() {return playerName;}
	/**
	 * a method for setting the name of the local player.
	 * @param playerName a string indicating the name of the local player
	 */
	public void setPlayerName(String playerName) {this.playerName=playerName;}
	/**
	 * a method for getting the IP address of the game server.
	 * @return the IP address of the game server
	 */
	public String getServerIP() {return serverIP;}
	/**
	 * a method for setting the IP address of the game server.
	 * @param serverIP the IP address of the game server
	 */
	public void setServerIP(String serverIP) {this.serverIP=serverIP;}
	/**
	 * a method for getting the TCP port of the game server.
	 * @return the TCP port of the game server
	 */
	public int getServerPort() {return serverPort;}
	/**
	 * a method for setting the TCP port of the game server.
	 * @param the TCP port of the game server
	 */
	public void setServerPort(int serverPort) {this.serverPort=serverPort;}
	
	/**
	 * a method for making a socket connection with the game server
	 */
	public void connect() {
		try {
			sock=new Socket(getServerIP(),getServerPort());		
			oos=new ObjectOutputStream(sock.getOutputStream());
			gui.printMsg("Connected to server at "+'/'+getServerIP()+':'+getServerPort()+"\n");
			gui.disableconnect();
			Thread receivemsg=new Thread(new ServerHandler());
			receivemsg.start();
		} catch(Exception ex){
			ex.printStackTrace();
		}	
	}
	
	/**
	 * a method for parsing the messages received from the game server
	 * @param message the message to be parsed
	 */
	public void parseMessage(GameMessage message) {
		if (message.getType()==0) {					//PLAYER_LIST
			setPlayerID(message.getPlayerID());
			this.gui.setPlayerID(playerID);
			String[] namelist=(String[]) message.getData(); 
			namelist[getPlayerID()]=getPlayerName();  
			for (int i=0;i<4;i++) {
				String name=namelist[i];
				if (name != null) {
					this.game.addmember(i,name);
				}
			}
			gui.repaint();
			String n=getPlayerName();
			sendMessage(new CardGameMessage(1,-1,n));
		}
		
		else if (message.getType()==1) {			//JOIN
			this.game.addmember(message.getPlayerID(),(String) message.getData());
			this.gui.repaint();
			if (message.getPlayerID()==playerID) {
				sendMessage(new CardGameMessage(4,-1,null));
			}
			else 		
				gui.printMsg(game.getPlayerList().get(message.getPlayerID()).getName()+" joins the game."+"\n");
		}
		
		else if (message.getType()==2) {			//FULL
			this.gui.printMsg("The server is full, so you can't join, bye."+"\n");
		}
		
		else if (message.getType()==3) {			//QUIT
			gui.clearMsgArea();
			this.game.deletemember(message.getPlayerID());
			if (game.getStarted()) {
				this.gui.disable();
				gui.repaint();
				sendMessage(new CardGameMessage(4,-1,null));
			}
		}
		
		else if (message.getType()==4) {			//READY
			this.gui.printMsg(this.game.getPlayerList().get(message.getPlayerID()).getName()+" is ready. "+"\n");
		}
		
		else if (message.getType()==5) {			//START
			BigTwoDeck deck=(BigTwoDeck) message.getData();
			gui.printMsg("Game starts!"+"\n");
			this.game.start(deck);
		}
		
		else if (message.getType()==6) {			//MOVE
			this.game.checkMove(message.getPlayerID(),(int[]) message.getData());
		}
		
		else if (message.getType()==7) {			//MSG
			this.gui.printChat((String)message.getData());
		}
	}
	
	/**
	 * a method to display the game results when the game ends
	 * @param sentence the game result to be printed in the dialog box
	 */
	public void end(String sentence) {
		int clicked=JOptionPane.showConfirmDialog(null,sentence,"END",JOptionPane.DEFAULT_OPTION);
		if (clicked==0) {
			sendMessage(new CardGameMessage(4,-1,null));
		}
	}
	
	/**
	 *  a method to handle quitting of a player
	 */
	public synchronized void quit() {
		try {
	        oos.close(); 
	        sock.close(); 
	        System.exit(0);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    } 
	}
	
	/**
	 * a method for sending the specified message to the game server
	 * @param message message to be parsed
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * an inner class that implements the Runnable interface to receive messages from the server
	 */
	class ServerHandler implements Runnable {
		private ObjectInputStream reader=null;
		public ServerHandler() {
			try {
				reader=new ObjectInputStream(sock.getInputStream());
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		public void run() {
			CardGameMessage msg;
			try {
				while((msg=(CardGameMessage)reader.readObject()) !=null) 
					parseMessage(msg);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}	
		}
	}
}