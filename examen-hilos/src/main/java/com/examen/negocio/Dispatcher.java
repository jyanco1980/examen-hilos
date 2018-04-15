package com.examen.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.examen.empleados.DirectorThread;
import com.examen.empleados.EmpleadoThread;
import com.examen.empleados.OperadorThread;
import com.examen.empleados.SupervisorThread;


public class Dispatcher {
	
	private Queue<Llamada> llamadasPendientes = new ConcurrentLinkedQueue<Llamada>();
	private Queue<EmpleadoThread> empleadosTotales = new ConcurrentLinkedQueue<EmpleadoThread>();

	private Queue<Llamada> llamadasProcesadas = new ConcurrentLinkedQueue<Llamada>();
	private final Logger logger = LogManager.getRootLogger();

	//se crea un pool con la capacidad maxima solicitda. 
	//"capacidad de poder procesar 10 llamadas al mismo tiempo (de modo concurrente)"	
	private ExecutorService pool = Executors.newFixedThreadPool(10); 
	
	public Dispatcher(Queue<EmpleadoThread> empleadosTotales) {
		this.empleadosTotales.addAll(empleadosTotales);
	}
	

	/**
	 * Recibe lote de llamadas y luego de enviar a filtrar las correspondientes manda a asignar cada llamada 
	 * a un empleado para que la atienda.
	 * @param llamadas
	 */
	public void procesarLoteLlamas(List<Llamada> llamadas){
		List<Llamada> llamadasAProcesar= filtrarTope10Llamadas(llamadas);
		for (Llamada llamada :  llamadasAProcesar) {
			dispatchCall(llamada);
		}
		logger.debug("Sale procesarLoteLlamas");
	}

	/**
	 *	Proceso el cual asigna a una llamada que llega por parametro un empleado disponible y estos mismos
	 *	cuando finalizan su llado se los envia al proceso que verifica si quedan llamadas pendientes sin atender.
	 *	En caso de no tener empleados disponibles guarda la llamada en una cola. 
	 * 
	 * @param llamada
	 */
	public void dispatchCall(Llamada llamada) {
		EmpleadoThread empleado =getEmpleado();
		if(empleado==null) {
			llamadasPendientes.add(llamada);	
		}else {
			empleado.setLlamada(llamada);
			llamada.setNombreEmpleadoAtendio(empleado.getNombre());
			llamadasProcesadas.add(llamada);
			CompletableFuture<EmpleadoThread> compfuture = CompletableFuture.supplyAsync(empleado::get,pool);
		    compfuture.thenAccept(this::buscarLlamadasPendientes);
		  }
	}

	/**
	 * Proceso que busca en la cola de llamadas pendientes y asigna una llamada al cliente que recibe por parametro.
	 * En caso de estar la cola vacia finaliza el pool de conexiones.
	 *  
	 * @param empleado
	 */
	private void buscarLlamadasPendientes(EmpleadoThread empleado) {
		if(!llamadasPendientes.isEmpty()) {
			logger.info(empleado.getNombre() + " busca llamada desde cola de pendientes");
			dispatchCall(llamadasPendientes.poll()); 
		}else {
			pool.shutdown();
		}
	}

	/**
	 * 
	 * Proceso que filtra un lote de llamada y deja disponible solo 10 de ellas para ser procesadas
	 * el resto es derivada al area de Retenciones.
	 * 
	 * @param llamadasTotales
	 * @return List<Llamada>
	 */
	private List<Llamada> filtrarTope10Llamadas(List<Llamada> llamadasTotales){
		List<Llamada> llamadasMenor10 =  new ArrayList<>(10);
		if(llamadasTotales.size()>10) {
			Map<Boolean, List<Llamada>> divisionLlamadas = llamadasTotales.stream()
					.collect(Collectors.partitioningBy(it -> llamadasTotales.indexOf(it) < 10));
			
			llamadasMenor10 = divisionLlamadas.get(true);
			List<Llamada> llamadasSuperaDisponibilidad = divisionLlamadas.get(false);
			Retenciones.getSingletonInstance().agruparLlamadasNoProcesadas(llamadasSuperaDisponibilidad);
		}else {
			llamadasMenor10=llamadasTotales;
		}
		return llamadasMenor10;
	}	
	
	/**
	 * Retorna un Empleado siempre que exista alguno disponible.
	 * El orden de retorno es Primero del tipo Operador, si no tiene o estan todos ocupados devuelve del tipo 
	 * Supervidor si tampoco tiene o estan todos ocupados 
	 * busca del tipo Director.
	 * 
	 * @return EmpleadoThread 
	 */
	private synchronized EmpleadoThread getEmpleado(){
		Optional<EmpleadoThread> empleadoThread =
				this.empleadosTotales.stream()
				.filter(empl -> empl instanceof OperadorThread
						&& empl.isEstaDisponible()).findFirst();
		if (empleadoThread.isPresent()){
			empleadoThread.get().setEstaDisponible(false);
			return empleadoThread.get();
		}
		empleadoThread =
				this.empleadosTotales.stream()
				.filter(empl -> empl instanceof SupervisorThread
						&& empl.isEstaDisponible()).findFirst();
		if (empleadoThread.isPresent()){
			empleadoThread.get().setEstaDisponible(false);
			return empleadoThread.get();
		}
		empleadoThread =
				this.empleadosTotales.stream()
				.filter(empl -> empl instanceof DirectorThread
						&& empl.isEstaDisponible()).findFirst();
		if (empleadoThread.isPresent()){
			empleadoThread.get().setEstaDisponible(false);
			return empleadoThread.get();
		}
		return null;
	}	
	
	/**
	 * Retorna una cola de llamadas que fueron procesadas con exito.
	 * @return Queue<Llamada> 
	 */
	public Queue<Llamada> getLlamadasProcesadas() {
		return llamadasProcesadas;
	}
	
}

