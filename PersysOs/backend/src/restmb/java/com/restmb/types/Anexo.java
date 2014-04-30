package com.restmb.types;

import br.com.model.interfaces.IAnexo;
import br.com.model.interfaces.IOrdem;
import br.com.principal.helper.UrlHelper;

import com.restmb.RestMB;

public class Anexo extends RestMbType<Anexo> implements IAnexo {
		
	@RestMB
	private String descricao = null; 
	
	@RestMB
	private String caminho = null; 
	
	@RestMB
	private int tamanho; 
	
	@RestMB
    private OrdemServico ordem;

	public Anexo() {
	 super("/anexo", Anexo.class);
	}

	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the caminho
	 */
	public String getCaminho() {
		return UrlHelper.END_POINT_SERVICE+"/ordem/anexo/"+getId()+"/imagem";
	}
	

	@Override
	public void setCaminho(String caminho) {
		// TODO Auto-generated method stub
	}


	/**
	 * @return the tamanho
	 */
	public int getTamanho() {
		return tamanho;
	}

	/**
	 * @param tamanho the tamanho to set
	 */
	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	/**
	 * @return the ordem
	 */
	public OrdemServico getOrdem() {
		return ordem;
	}
	

	@Override
	public void setOrdem(IOrdem ordem) {
		this.ordem = (OrdemServico) ordem;
	}
	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caminho == null) ? 0 : caminho.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((ordem == null) ? 0 : ordem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Anexo other = (Anexo) obj;
		if (caminho == null) {
			if (other.caminho != null)
				return false;
		} else if (!caminho.equals(other.caminho))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (ordem == null) {
			if (other.ordem != null)
				return false;
		} else if (!ordem.equals(other.ordem))
			return false;
		return true;
	}	
}
