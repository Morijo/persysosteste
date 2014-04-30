package br.com.model.interfaces;

public interface IOrdemProcedimento extends IModel {

	public IAnexo getAnexo();

	public void setAnexo(IAnexo anexo);
	
	public IProcedimento getProcedimento();

	public void setProcedimento(IProcedimento procedimento);
	
	public void setServico(IServico servico);

	public IServico getServico();

	public Boolean isObrigatorio();

	public void setObrigatorio(Boolean anexo);
	
	public Boolean isAnexo();

	public void setAnexo(Boolean anexo);
	
	public Boolean isVerificado();

	public void setVerificado(Boolean verificado);
	
	public IOrdem getOrdem();
	
	public void setOrdem(IOrdem ordem);
}
