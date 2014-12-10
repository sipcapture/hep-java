/*
 * $Id$
 *
 *  HEPStructure java  
 *
 *  Author: Alexandr Dubovikov <alexandr.dubovikov@gmail.com>
 *  (C) Homer Project 2012-2014 (http://www.sipcapture.org)
 *
 * Homer capture agent is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version
 *
 * Homer capture agent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
*/

package org.homer.hep;

import java.nio.ByteBuffer;

public class HEPStructure {
		
	public int ipFamily  = 0;
	public int protocolId = 0;
	public int sourcePort = 0;
	public int destinationPort = 0;
	public long timeSeconds = 0;
	public long timeUseconds = 0;
	public int protocolType = 0;
	public int captureId = 0;
	public String hepCorrelationID = null;
	public String captureAuthUser = null;
	public String sourceIPAddress = null;
	public String destinationIPAddress = null;
	public int uuid = 0;
	public boolean authorized = false;	
	public ByteBuffer payloadByteMessage = null;	
	public long recievedTimestamp;
	public String node;	
}
