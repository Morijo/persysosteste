package br.com.ordem.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.sun.xml.bind.CycleRecoverable;
import br.com.exception.ModelException;
import br.com.model.Model;

/**
 *  <p> O modelo SituacaoOrdemPermissao</p>
 * @author ricardosabatine, jpmorijo	
 * @version 1.0.0
 * @since 27/03/2013
 * @see Ordem
 */

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="situacaoordempermissao")
public class SituacaoOrdemPermissao extends Model<SituacaoOrdemPermissao> implements Serializable, CycleRecoverable{

	
	private static final long serialVersionUID = 1L;

	@Column(name = "permissao")
	private boolean  ativo = false;
	
	@XmlTransient
	@ManyToOne
	@JoinColumns({    
		@JoinColumn( name = "situacaoordemidpk", referencedColumnName="id"),
	})
	private SituacaoOrdem situacaoOrdem;
	
	public SituacaoOrdemPermissao(Long id) {
		super(SituacaoOrdemPermissao.class);
		setId(id);
	}

	public static final String CONSTRUTOR = "id,codigo";
	
	public SituacaoOrdemPermissao(Long id, String codigo) {
		super();
		setId(id);
		setCodigo(codigo);
	}

	public SituacaoOrdemPermissao() {
		super(SituacaoOrdemPermissao.class);
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public SituacaoOrdem getSituacaoOrdem() {
		return situacaoOrdem;
	}

	public void setSituacaoOrdem(SituacaoOrdem situacaoOrdem) {
		this.situacaoOrdem = situacaoOrdem;
	}

	@Override
	public void valida() throws ModelException {
		if(getCodigo().isEmpty()){
			setCodigo("PSOSP"+countPorConsumerSecret(SituacaoOrdemPermissao.class,getConsumerSecret().getConsumerKey()));
		}
	}
	
	@Override
	public Object onCycleDetected(Context arg0) {
		SituacaoOrdemPermissao situacaoOrdem = new SituacaoOrdemPermissao();
		return situacaoOrdem;
	}

	
}