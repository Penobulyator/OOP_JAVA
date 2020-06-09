package connection;

import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;

public interface TCPConnectionListener {
    void connectionReady(TCPConnection connection);

    void receiveDocument(TCPConnection connection, Document document);

    void disconnect(TCPConnection connection);

    void exception(TCPConnection connection, Exception e);
}
