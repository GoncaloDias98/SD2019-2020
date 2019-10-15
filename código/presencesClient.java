import java.util.*;
import java.net.*;
import java.io.*;

public class presencesClient {
	
	static final int DEFAULT_PORT=2000;
	static final String DEFAULT_HOST="127.0.0.1"; 

	public static void main(String[] args) {
		String servidor=DEFAULT_HOST;
		int porto=DEFAULT_PORT;
		
		if (args.length != 1) {
				System.out.println("Erro: use java presencesClient <ip>");
				System.exit(-1);
		}
	
		//Cria um objecto da classe InetAddress com base no nome do servidor
		InetAddress endereco = null;
		try {
			endereco = InetAddress.getByName(servidor);
		} catch (UnknownHostException e) {
			System.out.println("Endereco desconhecido: "+e);
			System.exit(-1);
		}

		System.out.println("Vai ligar ao porto "+porto+" da maquina "+endereco.getHostName());

		Socket ligacao = null; 
		
		try {
			ligacao = new Socket(endereco, porto);
		} catch (Exception e) {
			System.err.println("erro ao criar socket...");
			e.printStackTrace();
			System.exit(-1);
		}

		System.out.println("ligado ao porto " + porto + " no endereco " + endereco.getHostName());

		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(ligacao.getInputStream()));
 
			PrintWriter out = new PrintWriter(ligacao.getOutputStream());
			
			String request = "get" + " " + args[0];
			
			out.println(request);
			out.flush();
			
			String msg;
			while ((msg = in.readLine())!= null)
				System.out.println("Resposta do servidor: " + msg);
			
			ligacao.close();
			System.out.println("Terminou a ligacao!");
		} catch (IOException e) {
			System.out.println("Erro ao comunicar com o servidor: "+e);
			System.exit(1);
		}
	
	}
}


