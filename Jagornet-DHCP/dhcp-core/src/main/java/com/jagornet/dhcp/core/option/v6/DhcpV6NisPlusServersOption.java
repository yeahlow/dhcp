/*
 * Copyright 2009-2014 Jagornet Technologies, LLC.  All Rights Reserved.
 *
 * This software is the proprietary information of Jagornet Technologies, LLC. 
 * Use is subject to license terms.
 *
 */

/*
 *   This file DhcpV6NisPlusServersOption.java is part of Jagornet DHCP.
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
package com.jagornet.dhcp.core.option.v6;

import java.util.List;

import com.jagornet.dhcp.core.option.base.BaseIpAddressListOption;
import com.jagornet.dhcp.core.util.DhcpConstants;

/**
 * <p>Title: DhcpV6NisPlusServersOption </p>
 * <p>Description: </p>.
 * 
 * @author A. Gregory Rabil
 */
public class DhcpV6NisPlusServersOption extends BaseIpAddressListOption
{
	/**
	 * Instantiates a new dhcp nis plus servers option.
	 */
	public DhcpV6NisPlusServersOption()
	{
		this(null);
	}
	
	/**
	 * Instantiates a new dhcp nis plus servers option.
	 * 
	 * @param nisPlusServersOption the nis plus servers option
	 */
	public DhcpV6NisPlusServersOption(List<String> nisPlusServers)
	{
		super(nisPlusServers);
		setCode(DhcpConstants.V6OPTION_NISPLUS_SERVERS);
	}
}
