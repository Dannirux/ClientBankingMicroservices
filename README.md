# Configuración del Proyecto con Docker Compose

Este proyecto está diseñado para ser ejecutado utilizando Docker Compose. La configuración permite la flexibilidad de utilizar una o dos bases de datos dependiendo de los requisitos del entorno.

## Requisitos previos

- Docker instalado en tu máquina.
- Docker Compose instalado.

## Configuración

El archivo `docker-compose.yml` está preparado para soportar uno o dos servicios de base de datos, dependiendo de la configuración que necesites:

1. **Una sola base de datos**: Ambos servicios, tanto la aplicación como los scripts asociados, utilizarán la misma base de datos para todas las operaciones.
2. **Dos bases de datos**: Una base será utilizada exclusivamente para almacenar los datos de clientes, mientras que la otra manejará el resto de las operaciones. Esto se controla ajustando los servicios y volúmenes dentro del archivo `docker-compose.yml`.

Se deben llenar: o solo ocupar una sola para ambas.
MYSQL_DATABASE: ms_person_client puerto 33306
MYSQL_DATABASE: ms_accounts_movements puerto 3307

### Notas importantes
- Si decides usar dos bases de datos, asegúrate de actualizar el archivo de configuración del script para indicar cuál base de datos manejará los datos de clientes.
- El script detectará automáticamente si hay una o dos bases de datos configuradas y realizará las operaciones en consecuencia.

## Pasos para la ejecución

1. **Clona o descarga este repositorio**: 
   Asegúrate de que los archivos se encuentren en una carpeta accesible en tu máquina.

2. **Ubícate en la carpeta del proyecto**: 
   ```bash
   cd <carpeta-del-proyecto>
   ```

3. **Construye los servicios**: 
   Ejecuta el siguiente comando para construir las imágenes necesarias:
   ```bash
   docker-compose build
   ```

4. **Inicia los servicios**: 
   Levanta los contenedores con:
   ```bash
   docker-compose up
   ```

5. **Accede a los logs (opcional)**: 
   Puedes monitorear los logs de los servicios para asegurarte de que todo esté funcionando correctamente:
   ```bash
   docker-compose logs -f
   ```

## Personalización

Si deseas cambiar la configuración para usar una o dos bases de datos:

1. Abre el archivo `docker-compose.yml`.
2. Modifica las secciones correspondientes a los servicios de base de datos (`db1`, `db2`) según sea necesario.
3. Asegúrate de que los scripts en el contenedor de la aplicación estén configurados para conectarse correctamente a la(s) base(s) de datos especificada(s).

## Limpieza

Si deseas detener y eliminar los contenedores junto con las redes asociadas, usa:
```bash
docker-compose down
```

---

Si tienes preguntas o necesitas asistencia adicional, no dudes en comunicarte con el equipo de soporte del proyecto.
