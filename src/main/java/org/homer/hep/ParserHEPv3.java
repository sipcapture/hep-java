/*
 * $Id$
 *
 *  HEP java parser 
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class ParserHEPv3 {
	
	public ParserHEPv3(ByteBuffer msg, int totalLength, String remoteIPAddress) throws Exception {

		try {

			int chunk_id = 0;
			int chunk_type = 0;
			int chunk_length = 0;
			int i = 6;			

			HEPStructure hepStruct = new HEPStructure();
			/* recieved time */
			hepStruct.recievedTimestamp = hepStruct.timeSeconds * 1000000 + hepStruct.timeUseconds;			            

			while (i < totalLength) {
				chunk_id = msg.getShort();
				chunk_type = msg.getShort();
				chunk_length = msg.getShort();
				
				if(chunk_length > totalLength) {
					System.out.println("Corrupted HEP: CHUNK LENGHT couldnt be bigger as CHUNK_LENGHT");
					return;
				}
				
				if(chunk_length == 0) {
					System.out.println("Corrupted HEP: LENGTH couldn't be 0!");
					return;
				}

				/* working with based HEP */
				if (chunk_id != 0) {
					msg.position(msg.position() + chunk_length - 6);					
					continue;
				}

				switch (chunk_type) {

				case 1:
					hepStruct.ipFamily = msg.get();
					break;

				case 2:
					hepStruct.protocolId = msg.get();
					break;

				case 3:
					int src_ip4 = msg.getInt();
					hepStruct.sourceIPAddress = ArrayHelper.IPtoString(src_ip4);					
					break;

				case 4:
					int dst_ip4 = msg.getInt();
					hepStruct.destinationIPAddress = ArrayHelper.IPtoString(dst_ip4);
					break;

				case 7:
					hepStruct.sourcePort = (msg.getShort() & 0xffff);
					break;

				case 8:
					hepStruct.destinationPort = (msg.getShort() & 0xffff);
					break;

				case 9:
					hepStruct.timeSeconds = ((long) msg.getInt() & 0xffffffffL);
					break;

				case 10:
					hepStruct.timeUseconds = ((long) msg.getInt() & 0xffffffffL);
					break;

				case 11:
					hepStruct.protocolType = msg.get();
					break;

				case 12:
					hepStruct.captureId = msg.getInt();
					break;
					
				case 14:					
					hepStruct.captureAuthUser = new String(msg.array(), msg.position(), (chunk_length - 6));					
												
					msg.position(msg.position() + chunk_length - 6);					
					break;					

				case 15:					
					hepStruct.payloadByteMessage = ByteBuffer.allocate((chunk_length - 6));
					hepStruct.payloadByteMessage.put(msg.array(), msg.position(), (chunk_length - 6));
					msg.position(msg.position() + chunk_length -6 );					
					break;
					
				case 16:																		
					/* compressed payload */
					hepStruct.payloadByteMessage = ByteBuffer.wrap(extractBytes(msg.array(), msg.position(), (chunk_length - 6)));
					msg.position(msg.position() + chunk_length - 6);
					break;	
				
				case 17:
					hepStruct.hepCorrelationID = new String(msg.array(), msg.position(), (chunk_length - 6));
					msg.position(msg.position() + chunk_length - 6);
					break;	
					
				
				default:
					System.out.println("Unknown default chunk: ["+chunk_type+"]");
					msg.position(msg.position() + chunk_length - 6);
					break;
				
				}

				i += chunk_length;
								
			}
			
			
			hepStruct = null;


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable RUN WORKER");
		}
    }	
	
    public static byte[] extractBytes(byte[] input, int position, int len) throws UnsupportedEncodingException, IOException, DataFormatException
    {        

    	Inflater decompress = new Inflater();
    	
        decompress.setInput(input, position, len);        
 
        ByteArrayOutputStream baos = new ByteArrayOutputStream(len);
        byte[] buff = new byte[1024];
        while(!decompress.finished())
        {
            int count = decompress.inflate(buff);
            baos.write(buff, 0, count);
        }
        baos.close();
        byte[] output = baos.toByteArray(); 
        return output;
    }
    
	
}
