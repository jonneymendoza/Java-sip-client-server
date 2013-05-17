package com.josh.main;

/**
 * CallBack listener used for receiving and sending messages
 * 
 * @author Josh Solutions Limited
 * 
 */
public interface MessageInterface {

	/**
	 * callback listener when message has been sent
	 * 
	 * @param statusCode
	 *            The status code returned by the response
	 */
	void onMessageSent(int statusCode);

	/**
	 * Callback listener when message is received
	 * 
	 * @param message
	 *            The message received
	 * @param from
	 *            From address of message
	 */
	void onMessageRecieved(String message, String from);

}
