package com.josh.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import javax.sip.InvalidArgumentException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;

/**
 * Use to setup and create a message that is used to be sent/recieved with SIP
 * 
 * @author Josh Solutions Limited
 * 
 */
public class SipMessage {

	private SipURI toUri;
	private SipURI fromUri;
	private Address fromAddress;
	private Address toAddress;
	private ToHeader toHeader;
	private FromHeader fromHeader;
	private CallIdHeader callIdHeader;
	private SipStack sipStack;
	private HeaderFactory headerFactory;
	private AddressFactory addressFactory;
	private MessageFactory messageFactory;

	private String toUsername;
	private String toHost;
	private int toPort;
	private String fromHost;
	private String fromUsername;
	private int fromPort;

	private SipURI requestUri;
	private SipProvider sipProvider;
	private Request request;
	private CSeqHeader cSeqHeader;
	private MaxForwardsHeader maxForwards;
	private SipURI requestURI;
	private ViaHeader viaHeader;
	private ArrayList viaHeaders;

	public SipMessage(SipFactory sipFactory, Properties properties,
			SipProvider sipProvider, String toUsername, String toHost,
			int toPort, String fromUsername, String fromHost, int fromPort)
			throws PeerUnavailableException, ParseException,
			InvalidArgumentException {
		this.toUsername = toUsername;
		this.toHost = toHost;
		this.toPort = toPort;

		this.fromUsername = toUsername;
		this.fromHost = fromHost;
		this.fromPort = fromPort;

		sipStack = sipFactory.createSipStack(properties);
		this.sipProvider = sipProvider;

		headerFactory = sipFactory.createHeaderFactory();

		addressFactory = sipFactory.createAddressFactory();

		messageFactory = sipFactory.createMessageFactory();

		initialiseElements();
	}

	private void initialiseElements() throws ParseException,
			InvalidArgumentException {

		// set from elements
		fromUri = addressFactory.createSipURI(null, fromHost + ":"
				+ fromPort);
		fromAddress = addressFactory.createAddress(fromUri);
		fromAddress.setDisplayName("username");
		fromHeader = headerFactory.createFromHeader(fromAddress,
				"textclientv1.0");

		// set To elements
		toUri = addressFactory.createSipURI(null, toHost + ":" + toPort);
		toAddress = addressFactory.createAddress(toUri);
		toAddress.setDisplayName("username");
		toHeader = headerFactory.createToHeader(toAddress, null);

		// set the request elements
		requestURI = addressFactory.createSipURI("", toHost + ":"
				+ toPort);
		requestURI.setTransportParam("udp");

		viaHeaders = new ArrayList();
		viaHeader = headerFactory.createViaHeader(fromHost, fromPort, "udp",
				null);
		viaHeaders.add(viaHeader);

		callIdHeader = sipProvider.getNewCallId();

		cSeqHeader = headerFactory.createCSeqHeader(1, Request.MESSAGE);

		maxForwards = headerFactory.createMaxForwardsHeader(70);

	}



	public void sendMessage(String message) throws Exception {
		// create request object based on all the preparation objects we
		// instantiated
		request = messageFactory.createRequest(requestURI, Request.MESSAGE,
				callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders,
				maxForwards);
		ContentTypeHeader contentTypeHeader = headerFactory
				.createContentTypeHeader("text", "plain");
		request.setContent(message, contentTypeHeader);

		sipProvider.sendRequest(request);
	}

}