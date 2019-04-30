package com.mycompany.control;

import java.util.ArrayList;
import java.util.List;

public class Mensagem {
    private List<String> listaDeMensagens;
    private Boolean listaDeMensagemVazia = true;

    public void adcionarMensagemNaLista(String mensagem){
        if(listaDeMensagens == null){
            listaDeMensagens = new ArrayList<String>();
        }
        listaDeMensagens.add(mensagem);
        listaDeMensagemVazia = false;
    }


    public List<String> getListaDeMensagens() {
        return listaDeMensagens;
    }

    public void setListaDeMensagens(List<String> listaDeMensagens) {
        this.listaDeMensagens = listaDeMensagens;
    }

    public Boolean getListaVazia() {
        return listaDeMensagemVazia;
    }

    public void setListaVazia(Boolean listaVazia) {
        this.listaDeMensagemVazia = listaVazia;
    }
}
