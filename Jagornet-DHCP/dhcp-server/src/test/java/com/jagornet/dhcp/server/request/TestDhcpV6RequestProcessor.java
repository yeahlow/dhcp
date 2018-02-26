/*
 * Copyright 2009-2014 Jagornet Technologies, LLC.  All Rights Reserved.
 *
 * This software is the proprietary information of Jagornet Technologies, LLC. 
 * Use is subject to license terms.
 *
 */

/*
 *   This file TestDhcpV6RequestProcessor.java is part of Jagornet DHCP.
 *
 *   Jagornet DHCP is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Jagornet DHCP is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Jagornet DHCP.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.jagornet.dhcp.server.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.InetAddress;

import org.junit.Test;

import com.jagornet.dhcp.core.message.DhcpV6Message;
import com.jagornet.dhcp.core.util.DhcpConstants;

/**
 * The Class TestDhcpV6RequestProcessor.
 */
public class TestDhcpV6RequestProcessor extends BaseTestDhcpV6Processor
{
	/**
	 * Test solicit and request.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testNaSolicitAndRequest() throws Exception
	{
		DhcpV6Message requestMsg = buildNaRequestMessage(firstPoolAddr);
		requestMsg.setMessageType(DhcpConstants.V6MESSAGE_TYPE_SOLICIT);

		DhcpV6SolicitProcessor sProc = 
			new DhcpV6SolicitProcessor(requestMsg, requestMsg.getRemoteAddress().getAddress());

		DhcpV6Message advertiseMsg = sProc.processMessage();
		
		assertNotNull(advertiseMsg);
		
		// use the ADVERTISE message to create the REQUEST message
		advertiseMsg.setMessageType(DhcpConstants.V6MESSAGE_TYPE_REQUEST);
		DhcpV6RequestProcessor rProc = 
			new DhcpV6RequestProcessor(advertiseMsg, advertiseMsg.getRemoteAddress().getAddress());

		DhcpV6Message replyMsg = rProc.processMessage();
		
		assertNotNull(replyMsg);
		assertEquals(requestMsg.getTransactionId(), replyMsg.getTransactionId());
		assertEquals(DhcpConstants.V6MESSAGE_TYPE_REPLY, replyMsg.getMessageType());
		
		checkReply(replyMsg, 
				InetAddress.getByName("2001:DB8:1::A"),
				InetAddress.getByName("2001:DB8:1::FF"));
	}
	
	@Test
	public void testNaPdSolicitAndRequest() throws Exception
	{
		System.out.println("Running testNaPdSolicitAndRequest");
		
		DhcpV6Message requestMsg = buildNaPdRequestMessage(firstPoolAddr, null,
									DhcpConstants.ZEROADDR_V6.getHostAddress(), 56);
		requestMsg.setMessageType(DhcpConstants.V6MESSAGE_TYPE_SOLICIT);

		DhcpV6SolicitProcessor sProc = 
			new DhcpV6SolicitProcessor(requestMsg, requestMsg.getRemoteAddress().getAddress());

		DhcpV6Message advertiseMsg = sProc.processMessage();
		
		assertNotNull(advertiseMsg);
		
		// use the ADVERTISE message to create the REQUEST message
		advertiseMsg.setMessageType(DhcpConstants.V6MESSAGE_TYPE_REQUEST);
		DhcpV6RequestProcessor rProc = 
			new DhcpV6RequestProcessor(advertiseMsg, advertiseMsg.getRemoteAddress().getAddress());

		DhcpV6Message replyMsg = rProc.processMessage();
		
		assertNotNull(replyMsg);
		assertEquals(requestMsg.getTransactionId(), replyMsg.getTransactionId());
		assertEquals(DhcpConstants.V6MESSAGE_TYPE_REPLY, replyMsg.getMessageType());
		
		checkReply(replyMsg, 
				InetAddress.getByName("2001:DB8:1::A"),
				InetAddress.getByName("2001:DB8:1::FF"),
				3600,
				InetAddress.getByName("2001:DB8:1:4000::"),
				(short)56);
	}
	
	/**
	 * Test should multicast.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testShouldMulticast() throws Exception
	{
		DhcpV6Message requestMsg = buildNaRequestMessage(firstPoolAddr);
		requestMsg.setMessageType(DhcpConstants.V6MESSAGE_TYPE_REQUEST);

		DhcpV6SolicitProcessor sProc = 
			new DhcpV6SolicitProcessor(requestMsg, requestMsg.getRemoteAddress().getAddress());

		DhcpV6Message advertiseMsg = sProc.processMessage();
		
		assertNotNull(advertiseMsg);

		// hack the returned advertise to set unicast
		advertiseMsg.setUnicast(true);
		
		// use the ADVERTISE message to create the REQUEST message
		DhcpV6RequestProcessor rProc = 
			new DhcpV6RequestProcessor(advertiseMsg, advertiseMsg.getRemoteAddress().getAddress());

		DhcpV6Message replyMsg = rProc.processMessage();
		
		assertNotNull(replyMsg);
		assertEquals(requestMsg.getTransactionId(), replyMsg.getTransactionId());
		assertEquals(DhcpConstants.V6MESSAGE_TYPE_REPLY, replyMsg.getMessageType());
		
		checkReplyMsgStatus(replyMsg, DhcpConstants.V6STATUS_CODE_USEMULTICAST);
	}
	
	/**
	 * Test request not on link.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testRequestNotOnLink() throws Exception
	{
		DhcpV6Message requestMsg = buildNaRequestMessage(firstPoolAddr);
		requestMsg.setMessageType(DhcpConstants.V6MESSAGE_TYPE_SOLICIT);

		DhcpV6SolicitProcessor sProc = 
			new DhcpV6SolicitProcessor(requestMsg, requestMsg.getRemoteAddress().getAddress());

		DhcpV6Message advertiseMsg = sProc.processMessage();
		
		assertNotNull(advertiseMsg);

		// hack the returned advertise to request an off-link address
		advertiseMsg.getIaNaOptions().iterator().next().
				getIaAddrOptions().iterator().next().
						setIpAddress("2001:DB8:2::1");
		
		// use the ADVERTISE message to create the REQUEST message
		advertiseMsg.setMessageType(DhcpConstants.V6MESSAGE_TYPE_REQUEST);
		DhcpV6RequestProcessor rProc = 
			new DhcpV6RequestProcessor(advertiseMsg, advertiseMsg.getRemoteAddress().getAddress());

		DhcpV6Message replyMsg = rProc.processMessage();
		
		assertNotNull(replyMsg);
		assertEquals(requestMsg.getTransactionId(), replyMsg.getTransactionId());
		assertEquals(DhcpConstants.V6MESSAGE_TYPE_REPLY, replyMsg.getMessageType());
		
		checkReplyIaNaStatus(replyMsg, DhcpConstants.V6STATUS_CODE_NOTONLINK);
	}

}
