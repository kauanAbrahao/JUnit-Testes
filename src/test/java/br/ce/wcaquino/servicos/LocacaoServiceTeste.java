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
		
		//Contexto(cenário)
		Usuario usuario = new Usuario("Jorge");
		Filme filme = new Filme("Toy Story 2", 5, 5.0);
		
		//Ação
		LocacaoService service = new LocacaoService();
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		//Verificação
		assertEquals(5.0, locacao.getValor(), 0.1);
		assertNotEquals(4.0, locacao.getValor());
		assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
//		outra forma de fazer é com assertThat. Fica mais fácil a leitura do código
		MatcherAssert.assertThat(locacao.getValor(), CoreMatchers.is(5.0));
		MatcherAssert.assertThat(locacao.getValor(), CoreMatchers.not(4.0));
		MatcherAssert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		MatcherAssert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
		
//		*Certo. Mas a forma como está acima não é a mais indicada. O ideal é que um teste possua 1 contexto + 1 ação + 1 teste.
//		*Ou seja, para ficar mais organizado, criaremos métodos diferentes para cada teste, cada um com seu contexto e sua ação.
//		*Outra solução é a utilização de @Rules ~ ErrorCollector, trocando assertThat por error.checkThat. As duas soluções
//		permitem que todos os testes sejam corridos, mesmo que o primeiro já aponte erro.
		
		error.checkThat(locacao.getValor(), CoreMatchers.is(5.0)); //Erro de propósito, o certo seria 5.0
		error.checkThat(locacao.getValor(), CoreMatchers.not(6.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true)); //Erro de propósito. O certo seria true
		
	}
	
//	E se eu tiver esperando um Exception, tenho que testá-lo tbm, correto? Vamos ver como fazer
	
	@Test(expected = Exception.class)
	public void testaLocacao_filmeSemEstoque() throws Exception {
		//Contexto(cenário)
		Usuario usuario = new Usuario("Jorge");
		Filme filme = new Filme("Toy Story 2", 0, 5.0);
				
		//Ação
		LocacaoService service = new LocacaoService();
		service.alugarFilme(usuario, filme);
	}
	
//	Outra forma de tratar a Exception:
	
	@Test
	public void testaLocacao_filmeSemEstoque2() {
		//Contexto(cenário)
		Usuario usuario = new Usuario("Jorge");
		Filme filme = new Filme("Toy Story 2", 0, 5.0); //Se você colocar 0, vai cair no asserThat, porque gerou uma Exception.
//		Se você colocar um valor maior que 0, vai cair no Assert.fail, ou seja, não gerou uma Exception, então está errado.
						
		//Ação
		LocacaoService service = new LocacaoService();
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lançado uma excessão");
		} catch (Exception e) {
			MatcherAssert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}
	}
	
//	Uma terceira forma de verificar, é através de @Rule ~ ExpectedException. ATENÇÃO!! Precisa vir antes de executar a açao.
	@Test
	public void testaLocacao_filmeSemEstoque3() throws Exception {
		//Contexto(cenário)
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Jorge");
		Filme filme = new Filme("Toy Story 2", 0, 5.0); //Se colocar um valor diferente de 0, o teste vai falhar, pois ele espera uma Exception
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
				
		//Ação
		service.alugarFilme(usuario, filme);
	}
	
}
