package com.mycompany.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name="User")
public class User implements Serializable{

	private static final long serialVersionUID = 7966191167704263868L;

	@OneToMany(mappedBy = "user", targetEntity = Colaborador.class,
			fetch = FetchType.LAZY)
	private List<Colaborador> colaboradores;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(nullable=false, length=200)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String perfil;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public List<Colaborador> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}
}
