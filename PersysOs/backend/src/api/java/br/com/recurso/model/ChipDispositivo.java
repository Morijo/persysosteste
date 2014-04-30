package br.com.recurso.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import com.sun.xml.bind.CycleRecoverable;
import br.com.model.interfaces.IChip;
import br.com.model.interfaces.IDispositivo;
import br.com.principal.helper.HibernateHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *  <p> O modelo ChipDispositivo eh composto pelos seguintes campos </p>
 *  <p>id				(Long) Campo Obrigatorio
 *  <p> ativacao 		(date)Campo que retorna a data de ativacao do dispositivo</p>
 *  <p>desativacao 		(date)Campo que retorna a data de desativacao do dispositivo</p> 
 *  <p>Principal		(boolean) Funcao retorna se o numero é o principal do dispositivo</p>
 *  <p>chipidpk 		(bigint)Join com a tabela Chip</p>
 *	<p>dispositivoidpk 	(bigint)Join com a tabela Dispositivo</p>
 *  <br>Tabela no banco: telefonedispositivo </br>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @see Dispositivo
 * @see Chip
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="telefonedispositivo")
@PrimaryKeyJoinColumn(name="id")
public class ChipDispositivo extends Recurso implements Serializable , CycleRecoverable,br.com.model.interfaces.IDispositivoChip {
	
	private static final long serialVersionUID = 1L;

	@Column(name="principal")
	private Boolean principal;

	@Temporal(TemporalType.DATE)
	@Column(name="ativacao")
	private Date ativacao = null;

	@Temporal(TemporalType.DATE)
	@Column(name="desativacao")
	private Date desativacao = null;

	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "dispositivoidpk", referencedColumnName="id"),
	})
	@Cascade(CascadeType.ALL)
	private Dispositivo dispositivo = null;

	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "chipidpk", referencedColumnName="id"),
	})
	@Cascade(CascadeType.ALL)
	private Chip chip = null;


	public ChipDispositivo(){}

	public Boolean getPrincipal() {
		return principal;
	}
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public Date getAtivacao() {
		return ativacao;
	}
	public void setAtivacao(Date ativacao) {
		this.ativacao = ativacao;
	}

	public Date getDesativacao() {
		return desativacao;
	}

	public void setDesativacao(Date desativacao) {
		this.desativacao = desativacao;
	}

	public Chip getChip() {
		return chip;
	}

	public void setChip(Chip chip) {
		this.chip = chip;
	}

	public Dispositivo getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

	@Override
	public void setChip(IChip chip) {
		this.chip = (Chip) chip;
	}

	@Override
	public void setDispositivo(IDispositivo dispositivo) {
		this.dispositivo = (Dispositivo) dispositivo;
	}

	@Override
	public Boolean isPrincipal() {
		return principal;
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<ChipDispositivo> pesquisalistaChipDispositivoPorConstrutor(String consumerKey){
		Session session = HibernateHelper.openSession(ChipDispositivo.class);
		Transaction tx = session.beginTransaction();
		try{
			return (ArrayList<ChipDispositivo>) ChipDispositivo.pesquisaListaPorConsumerSecret(session, 0, -1, CONSTRUTOR,consumerKey, ChipDispositivo.class);
		}finally{
			tx.commit();
			session.close();
		}
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		ChipDispositivo telefone = new ChipDispositivo();
		return telefone;
	}
}