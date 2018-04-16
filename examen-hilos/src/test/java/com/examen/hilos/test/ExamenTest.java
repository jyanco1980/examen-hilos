package com.examen.hilos.test;


import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.examen.negocio.CentralTelefonica;
import com.examen.negocio.Llamada;
import com.examen.negocio.Retenciones;

/**
 * Created by Jonathan 14/04/2018
 */
public class ExamenTest {
	List<Llamada> llamadas;

	public ExamenTest() {
		this.llamadas = new ArrayList<>();
	}

	@Before
	public void beforeEachTest() throws Exception {
		llamadas = new ArrayList<>();
	}

	/**
	 * 1 Test 
	 * 
	 * Se envia a procesar 10 llamdas de forma concurrente en modalidad lote.
	 * 7 llamadas son tomadas por los 7 Operadores, 2 por los 2 supervisores y una por el director.
	 * Se confirma que las 10 llamadas son procesadas (ninguna queda en "no procesada") en forma concurrente. 
	 *	
	 * Verifica:
	 *  	"Tener la capacidad de poder procesar 10 llamadas al mismo tiempo (de modo concurrente)"
	 * 		"Debe tener un test unitario donde lleguen 10 llamadas"
	 */
	
	@Test
	public void testUnoCall() throws Exception {
		CentralTelefonica centralTelefonica = new CentralTelefonica(7,2,1);
		int cantidadLlamadas = 10;
		IntStream.range(1, cantidadLlamadas+1)
						.forEach(i -> this.llamadas.add(new Llamada("cliente n° " + String.valueOf(i), i)));
		centralTelefonica.resolverLoteDeLlamadas(llamadas);
		sleep(15000); //tiempo para que termine de procesar todo
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_1");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_2");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_3");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_4");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_5");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_6");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_7");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Supervisor_1");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Supervisor_2");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Director_1");
		assertEquals(Retenciones.getSingletonInstance().getLlamadasNoProcesadas().size(),0);
	}
		

	/**
	 * 2 Test
	 * Se envian 2 llamadas en lote, pero hay disponible 1 solo empleado del tipo Operador,  
	 * por ende se atiende un llamado y el otro queda en espera hasta que el operador este nuevamente disponible
	 * 
	 * Verifica :
	 * 		Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre
	 */
	
	@Test
	public void testDosCall() throws Exception {
		CentralTelefonica centralTelefonica = new CentralTelefonica(1,0,0);
		int cantidadLlamadas = 2;
		IntStream.range(1, cantidadLlamadas+1)
						.forEach(i -> this.llamadas.add(new Llamada("cliente n° " + String.valueOf(i), i)));
		centralTelefonica.resolverLoteDeLlamadas(llamadas);
		sleep(15000);
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_1");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_1");
		assertEquals(Retenciones.getSingletonInstance().getLlamadasNoProcesadas().size(),0);
	}
	

	/**
	 * Test 3
	 * Se envian 15 llamadas en lote, de las cuales solo se pueden procesar un maximo de 10. 
	 * 			(Se entiende que solo se pueden procesar 10 llamadas cuando es por del tipo Lote, 
	 * 			Si se desea modificar esta restriccion se quita el filtro "filtrarTope10Llamadas" y se procesaran todas, pasando a una cola de pendiente 
	 * 			las que no se puedan atender en primera instancia)
	 *  
	 * Las 5 restantes (no procesadas) se derivan a otra funcionalida del area de retenciones la cual 
	 * debera comunicarse con el cliente luego (es para evitar espera prolongada al cliente)
	 * 
	 * Se utiliza solamente 3 Operadores,1 Supervisor, por lo tanto solo 4 llamadas son atendidas de modo concurrente e inmediatamente 
	 * y las otras 6 queden en espera ya que tienen a sus empleados ocupados.
	 * Luego las mismas son tambien procesadas a medida que los empleados se liberan.
	 *   
	 * Se comprueba que 5 llamadas son "no procesadas" y 10 Procesadas.
	 * 
	 * Verifica:
	 * 		Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes (lote)
	 * 		Debe tener un test unitario donde lleguen 10 llamadas
	 *  	Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre
	 */
	@Test
	public void testTresCall() throws Exception {
		CentralTelefonica centralTelefonica = new CentralTelefonica(3,1,0);
		int cantidadLlamadas = 15;
		IntStream.range(0, cantidadLlamadas)
						.forEach(i -> this.llamadas.add(new Llamada("cliente n° " + String.valueOf(i), i)));
		centralTelefonica.resolverLoteDeLlamadas(llamadas);
		sleep(15000);
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_1");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_2");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Operador_3");
		assertEquals(centralTelefonica.getLlamadasProcesadas().poll().getNombreEmpleadoAtendio(), "Supervisor_1");
		assertEquals(Retenciones.getSingletonInstance().getLlamadasNoProcesadas().size(),5);
	}	
	
	/**
	 * Test 4
	 * Se puede llamar al metodo dispatchCall desde distintos hilos y la llamda es asignada al empleado que este disponible
	 * Se comprueba que puede recibir más de 10 llamadas en forma paralela (no proceso lote, que solo tomara 10 llamadas maximo)
	 * Se verifica que a pesar de disponer solo de 10 empleados, las 20 llamadas son atendidas quedando ninguna como "no procesada". 
	 * 
	 * Verifica
	 * 	El método dispatchCall puede invocarse por varios hilos al mismo tiempo
	 * 	Tener la capacidad de poder procesar 10 llamadas al mismo tiempo (de modo concurrente)
	 *  Dar alguna solución sobre qué pasa con una llamada cuando no hay ningún empleado libre
	 *  Dar alguna solución sobre qué pasa con una llamada cuando entran más de 10 llamadas concurrentes (distintos hilos)
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testCuatroCall() throws Exception {
		CentralTelefonica centralTelefonica = new CentralTelefonica(6,3,1);
		
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 1", 1));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 2", 2));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 3", 3));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 4", 4));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 5", 5));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 6", 6));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 7", 7));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 8", 8));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 9", 9));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 10",10));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 11",11));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 12",12));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 13",13));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 14",14));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 15",15));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 16",16));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 17",17));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 18",18));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 19",19));
		centralTelefonica.resolverLlamada(new Llamada("cliente n° 20",20));
		sleep(30000);
		assertEquals(Retenciones.getSingletonInstance().getLlamadasNoProcesadas().size(),0);
	}	
		
		

}