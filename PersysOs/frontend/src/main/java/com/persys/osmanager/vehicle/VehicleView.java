package com.persys.osmanager.vehicle;
import com.persys.osmanager.componente.FormView;
import com.persys.osmanager.componente.interfaces.IMessage;
import com.persys.osmanager.dashboard.DashboardUI;
import com.persys.osmanager.data.TransactionsContainer.TransationsContainerTipo;
import com.persys.osmanager.vehicle.data.TransactionsContainerVehicle;
import com.restmb.RestMBClient;
import com.restmb.types.Veiculo;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class VehicleView extends FormView implements View{

	private static final long serialVersionUID = 1L;
	private final TransactionsContainerVehicle data = new TransactionsContainerVehicle();
	private RestMBClient client = null;
	private Item item = null;
	private VehicleForm veiculoForm =new VehicleForm();


	@Override
	public void enter(ViewChangeEvent event) {
		client =  ((DashboardUI)getUI()).getClient();
		setSizeFull();
		modoTabelaView(buildPagedFilterTable(data),"Veiculo",false);
		filterTable.setVisibleColumns("Código","Veiculo","Situação");
		filterTable.setColumnExpandRatio("Veiculo",0.7f);
	}
	
	@Override
	public void editar() {
		try{
			Veiculo veiculo = (com.restmb.types.Veiculo) veiculoForm.commit();
			veiculo.alterar(client);
			notificationTray("Alteração", "Sucesso");
			data.setItemProperty(veiculo,item);
			voltar();
		}catch(Exception e){
			notificationError("Alteração", "Erro " + e.getMessage() );
		}
	}

	@Override
	public void adicionar() {
		veiculoForm.initData(new Veiculo()); 
		modoAdicionarView(veiculoForm,"Novo Veiculo");
	}

	@Override
	public void remover(Object target) {
		messageSucess("Remover","Deseja remover este Veiculo?","Remover","Cancelar",false,true,true,new IMessage() {

			@Override
			public void ok() {
				try{
					Veiculo veiculo = (com.restmb.types.Veiculo) veiculoForm.commit();
					veiculo.remover(client);
					notificationTray("Remover", "Sucesso");
					defaultTable();
					voltar();
				}catch(Exception e){
					notificationError("Remover", "Erro " + e.getMessage() );
				}
			}
			@Override
			public void discard() {}

			@Override
			public void cancel() {}
		});
	}

	@Override
	public void salvar()  {
		try {
			Veiculo veiculo = (Veiculo) veiculoForm.commit();
			veiculo.salvar(client);
			data.addTransaction(veiculo);
			notificationTray("Salvar", "Sucesso");
			voltar();
		} catch (Exception e) {
			notificationTray("Erro salvar", e.getMessage());
		}
	}

	@Override
	public void visualizar(Object target) {
		try{
			item = ((Item)target);
			Long id = (Long) item.getItemProperty("Id").getValue();
			Veiculo veiculo = new Veiculo();
			veiculo = veiculo.pesquisa(client,id);
			veiculoForm.initData(veiculo);
			modoVisualizarView(veiculoForm, veiculo.getNome());
		}catch(Exception e){}
	}

	@Override
	public void voltar() {
		modoTabelaView(filterTable,"Veiculo",false);
	}

	@Override
	public void defaultTable() {
		data.loadTable(client,TransationsContainerTipo.BD);
	}
}