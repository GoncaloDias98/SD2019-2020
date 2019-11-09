import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.*;

public class ListaMensagens {
    private ArrayList<Mensagem> listaMensagens;

    public ListaMensagens() {
        System.out.println("======== instanciacao ListaMensagens =========");
        listaMensagens = new ArrayList<>();
    }

    public ArrayList getListaMensagens() {
        return listaMensagens;
    }

    public void PrintAll() {                            //imprime os todos os nicknames inseridos e os ids atribuidos
        for (int i= 0; i < listaMensagens.size(); i++) {
            Mensagem m = listaMensagens.get(i);
            System.out.println("i:" + i + " mensagem:" + m.getMensagem() + " id:" + m.getId());	
        }   
    }

    public String registarMensagem(String  mensagem, String nickname, String id) {
                   
        System.out.println("registarMensagem:" + mensagem); 

            Mensagem novaMensagem = new Mensagem(id, mensagem, nickname);
            listaMensagens.add(novaMensagem); //Se é inserido um nickname, adiciona-o à listaClientes

             return "";
        }
}