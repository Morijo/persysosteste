package br.com.model.interfaces;

import java.util.Date;

public interface ICliente extends IUsuario {

	public String getSituacao();
	public void setSituacao(String situacao);
	public String getTipoCliente() ;
	public void setTipoCliente(String tipoCliente);
	public String getSite();
	public void setSite(String site);
	public Date getDataNascimento();
	public void setDataNascimento(Date dataNascimento);
	
}
