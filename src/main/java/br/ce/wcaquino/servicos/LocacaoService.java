package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.dao.EmailService;
import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.dao.SerasaService;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.UsuarioNegativadoException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;
	private SerasaService serasa;
	private EmailService email;
	
	


	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException, UsuarioNegativadoException {
		
		if(usuario == null) {
			throw new LocadoraException("Usu�rio vazio");	
		}
		
		if(serasa.possuiNegativacao(usuario)) {
			throw new UsuarioNegativadoException("Usu�rio Negativado");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}
		
		Filme filme;
//		Verifica se todos os filmes est�o em estoque
		for(int i = 0; i<filmes.size(); i++){
			filme = filmes.get(i);
			if(filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme sem estoque");  
			}	
		}
		
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		double desconto = 0;
		double valorTotal = 0;
		for(int i = 0; i<filmes.size(); i++) {
			filme = filmes.get(i);
			if(i>= 2 && i<6) {
				desconto = desconto + 0.25;
				valorTotal = (valorTotal + filme.getPrecoLocacao() * (1-desconto));
			}
			else {
				valorTotal = (valorTotal + filme.getPrecoLocacao());
			}
		}
		locacao.setValor(valorTotal);
		
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.MONDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 2);
		}
		else {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Persistindoo a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}
	
	public void notificaAtraso() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		for(Locacao locacao: locacoes) {
			email.notificaAtraso(locacao.getUsuario());
		} 
	}

	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao=dao;
	}
	
	public void setSerasaService(SerasaService serasa) {
		this.serasa = serasa;
	}
	
	public void setEmail(EmailService email) {
		this.email = email;
	}

	public void setEmailService(EmailService email) {
		this.email = email;
	}

	
}