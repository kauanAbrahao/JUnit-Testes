package processador;

public class Processador {
	
	private FonteOrigem fonteOrigem;
	private FonteDestino fonteDestino;
	
	
	public Processador(FonteOrigem fonteOrigem, FonteDestino fonteDestino) {
		this.fonteOrigem = fonteOrigem;
		this.fonteDestino = fonteDestino;
	}
	
	public void processaDados() throws Exception {
		String entrada = null;
		
		
		try {
		entrada = fonteOrigem.leDados();
		} catch (Exception ex) {
			throw new Exception("Ocorreu um problema para ler os dados da Fonte de Origem");
		}
		
		if (entrada != null) {
			String saida = transformaDados(entrada);
			
			try {
				fonteDestino.gravaDados(saida);
			} catch (Exception e) {
				throw new Exception("Não foi possível enviar os dados de saída para a Fonta de Destino");
			}
		}
		
	}

	private String transformaDados(String entrada) {
		if( "[HoraAtual]".equals(entrada)) {
			return "hora atual: " + System.currentTimeMillis();
		}
		return entrada;
	}

}
