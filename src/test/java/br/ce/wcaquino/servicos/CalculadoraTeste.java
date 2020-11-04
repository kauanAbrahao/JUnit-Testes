package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.DivisaoPorZeroException;

public class CalculadoraTeste {
	Calculadora calc;
	
//	Faremos aqui um TDD (Test Driven Development)
	
	@Before
	public void start() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		//cenário
		int a = 5;
		int b = 3;
		
		//ação
		int resultado = calc.somar(a, b);
		
		//verificação
		assertEquals(8, resultado);
		
	}
	
	@Test
	public void deveSubDoisValores() {
		//cenário
		int a = 5;
		int b = 3;
		
		//ação
		int resultado = calc.sub(a, b);
			
		//verificação
		assertEquals(2, resultado);
				
	}
	
	@Test
	public void deveDividirDoisValores() throws DivisaoPorZeroException {
		//cenário
		double a = 10.0;
		double b = 3.0;
		
		//ação
		double resultado = calc.div(a, b);
		
		//verificação
		assertEquals(3.333, resultado, 0.001 );
		
		
	}
	
	@Test
	public void ExceptionAoDividirPorZero() {
		
		//cenário
		int a = 10;
		int b = 0;
		
		//ação
		
		try {
			calc.div(a, b);
			Assert.fail("Não pode dividir por 0");
		} catch (DivisaoPorZeroException e) {
			MatcherAssert.assertThat(e.getMessage(), CoreMatchers.is("Divisão por 0"));
		}
		
	}

}
