package com.josh.main;

import java.text.ParseException;
import java.util.Properties;
import java.util.TooManyListenersException;

import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransportNotSupportedException;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * Sip wrapper class that creates a sip object that can be used to send and
 * recieve messages.
 * 
 * @author Josh Solutions Limited
 * 
 */
public class SipWrapper implements SipListener {

	private String toUsername;
	private String toHost;
	private int toPort;
	private String fromHost;
	private String fromUsername;
	private int fromPort;

	private SipFactory sipFactory;
	private SipStack sipStack;
	private SipProvider sipProvider;
	private Properties properties;
	private MessageFactory messageFactory;

	/**
	 * Setup SIP and its listeners
	 * 
	 * @param fromHost
	 *            The Host IP you wish to receive SIP requests
	 * @param fromPort
	 *            The Port you wish to receive SIP requests
	 * @throws PeerUnavailableException
	 */
	public SipWrapper(String fromHost, int fromPort)
			throws PeerUnavailableException {

		properties = new Properties();
		this.fromHost = fromHost;
		this.fromPort = fromPort;
		sipFactory = SipFactory.getInstance();
		messageFactory = sipFactory.createMessageFactory();

		properties.setProperty("javax.sip.STACK_NAME", "TextClientServer");

		properties.setProperty("javax.sip.IP_ADDRESS", fromHost);
		sipStack = sipFactory.createSipStack(properties);
		createSipListeners();
	}

	/**
	 * Send a SIP message
	 * 
	 * @param toUsername The username to send to
	 * @param toHost  The Host IP address to send to
	 * @param toPort The port to send to
	 * @param fromUsername The clients username
	 * @param message The Message content
	 * @throws Exception
	 */
	public void sendMessage(String toUsername, String toHost, int toPort,
			String fromUsername, String message) throws Exception {

		this.toUsername = toUsername;
		this.toHost = toHost;
		this.toPort = toPort;

		this.fromUsername = fromUsername;

		SipMessage sipMessage = new SipMessage(sipFactory, properties,
				sipProvider, toUsername, toHost, toPort, fromUsername,
				fromHost, fromPort);

		sipMessage.sendMessage(message);
	}

	/**
	 * Create sip listeners that listens for requests.
	 */
	private void createSipListeners() {

		try {

			@SuppressWarnings("deprecation")
			ListeningPoint udp = sipStack.createListeningPoint(fromPort, "udp");
			sipProvider = sipStack.createSipProvider(udp);
			sipProvider.addSipListener(this);
			sipProvider.addSipListener(this);

		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ObjectInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processIOException(IOExceptionEvent ioex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processRequest(RequestEvent evt) {
		Request request = evt.getRequest();
		String method = request.getMethod();

		if (method.endsWith(Request.MESSAGE)) {
			// got a message. lets see whats inside it
			System.out.println(".................");
			System.out.println(new String(request.getRawContent()));
			FromHeader from = (FromHeader) request.getHeader("From");
			System.out.println(from.getAddress().toString());

			// RESPONSE
			Response response = null;

			try {

				response = messageFactory.createResponse(200, request);
				ToHeader toHeader = (ToHeader) response
						.getHeader(ToHeader.NAME);
				toHeader.setTag("888"); // This is mandatory as per the spec.
				ServerTransaction st = sipProvider
						.getNewServerTransaction(request);
				// response.setContent(arg0, arg1);
				st.sendResponse(response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void processResponse(ResponseEvent evt) {
		Response response = evt.getResponse();

		int status = response.getStatusCode();
		if ((status >= 200) && (status < 300)) {
			// success. relay this back to the client
			System.out.println("succesfuly sent message");
		} else {
			// error
			System.out.println("message failed to send due to status code "
					+ status);
		}

	}

	@Override
	public void processTimeout(TimeoutEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		// TODO Auto-generated method stub

	}

	public String getToUsername() {
		return toUsername;
	}

	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}

	public String getToHost() {
		return toHost;
	}

	public void setToHost(String toHost) {
		this.toHost = toHost;
	}

	public int getToPort() {
		return toPort;
	}

	public void setToPort(int toPort) {
		this.toPort = toPort;
	}

	public String getFromHost() {
		return fromHost;
	}

	public void setFromHost(String fromHost) {
		this.fromHost = fromHost;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}

	public int getFromPort() {
		return fromPort;
	}

	public void setFromPort(int fromPort) {
		this.fromPort = fromPort;
	}

}
