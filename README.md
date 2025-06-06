# base-de-datos-2
Trabajo práctico de la materia Base de Datos 2


Para crear la base de datos y el usuario:

```js
use bd2_grupo_21;
db.createUser({
    user: "bd2",
    pwd: "asdasd",
    roles: [{role: "readWrite", db: "bd2_grupo_21"}]
});
```

Para correr los tests, ejecutar en la raiz del proyecto:

```bash
mvn clean install
```