package com.mycompany.control;

import com.mycompany.vision.BaseSession;
import org.apache.wicket.MarkupContainer;

public class ServicePerfil {


    public String verificaPerfil(){
        String perfil =  BaseSession.get().getUser().getPerfil();
        System.out.println(perfil);
        return perfil;
    }
    public void hide(MarkupContainer m){
        m.setVisible(false);
    }


}
