package br.com.rest.resources;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import br.com.cliente.model.Cliente;
import br.com.clienteobjeto.model.ClienteObjeto;
import br.com.clienteobjeto.model.ClienteObjetoProduto;
import br.com.clienteobjeto.model.Contrato;
import br.com.empresa.model.Organizacao;
import br.com.eventos.model.Evento;
import br.com.eventos.model.TipoEvento;
import br.com.ordem.model.AgendaOrdemFuncionario;
import br.com.ordem.model.Anexo;
import br.com.ordem.model.BaseConhecimento;
import br.com.ordem.model.Ordem;
import br.com.ordem.model.OrdemProcedimento;
import br.com.ordem.model.Prioridade;
import br.com.ordem.model.RecursoOrdem;
import br.com.ordem.model.SituacaoOrdem;
import br.com.produto.model.Produto;
import br.com.recurso.model.Dispositivo;
import br.com.recurso.model.Medida;
import br.com.recurso.model.Recurso;
import br.com.rest.hateoas.AgendaOrdemFuncionarioData;
import br.com.rest.hateoas.AnexoData;
import br.com.rest.hateoas.BaseConhecimentoData;
import br.com.rest.hateoas.ClienteData;
import br.com.rest.hateoas.ClienteObjetoProdutoData;
import br.com.rest.hateoas.ContatoData;
import br.com.rest.hateoas.ContratoData;
import br.com.rest.hateoas.DepartamentoData;
import br.com.rest.hateoas.DispositivoData;
import br.com.rest.hateoas.EquipamentoData;
import br.com.rest.hateoas.EventoDataDTO;
import br.com.rest.hateoas.EventosData;
import br.com.rest.hateoas.FuncionarioData;
import br.com.rest.hateoas.MaterialData;
import br.com.rest.hateoas.MedidaData;
import br.com.rest.hateoas.NotaData;
import br.com.rest.hateoas.OrdemProcedimentoData;
import br.com.rest.hateoas.OrdemServicoData;
import br.com.rest.hateoas.OrdemServicoDataDTO;
import br.com.rest.hateoas.OrganizacaoData;
import br.com.rest.hateoas.PrioridadeData;
import br.com.rest.hateoas.ProdutoData;
import br.com.rest.hateoas.RecursoData;
import br.com.rest.hateoas.RecursoOrdemData;
import br.com.rest.hateoas.ResponseBatchData;
import br.com.rest.hateoas.ServicoData;
import br.com.rest.hateoas.ServicoOrdemData;
import br.com.rest.hateoas.ServicoProcedimentoData;
import br.com.rest.hateoas.SituacaoOrdemData;
import br.com.rest.hateoas.TipoEventoData;
import br.com.rest.hateoas.UnidadeData;
import br.com.rest.hateoas.dto.OrdemDTO;
import br.com.rest.represention.ErrorEntity;
import br.com.rest.represention.ResponseBatch;
import br.com.servico.model.Servico;
import br.com.servico.model.ServicoProcedimento;
 
@Provider 
public class JaxbContextResolver implements ContextResolver<JAXBContext> { 

private JAXBContext context; 
@SuppressWarnings("rawtypes")
private Class[] types = { FuncionarioData.class, EquipamentoData.class, MaterialData.class, ContatoData.class, ErrorEntity.class, EventosData.class, Evento.class, OrganizacaoData.class
		,Organizacao.class, UnidadeData.class, com.sun.research.ws.wadl.Application.class, DepartamentoData.class, Cliente.class, ClienteData.class,
		Produto.class, ProdutoData.class, Dispositivo.class, DispositivoData.class, ContratoData.class,Contrato.class,ClienteObjeto.class, ClienteObjetoProduto.class,
		ClienteObjetoProdutoData.class, TipoEventoData.class, TipoEvento.class, Servico.class, ServicoData.class, Ordem.class,
		Evento.class, EventoDataDTO.class, Medida.class, MedidaData.class, OrdemServicoData.class,
		AnexoData.class, OrdemDTO.class, OrdemServicoDataDTO.class, AnexoData.class, Anexo.class, RecursoOrdem.class, Recurso.class,
		RecursoOrdemData.class, Prioridade.class, PrioridadeData.class, SituacaoOrdem.class,
		SituacaoOrdemData.class, ServicoOrdemData.class,
		NotaData.class, BaseConhecimentoData.class, BaseConhecimento.class, AgendaOrdemFuncionario.class, AgendaOrdemFuncionarioData.class, 
		RecursoData.class, ServicoProcedimento.class, ServicoProcedimentoData.class,
		OrdemProcedimentoData.class, OrdemProcedimento.class, ResponseBatchData.class, ResponseBatch.class}; 

	public JaxbContextResolver() throws Exception { 
		this.context = new JSONJAXBContext(JSONConfiguration.mapped().arrays("data").build(), types); 
	}  

	public JAXBContext getContext(Class<?> objectType) { 
		return context; 
	} 
} 
