/*
 * Copyright 2009-2014 Jagornet Technologies, LLC.  All Rights Reserved.
 *
 * This software is the proprietary information of Jagornet Technologies, LLC. 
 * Use is subject to license terms.
 *
 */

/*
 *   This file DhcpV6PacketDecoder.java is part of Jagornet DHCP.
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
package com.jagornet.dhcp.server.netty4;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.DatagramPacketDecoder;

/**
 * Title: DhcpV6PacketDecoder
 * Description: The protocol decoder used by the NETTY-based DHCPv6 server
 * when receiving packets.
 * 
 * @author A. Gregory Rabil
 */
@ChannelHandler.Sharable
public class DhcpV6PacketDecoder extends DatagramPacketDecoder
{
	DhcpV6ChannelDecoder dhcpV6ChannelDecoder;

    public DhcpV6PacketDecoder(DhcpV6ChannelDecoder dhcpV6ChannelDecoder)
    {
    	super(dhcpV6ChannelDecoder);
    	this.dhcpV6ChannelDecoder = dhcpV6ChannelDecoder;
    }
    
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
		if (obj instanceof DatagramPacket) {
			dhcpV6ChannelDecoder.setRemoteSocketAddress(((DatagramPacket) obj).sender());
			super.channelRead(ctx, obj);
		}
	}
	
}
