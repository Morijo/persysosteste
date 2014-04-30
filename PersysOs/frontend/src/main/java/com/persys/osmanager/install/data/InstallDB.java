package com.persys.osmanager.install.data;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import br.com.contato.model.Endereco;
import br.com.empresa.model.Departamento;
import br.com.empresa.model.Organizacao;
import br.com.empresa.model.Unidade;
import br.com.eventos.model.TipoEvento;
import br.com.exception.ModelException;
import br.com.model.Model;
import br.com.oauth.model.ConsumerSecret;
import br.com.ordem.model.SituacaoOrdem;
import br.com.principal.helper.HashHelper;
import br.com.recurso.model.Medida;
import br.com.usuario.model.GrupoUsuario;
import br.com.usuario.model.Permissao;
import br.com.usuario.model.Usuario;

public class InstallDB {

	public static boolean createUser(Long consumerKey, Usuario usuario) throws ModelException {
		try {
			usuario.setRazaoNome("admin");
			usuario.setFantasiaSobrenome("admin");
			usuario.setPermitidoExcluir(false);
			usuario.setConsumerSecret(new ConsumerSecret("", consumerKey));
			usuario.setCnpjCpf(consumerKey.toString());
			usuario.setGrupoUsuario(createGrupo(consumerKey));
			try {
				usuario.setSenha(HashHelper.shaCodigo(usuario.getSenha()));
			} catch (NoSuchAlgorithmException e) {}
			usuario.salvar();
			return true;
		} catch (ModelException e) {
			throw new ModelException(e.getMessage());
		}
	}

	public static GrupoUsuario createGrupo(Long consumerKey){
		GrupoUsuario grupoUsuario = new GrupoUsuario();
		grupoUsuario.setConsumerSecret(new ConsumerSecret("", consumerKey));
		grupoUsuario.setDataAlteracao(new Date());
		grupoUsuario.setDataCriacao(new Date());
		grupoUsuario.setNome("Admin");
		grupoUsuario.setAdministrado(true);
		setDataPermissao(grupoUsuario);
		try {
			grupoUsuario.salvar();
		} catch (ModelException e) {
			e.getMessage();
		}
		return grupoUsuario;
	}

	public static void createPermissao(Long consumerKey){

		try {
			createPermissao(consumerKey, "PPER0","Dashboard");
			createPermissao(consumerKey, "PPER1","Organização");
			createPermissao(consumerKey, "PPER2","Ordem de Serviço");
			createPermissao(consumerKey, "PPER3","Agenda");
			createPermissao(consumerKey, "PPER4","Rastreador");
			createPermissao(consumerKey, "PPER5","Empregado");
			createPermissao(consumerKey, "PPER6","Cliente");
			createPermissao(consumerKey, "PPER8","Recurso");
			createPermissao(consumerKey, "PPER9","Serviço");
			createPermissao(consumerKey, "PPER10","Produto");
			createPermissao(consumerKey, "PPER11","Relatório");
			createPermissao(consumerKey, "PPER12","Sistema");
		} catch (ModelException e) {
			e.getMessage();
		}
	}

	private static void createPermissao(Long consumerKey, String codigo, String nome) throws ModelException {
		Permissao permissao = new Permissao();
		permissao.setCodigo(codigo);
		permissao.setNome(nome);
		permissao.setPermitidoAlterar(false);
		permissao.setPermitidoExcluir(false);
		permissao.setStatusModel(1);
		permissao.setConsumerId(consumerKey);
		permissao.salvar();
	}

	private static void createStatusOrder(Long consumerKey, String nome, String descricao, String codigo) {
		SituacaoOrdem situacaoOrdem = new SituacaoOrdem();
		setDataPermissao(situacaoOrdem);
		situacaoOrdem.setNome(nome);
		situacaoOrdem.setDescricao(descricao);
		situacaoOrdem.setCodigo(codigo);
		situacaoOrdem.setConsumerId(consumerKey);
		try {
			situacaoOrdem.salvar();
		} catch (ModelException e) {
			e.getMessage();
		}
	}

	public static void createStatusOrder(Long consumerKey) {
		createStatusOrder(consumerKey, "Aberta", "A ordem de serviço está aberta", "PS1");
		createStatusOrder(consumerKey, "Pendente", "A ordem de serviço está pendente", "PS2");
		createStatusOrder(consumerKey, "Agendada", "A ordem de serviço está agendada", "PS3");
		createStatusOrder(consumerKey, "Pré Atendimento", "O técnico está iniciando a execução da ordem de serviço", "PS4");
		createStatusOrder(consumerKey, "Em Execução", "O técnico está executando a ordem de serviço", "PS5");
		createStatusOrder(consumerKey, "Execução Concluída", "A execução da ordem foi concluída", "PS6");
		createStatusOrder(consumerKey, "Fechada", "A ordem de serviço está fechada", "PS7");
		createStatusOrder(consumerKey, "Cancelada", "A ordem de serviço foi cancelada", "PS8");
	}

	public static void createUnidade(Organizacao organizacao, Long consumerKey){
		Unidade unidade= new Unidade();
		unidade.setNome("Padrão");
		setDataPermissao(unidade);
		unidade.setEmail(organizacao.getEmail());
		unidade.setTelefone(organizacao.getTelefone());

		try{
			Endereco endereco = organizacao.getEndereco();
			endereco.setConsumerId(consumerKey);
			endereco.setId(null);
			endereco.setCodigo("");
			endereco.salvar();
			unidade.setEndereco(endereco);
		} catch (ModelException e1) {
			e1.getMessage();
		}

		unidade.setStatusModel(1);
		unidade.setOrganizacao(organizacao);
		unidade.setConsumerId(consumerKey);
		try {
			unidade.salvar();
		} catch (ModelException e) {}
		createDepartamento(unidade,consumerKey);
	}

	private static void createDepartamento(Unidade unidade, Long consumerKey){
		Departamento departamento = new Departamento();
		departamento.setNomeDepartamento("Padrão");
		setDataPermissao(departamento);
		departamento.setEmail(unidade.getEmail());
		departamento.setTelefone(unidade.getTelefone());
		departamento.setUnidade(unidade);
		departamento.setNomeDepartamento("Padrão");
		departamento.setTipo("Interno");
		departamento.setConsumerId(consumerKey);
		try {
			departamento.salvar();
		} catch (ModelException e) {
			e.getMessage();
		}
	}

	@SuppressWarnings("rawtypes")
	private static void setDataPermissao(Model model) {
		model.setDataAlteracao(new Date());
		model.setDataCriacao(new Date());
		model.setPermitidoExcluir(false);
		model.setPermitidoAlterar(true);
		model.setStatusModel(1);

	}

	private static void createMedidas(String nome, String Abreviacao, Long consumerKey) {
		Medida medida = new Medida();
		medida.setAbreviacao(Abreviacao);
		medida.setNome(nome);
		medida.setDataAlteracao(new Date());
		medida.setDataCriacao(new Date());
		medida.setStatusModel(1);
		medida.setConsumerId(consumerKey);
		try {
			medida.salvar();
		} catch (ModelException e) {
			e.getMessage();
		}

	}

	public static void medidas(Long consumerKey) {

		createMedidas("Unidade", "uni", consumerKey);

		// Medidas de Distâncias
		createMedidas("Quilômetro", "km", consumerKey);
		createMedidas("Hectômetro", "hm", consumerKey);
		createMedidas("Decâmetro", "dam", consumerKey);
		createMedidas("Metros", "m", consumerKey);
		createMedidas("Decímetro", "dm", consumerKey);
		createMedidas("Centímetro", "cm", consumerKey);
		createMedidas("Milímetro", "mm", consumerKey);
		createMedidas("Metros Quadrados", "m²", consumerKey);
		createMedidas("Metros Cúbicos", "m³", consumerKey);
		createMedidas("Quilogramas", "kg", consumerKey);
		// Medidas de Massa
		createMedidas("Hectograma", "g", consumerKey);
		createMedidas("Decagrama", "dag", consumerKey);
		createMedidas("Grama", "g", consumerKey);
		createMedidas("Decigrama", "dg", consumerKey);
		createMedidas("Centigrama", "cg", consumerKey);
		createMedidas("Miligrama", "mg", consumerKey);
		createMedidas("Micrograma", "mcg", consumerKey);
		// Medidas Volumétricas
		createMedidas("Quilolitro", "kl", consumerKey);
		createMedidas("Hectolitro", "hl", consumerKey);
		createMedidas("Decalitro", "dal", consumerKey);
		createMedidas("Litro", "l", consumerKey);
		createMedidas("Decilitro", "dl", consumerKey);
		createMedidas("Centilitro", "cl", consumerKey);
		createMedidas("Mililitro", "ml", consumerKey);
	}

	public static void tipoEvento(Long consumerKey) {
		createTipo(consumerKey, "Login", "PTE1","Login");
		createTipo(consumerKey, "Logout", "PTE2","Logout");
		createTipo(consumerKey, "Livre", "PTE3","Livre");
		createTipo(consumerKey, "Percurso", "PTE4","Percurso");
		createTipo(consumerKey, "Atendimento", "PTE5","Atendimento");
		createTipo(consumerKey, "Intervalo", "PTE6","Intervalo");
	}

	private static void createTipo(Long consumerKey, String titulo, String codigo, String observacao){
		TipoEvento tipoEvento = new TipoEvento();
		tipoEvento.setTitulo(titulo);
		tipoEvento.setCodigo(codigo);
		tipoEvento.setObservacao(observacao);
		tipoEvento.setStatusModel(1);
		tipoEvento.setConsumerId(consumerKey);
		try {
			tipoEvento.salvar();
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}
}