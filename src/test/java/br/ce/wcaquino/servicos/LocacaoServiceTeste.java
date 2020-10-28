package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTeste {

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	
	@Test
	public void alugarFilmeTeste() throws Exception {
		//Testes
		
		//Contexto(cen�rio)
		Usuario usuario = new Usuario("Jorge");
		Filme filme = new Filme("Toy Story 2", 5, 5.0);
		
		//A��o
		LocacaoService service = new LocacaoService();
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		//Verifica��o
		assertEquals(5.0, locacao.getValor(), 0.1);
		assertNotEquals(4.0, locacao.getValor());
		assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
//		outra forma de fazer � com assertThat. Fica mais f�cil a leitura do c�digo
		MatcherAssert.assertThat(locacao.getValor(), CoreMatchers.is(5.0));
		MatcherAssert.assertThat(locacao.getValor(), CoreMatchers.not(4.0));
		MatcherAssert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		MatcherAssert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
		
//		*Certo. Mas a forma como est� acima n�o � a mais indicada. O ideal � que um teste possua 1 contexto + 1 a��o + 1 teste.
//		*Ou seja, para ficar mais organizado, criaremos m�todos diferentes para cada teste, cada um com seu contexto e sua a��o.
//		*Outra solu��o � a utiliza��o de @Rules ~ ErrorCollector, trocando assertThat por error.checkThat. As duas solu��es
//		permitem que todos os testes sejam corridos, mesmo que o primeiro j� aponte erro.
		
		error.checkThat(locacao.getValor(), CoreMatchers.is(5.0)); //Erro de prop�sito, o certo seria 5.0
		error.checkThat(locacao.getValor(), CoreMatchers.not(6.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true)); //Erro de prop�sito. O certo seria true
		
	}
	
//	E se eu tiver esperando um Exception, tenho que test�-lo tbm, correto? Vamos ver como fazer
	
	@Test(expected = Exception.class)
	public void testaLocacao_filmeSemEstoque() throws Exception {
		//Contexto(cen�rio)
		Usuario usuario = new Usuario("Jorge");
		Filme filme = new Filme("Toy Story 2", 0, 5.0);
				
		//A��o
		LocacaoService service = new LocacaoService();
		service.alugarFilme(usuario, filme);
	}
	
//	Outra forma de tratar a Exception:
	
	@Test
	public void testaLocacao_filmeSemEstoque2() {
		//Contexto(cen�rio)
		Usuario usuario = new Usuario("Jorge");
		Filme filme = new Filme("Toy Story 2", 0, 5.0); //Se voc� colocar 0, vai cair no asserThat, porque gerou uma Exception.
//		Se voc� colocar um valor maior que 0, vai cair no Assert.fail, ou seja, n�o gerou uma Exception, ent�o est� errado.
						
		//A��o
		LocacaoService service = new LocacaoService();
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lan�ado uma excess�o");
		} catch (Exception e) {
			MatcherAssert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}
	}
	
//	Uma terceira forma de verificar, � atrav�s de @Rule ~ ExpectedException. ATEN��O!! Precisa vir antes de executar a a�ao.
	@Test
	public void testaLocacao_filmeSemEstoque3() throws Exception {
		//Contexto(cen�rio)
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Jorge");
		Filme filme = new Filme("Toy Story 2", 0, 5.0); //Se colocar um valor diferente de 0, o teste vai falhar, pois ele espera uma Exception
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
				
		//A��o
		service.alugarFilme(usuario, filme);
	}
	
//	Vamos dar prefer�ncia para o primeiro m�todo. Contudo, precisamos melhor�-lo! Exception � muito gen�rico. Como garantir
//	que essa Exception foi lan�ada pelo motivo correto? Atrav�s da mensagem que a Exception est� enviando!!! Vamos refazer:
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque1v2() throws Exception {
		//Cen�rio
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario();
		Filme filme = new Filme("A Bela e a Fera", 0, 4.0);
		
		 //A��o
		service.alugarFilme(usuario, filme);
	}
	
	@Test
	public void testLocacao_usuarioVazio() {
		
	}
	
}
