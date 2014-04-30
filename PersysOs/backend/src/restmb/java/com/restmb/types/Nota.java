package com.restmb.types;

import br.com.model.interfaces.INota;
import br.com.model.interfaces.IOrdem;

import com.restmb.RestMB;

public class Nota extends RestMbType<Nota> implements INota{

	@RestMB
	private String titulo = ""; 
	
	@RestMB
	private String nota = ""; 
	
	public Nota(){
		super("/ordem/nota", Nota.class);
	}
	
	public Nota(Long idOrdem){
		super("/ordem/"+idOrdem+"/nota", Nota.class);
	}
	

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	@Override
	public IOrdem getOrdem() {
		return null;
	}

	@Override
	public void setOrdem(IOrdem ordem) {
	}
	
	
	
}
