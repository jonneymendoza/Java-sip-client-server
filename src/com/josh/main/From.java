package com.josh.main;

import javax.sip.address.Address;
import javax.sip.header.FromHeader;

import gov.nist.javax.sip.address.SipUri;

/**
 * From class that deals with From objects used for SIP
 * 
 * 
 * @author Josh Solutions Limited
 *
 */
public class From {

	private SipUri fromUri;
	private FromHeader fromHeader;
	private Address fromAddress;
}
