package org.homer.hep;

import java.net.*;
import java.nio.ByteBuffer;

public class SendHEPv3 {

	public static void main(String args[]) {
		try {

			String host = "hep.sipcapture.org";
			int port = 9063;
			int capacity = 200;
	        	ByteBuffer msg = ByteBuffer.allocate(capacity);
            		HEPStructure hepStruct = new HEPStructure();

			//set that you want to the hepStruct. check the HEP3 protocol
            		hepStruct.sourcePort = 5060;
            		hepStruct.destinationPort = 5061;

	        	//Convert to ByteArray

	        	//https://github.com/sipcapture/HEP/blob/master/docs/HEP3_Network_Protocol_Specification_REV_36.pdf
	        	//HEP Header
	        	msg.put("HEP3".getBytes(), 0, 4);
	        	// IP proto 0x0000 - vendor chunk - 0x0001 - type , 2+2+2+1 (2 bytes * 3) + 1 byte - total length - and IP
	        	msg.putShort((short)0x0000).putShort((short)0x0001).putShort((short)(2+2+2+1)).put((byte) hepStruct.ipFamily);
	        	// Protocol ID
	        	msg.putShort((short)0x0000).putShort((short)0x0002).putShort((short)(2+2+2+1)).put((byte) hepStruct.protocolId);

	        	// Source IP
            		InetAddress srcAddr = InetAddress.getByName(hepStruct.sourceIPAddress);
            		ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE);
            		buffer.put(srcAddr.getAddress());
            		buffer.position(0);
	        	msg.putShort((short)0x0000).putShort((short)0x0003).putShort((short)(2+2+2+4)).putInt(buffer.getInt());

	        	//Destination IP
	        	InetAddress dstAddr = InetAddress.getByName(hepStruct.destinationIPAddress);
	        	buffer.clear();
            		buffer.put(dstAddr.getAddress());
            		buffer.position(0);
	        	msg.putShort((short)0x0000).putShort((short)0x0004).putShort((short)(2+2+2+4)).putInt(buffer.getInt());

	        	//Source Port
	        	msg.putShort((short)0x0000).putShort((short)0x0005).putShort((short)(2+2+2+2)).putShort((short) (hepStruct.sourcePort & 0xffff));
	        	//Destination Port
	        	msg.putShort((short)0x0000).putShort((short)0x0006).putShort((short)(2+2+2+2)).putShort((short) (hepStruct.destinationPort & 0xffff));

			// Get the internet address of the specified host
			InetAddress address = InetAddress.getByName(host);

			//Our size
			int length = msg.position();
			//set limit
			msg.limit(length);
			//go to position 0
            		msg.position(0);

            		//Our message
            		byte[] message = new byte[msg.remaining()];

			// Initialize a datagram packet with data and address
			DatagramPacket packet = new DatagramPacket(message, message.length, address, port);

			// Create a datagram socket, send the packet through it, close it.
			DatagramSocket dsocket = new DatagramSocket();
			dsocket.send(packet);
			dsocket.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
