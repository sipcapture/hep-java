<img src="https://user-images.githubusercontent.com/1423657/55069501-8348c400-5084-11e9-9931-fefe0f9874a7.png" width=200/>

# HEP-JAVA
### HEP/EEP Encapsulation Protocol Library for Java

This repository provides an example [HEP3](http://hep.sipcapture.org/) library and reference decoder implementation for Developers and Integrators.


#### About HEP/EEP
EEP/HEP: Extensible Encapsulation protocol provides a method to duplicate an IP datagram to a collector by encapsulating the original datagram and its relative header properties (as payload, in form of concatenated chunks) within a new IP datagram transmitted over UDP/TCP/SCTP connections for remote collection. Encapsulation allows for the original content to be transmitted without altering the original IP datagram and header contents and provides flexible allocation of additional chunks containing additional arbitrary data.

HEP/EEP is currently supported natively in platforms such as Kamailio/SER, OpenSIPS, FreeSWITCH, Asterisk and tools such as Captagent, sipgrep, sngrep and more.

## Build
<pre>
mvn package
</pre>

## Usage Example
<pre>
//...
import org.homer.hep.ParserHEPv3;
import org.homer.hep.HEPStructure;
//...

void consume(Socket socket) {
    // hep input stream      
    DataInputStream in = new DataInputStream(socket.getInputStream());

    // get header bytes
    byte[] header = new byte[4];
    byte[] expectedHeader = {0x48, 0x45, 0x50, 0x33};
    in.read(header);

    // check if header spells "HEP3"
    if (Arrays.equals(header, expectedHeader)){

        // get the payload length
        int len = in.readUnsignedShort();
        
        // get payload bytes
        byte[] bytes = new byte[len];
        in.read(bytes);

        // parse out HEP structure
        ParserHEPv3 parser = new ParserHEPv3();
        HEPStructure hepStructure = parser.parse(ByteBuffer.wrap(bytes), len, socket.getInetAddress().toString());
        // this prints out the HEP structure string representation
        System.out.println("HEP Structure: " + hepStructure);

        // grab payload from HEP structure
        String payload = new String(hepStructure.payloadByteMessage.array());
        //...
        //... do stuff with payload
        //...
    }
}

</pre>

### Support
For any question or comment, please visit http://sipcapture.org or contact support@sipcapture.org
