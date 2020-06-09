package server;

import connection.TCPConnection;
import org.w3c.dom.*;
import connection.TCPConnectionListener;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server implements TCPConnectionListener {
    private final static int PORT = 8189;

    private final Map<Integer, String> namesMap = new TreeMap<>(); // <Name, ID>
    private final Map<Integer, TCPConnection> connectionMap = new TreeMap<>(); // <ID, Connection>

    private int idCounter = 0; //unique id for every new client

    private final Logger logger = Logger.getLogger("log");

    public static void main(String[] args) throws Exception {
        new Server();
    }

    private Server() throws Exception {
        try(ServerSocket serverSocket = new ServerSocket(PORT))
        {
            //setup logger
            logger.setLevel(Level.INFO);
            FileHandler fileHandler = new FileHandler("server/src/log/server.log");

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.addHandler(fileHandler);
            //start accepting clients
            logger.log(Level.INFO, "Server started");
            while (true){
                //accept connection and run new connection.TCPConnection to listen for messages
                new TCPConnection(serverSocket.accept(), this);
            }
        }
        catch (IOException e)
        {
            logger.log(Level.WARNING, "Server can't start");
            throw new Exception(e);
        }
    }

    private synchronized void sendError(TCPConnection connection, String reason) {
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.log(Level.WARNING, e.getMessage());
            return;
        }
        Document document = documentBuilder.newDocument();

        Element error = document.createElement("error");
        document.appendChild(error);

        Element message = document.createElement("message");
        message.setTextContent(reason);
        error.appendChild(message);

        connection.sendDocument(document);
        logger.log(Level.INFO, connection.toString() + ": ERROR: " + reason);
    }

    private synchronized void sendUserConnectionInfo(Integer sessionId){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.log(Level.WARNING, e.getMessage());
            return;
        }
        Document document = documentBuilder.newDocument();

        Element event = document.createElement("event");
        event.setAttribute("name", "userlogin");
        document.appendChild(event);

        Element name = document.createElement("name");
        name.setTextContent(namesMap.get(sessionId));
        event.appendChild(name);

        for (Map.Entry<Integer, TCPConnection> entry : connectionMap.entrySet()) {
            if (!entry.getValue().equals(connectionMap.get(sessionId))){
                entry.getValue().sendDocument(document);
                logger.log(Level.INFO, "ID: " + entry.getKey() +" USERNAME: " + namesMap.get(entry.getKey()) + ": user connection info");
            }
        }
    }

    private synchronized void sendLoginResponse(Integer sessionId){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            return;
        }
        Document document = documentBuilder.newDocument();

        Element success = document.createElement("success");
        document.appendChild(success);

        Element session = document.createElement("session");
        session.setTextContent(sessionId.toString());
        success.appendChild(session);

        connectionMap.get(sessionId).sendDocument(document);
        logger.log(Level.INFO, "ID: " + session +" USERNAME: " + namesMap.get(sessionId) + ": user connection info");
    }

    private synchronized void handleLoginRequest(TCPConnection connection, NodeList parameters){
        for (int i = 0; i < parameters.getLength(); i++){
            Node node = parameters.item(i);
            if (node.getNodeName().equals("name")){
                String userName = node.getTextContent();
                if (namesMap.containsValue(userName)){
                    sendError(connection, "User with this name already exists");
                }
                else{
                    namesMap.put(idCounter, userName);
                    connectionMap.put(idCounter, connection);

                    sendLoginResponse(idCounter);
                    sendUserConnectionInfo(idCounter);

                    idCounter++;

                    return;
                }
            }
        }
    }

    private synchronized void sendUserDisconnectInfo(Integer sessionId){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            return;
        }
        Document document = documentBuilder.newDocument();

        Element event = document.createElement("event");
        event.setAttribute("name", "userlogout");
        document.appendChild(event);

        Element name = document.createElement("name");
        name.setTextContent(namesMap.get(sessionId));
        event.appendChild(name);

        for (Map.Entry<Integer, TCPConnection> entry : connectionMap.entrySet()) {
            if (!entry.getValue().equals(connectionMap.get(sessionId))){
                entry.getValue().sendDocument(document);
                logger.log(Level.INFO, "ID: " + entry.getKey() +" USERNAME: " + namesMap.get(entry.getKey()) + ": user disconnect info");
            }
        }
    }

    private synchronized void sendLogoutResponse(Integer clientSessionId){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            return;
        }
        Document document = documentBuilder.newDocument();

        Element success = document.createElement("success");
        success.setTextContent("");
        document.appendChild(success);

        connectionMap.get(clientSessionId).sendDocument(document);
        logger.log(Level.INFO, "ID: " + clientSessionId +" USERNAME: " + namesMap.get(clientSessionId) + ": sent logout response");
    }

    private synchronized void handleLogoutRequest(TCPConnection connection, NodeList parameters){
        for (int i = 0; i < parameters.getLength(); i++){
            Node node = parameters.item(i);

            if (node.getNodeName().equals("session")){
                Integer sessionId = Integer.parseInt(node.getTextContent());
                if (!connectionMap.get(sessionId).equals(connection)){
                    sendError(connection, "Bad SESSION_ID");
                }
                else{
                    sendLogoutResponse(sessionId);
                    sendUserDisconnectInfo(sessionId);

                    connectionMap.get(sessionId).disconnect();
                }
            }
        }
    }

    private synchronized void sendListResponse(int clientSessionId){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            return;
        }
        Document document = documentBuilder.newDocument();

        Element success = document.createElement("success");
        document.appendChild(success);

        Element listusers = document.createElement("listusers");
        success.appendChild(listusers);

        for (Map.Entry<Integer, String> entry : namesMap.entrySet()) {
            Element user = document.createElement("user");

            Element name = document.createElement("name");
            name.setTextContent(entry.getValue());
            user.appendChild(name);

            listusers.appendChild(user);
        }


        connectionMap.get(clientSessionId).sendDocument(document);
        logger.log(Level.INFO, "ID: " + clientSessionId +" USERNAME: " + namesMap.get(clientSessionId) + ": sent users list response");
    }

    private synchronized void handleListRequest(TCPConnection connection, NodeList parameters){
        for (int i = 0; i < parameters.getLength(); i++){
            Node node = parameters.item(i);

            if (node.getNodeName().equals("session")){
                int sessionId = Integer.parseInt(node.getTextContent());

                if (connectionMap.get(sessionId).equals(connection)){
                    logger.log(Level.INFO, "ID: " + sessionId +" USERNAME: " + namesMap.get(sessionId) + ": got users list request");
                    sendListResponse(sessionId);
                }
                else{
                    sendError(connection,"Bad SESSION_ID");
                }
            }
        }
    }

    private synchronized void sendMessageResponse(Integer clientSessionId, String message){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            return;
        }
        Document document = documentBuilder.newDocument();

        Element event = document.createElement("event");
        event.setAttribute("name", "message");
        document.appendChild(event);

        Element messageElement = document.createElement("message");
        messageElement.setTextContent(message);
        event.appendChild(messageElement);

        Element name = document.createElement("name");
        name.setTextContent(namesMap.get(clientSessionId));
        event.appendChild(name);

        for (Map.Entry<Integer, TCPConnection> entry : connectionMap.entrySet()) {
            entry.getValue().sendDocument(document);
            logger.log(Level.INFO, "ID: " + entry.getKey() +" USERNAME: " + namesMap.get(entry.getKey()) + ": sent message response");
        }
    }

    private synchronized void handleMessage(TCPConnection connection, NodeList parameters){
        String message = "";
        int sessionId = -1;
        for (int i = 0; i < parameters.getLength(); i++) {
            Node node = parameters.item(i);
            if (node.getNodeName().equals("message")){
                message = node.getTextContent();
            }
            else if (node.getNodeName().equals("session")){
                sessionId = Integer.parseInt(node.getTextContent());
            }
        }
        if (sessionId == -1){
            sendError(connection, "No SESSION_ID");
        }
        else if (!connectionMap.get(sessionId).equals(connection)){
            sendError(connection, "Bad SESSION_ID");
        }
        else if (message.equals("")){
            sendError(connection, "Empty massage");
        }
        else{
            logger.log(Level.INFO, "ID: " +sessionId +" USERNAME: " + namesMap.get(sessionId) + ": got message");
            sendMessageResponse(sessionId, message);
        }
    }

    @Override
    public synchronized void receiveDocument(TCPConnection connection, Document document){
        //parse xml
        logger.log(Level.INFO, "Got XML from " + connection.toString());
        Node root = document.getDocumentElement();
        if (!root.getNodeName().equals("command")){
            logger.log(Level.INFO, "Got bad XML from " + connection.toString());
        }
        else {
            Node name = root.getAttributes().getNamedItem("name");
            if (name == null){
                logger.log(Level.INFO, "Got bad XML from " + connection.toString());
            }
            else {
                switch (name.getNodeValue()){
                    case "login":
                        handleLoginRequest(connection, root.getChildNodes());
                        break;
                    case "logout":
                        handleLogoutRequest(connection, root.getChildNodes());
                        break;
                    case "list":
                        handleListRequest(connection, root.getChildNodes());
                        break;
                    case "message":
                        handleMessage(connection, root.getChildNodes());
                        break;
                    default:
                        logger.log(Level.INFO, "Got bad XML from " + connection.toString());
                        break;
                }
            }
        }
    }

    @Override
    public synchronized void connectionReady(TCPConnection connection) {
        logger.log(Level.INFO, connection.toString() + ": connection is ready");
    }

    @Override
    public synchronized void disconnect(TCPConnection connection) {
        for (Map.Entry<Integer, TCPConnection> entry : connectionMap.entrySet()) {
            if (entry.getValue().equals(connection)){
                namesMap.remove(entry.getKey());
            }
        }

        logger.log(Level.INFO, connection.toString() + ": disconnected");
    }

    @Override
    public synchronized void exception(TCPConnection connection, Exception e) {
        logger.log(Level.WARNING, connection.toString() + ": exception: " + e.getMessage());
        connection.disconnect();
    }

}
