import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.*;

public class httpServerHandler extends Thread {

  Socket clienteConectado = null;
  BufferedReader in = null;
  DataOutputStream out = null;
  ListaClientes listaClientes = null;
  ListaMensagens listaMensagens = null;
  ArrayList<String> lstCabecalho = null;

  static final String HTML_START = "<html>" + "<title>HTTP Server in java</title>" + "<body>";
  static final String HTML_END = "</body>" + "</html>";

  public httpServerHandler(Socket ligacaoCliente) {
    clienteConectado = ligacaoCliente;
 
  }

  public void setListaClientes(ListaClientes L){ //Vai buscar a lista de clientes registados
    this.listaClientes = L;
  }

  public void setListaMensagens(ListaMensagens M){ //Vai buscar a lista de clientes registados
    this.listaMensagens = M;
  }

  public void run() {

    try {

      lstCabecalho = new ArrayList<String>();

      System.out.println(
          "O cliente " + clienteConectado.getInetAddress() + ":" + clienteConectado.getPort() + " está conectado.");

      in = new BufferedReader(new InputStreamReader(clienteConectado.getInputStream()));
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
      System.out.println(">>>>Inicio Request");
      
      
      while (in.ready()) {
        // Read the HTTP complete HTTP Query
        bufferResposta.append(requestString + "<BR>");
        lstCabecalho.add(requestString + "\r\n"); //adiciona espaços à mensagem
        System.out.println(requestString);
        requestString = in.readLine();   //mensagem sem espaços
      }

      System.out.println(">>>>Fim Request");

       

      if (metodoHttp.equals("GET")) { //Quando recebe pedidos get

        String[] arrOfStr = httpQuery.split("/"); //divide os elementos do endereço a partir da /
        
        for (int i=0; i<arrOfStr.length; i++) 
          System.out.println ("i:" + i + " v:" + arrOfStr[i]); //mostra os elementos do endereço

        //HomePage
        if (arrOfStr[1].equals("")) { //Se o primeiro elemento for igual a /

          //http://127.0.0.1:8081/

          System.out.println ("Bem vindo à homepage " + arrOfStr[2] );  //arrOfStr[2] -> /nickname

          String Nickname = arrOfStr[2];

          }      


        

        //Consulta
        if (arrOfStr[1].equals("consulta")) {


          //http://127.0.0.1:8081/consulta
          

          System.out.println ("Vai consultar " );

          String Consulta = getConsulta();

          resposta(200, Consulta, false);

        }  
      } // Fim GET

          else if (metodoHttp.equals("POST")) {

            String[] arrOfStr = httpQuery.split("/"); //divide os elementos do endereço a partir da /



                 System.out.println(">>POST:" + httpQuery);
                 System.out.println(">>HEADER:" + cabecalho);
                 
                 //Submissao mensagem
                  if (arrOfStr[1].equals("mensagem")) {

                    // http://127.0.0.1:8081/mensagem/[id]/[mensagem]

                    String ID = arrOfStr[2];

                    String Mensagem = GetPostVariable("mensagem");
                    String nickname = listaClientes.devolveNickname(ID);
                    
                    if (nickname.equals(""))
                        resposta(404, "ID Invalido", false); 
                    else {
                     
                          System.out.println("Vai registar a mensagem:[" + Mensagem + "]");

                          listaMensagens.registarMensagem(Mensagem, nickname, ID);

                          String Consulta = getConsulta();

                          resposta(200, Consulta, false);


                    }
                    
                }

                  //Registo
                  if (arrOfStr[1].equals("registo")) { // Se o primeiro elemento for igual a /registo

                    // http://127.0.0.1:8081/registo/[Nickname]

                    String Nickname = GetPostVariable("nickname");
                    System.out.println("Vai registar o nickname " + Nickname); // arrOfStr[2] -> /nickname
                    
                    
                    if (Nickname.equals("")) // se não existe o nickname inserido
                      resposta(404, "Nickname vazio", false); // resposta -> "Nickname vazio"
                    else if (listaClientes.existeCliente(Nickname))
                      resposta(404, "Nickname existente", false); // resposta -> "Nickname já existe"
                    else {

                      String id = listaClientes.registarCliente(Nickname); // ---->>>>Classe "ListaClientes" -- Se é inserido um
                                                                          // nickname novo, adiciona-o à lista

                      System.out.println("Registo:" + id);

                      resposta(200, id, false); // resposta => atribui um id

                    }

                    listaClientes.PrintAll(); // imprime todos os elementos adicionados à lista

                  }  


            }
          else  resposta(404, "<b>Pagina nao encontrada xx...." + "Use: http://127.0.0.1:8081 ou localhost:8081/</b>", false);
    
        

    } catch (Exception e) {
      e.printStackTrace();
    }

  } //Run

  private String getConsulta() {

    String Resposta = "";

    //Clientes

    Resposta += "<p><strong> Clientes Registados</strong></p>";
    Resposta += "<table class=\"table\">";
    Resposta += "  <thead class=\"thead-light\">";
    Resposta += "   <tr>";
    Resposta += "    <th scope=\"col\">Nickname</th>";
    Resposta += "    <th scope=\"col\">ID</th>";
    Resposta += "   </tr>";
    Resposta += "  </thead>";
    Resposta += "  <tbody>";

    for (int i= 0; i < listaClientes.getListaClientes().size(); i++) {
      Cliente c = (Cliente)listaClientes.getListaClientes().get(i);
      Resposta += "   <tr>";
			Resposta += "  	  <td>" + c.getNickname() + "</td>";
			Resposta += "  	  <td>" + c.getId() + "</td>";
			Resposta += "  	</tr>";
   
    }   
  
    Resposta += "   </tbody>";
    Resposta += "</table>";
    
    // Fim Clientes

    // Mensagens 
    Resposta += "<p><strong> Mensagens</strong></p>";

    for (int i= 0; i < listaMensagens.getListaMensagens().size(); i++) {
      Mensagem m = (Mensagem)listaMensagens.getListaMensagens().get(i);
      Resposta += "<div class=\"card\" >";
      Resposta += "	  <div class=\"card-body\">";
      Resposta += "	    <h6 class=\"card-subtitle mb-2 text-muted\">" + m.getNickname() + "</h6>";
      Resposta += "		  <p class=\"card-text\">" + m.getMensagem() + "</p>";
			
      Resposta += "	    </div>";
      Resposta += "</div>";
     	
  }   

    return Resposta;

  }

  private String GetPostVariable (String Variavel){

    int p = -1;


    for (int i=0; i<lstCabecalho.size();i++){

      if (lstCabecalho.get(i).contains("Content-Disposition:") && lstCabecalho.get(i).contains("\"" + Variavel + "\"")){
        p = i;
      }

    }

    if (p==-1) return "";
    p++;
    String r = "";

    while (p<lstCabecalho.size() && !lstCabecalho.get(p).contains("----------")){
      r = r + lstCabecalho.get(p);
      p++;
    }

    return r;

  }

  public void resposta(int statusCode, String responseString, boolean isFile) throws Exception {

    String statusLine = null;
    String detalhesServidor = "Server: Java HTTPServer"+ "\r\n";
    String cors = "Access-Control-Allow-Origin: *" + "\r\n";
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
    } else {
      responseString = responseString + "";
      contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
    }

    out.writeBytes(statusLine);
    out.writeBytes(detalhesServidor);
    out.writeBytes(cors);
    
    out.writeBytes(contentTypeLine);
    out.writeBytes(contentLengthLine);
    out.writeBytes("Connection: close\r\n");
    out.writeBytes("\r\n");

    if (isFile)
      sendFile(fin, out);
    else
      out.writeBytes(responseString);

    out.close();
  }

  public void sendFile(FileInputStream fin, DataOutputStream out) throws Exception {
    byte[] buffer = new byte[1024];
    int bytesRead;

    while ((bytesRead = fin.read(buffer)) != -1) {
      out.write(buffer, 0, bytesRead);
    }
    fin.close();
  }

}