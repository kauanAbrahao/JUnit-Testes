package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //Por ordem alfabética
public class OrdemTestes {
	
//	O JUnit não garante que os testes sejam executados na sequência. Veja só:
//	Para isso, podemos usar o FixMethodOrder
	
	
	static int c = 0;
	
	@Test
	public void inicia() {
		c = 1;
	}
	
	@Test
	public void verifica() {
		assertEquals(1, c);
	}

}
