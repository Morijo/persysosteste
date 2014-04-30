package com.persys.osmanager.system;

import br.com.usuario.model.EmailSMTP;

import com.persys.osmanager.componente.FormView;
import com.restmb.RestMBClient;

public class EmailView extends FormView{

	private static final long serialVersionUID = 1L;
	
	private EmailForm    emailForm = null;
	private RestMBClient restMBClient;
	
	public EmailView(RestMBClient restMBClient) {
		this.restMBClient = restMBClient;
		
		setSizeFull();
		visualizar(null);
		
		buttonDeletar.setVisible(false);
		buttonVoltar.setVisible(false);
	}

	
	@Override
	public void editar() {
		try{
			EmailSMTP emailSMTP = (EmailSMTP) emailForm.commit();
			emailSMTP.setConsumerId(Long.parseLong(restMBClient.getOauth().getApiKey()));
			emailSMTP.salvarAlterar();
			notificationTray("Alteração", "Sucesso");
		}catch(Exception e){
			notificationError("Alteração", "Erro " + e.getMessage() );
		}
	}

	@Override
	public void adicionar() {
		emailForm = new EmailForm();
		modoAdicionarView(emailForm,"Novo Email");
		emailForm.initData(new EmailSMTP()); ;
	}

	@Override
	public void remover(Object target) {}

	@Override
	public void salvar() {}

	@Override
	public void visualizar(Object target) {
		emailForm = new EmailForm();
		emailForm.initData(EmailSMTP.pesquisaEmail(Long.parseLong(restMBClient.getOauth().getApiKey())));
		modoVisualizarView(emailForm, "");
	}

	@Override
	public void defaultTable() {}

	@Override
	public void voltar() {}

}
