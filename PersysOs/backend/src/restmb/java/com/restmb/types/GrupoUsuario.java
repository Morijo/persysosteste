package com.restmb.types;


import com.restmb.RestMB;

public class GrupoUsuario extends RestMbType<GrupoUsuario> implements br.com.model.interfaces.IGrupoUsuario {

	
	@RestMB
	private String nome = "";
	
	@RestMB
	private Boolean administrador = false;
	
	
	public GrupoUsuario(String resourcePath) {
		super(resourcePath, GrupoUsuario.class);
	}
	
	@Override
	public Boolean getAdministrado() {
		return administrador;
	}

	@Override
	public void setAdministrado(Boolean administrador) {
		this.administrador = administrador;
	}
	
	public GrupoUsuario() {
		super("", GrupoUsuario.class);
	}
	public GrupoUsuario(Long id) {
		super("", GrupoUsuario.class);
		setId(id);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public int hashCode() {
		return getId().intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrupoUsuario other = (GrupoUsuario) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}
}
