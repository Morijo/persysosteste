package br.com.model.interfaces;

import java.math.BigDecimal;
import java.util.Date;

public interface IRecursoOrdem {

	public IOrdem getOrdem();
	public void setOrdem(IOrdem ordem);
	public String getObs();
	public BigDecimal getQuantidadeConsumida();
	public void setQuantidadeConsumida(BigDecimal quantidadeConsumida);
	public IRecurso getRecurso();
	public void setRecurso(IRecurso recurso);
	public void setObs(String obs);
	public Date getDataCriacaoOrigem();
	public void setDataCriacaoOrigem(Date dataCriacaoOrigem);
}
