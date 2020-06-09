package connection;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection extends Thread{
    private final static boolean SERIALIZE = true;

    private Socket socket;

    private BufferedReader in;
    private BufferedWriter out;

    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;

    private TCPConnectionListener tcpConnectionListener;

    public TCPConnection(TCPConnectionListener tcpConnectionListener, String ipAddress, int port) throws IOException {
            this(new Socket(ipAddress, port), tcpConnectionListener);
    }

    public TCPConnection(Socket socket, TCPConnectionListener tcpConnectionListener) throws IOException{

        this.socket = socket;
        this.tcpConnectionListener = tcpConnectionListener;

        if (SERIALIZE){
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objOut.flush();
            objIn = new ObjectInputStream(socket.getInputStream());
        }
        else {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        }

        tcpConnectionListener.connectionReady(this);
        new Thread(this).start();
    }

    private synchronized Document receiveDocument(){
        Document document = null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String XMLString;

            if (SERIALIZE){
                XMLString = (String)objIn.readObject();
            }
            else{
                XMLString = in.readLine();
            }

            //System.out.println(xml)
            if (XMLString != null) {
                document = documentBuilder.parse(new InputSource(new StringReader(XMLString)));
            }
        } catch (SAXException | IOException | ParserConfigurationException | ClassNotFoundException e) {
            tcpConnectionListener.exception(this, e);
        }
        return document;
    }

    @Override
    public void run() {
        //read messages
        while(!currentThread().isInterrupted()){
            try {
                Document document = receiveDocument();
                if (document != null)
                    tcpConnectionListener.receiveDocument(this, document);
            } catch (ParserConfigurationException e) {
                tcpConnectionListener.exception(this, e);
            }
        }
    }

    public void sendDocument(Document document){
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            if (SERIALIZE){
                StringWriter stringWriter = new StringWriter();
                StreamResult stringStreamResult = new StreamResult(stringWriter);

                transformer.transform(source, stringStreamResult);

                String XMLString = stringWriter.getBuffer().toString().concat("\n");
                objOut.writeObject(XMLString);
            }
            else{
                StreamResult outStreamResult = new StreamResult(out);

                transformer.transform(source, outStreamResult);

                out.write("\n");
                out.flush();
            }
        } catch (TransformerException | IOException e) {
            tcpConnectionListener.exception(this, e);
        }
    }

    public synchronized void disconnect(){
        try {
            currentThread().interrupt();
            if (!socket.isClosed())
                socket.close();
            tcpConnectionListener.disconnect(this);
        } catch (IOException e) {
            tcpConnectionListener.exception(this, e);
        }
    }

    @Override
    public String toString(){
        return "connection.TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}