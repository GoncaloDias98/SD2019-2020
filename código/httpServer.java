/*
* myHTTPServer.java
* Author: S.Prasanna
* @version 1.00
*/

import java.io.*;
import java.net.*;
import java.util.*;

public class httpServer extends Thread {

Socket clienteConectado = null;
BufferedReader in = null;
DataOutputStream out = null;

static final String HTML_START =
"<html>" +
"<title>HTTP Server in java</title>" +
"<body>";

static final String HTML_END =
"</body>" +
"</html>";



public httpServer(Socket cliente) {
clienteConectado = cliente;
}

public void run() {

try {

System.out.println( "O cliente "+
  clienteConectado.getInetAddress() + ":" + clienteConectado.getPort() + " está conectado.");

  in = new BufferedReader(new InputStreamReader (clienteConectado.getInputStream()));
  out = new DataOutputStream(clienteConectado.getOutputStream());

String requestString = in.readLine();
  String cabecalho = requestString;

  StringTokenizer tokenizer = new StringTokenizer(cabecalho);
String metodoHttp = tokenizer.nextToken();
String httpQuery = tokenizer.nextToken();

StringBuffer bufferResposta = new StringBuffer();
bufferResposta.append("<b>  Sistema de Resposta de Audiências </b><BR>");
  bufferResposta.append("O pedido do cliente....<BR>");

  System.out.println("A String do pedido HTTP do cliente é....");
  while (in.ready())
  {
    // Read the HTTP complete HTTP Query
    bufferResposta.append(requestString + "<BR>");
System.out.println(requestString);
requestString = in.readLine();
}

if (metodoHttp.equals("GET")) {
if (httpQuery.equals("/")) {
 // The default home page
resposta(200, bufferResposta.toString(), false);
} else {
//This is interpreted as a file name
String fileName = httpQuery.replaceFirst("/", "");
fileName = URLDecoder.decode(fileName);
if (new File(fileName).isFile()){
resposta(200, fileName, true);
}
else {
resposta(404, "<b>Página nao encontrada ...." +
"Use: http://127.0.0.1:8081 ou localhost:8081/</b>", false);
}
}
}
else resposta(404, "<b>Pagina nao encontrada ...." +
"Use: http://127.0.0.1:8081 ou localhost:8081/</b>", false);
} catch (Exception e) {
e.printStackTrace();
}
}

public void resposta (int statusCode, String responseString, boolean isFile) throws Exception {

String statusLine = null;
String detalhesServidor = "Server: Java HTTPServer";
String contentLengthLine = null;
String fileName = null;
String contentTypeLine = "Content-Type: text/html" + "\r\n";
FileInputStream fin = null;

if (statusCode == 200)
statusLine = "HTTP/1.1 200 OK" + "\r\n";
else
statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

if (isFile) {
fileName = responseString;
fin = new FileInputStream(fileName);
contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + "\r\n";
if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))
contentTypeLine = "Content-Type: \r\n";
}
else {
responseString = httpServer.HTML_START + responseString + httpServer.HTML_END;
contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
}

out.writeBytes(statusLine);
out.writeBytes(detalhesServidor);
out.writeBytes(contentTypeLine);
out.writeBytes(contentLengthLine);
out.writeBytes("Connection: close\r\n");
out.writeBytes("\r\n");

if (isFile) sendFile(fin, out);
else out.writeBytes(responseString);

out.close();
}

public void sendFile (FileInputStream fin, DataOutputStream out) throws Exception {
byte[] buffer = new byte[1024] ;
int bytesRead;

while ((bytesRead = fin.read(buffer)) != -1 ) {
out.write(buffer, 0, bytesRead);
}
fin.close();
}

public static void main (String args[]) throws Exception {

ServerSocket Server = new ServerSocket (8081, 10, InetAddress.getByName("127.0.0.1"));
System.out.println ("Servidor TCP à espera na porta 8081");

while(true) {
Socket connected = Server.accept();
    (new httpServer(connected)).start();
}
}
}