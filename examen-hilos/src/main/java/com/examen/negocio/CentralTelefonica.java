package com.examen.negocio;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

import org.apache.log4j.BasicConfigurator;

import com.examen.empleados.DirectorThread;
import com.examen.empleados.EmpleadoThread;
import com.examen.empleados.OperadorThread;
import com.examen.empleados.SupervisorThread;

public class CentralTelefonica {

	private Queue<EmpleadoThread> empleadosTotales = new ConcurrentLinkedQueue<EmpleadoThread>();
	
	@SuppressWarnings("unused")
	private Dispatcher dispatcher; 

	
	/**
	 * 
	 * @param cantOperadores cantidad de Operadores a crear 
	 * @param cantSuper		 cantidad de Supervisores a crear	
	 * @param cantDirector   cantidad de Directores a crear
	 */
	public CentralTelefonica(int cantOperadores, int cantSuper, int cantDirector) {
		BasicConfigurator.configure();
		IntStream.range(1, cantOperadores+1)
				.forEach(i -> this.empleadosTotales.add(new OperadorThread(i)));
		IntStream.range(1, cantSuper+1)
				.forEach(i -> this.empleadosTotales.add(new SupervisorThread(i)));
		IntStream.range(1, cantDirector+1)
				.forEach(i -> this.empleadosTotales.add(new DirectorThread(i)));
		this.dispatcher = new Dispatcher(empleadosTotales);	
	}
	
	//Cargara valores por defecto de cantidad de empleados, mejorable con archivo .properties
	// 3 Operadores
	// 2 Supervisores
	// 1 Director
	public CentralTelefonica() {
		this(3, 2, 1);
	}		
	
	
	public void resolverLlamada(Llamada llamada) {
		this.dispatcher.dispatchCall(llamada);
	}
	
	
	public void resolverLoteDeLlamadas(List<Llamada> llamadas) {
		this.dispatcher.procesarLoteLlamas(llamadas);
	}
	
	public Queue<Llamada> getLlamadasProcesadas() {
		return this.dispatcher.getLlamadasProcesadas();
	}
	
}
