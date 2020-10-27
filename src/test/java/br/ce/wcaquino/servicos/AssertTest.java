package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void test() {
		assertTrue(true);
		assertFalse(false);
		
		assertEquals(1, 1);
		assertEquals(0.51, 0.51, 0.01); //Para valores double, o �ltimo valor � uma margem de erro!
		assertEquals(Math.PI, 3.14, 0.01);
		
//		Comparar objeto com tipo primitivo n�o � aceito no java, exemplo:
		int i = 5;
		Integer i2 = 5;
//		assertEquals(i, i2); Isto n�o funciona. Temos duas solu��es: a. Transformar int em Integer; b. Transformar Integer em int
		assertEquals(Integer.valueOf(i), i2);
		assertEquals(i, i2.intValue());
		
		assertEquals("bola", "bola");
		assertTrue("bola".equalsIgnoreCase("Bola"));
		assertTrue("bola".startsWith("bo"));
		
		Usuario usuario1 = new Usuario("Usuario 1");
		Usuario usuario2 = new Usuario("Usuario 1");
		Usuario usuario3 = usuario2;
		Usuario usuario4 = null;
		
		assertEquals(usuario1, usuario2); //Ele compara se os dois objetos s�o a mesma inst�ncia. No caso, n�o s�o; logo, n�o
//		passa no teste. Mas eu quero comparar se eles possuem os mesmos atributos!! Ent�o l� na classe do Objeto, � necess�rio
//		criar um HashCode e equals(). O eclipse faz para n�s.
		
		assertSame(usuario2, usuario2); // Nesse caso, agora sim est� sendo comparado inst�ncia! 
		assertNotSame(usuario1, usuario2);
		assertSame(usuario3, usuario2);
		assertNull(usuario4);
		assertNotNull(usuario3);
		
		
	}

}
