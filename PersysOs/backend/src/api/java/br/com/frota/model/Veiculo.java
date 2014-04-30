package br.com.frota.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.sun.xml.bind.CycleRecoverable;
import br.com.dao.ParameterDAO;
import br.com.dao.ParameterDAO.ParameterDAOHelper;
import br.com.exception.ModelException;
import br.com.frota.dao.VeiculoDAO;
import br.com.model.PreconditionsModel;
import br.com.model.interfaces.IVeiculo;
import br.com.principal.helper.HibernateHelper;
import br.com.recurso.model.Recurso;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="recursoveiculo")
@PrimaryKeyJoinColumn(name="id")
public class Veiculo extends Recurso implements Serializable , CycleRecoverable, IVeiculo {

	public static final String CONSTRUTOR = "id,codigo,nome,placa";
	
	private static final long serialVersionUID = 1L;

	@Column(name = "placa", length=10)
	private String placa;
	
	@Column(name = "renavan", length=20)
	private String renavam;
	
	@Column(name = "cor")
	private String cor;

	@Column(name = "motorizado")
	private boolean motorizado = false;

	@Column(name = "hodometroinicial")
	private Long hodometroInicial;

	@Column(name = "capacidadetanque")
	private BigDecimal capacidadeTanque;

	@Column(name = "consumopadrao")
	private BigDecimal consumoPadrao;

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void setPlaca(Long id, String codigo, String nome, String placa) {
		setId(id);
		setCodigo(codigo);
		setNome(nome);
		this.placa = placa;
	}
	
	public boolean isMotorizado() {
		return motorizado;
	}

	public void setMotorizado(boolean motorizado) {
		this.motorizado = motorizado;
	}

	public Long getHodometroInicial() {
		return hodometroInicial;
	}

	public void setHodometroInicial(Long hodometroInicial) {
		this.hodometroInicial = hodometroInicial;
	}

	public BigDecimal getCapacidadeTanque() {
		return capacidadeTanque;
	}

	public void setCapacidadeTanque(BigDecimal capacidadeTanque) {
		this.capacidadeTanque = capacidadeTanque;
	}

	public BigDecimal getConsumoPadrao() {
		return consumoPadrao;
	}

	public void setConsumoPadrao(BigDecimal consumoPadrao) {
		this.consumoPadrao = consumoPadrao;
	}

	public String getRenavam() {
		return renavam;
	}

	public void setRenavam(String renavam) {
		this.renavam = renavam;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
    
	public static ArrayList<Veiculo> lista(String consumerKey){
		try{
			VeiculoDAO veiculoDAO = new VeiculoDAO();
			return (ArrayList<Veiculo>) veiculoDAO.lista(consumerKey);
		}
		catch (Exception e) {
			return new ArrayList<Veiculo>();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Veiculo> buscaVeiculo(String cs, String placa, String codigo,
			Integer stauts) throws ModelException {

		Session session = HibernateHelper.openSession(Veiculo.class.getClass());
		Transaction tx = session.beginTransaction();
		ArrayList<ParameterDAO> parameter = new ArrayList<ParameterDAO>();
		
		try{
			ArrayList<Veiculo> VeiculoLista = null;

			if(placa.length() >= 3){
				parameter.add(ParameterDAO.with("placa",placa,ParameterDAOHelper.ILIKE));
			}else if(codigo.length() >= 1){
				parameter.add(ParameterDAO.with("codigo",codigo,ParameterDAOHelper.ILIKE));
			}else{
				throw new ModelException("not found");
			}

			parameter.add(ParameterDAO.with("statusModel",stauts,ParameterDAOHelper.EQ));

			VeiculoLista =(ArrayList<Veiculo>) Veiculo.pesquisaListaPorConsumerSecret(session,Veiculo.class,cs, parameter);
			tx.commit();

			return VeiculoLista;

		}finally{
			parameter = null;
			tx = null;
			session.close();
			session = null;
		}
	}
	@Override
	public void valida() throws ModelException {
		PreconditionsModel.checkEmptyString(placa, "Placa inválida");
		setTipoRecurso("veiculo");
		if(getCodigo().isEmpty()){
			setCodigo("PVEI"+countPorConsumerSecret(Veiculo.class,getConsumerSecret().getConsumerKey()));
		}
	}

	@Override
	public Object onCycleDetected(Context arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
