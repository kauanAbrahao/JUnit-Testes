package br.ce.wcaquino.servicos;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.dao.SerasaService;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.UsuarioNegativadoException;

//	DATA DRIVEN TEST. Aqui tudo é parâmetro, sem mais cambos chumbados.
//	O que nós estamos fazendo é: Definir um @RunWith para rodar a classe como Parametrizada.
//	Em seguida, definimos os parâmeotros: Lista de filmes, ValorLocaoEsperado e uma String para dar nome ao teste.
//	Para definir o parâmetros, usamos @Parameter e um valor para identificá-lo.
//	Parâmetros precisam ser públicos.
//	Em seguida, definimos @Parameters, onde colocamos os valores dos parâmetros.
//	Este modelo DDT torna as classes de testes menores e mais adaptáveis.
//	Para cada Array.asList, teremos um teste. Aqui, estamos testando locações com 2 a 7 filmes alocados por usuário.

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTeste {
	
	
	private LocacaoService service;
	private SerasaService serasa;
	
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public double valorLocacaoEsperado;
	
	@Parameter(value = 2)
	public String qntdFilmes;
	
	@Before
	public void before() {
		service = new LocacaoService();
		LocacaoDAO dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		serasa = Mockito.mock(SerasaService.class);
		service.setSerasaService(serasa);
	}
	
//	Preparando os dados a serem testados
	private static Filme filme1 = new Filme("Filme 1", 1, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 1, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 1, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 1, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 1, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 1, 4.0);
	private static Filme filme7 = new Filme("Filme 7", 1, 4.0);
	
	@Parameters(name= "{2}") // O nome do teste será o parâmetro 2, ou seja, String qntdFilmes
	public static Collection<Object[]>	getParametros(){
		return Arrays.asList(new Object[][]{
			{Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem Desconto"},
			{Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%"}, 
			{Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%"}, 
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 Filmes: Sem desconto"}
			});
	}
	
	@Test
	public void deveCalcularValorComDesconto() throws FilmeSemEstoqueException, LocadoraException, UsuarioNegativadoException {
//		cenário
		Usuario usuario = new Usuario("Usuario 1");
		
//		ação	
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
//		verificação
		MatcherAssert.assertThat(resultado.getValor(), CoreMatchers.is(valorLocacaoEsperado));
		
	}
	
}
