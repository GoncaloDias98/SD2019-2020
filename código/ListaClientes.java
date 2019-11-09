import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.*;

public class ListaClientes {
    private ArrayList<Cliente> listaClientes;

    public ListaClientes() {
        System.out.println("======== instanciacao ListaClientes =========");
        listaClientes = new ArrayList<>();
    }

    public ArrayList getListaClientes() {
        return listaClientes;
    }

    public boolean existeCliente(String Nickname) { //verifica se o nickname registado já existe

        for (int i= 0; i < listaClientes.size(); i++) {
            Cliente c = listaClientes.get(i);
            if (c.getNickname().equals(Nickname))
                return true; 		
        }   	
        return false;	
  
    }

    public void PrintAll() {                            //imprime os todos os nicknames inseridos e os ids atribuidos
        for (int i= 0; i < listaClientes.size(); i++) {
            Cliente c = listaClientes.get(i);
            System.out.println("i:" + i + " cliente:" + c.getNickname() + " id:" + c.getId());	
        }   
    }
     	



    public String registarCliente(String  Nickname) {
        String uniqueID = UUID.randomUUID().toString(); //Atribui um id (UniqueID)
            
        System.out.println("registarCliente:" + Nickname); 

        if (Nickname.equals("")) return "";        

        if (!Nickname.equals("")) { 
            Cliente novoCliente = new Cliente(uniqueID, Nickname);
            listaClientes.add(novoCliente); //Se é inserido um nickname, adiciona-o à listaClientes

            System.out.println("registarCliente2:" + novoCliente.getId()); //imprime o id atribuido 

            return novoCliente.getId();
        } else return ""; 
    }

    public String devolveNickname (String id) { //verifica se o nickname registado já existe

        for (int i= 0; i < listaClientes.size(); i++) {
            Cliente c = listaClientes.get(i);
            if (c.getId().equals(id))
                return c.getNickname(); 		
        }   	
        return "";  
    }
}
