# Proyecto de lista de tareas a completar
Este proyecto se utiliza como base para la introducción de conceptos de desarrollo de servicios Web REST en clase.

# Construir el proyecto
Aunque todo se puede hacer desde el IDE, al ser un proyecto maven la forma más rápida de construirlo es ejecutar el siguiente comando en la consola, estando en el directorio donde se encuentra el fichero `pom.xml`.
```bash
mvn package
```
Maven puede instalarse siguiendo las instrucciones de la [página oficial de Maven](https://maven.apache.org/install.html).

# Ejecutar el proyecto
Una vez construido el proyecto podemos encontrar un fichero de extensión `jar` en el directorio `target`. 
Para ejecutar el servicio debemos escribir (asumiendo que estamos en el directorio donde se encuentra el fichero `pom.xml`:
```bash
java -jar target/todolist-0.0.1-SNAPSHOT.jar
```

# Probar el servicio
El servicio arranca sin tareas en memoria. Para añadir una tarea podemos hacer un POST al endpoint `/api/tareas`.  Se puede usar [Postman](https://www.postman.com) para ello. Alternativamente se puede utilizar el comando `curl` disponible
en muchos sistemas operativos. Un ejemplo con `curl` es el siguiente:
```bash
curl --location 'http://localhost:8080/api/tareas' \
--header 'Content-Type: application/json' \
--data-raw '{
    "titulo": "Estudiar DAW",
    "completada": false,
    "prioridad": "alta"
}'
```

Podemos consultar la lista completa de tareas con:
```bash
curl --location 'http://localhost:8080/api/tareas' \
--header 'Accept: application/json'
```

También se puede importar a Postman el fichero `todolist.postman_collection.json` incluido en el repositorio. Este fichero contiene una colección de peticiones HTTP a los diferentes endpoints de la aplicación.
