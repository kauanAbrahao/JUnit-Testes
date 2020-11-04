package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.DivisaoPorZeroException;

public class Calculadora {

	public int somar(int a, int b) {
		
		return a+b;
	}

	public int sub(int a, int b) {
		
		return a-b;
	}

	public double div(double a, double b) throws DivisaoPorZeroException {
		
		if(b == 0) {
			throw new DivisaoPorZeroException("Divisão por 0");
		}
		
			
		return(a/b);
	}
	
}
