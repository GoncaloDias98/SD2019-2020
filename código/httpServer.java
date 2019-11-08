import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.*;

public class httpServer extends Thread {
  static int DEFAULT_PORT=8081;


  public static void main(String[] args) {
		int port=DEFAULT_PORT;
		
		ServerSocket servidor = null; 
	
		try	{ 
			servidor = new ServerSocket(port);
		} catch (Exception e) { 
			System.err.println("erro ao criar socket servidor...");
			e.printStackTrace();
			System.exit(-1);
		}
			
		System.out.println("Servidor a espera de ligacoes na port " + port);
		ListaClientes listaClientes = new ListaClientes();
    ListaMensagens listaMensagens = new ListaMensagens();

		while(true) {
			try {
				Socket ligacaoCliente = servidor.accept();
								
        httpServerHandler t = new httpServerHandler(ligacaoCliente);
        t.setListaClientes(listaClientes);
        t.setListaMensagens(listaMensagens);
        t.start();
				t.start();
				
			} catch (IOException e) {
				System.out.println("Erro na execucao do servidor: "+e);
				System.exit(1);
			}
		}
	} 
}