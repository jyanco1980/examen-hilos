package com.examen.empleados;

import java.util.function.Supplier;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.examen.negocio.Llamada;


public abstract class EmpleadoThread implements Supplier<EmpleadoThread>{
	
	private final Logger logger = LogManager.getRootLogger();
	protected Llamada llamada;
	private boolean estaDisponible;
	private String nombre;

	
	public EmpleadoThread(String nombre) {
		this.nombre=nombre;
		estaDisponible=true;
	}
	
	public boolean isEstaDisponible() {
		return estaDisponible;
	}

	public void setEstaDisponible(boolean estado) {
		this.estaDisponible = estado;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Llamada getLlamada() {
		return llamada;
	}

	public void setLlamada(Llamada llamada) {
		this.llamada = llamada;
	}
	
	
	public EmpleadoThread get() {
		logger.trace("se llama al get del empleado");
		logger.info("Info del proceso, id cliente: " + llamada.getIdCliente() + " atendido por: "+ this.nombre);
		realizarTarea();
		this.setEstaDisponible(true);
		logger.info(this.nombre + " termine, Paso a estar disponible");
		return this;
	}
	
	protected long getTiempoDeTarea() {
		return this.llamada.getProblema();
	}	
	
	
	protected void realizarTarea() {
		try {
			logger.trace("Comienzo resolucion de llamada");
			Thread.sleep(getTiempoDeTarea());
		} catch (InterruptedException e) {
			logger.error("Problemas en la atención de la llamada ", e);
		}
		logger.trace("Fin  resolucion de llamada");
	}
}
