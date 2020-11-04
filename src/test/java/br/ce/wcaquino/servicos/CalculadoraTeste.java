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
		//cen�rio
		int a = 5;
		int b = 3;
		
		//a��o
		int resultado = calc.somar(a, b);
		
		//verifica��o
		assertEquals(8, resultado);
		
	}
	
	@Test
	public void deveSubDoisValores() {
		//cen�rio
		int a = 5;
		int b = 3;
		
		//a��o
		int resultado = calc.sub(a, b);
			
		//verifica��o
		assertEquals(2, resultado);
				
	}
	
	@Test
	public void deveDividirDoisValores() throws DivisaoPorZeroException {
		//cen�rio
		double a = 10.0;
		double b = 3.0;
		
		//a��o
		double resultado = calc.div(a, b);
		
		//verifica��o
		assertEquals(3.333, resultado, 0.001 );
		
		
	}
	
	@Test
	public void ExceptionAoDividirPorZero() {
		
		//cen�rio
		int a = 10;
		int b = 0;
		
		//a��o
		
		try {
			calc.div(a, b);
			Assert.fail("N�o pode dividir por 0");
		} catch (DivisaoPorZeroException e) {
			MatcherAssert.assertThat(e.getMessage(), CoreMatchers.is("Divis�o por 0"));
		}
		
	}

}
