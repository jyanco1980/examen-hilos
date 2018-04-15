package com.examen.negocio;

import java.util.Random;
/**
 * Representa una llamada del cliente y registra, el numero del cliente, el nombre y el empleado que la atendio.
 * @author Jonathan 14/04/2018
 *
 */
public class Llamada {
	

	private String nombreCliente;
	private int idCliente;
	private String nombreEmpleadoAtendio;

	public Llamada(String nombreCliente, int idCliente) {
		this.idCliente = idCliente;
		this.nombreCliente = nombreCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombreEmpleadoAtendio() {
		return nombreEmpleadoAtendio;
	}

	public void setNombreEmpleadoAtendio(String nombreEmpleadoAtendio) {
		this.nombreEmpleadoAtendio = nombreEmpleadoAtendio;
	}

	/**
	 * Calcula aleatoriamente el tiempo que lleva resolver la llamada 
	 * El tiempo oscila entre 5 y 8 segundos. 
	 * Los 10 segundos solicitados se pueden dar por la penalidad segun el tipo de Empleado:
	 * 		Si es del tipo Supervisor tienen una penalidad de 1 segundo para ponerse "en tema". 
	 * 		Si es del tipo Director tienen una penalidad de 2 segundo para ponerse "en tema".
	 * 
	 * @return long
	 */
	public long getProblema() {
		Random rnd = new Random();
		int tiempoBase = rnd.nextInt(8000 - 5000 + 1) + 5000;
		return tiempoBase;
	}

}
