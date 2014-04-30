package br.com.model.interfaces;

public interface IDispositivoChip extends IModel {

	public IChip getChip();

	public void setChip(IChip chip);

	public void setDispositivo(IDispositivo dispositivo);

	public IDispositivo getDispositivo();

	public Boolean isPrincipal();

	public void setPrincipal(Boolean principal);

}
