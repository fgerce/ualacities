# Arquitectura

La aplicación sigue los principios de **Clean Architecture**, dividiéndose en las siguientes capas:

1. **Domain**: Contiene las reglas de negocio y casos de uso. No depende de ninguna librería o framework.
2. **Data**: Se encarga de obtener y procesar los datos, ya sea desde la API o el almacenamiento local.
3. **Presentation**: Implementa la UI utilizando Jetpack Compose y maneja la lógica de presentación a través de `ViewModel`.

Se utiliza **Hilt** para la inyección de dependencias, lo que permite desacoplar módulos y facilitar las pruebas.


# City Search Engine

## Enfoque para resolver el problema de búsqueda

Para optimizar la búsqueda dentro de un conjunto de aproximadamente 200,000 ciudades, implementé una estructura `Trie` como motor de búsqueda. Este enfoque penaliza la carga inicial a cambio de ofrecer búsquedas extremadamente rápidas, asegurando una experiencia fluida para el usuario.

### Justificación de la estructura de datos elegida

El `Trie` es una estructura de árbol optimizada para búsquedas basadas en prefijos. Su principal ventaja es que permite encontrar coincidencias en **O(M)**, donde **M** es la longitud de la consulta, independientemente del tamaño del conjunto de datos. En contraste, una búsqueda lineal tradicional tendría una complejidad de **O(N)**, donde **N** es el número de ciudades, lo que sería inviable en nuestro caso.

En resumen:
- **Carga inicial (Construcción del Trie)**: **O(N * M)** (penalización controlada)
- **Búsqueda por prefijo**: **O(M)** (independiente de la cantidad total de ciudades)

Dado que el desafío no establecía restricciones sobre el tiempo de carga inicial, se optó por maximizar la eficiencia en la búsqueda.

## Optimización de memoria

Las primeras implementaciones del `Trie` presentaron problemas de consumo de memoria debido a:
1. **Uso de objetos grandes**: Se estaban utilizando clases de datos personalizadas para representar los nodos del árbol, lo que generaba una alta sobrecarga de memoria.
2. **Uso de tipos no optimizados**: Se empleaban tipos de datos como `Integer` en lugar de tipos primitivos.

Para solucionar estos problemas:
- Se reemplazaron las estructuras de datos complejas por estructuras más compactas.
- Se priorizó el uso de **tipos primitivos** (`Char`, `Boolean`, `Array`) en lugar de objetos innecesarios.

Como resultado, la nueva implementación redujo significativamente el consumo de memoria sin afectar la velocidad de búsqueda.

## Conclusión

Este enfoque logra un balance óptimo entre carga inicial y velocidad de búsqueda, asegurando que la experiencia del usuario sea fluida incluso con grandes volúmenes de datos. El `Trie` permite buscar eficientemente sin depender del tamaño del dataset, y las optimizaciones en la gestión de memoria evitaron problemas de rendimiento en dispositivos con menos recursos.

## Resumen de la solución

1. **Interfaz optimizada**: La UI se diseñó en Jetpack Compose, asegurando rendimiento y flexibilidad.
2. **Búsqueda en tiempo real**: Gracias al `Trie`, los resultados aparecen de manera instantánea sin bloquear la UI.
3. **Arquitectura limpia y modular**: Separación de responsabilidades siguiendo Clean Architecture y SOLID principles.
4. **Uso eficiente de memoria**: Implementación optimizada para manejar grandes volúmenes de datos sin problemas.

Esta solución garantiza una experiencia fluida y escalable, permitiendo futuras mejoras sin afectar el rendimiento.
