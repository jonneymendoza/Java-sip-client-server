package com.josh.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.pjsip.pjsua.pjsua;

/**
 * 
 * Command line application client server that can send and receive SIP Messages
 * 
 * @author Josh Solutions Limited
 * 
 */
public class Server {

	private static BufferedReader br;

	private static String toUserName;

	private static String toHost;

	private static String toPort;

	private static String message;

	private static String fromHost;

	private static String fromUsername = "Jonney";

	private static String fromPort;

	private static SipWrapper wrapper;

	/**
	 * Message thread used to send multiple messages.
	 * 
	 * Messages are sent by simply typing the text and hitting the enter key to
	 * automatically send the message to the specified SIP address
	 */
	private static Thread messagingThread = new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				while (true) {
					message = br.readLine();
					wrapper.sendMessage(toUserName, toHost,
							Integer.parseInt(toPort), fromUsername, message);
				}

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	});;

	/**
	 * The Message callback interface
	 */
	private static MessageInterface messageCallback = new MessageInterface() {

		@Override
		public void onMessageSent(int statusCode) {
			if ((statusCode >= 200) && (statusCode < 300)) {
				// success. relay this back to the client
				System.out.println("succesfuly sent message");
			} else {
				// error
				System.out.println("message failed to send due to status code "
						+ statusCode);
			}

		}

		@Override
		public void onMessageRecieved(String message, String from) {
			System.out.println(from + " ---" + message);
		}
	};

	/**
	 * Setup the message to send by specifying the address to send a message and
	 * executing the message sending thread
	 */
	private static void sendMessageToUser() {
		try {

			System.out.println("............");
			System.out.println("............");

			System.out
					.println("Please enter username you wish to send message");
			br = new BufferedReader(new InputStreamReader(System.in));
			toUserName = br.readLine();
			System.out
					.println("Please enter host of the user you want to send a message to");
			toHost = br.readLine();
			System.out
					.println("Please enter Port of the user you want to send a message to");
			toPort = br.readLine();

			System.out.println("You are about to start sending  messages to : "
					+ "to " + toUserName + " " + toHost + ":" + toPort);

			System.out
					.println("Please enter Messages below and press enter to send");
			messagingThread.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Start listening for sip messages and send messages
	 * 
	 * @param arg
	 */
	public static void main(String[] args) {

		System.out.println("welcome to the sip java client-server app"
				+ " \nBelow are your ip  details");
		// get ip address
		InetAddress ip;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));

			ip = InetAddress.getLocalHost();
			fromHost = ip.getHostAddress();

			System.out.println("Please enter your port you wish to recieve messages");
			fromPort = br.readLine();
			wrapper = new SipWrapper(fromHost, Integer.parseInt(fromPort),
					messageCallback);

			System.out.println("Current IP address : " + fromHost);

			System.out.println("Options: ");
			System.out.println("S = Send and receive messags from a user");
			System.out.println("R = Listen for messages only");

			System.out.println("Enter key to exectue a certain option");

			char value = br.readLine().charAt(0);

			if (value == Option.SEND_MESSAGE_TO_A_USER.getOption()) {
				sendMessageToUser();
			} else if (value == Option.LISTEN_FOR_MESSAGES.getOption()) {
				System.out.println("Recieving messages..");
				return;
			} else if (value == Option.EXIT.getOption()) {
				System.exit(0);
			}

		} catch (Exception e1) {

			e1.printStackTrace();
		}

	}

	/**
	 * Enum class used to choose different options in the application
	 * 
	 * @author Josh Solutions Limited
	 * 
	 */
	private enum Option {
		SEND_MESSAGE_TO_A_USER('S'), LISTEN_FOR_MESSAGES('R'), EXIT('E');

		private char option;

		private Option(char option) {
			this.setOption(option);
		}

		public char getOption() {
			return option;
		}

		public void setOption(char option) {
			this.option = option;
		}

	}

}
