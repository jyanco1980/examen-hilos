Lo realizado resuelve los puntos principales como así también los "extras"

	La solución puede atender un máximo de 10 llamadas de forma concurrente/hilos.
	La misma dispone de 2 formas de atención las llamadas, de a una o por lote.
		Si es de a una, todas serán atendidas ya sea de manera inmediata por uno de los 10 hilos disponibles o pasaran 
			a estar en una cola y serán atendidas cuando alguno de los empleados se libere.
		
	Si las llamadas entran por lote, entonces se propuso que se dividan las mismas dejando solo 10 que serán procesadas y 
	las restantes son enviadas a otra clase "Retenciones" (de otra área de la empresa) la cual debería darle un tratamiento 
	en otro momento.
		
	Se solicito que el tiempo de llamadas sea de 5 a 10 segundos y para darle una "utilidad" a las distintas clases que heredan 
	del tipo "EmpleadoThread" se determino que los Directores tienen una penalidad de 2 segundos para ponerse
	en tema (antes de resolver la llamada) y los supervisores la penalidad es de 1segundo.
	Como el random de atención varia de 5 a 8 segundos queda definido asi:
		Los Operadores demoraran en resolver una llamada entre 5 a 8 segundos.
		Los Supervidores entre 6 y 9 segundos.
		Los Directores de 7 a 10 segundos.
	
	La cantidad de Empleados (operadores/supervisores/directores) puede ser menor a 10, por ende la de llamadas
	que se atiendan en forma paralela/concurrente dependerá de la cantidad de llamadas y la de empleados. 
	De todas formas como la cantidad máxima de hilos son 10, nunca podrán trabajar más de 10 llamadas en paralelo,
	Si excede las 10 llamadas de forma individual estas serán puesta en una cola y atendida a medida que los empleados estén disponibles.
	
	La atención de las llamadas siempre será resuelta primero por los "operadores" si no hay disponible la atenderá los "supervisores" y 
	por ultimo los "directores".
	
	Hay comentarios tanto en los métodos como en los 4 casos de Test que describen que hacer y verifica cada uno de los mismos.
