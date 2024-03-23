# Proyecto "La Mina"

Este proyecto simula el funcionamiento de una mina automatizada donde varios tipos de robots colaboran para extraer y transportar beepers desde una veta hasta bodegas en la superficie. El proyecto hace uso del concepto de paralelismo con bloqueos para coordinar las acciones de los robots de manera eficiente.

## Descripción del Proyecto

El proyecto "La Mina" implementa un sistema automatizado para la extracción y transporte de beepers en una mina simulada. Los robots, divididos en tres tipos (mineros, trenes y extractores), realizan tareas específicas de acuerdo a las siguientes reglas:

1. **Reglas del Mundo de la Mina**:
   - Los robots no pueden ocupar la misma posición en el mundo de la mina simultáneamente. Si esto ocurre, ambos robots se deshabilitan.
   - Las tareas se realizan en áreas designadas: entrada, veta, punto de extracción y bodegas en la superficie.
   - Los beepers representan el material a extraer de la veta.

2. **Tipos de Robots y Funciones**:
   - **Minero (Color Negro)**:
     - Extrae beepers de la veta y los transporta al punto de transporte.
     - Espera en una posición designada si otro minero está trabajando en la veta.
     - Informa a los trenes si la veta se ha terminado.
     - Cantidad máxima: 2, Cantidad mínima: 1.
   - **Trenes (Color Azul)**:
     - Transportan los beepers desde el punto de transporte al punto de extracción.
     - Esperan si no hay suficientes beepers pero los mineros están trabajando en la veta.
     - Cantidad máxima: 16, Cantidad mínima: 1.
   - **Extractores (Color Rojo)**:
     - Transportan los beepers desde el punto de extracción a las bodegas en la superficie.
     - Informan a los demás robots cuando pueden salir de la mina.
     - Cantidad máxima: 4, Cantidad mínima: 1.

3. **Argumentos del Programa**:
   - Se pueden especificar la cantidad de cada tipo de robot al iniciar el programa.

## Ejecución del Programa

Para ejecutar el programa, sigue los siguientes pasos:

1. Clona o descarga este repositorio en tu máquina local.
2. Asegúrate de tener instalado Java JDK 8 o superior.
3. Importa el proyecto en tu IDE preferido.
4. Configura los argumentos del programa según sea necesario para definir la cantidad de cada tipo de robot.
5. Ejecuta el programa utilizando la línea de comandos. Asegúrate de estar en el directorio raíz del proyecto y utiliza el siguiente comando:

```bash
java Minero -m 2 -t 5 -e 2
