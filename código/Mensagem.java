public class Mensagem {
    private String id;
    private String mensagem;
    private String nickname;
    

    public Mensagem (String id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    public String getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String Nickname) {
        this.nickname = Nickname;
    }



}