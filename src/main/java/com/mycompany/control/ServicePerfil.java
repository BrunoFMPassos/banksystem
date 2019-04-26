package com.mycompany.control;

import com.mycompany.vision.BaseSession;
import org.apache.wicket.MarkupContainer;

public class ServicePerfil {

    public String verificaPerfil() {
        String perfil = BaseSession.get().getUser().getPerfil();
        return perfil;
    }

    public  String verificaUser(){
        String user = BaseSession.get().getUser().getUsername();
        return user;
    }

    public void hide(MarkupContainer markupContainer) {
        markupContainer.setVisible(false);
    }


}
