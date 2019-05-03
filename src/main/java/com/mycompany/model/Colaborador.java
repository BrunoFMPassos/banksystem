package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Colaborador")
public class Colaborador extends Pessoa implements Serializable {

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="agencia_id")
    private Agencia agencia;

    @Transient
    private String perfil;

    @Transient
    private String username;

    @Transient
    private String password;

    @Transient
    private String agenciapesquisa;



    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia_colaborador) {
        this.agencia = agencia_colaborador;
    }

    public String getAgenciapesquisa() {
        return agenciapesquisa;
    }

    public void setAgenciapesquisa(String agenciapesquisa) {
        this.agenciapesquisa = agenciapesquisa;
    }
}
