package client;

import connection.TCPConnection;
import connection.TCPConnectionListener;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class Client implements TCPConnectionListener {
    private final static String IP_ADDRESS = "127.0.0.1";
    private final static int PORT = 8189;

    private TCPConnection connection;

    private Integer sessionId = 0; // 0 means that we didn't login yet
    private ClientChatListener clientChatListener;
    private ClientLoginListener clientLoginListener;


    public Client(ClientLoginListener clientListener) {
        this.clientLoginListener = clientListener;
        try {
            this.connection = new TCPConnection(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
            printMassage(e.getMessage());
        }

    }

    public void setClientChatListener(ClientChatListener clientChatListener) {
        this.clientChatListener = clientChatListener;
    }

    public void sendMessage(String message){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            logout();
            return;
        }
        Document document = documentBuilder.newDocument();

        Element event = document.createElement("command");
        event.setAttribute("name", "message");
        document.appendChild(event);

        Element messageElement = document.createElement("message");
        messageElement.setTextContent(message);
        event.appendChild(messageElement);

        Element session = document.createElement("session");
        session.setTextContent(sessionId.toString());
        event.appendChild(session);

        connection.sendDocument(document);
    }

    public void sendRegistrationRequest(String userName){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            logout();
            return;
        }
        Document document = documentBuilder.newDocument();

        Element command = document.createElement("command");
        document.appendChild(command);
        command.setAttribute("name", "login");

        Element name = document.createElement("name");
        name.setTextContent(userName);
        command.appendChild(name);

        Element type = document.createElement("type");
        type.setTextContent("DIMAS_CLIENT");
        command.appendChild(type);

        connection.sendDocument(document);
    }
    public void startToChat(){
        sendUsersListRequest();
    }

    private void sendUsersListRequest(){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            logout();
            return;
        }
        Document document = documentBuilder.newDocument();

        Element command = document.createElement("command");
        document.appendChild(command);
        command.setAttribute("name", "list");

        Element session = document.createElement("session");
        session.setTextContent(sessionId.toString());
        command.appendChild(session);

        connection.sendDocument(document);

    }

    private void handleError(@NotNull NodeList parameters){
        for (int i = 0; i < parameters.getLength(); i++){
            Node node = parameters.item(i);

            if (node.getNodeName().equals("message")){
                clientLoginListener.alert(node.getTextContent());
            }
        }
    }

    private void handleUsersListResponse(@NotNull NodeList users){
        ArrayList<String> names = new ArrayList<>();
        //parse users
        for (int i = 0; i < users.getLength(); i++){
            Node node = users.item(i);
            if (node.getNodeName().equals("user")){
                NodeList parameters = node.getChildNodes();
                //find users name
                for (int j = 0; j < parameters.getLength(); j++){
                    if (parameters.item(j).getNodeName().equals("name")){
                        names.add(parameters.item(j).getTextContent());
                    }
                }
            }
        }

        clientChatListener.updateUserList(names);
    }

    private void handleSuccess(TCPConnection connection, @NotNull NodeList parameters){
        if (parameters.getLength() == 0){
            //we got a logout response
            connection.disconnect();
        }
        if (parameters.getLength() != 0){
            for (int i = 0; i < parameters.getLength(); i++){
                Node node = parameters.item(i);
                switch (node.getNodeName()){
                    case "session":
                        //we are successfully signed up
                        sessionId = Integer.parseInt(node.getTextContent());

                        clientLoginListener.loginIsSuccessful();
                        break;
                    case "listusers":
                        //we got a users list
                        handleUsersListResponse(node.getChildNodes());
                        break;
                }
            }
        }
    }

    private void handleMessage(@NotNull NodeList parameters){
        String message = "";
        String name = "";
        for (int i = 0; i < parameters.getLength(); i++){
            Node node = parameters.item(i);

            if (node.getNodeName().equals("message")){
                message = node.getTextContent();
            }
            else if (node.getNodeName().equals("name")){
                name = node.getTextContent();
            }
        }

        if (message.isEmpty() || name.isEmpty()){
            printMassage("bad xml");
        }
        else{
            clientChatListener.receiveMessage(name, message);
        }
    }

    private void handleLoginEvent(@NotNull NodeList parameters){
        for (int i=0; i< parameters.getLength();i++){
            Node node = parameters.item(i);

            if (node.getNodeName().equals("name")){
                clientChatListener.userConnected(node.getTextContent());
                return;
            }
        }
    }

    private void handleLogoutEvent(@NotNull NodeList parameters){
        for (int i=0; i< parameters.getLength();i++){
            Node node = parameters.item(i);

            if (node.getNodeName().equals("name")){
                clientChatListener.userDisconnected(node.getTextContent());
                return;
            }
        }

    }

    private void handleEvent(@NotNull Node event){
        String name = event.getAttributes().item(0).getNodeValue();

        switch (name){
            case "message":
                handleMessage(event.getChildNodes());
                break;
            case "userlogin":
                handleLoginEvent(event.getChildNodes());
                break;
            case "userlogout":
                handleLogoutEvent(event.getChildNodes());
                break;

        }
    }

    public void logout(){
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
            return;
        }
        Document document = documentBuilder.newDocument();

        Element command = document.createElement("command");
        command.setAttribute("name", "logout");
        document.appendChild(command);

        Element session = document.createElement("session");
        session.setTextContent(sessionId.toString());
        command.appendChild(session);

        connection.sendDocument(document);

    }


    @Override
    public void receiveDocument(TCPConnection connection, @NotNull Document document) {
        //message from server is ether SUCCESS or ERROR
        Node root = document.getDocumentElement();

        switch(root.getNodeName()){
            case "error":
                handleError(root.getChildNodes());
                break;
            case "success":
                handleSuccess(connection, root.getChildNodes());
                break;
            case "event":
                handleEvent(root);
                break;
            default:
                printMassage("bad xml");
                break;
        }

        synchronized (this){
            notifyAll();
        }
    }
    @Override
    public void connectionReady(@NotNull TCPConnection connection) {
        printMassage(connection.toString() + ": connection ready");
    }

    @Override
    public void disconnect(@NotNull TCPConnection connection) {
        printMassage(connection.toString() + ": connection closed");
    }

    @Override
    public void exception(TCPConnection connection, Exception e) {
        logout();

    }

    private synchronized void printMassage(String massage){
        System.out.println(massage);
    }
}
