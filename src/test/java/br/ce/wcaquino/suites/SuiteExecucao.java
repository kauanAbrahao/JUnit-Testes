package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.AssertTest;
import br.ce.wcaquino.servicos.CalculadoraTeste;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTeste;
import br.ce.wcaquino.servicos.LocacaoServiceTeste;
import br.ce.wcaquino.servicos.OrdemTestes;

//@RunWith(Suite.class)
//@SuiteClasses({CalculadoraTeste.class, AssertTest.class, LocacaoServiceTeste.class, OrdemTestes.class, CalculoValorLocacaoTeste.class})
public class SuiteExecucao {
//	Se quiser pode usar o @BeforeClass e @AfterClass. Assim, a lógica marcada
//	será executada antes de cada classe de teste.
//	A classe SuiteExecução pode ficar vazia, entretanto.
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before Class na Suite");
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("After Class na Suite");
	}
}
