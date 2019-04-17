package com.mycompany.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Colaborador")
public class Colaborador extends Pessoa implements Serializable {

    @OneToMany(mappedBy = "colaborador", targetEntity = User.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users;

    @Column(nullable = false, length = 50)
    private String perfil;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 10)
    private String password;


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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
