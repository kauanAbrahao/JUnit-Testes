package processador;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ProcessadorTeste {
	/*
	FonteOrigem fonteOrigem = null;
	FonteDestino fonteDestino = null;
	
-----------  Easy Mock	
 
	@Before
	public void inicializa() {
		//Criando os Mocks
		fonteOrigem = EasyMock.createMock(FonteOrigem.class);
		fonteDestino = EasyMock.createMock(FonteDestino.class);
	}
	
	@Test
	public void testProcessaDados() {
		
		EasyMock.expect(fonteOrigem.leDados()).andReturn("DadoTeste");
		fonteDestino.gravaDados("DadoTeste");
		
		EasyMock.replay(fonteOrigem, fonteDestino);
		
		Processador processador = new Processador(fonteOrigem, fonteDestino);
		
		try {
			processador.processaDados();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EasyMock.verify(fonteOrigem, fonteDestino);
	}
	*/
	
//	------------ Mockito
	
	@Mock
	FonteOrigem fonteOrigem = null;
	
	@Mock
	FonteDestino fonteDestino = null;
	
	@Before
	public void inicializa() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testaProcessaDados() {
		Mockito.when(fonteOrigem.leDados()).thenReturn("Dado que iria vir da Fonte de Origem");
		
		Processador processador = new Processador(fonteOrigem, fonteDestino);
		
		try {
			processador.processaDados();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		Mockito.verify(fonteOrigem, Mockito.times(1)).leDados();
		Mockito.verify(fonteDestino, Mockito.times(1)).gravaDados("Dado que iria vir da Fonte de Origem");
	}
	
	
	
	
}
