package br.com.rest.resources;

import br.com.contato.model.Endereco;

public class EnderecoResource {

	
	protected Endereco alterarEndereco(Endereco endAtual,Endereco end){
		  
		  endAtual.setBairro(end.getBairro());
		  endAtual.setCidade(end.getCidade()) ;
		  endAtual.setLogradouro(end.getLogradouro());
		  endAtual.setCep(end.getCep());
		  endAtual.setNumero(end.getNumero());
		  endAtual.setEstado(end.getEstado());
		  endAtual.setCodigo(end.getCodigo());
		  return endAtual;
	}
}
