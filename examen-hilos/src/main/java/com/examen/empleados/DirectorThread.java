package com.examen.empleados;

import java.util.ArrayList;
import java.util.List;

public class DirectorThread extends EmpleadoThread{

	// Los Directores tienen 2 segundos de penalidad para ponerse en tema antes de resolver la llamada.
	private static final long tiempo_ponerse_tema  = 2000; 
	
	// Lista de Operadores que supervisa
	private List<SupervisorThread> supervisores = new ArrayList<SupervisorThread>();
	
	
	public DirectorThread(String nombre) {
		super(nombre);
	}

	public DirectorThread(int numero) {
		this("Director_" + numero);
	}

	@Override
	protected long getTiempoDeTarea() {
		return tiempo_ponerse_tema + this.llamada.getProblema();
	}

	public List<SupervisorThread> getSupervisores() {
		return supervisores;
	}

	public void setSupervisores(List<SupervisorThread> supervisores) {
		this.supervisores = supervisores;
	}

}
