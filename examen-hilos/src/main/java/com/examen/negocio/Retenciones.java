package com.examen.negocio;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Retenciones {
	/**
	 * Clase que debe ser administrada por otro area la cual debe recoger periodicamente las llamadas que no pudieron
	 * ser procesadas por superar el permitido de 10 llamadas por lote. 
	 */
	

	private static Retenciones retenciones = new Retenciones();
	private List<Llamada> llamadasNoProcesadas = new ArrayList<Llamada>();
	private final Logger logger = LogManager.getRootLogger();
	
	private Retenciones() {}

	public static Retenciones getSingletonInstance() {
		return retenciones;
	}
	
	
	public synchronized void agruparLlamadasNoProcesadas(List<Llamada> llamadas) {
		llamadasNoProcesadas.addAll(llamadas);
		logger.info("Se supero la capacidad maxima de 10 llamadas concurrentes estas fueron derivadas al area de retencion para su posterior resolucion");
		llamadas.forEach(llamada -> logger.info("info llamada que excede capacidad :" + llamada.getNombreCliente() ));
	}

	public synchronized  List<Llamada> getLlamadasNoProcesadas() {
		return llamadasNoProcesadas;
	}

	public synchronized void limpiarLlamadasNoProcesadas() {
		llamadasNoProcesadas.clear();
	}

}
