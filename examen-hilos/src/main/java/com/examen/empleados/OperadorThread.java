package com.examen.empleados;

public class OperadorThread extends EmpleadoThread {

	private static final long tiempo_ponerse_tema = 0; // los operadores les lleva 0 segundos en ponerse en tema

	public OperadorThread(String nombre) {
		super(nombre);
	}

	public OperadorThread(int numero) {
		this("Operador_"+ numero);
	}

	@Override
	protected long getTiempoDeTarea() {
		return tiempo_ponerse_tema + this.llamada.getProblema();
	}
}
