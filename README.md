# HEP-JAVA
### HEP/EEP Encapsulation Protocol Library for Java

#### About HEP/EEP
EEP/HEP: Extensible Encapsulation protocol (“EEP” pronounced “HEPPY”) provides a method to duplicate an IP datagram to a collector by encapsulating the original datagram and its relative header properties (as payload, in form of concatenated chunks) within a new IP datagram transmitted over UDP/TCP/SCTP connections for remote collection. Encapsulation allows for the original content to be transmitted without altering the original IP datagram and header contents and provides flexible allocation of additional chunks containing additional arbitrary data.

HEP/EEP is currently supported natively in platforms such as Kamailio/SER, OpenSIPS, FreeSWITCH, Asterisk and tools such as Captagent, sipgrep, sngrep and more.

## Build
<pre>
mvn package
</pre>

This repository provides an example library and reference implementations for Developers and Integrators.

### Support
For any question or comment, please visit http://sipcapture.org or contact support@sipcapture.org
