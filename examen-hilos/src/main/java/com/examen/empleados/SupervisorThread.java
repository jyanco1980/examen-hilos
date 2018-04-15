package com.examen.empleados;

import java.util.ArrayList;
import java.util.List;

public class SupervisorThread extends EmpleadoThread{

	// Los Supervisores tienen 1 segundos de penalidad para ponerse en tema antes de resolver la llamada.
	private static final long tiempo_ponerse_tema = 1000; 

	// Lista de Operadores que supervisa
	private List<OperadorThread> operadores = new ArrayList<OperadorThread>();
	
	public SupervisorThread(String nombre) {
		super(nombre);
	}

	public SupervisorThread(int numero) {
		this("Supervisor_" + numero);
	}
	
	@Override
	protected long getTiempoDeTarea() {
		return tiempo_ponerse_tema + this.llamada.getProblema();
	}

	public List<OperadorThread> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<OperadorThread> operadores) {
		this.operadores = operadores;
	}
}
