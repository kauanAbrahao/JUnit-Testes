package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

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

public class LocacaoServiceTeste {

	private LocacaoService service;
	private SerasaService serasa;
	private LocacaoDAO dao;
	private EmailService email;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();	
	
	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
//	Antes de cada método ser iniciado, roda essas linhas de código.
	@Before
	public void setup() {
		service = new LocacaoService();
		email = Mockito.mock(EmailService.class);
		service.setEmailService(email);
		dao = Mockito.mock(LocacaoDAO.class);
		service.setLocacaoDAO(dao);
		serasa = Mockito.mock(SerasaService.class);
		service.setSerasaService(serasa);
	}	
	
//	Após cada método ser finalizado, roda essas linhas de código.
	@After
	public void tearDown() {
	}
	
//	Antes da CLASSE ser iniciada, rodam essas linhas
	@BeforeClass
	public static void setupClass() {
		System.out.println("Inicializando testes...");
		
	}
	
//	Após a CLASSE ser finalizada, rodam essas linhas
	@AfterClass
	public static void tearDownClass() {
		System.out.println("Todos os testes foram finalizados!");
	}
	
	
	@Test
	public void alugarFilmeTeste() throws Exception {
		//Testes
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//Contexto(cenário)
		Usuario usuario = new Usuario("Jorge");
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(new Filme("Kung Fu Panda", 3, 8.0));
		filmes.add(new Filme("Toy Story 2", 5, 5.0));
		
		
		//Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertEquals(13.0, locacao.getValor(), 0.1);
		assertNotEquals(4.0, locacao.getValor());
		assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
//		outra forma de fazer é com assertThat. Fica mais fácil a leitura do código
		MatcherAssert.assertThat(locacao.getValor(), CoreMatchers.is(13.0));
		MatcherAssert.assertThat(locacao.getValor(), CoreMatchers.not(4.0));
		MatcherAssert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		MatcherAssert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
		
//		*Certo. Mas a forma como está acima não é a mais indicada. O ideal é que um teste possua 1 contexto + 1 ação + 1 teste.
//		*Ou seja, para ficar mais organizado, criaremos métodos diferentes para cada teste, cada um com seu contexto e sua ação.
//		*Outra solução é a utilização de @Rules ~ ErrorCollector, trocando assertThat por error.checkThat. As duas soluções
//		permitem que todos os testes sejam corridos, mesmo que o primeiro já aponte erro.
		
		error.checkThat(locacao.getValor(), CoreMatchers.is(13.0)); //Erro de propósito, o certo seria 13
		error.checkThat(locacao.getValor(), CoreMatchers.not(6.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true)); //Erro de propósito. O certo seria true
		
	}
	
//	E se eu tiver esperando um Exception, tenho que testá-lo tbm, correto? Vamos ver como fazer
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void testaLocacao_filmeSemEstoque() throws Exception {
		//Contexto(cenário)
		Usuario usuario = new Usuario("Jorge");
		List<Filme> filmes = Arrays.asList(new Filme("Toy Story 2", 0, 5.0));
				
		//Ação
		service.alugarFilme(usuario, filmes);
	}
	
//	Outra forma de tratar a Exception:
	
	@Test
	public void testaLocacao_filmeSemEstoque2() throws LocadoraException, UsuarioNegativadoException {
		//Contexto(cenário)
		Usuario usuario = new Usuario("Jorge");
		List<Filme> filmes = Arrays.asList(new Filme("Toy Story 2", 0, 5.0)); //Se você colocar 0, vai cair no asserThat, porque gerou uma Exception.
//		Se você colocar um valor maior que 0, vai cair no Assert.fail, ou seja, não gerou uma Exception, então está errado.
						
		//Ação
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lançado uma excessão");
		} catch (FilmeSemEstoqueException e) {
			MatcherAssert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}
	}
	
//	Uma terceira forma de verificar, é através de @Rule ~ ExpectedException. ATENÇÃO!! Precisa vir antes de executar a açao.
	@Test
	public void testaLocacao_filmeSemEstoque3() throws FilmeSemEstoqueException, LocadoraException, UsuarioNegativadoException {
		//Contexto(cenário)
		Usuario usuario = new Usuario("Jorge");
		List<Filme> filmes = Arrays.asList(new Filme("Toy Story 2", 0, 5.0)); //Se colocar um valor diferente de 0, o teste vai falhar, pois ele espera uma Exception
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
				
		//Ação
		service.alugarFilme(usuario, filmes);
	}
	
//	Vamos dar preferência para o primeiro método quando temos certeza de qual será nossa exception. 
//	Contudo, precisamos melhorá-lo! 
//	Exception é muito genérico. Como garantir
//	que essa Exception foi lançada pelo motivo correto? Através da mensagem que a Exception está enviando!!! Vamos refazer:
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque1v2() throws Exception {
		//Cenário
		Usuario usuario = new Usuario();
		List<Filme> filmes = Arrays.asList(new Filme("Toy Story 2", 0, 5.0));
		
		 //Ação
		service.alugarFilme(usuario, filmes);
	}
	
	//Fizemos novamente a segunda forma. Ela é a mais robusta e permite maior controle. Na dúvida, use essa forma!!
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException, UsuarioNegativadoException {
		//cenário
		List<Filme> filmes = Arrays.asList(new Filme("Toy Story 2", 5, 5.0));
		
		//ação
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
			
		} catch (LocadoraException e) {
			MatcherAssert.assertThat(e.getMessage(), CoreMatchers.is("Usuário vazio"));
		}
		
	}
	
//	E finalmente, novamente a terceira forma.
	@Test
	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException, UsuarioNegativadoException {
		//cenário
		Usuario usuario = new Usuario("Matheus");
		
		
		//ação
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		service.alugarFilme(usuario, null);
		
	}
	
	@Test
	public void testLocacao_Descontos() throws FilmeSemEstoqueException, LocadoraException, UsuarioNegativadoException {
//		cenário
		Usuario usuario = new Usuario("Renan");
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(new Filme("A Coisa", 1, 5.0));
		filmes.add(new Filme("Dark Age", 2, 5.0));
		filmes.add(new Filme("Pocahontas", 3, 5.0));
		
//		ação
		Locacao locacao = service.alugarFilme(usuario, filmes);
		assertEquals(13.75, locacao.getValor(), 0.01);
		
		filmes.add(new Filme("Rei Leao", 9, 5.0));
		locacao = service.alugarFilme(usuario, filmes);
		assertEquals(16.25, locacao.getValor(), 0.01);
		
		filmes.add(new Filme("Filme 5", 9, 5.0));
		filmes.add(new Filme("Filme 6", 9, 5.0));
		filmes.add(new Filme("Filme 7", 9, 5.0));
		locacao = service.alugarFilme(usuario, filmes);
		assertEquals(22.5, locacao.getValor(), 0.01);
		
	}
	
		
	@Test
	public void testLocacao_NaoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException, UsuarioNegativadoException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
//		cenário
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(new Filme("Filme teste", 5, 3.0));
		
//		ação
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
//		verificação
		
		boolean segunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		assertTrue(segunda);	
	}
	
	@Test
	public void LocacaoService_UsuarioNegativadoNoSerasa() throws FilmeSemEstoqueException, LocadoraException {
//		cenário
		Usuario usuario = new Usuario("Usuario1");
//		Usuario usuario2 = new Usuario("Usuario2");
		List<Filme>filmes = Arrays.asList(new Filme("Filme x", 4, 4.0));
		
/*		Definimos abaixo o comportamento do usuario para retornar true, ou seja, negativado.
		Se colocarmos usuario2 ao invés do usuario na ação, o teste dará errado
		Pois não definimos um comportamento para usuário, portanto, por padrão, o Mockito
		Retorna false. */
		Mockito.when(serasa.possuiNegativacao(usuario)).thenReturn(true);
		
//		ação
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail();
//		Verificação
		} catch (UsuarioNegativadoException e) {
			Assert.assertEquals("Usuário Negativado", e.getMessage());
		}
		Mockito.verify(serasa).possuiNegativacao(usuario	);
	}
	
	@Test	
	public void notificaLocacaoAtrasada() {
//		cenário
		Usuario usuario = new Usuario("Usuáro1");
		List<Locacao> locacoes = Arrays.asList(new Locacao());
		Locacao teste = locacoes.get(0);
		teste.setDataRetorno(DataUtils.obterDataComDiferencaDias(-1));
		teste.setUsuario(usuario);
//		Usuario usuario2 = new Usuario("Usuário2");
		
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
//		ação
		service.notificaAtraso();
		
//		Verificação
		Mockito.verify(email).notificaAtraso(usuario);
		
	}
		
	

	
}
