package com.jagornet.dhcpv6.server.request.binding;

import java.net.InetAddress;

import com.jagornet.dhcpv6.server.config.DhcpConfigObject;
import com.jagornet.dhcpv6.xml.LinkFilter;

/**
 * Interface BindingPool.  
 * Common interface for AddressBindingPool, PrefixBindingPool and V4AddressBindingPool
 * 
 * @author A. Gregory Rabil
 */
public interface BindingPool extends DhcpConfigObject
{
	public InetAddress getStartAddress();
	public InetAddress getEndAddress();
	public InetAddress getNextAvailableAddress();
	public void setUsed(InetAddress addr);
	public void setFree(InetAddress addr);
	public boolean contains(InetAddress addr);
	public LinkFilter getLinkFilter();
}