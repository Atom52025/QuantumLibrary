# Quantum Library: Desarrollo de una aplicación para la gestión de colecciones multimedia

La aplicación se divide en backend y frontend. El backend desarrollado en Spring Boot expone una API que usara nuestro
frontend desarrollado en Next.JS.
Ambas partes se encuentran en este repositorio, dentro de la carpeta src encontramos app, donde se encuentra el front y
la carpeta main donde se encuentra el back.

## Tecnologias usadas

### Front

- NextJs
- Tailwind
- Next UI
- Framer Motion
- Next Auth
- React Icons

### Back

- Prettier
- SonarLint
- Lombok
- Spring boot
- MapStruct
- Jwt
- MySql

## Lanzamiento de la aplicacion

Para lanzar la aplicación completa sera necesario levantar ambos servicios pero podemos levantar cada uno por separado.
Estan configurados 3 scripts para los distintos tipos de levantamiento.

- `npm run frontend`: levanta el servicio del frontend
- `npm run backend`: levanta el servicio del backend
- `npm run start`: levanta ambos servicios a la vez

Deberan definirse 3 variables de entorno.

- `SB_SECURITY_PASSWORD`: Contraseña de spring security arbitraria (Ej: "Password")
- `STEAM_API_KEY`: Api Key de Steam, sera necesario solicitar una personal
  en [Steam Api](https://steamcommunity.com/dev/apikey)
- `STEAMDB_API_KEY`: Api Key de Steam Grid Data Base, sera necesario solicitar una personal
  en [SGBD](https://www.steamgriddb.com/profile/preferences)

Para lanzarlo en local tambien sera necesario levantar una mysql y establecer su ruta y contraseña dentro de <ins>
...src/main/resources/application-dev.properties </ins>


