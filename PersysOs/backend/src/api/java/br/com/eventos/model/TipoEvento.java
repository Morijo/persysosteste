package br.com.eventos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import br.com.evento.dao.TipoEventoDAO;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.model.PreconditionsModel;
import br.com.oauth.model.ConsumerSecret;

@XmlRootElement
@XmlAccessorType( XmlAccessType.FIELD )
@Entity
@Table(name="tipoevento")
public class TipoEvento extends Model<TipoEvento> {

        @Column(name = "titulo")
        private String titulo;
        
        @Column(name = "observacao", length=255)
        private String observacao;

        public TipoEvento(){
                super(TipoEvento.class);
        }
        
        public TipoEvento(Long id, String titulo){
            super(TipoEvento.class);
            setId(id);
            setTitulo(titulo);
    }
    
        public String getTitulo() {
                return titulo;
        }

        public void setTitulo(String titulo) {
                this.titulo = titulo;
        }

        public String getObservacao() {
                return observacao;
        }

        public void setObservacao(String observacao) {
                this.observacao = observacao;
        }

        public static TipoEvento pesquisaTipoEvento(String titulo, ConsumerSecret consumerSecret){
                TipoEventoDAO tipoEventoDAO = new TipoEventoDAO();
                return tipoEventoDAO.pesquisaTitulo(titulo, consumerSecret);
        }
        
        @Override
        public void valida() throws ModelException {
                PreconditionsModel.checkEmptyString(titulo, "not found");
                PreconditionsModel.checkNull(TipoEvento.pesquisaTipoEvento(titulo, getConsumerSecret()), "Tipo evento");
        }

        @Override
        public String toString() {
                return "TipoEvento [titulo=" + titulo + ", observacao=" + observacao
                                + "]";
        }
        
        
}