package br.com.model.interfaces;


public interface IServicoProcedimento extends IModel {

	public IProcedimento getProcedimento();

	public void setProcedimento(IProcedimento procedimento);
	
	public void setServico(IServico servico);

	public IServico getServico();

	public Boolean isObrigatorio();

	public void setObrigatorio(Boolean anexo);
	
	public Boolean isAnexo();

	public void setAnexo(Boolean anexo);

}
