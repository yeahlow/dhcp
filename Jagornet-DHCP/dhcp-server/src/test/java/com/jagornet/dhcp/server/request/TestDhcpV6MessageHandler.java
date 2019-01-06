/*
 * Copyright 2009-2014 Jagornet Technologies, LLC.  All Rights Reserved.
 *
 * This software is the proprietary information of Jagornet Technologies, LLC. 
 * Use is subject to license terms.
 *
 */

/*
 *   This file TestDhcpV6MessageHandler.java is part of Jagornet DHCP.
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

import static org.junit.Assert.assertNotNull;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.junit.Test;

import com.jagornet.dhcp.core.message.DhcpV6Message;
import com.jagornet.dhcp.core.message.DhcpV6RelayMessage;
import com.jagornet.dhcp.core.message.TestDhcpRelayMessage;
import com.jagornet.dhcp.core.option.v6.DhcpV6ClientIdOption;
import com.jagornet.dhcp.core.option.v6.DhcpV6InterfaceIdOption;
import com.jagornet.dhcp.core.option.v6.DhcpV6RelayOption;
import com.jagornet.dhcp.core.util.DhcpConstants;
import com.jagornet.dhcp.server.config.OpaqueDataUtil;
import com.jagornet.dhcp.server.config.xml.OpaqueData;
import com.jagornet.dhcp.server.db.BaseTestCase;

public class TestDhcpV6MessageHandler extends BaseTestCase
{
	@Test
	public void testHandleMessage() throws Exception
	{
		DhcpV6RelayMessage relayMessage = TestDhcpRelayMessage.buildMockDhcpRelayMessage();
		
		// change the mock relay_reply message to a relay_forward
		relayMessage.setMessageType(DhcpConstants.V6MESSAGE_TYPE_RELAY_FORW);
		
        DhcpV6Message dhcpMessage = 
        	new DhcpV6Message(new InetSocketAddress("2001:db8:1::1", DhcpConstants.V6_SERVER_PORT),
        			new InetSocketAddress("fe80::1", DhcpConstants.V6_CLIENT_PORT));
        dhcpMessage.setMessageType(DhcpConstants.V6MESSAGE_TYPE_INFO_REQUEST);    // 1 byte
        dhcpMessage.setTransactionId(90599);                // 3 bytes

        OpaqueData opaque = new OpaqueData();
        opaque.setAsciiValue("jagornet-dhcp-server");	// 20 bytes

        dhcpMessage.putDhcpOption(new DhcpV6ClientIdOption(OpaqueDataUtil.toBaseOpaqueData(opaque)));
        
        // replace the reply message with a client info-request message
        DhcpV6RelayOption relayOption = relayMessage.getRelayOption();
        relayOption.setDhcpMessage(dhcpMessage);
        relayMessage.setRelayOption(relayOption);
        
		relayMessage.putDhcpOption(TestDhcpRelayMessage.buildMockInterfaceIdOption());
		DhcpV6Message replyMessage = 
			DhcpV6MessageHandler.handleMessage(InetAddress.getByName("3ffe::1"), relayMessage);
		DhcpV6InterfaceIdOption ifIdOption = 
			(DhcpV6InterfaceIdOption) replyMessage.getDhcpOption(DhcpConstants.V6OPTION_INTERFACE_ID);
		assertNotNull(ifIdOption);
	}
}
