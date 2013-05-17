package com.josh.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.pjsip.pjsua.pjsua;

/**
 * 
 * @author Josh Solutions Limited
 * 
 */
public class Server {

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
	 * Initialise thread and sip stuff here
	 */
	private static void initialiseComponents() {
		// Use JNI or some wrapper to initialise SIP components so we can listen
		// out for request
		// System.loadLibrary("pjsua");
		// System.out.println("initialise components" + pjsua.create());
		// pjsua.create();
		System.out.println();
		// setup thread with sip stuff to listen and respond to sip messages in
		// a while loop.

	}

	private static void sendMessageToUser() {
		try {

			System.out.println("............");
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
	 * Start listening for sip requests
	 * 
	 * @param arg
	 */
	public static void main(String[] args) {
		initialiseComponents();

		System.out
				.println("welcome to the sip java client-server app \nBelow are your ip and port details");
		// get ip address
		InetAddress ip;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			// TODO: fix issue where it seems to retrieve the virtual machine IP
			// address and not the laptops wifi one
			ip = InetAddress.getLocalHost();
			fromHost = ip.getHostAddress();
			//fromHost = "192.168.43.227";
			System.out.println("Please enter Your port");
			fromPort = br.readLine();
			wrapper = new SipWrapper(fromHost, Integer.parseInt(fromPort));
			// fromHost = "172.29.168.113";
			System.out.println("Current IP address : " + fromHost);

			// TODO: Create a nice usable optios ui so user can eiither send
			// messages or listen for messages
			System.out.println("Options: ");
			System.out.println("S = Send message to user");
			System.out.println("R = Liste for messages");

			System.out.println("Enter key to exectue a certain option");

			char value = br.readLine().charAt(0);

			if (value == Option.SEND_MESSAGE_TO_A_USER.getOption()) {
				sendMessageToUser();
			} else if (value == Option.LISTEN_FOR_MESSAGES.getOption()) {
				return;
			} else if (value == Option.EXIT.getOption()) {
				System.exit(0);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// listenerThread.start();

	}

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
