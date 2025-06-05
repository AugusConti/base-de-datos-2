# Base de Datos 2 - Práctica 3

# Sección 1: Bases de Datos NoSQL y Relacionales

1. ¿Cuáles de los siguientes conceptos de RDBMS existen en MongoDB? En caso de no existir, ¿hay alguna alternativa? ¿Cuál es?

	- Base de Datos

		EXISTE

	- Tabla / Relación

		NO EXISTE. La alternativa es utilizar colecciones de documentos en formato BSON (JSON codificado en binario).

	- Fila / Tupla

		NO EXISTE. La alternativa es utilizar documentos individuales.

	- Columna

		NO EXISTE. La alternativa es utilizar atributos en los documentos, pero no se puede definir (por modelo) una estructura unificada.

2. ¿Existen claves foráneas en MongoDB? ¿Qué diferencias existen con las bases de datos de tipo relacional?

	En MongoDB existen claves foráneas, que se corresponden con el identificador interno del documento. Sin embargo, no se garantiza que este identificador sea único.

3. Para acelerar las consultas, MongoDB tiene soporte para índices. ¿Qué tipos de índices soporta?

	MongoDB soporta los diferentes tipos de índices:

	- Simple: Ordena documentos mediante un único atributo.
	- Compuesto: Ordena documentos mediante dos o más atributos, de forma secuencial (primero por uno, despues por otro, etc.).
	- Multiclave: Ordena un arreglo de documentos anidados dentro del documento.
	- Geoespacial
	- Texto
	- Hasheados

4. En MongoDB existen dos tipos de vistas, explique brevemente cuáles son y qué diferencias existen entre ellas. Además mencione algunos casos donde podría utilizarlas.

	En MongoBD, los tipos de vistas son:

	- **Vistas normales**: Funcionan de la misma forma que las vistas en bases de datos relacionales. Es decir, son consultas que se utilizan como una colección, y se ejecutan de forma dinámica.

		Se puede usar principalmente para simplificar consultas complejas, por ejemplo, para extraer una subconsulta recurrente en varias consultas.

		Normalmente, resultan útiles para consultas sobre colecciones que cambian frecuentemente.

	- **Vistas automatizadas**: Son documentos que almacenan el resultado de una consulta, y se actualizan periódicamente de forma automática.

		Se puede usar para optimizar el tiempo de la BD, ya que la consulta solo se realiza de forma periódica en lugar de ante cada petición.

		Resultan útiles si las colecciones involucradas no cambian con mucha frecuencia, lo cual asegura la consistencia de los datos almacenados durante un tiempo razonable.

5. Los documentos de una colección pueden diferir en la cantidad y sus tipos de campos, ¿existen algunas formas de validar los elementos a insertar en una colección para evitar esta disparidad?

	SI, MongoDB permite agregar validadores de esquema a las colecciones, los cuales fuerzan tipos o campos requeridos.

6. MongoDB tiene soporte para transacciones, pero no es igual que el de los RDBMS. ¿Cuál es el alcance de una transacción en MongoDB?

	En MongoDB, las operaciones por defecto son atómicas a nivel de documento.

	También permite transacciones distribuidas, que involucran varios documentos.

7. Las relaciones entre documentos en MongoDB pueden establecerse mediante documentos embebidos o referencias. Investigue cómo se implementa cada una y analice las ventajas y desventajas de cada una, comparándola con la forma estándar de establecer relaciones en una base de datos relacional.

	- **Referencias**: El documento almacena un campo con un identificador de otro documento (un campo con restricción de unicidad), de forma similar a las BBDD Relacionales.

		Facilita la consistencia, ya que cualquier cambio en el objeto relacionado involucra un único documento. Sin embargo, si se quieren datos de varios objetos, es necesario realizar JOINS.

	- **Documentos embebidos**: El documento almacena un campo con todos los datos de otro documento.

		Son mejores en performance, ya que toda la información necesaria se encuentra en un único documento (no requiere JOINS). Sin embargo, si se quiere modificar un documento embebido, se debe reflejar el cambio en todas las ocurrencias de este (MongoDB no se encarga de esta modificación).

8. Tomando como referencia el modelo de los trabajos prácticos anteriores y suponiendo que este podría mapearse a una base de datos en MongoDB, proponga algunos casos donde la relación sería conveniente ser mapeada como referencia y otros como documentos embebidos. Justifique la elección.

	**Route - Stop**: Estaría bueno que una Route conozca sus stops con documentos embebidos, ya que, muchas veces que se acceda a una Route vamos a querer usar sus stops, y la Ruta es la única que conoce a las stops, por lo que no es difícil mantener la consistencia (Solo hay que mantenerla entre las mismas rutas xq es una relación muchos a muchos). Además, cada ruta va a tener una cantidad no muy alta de stops, por lo que la lista sería bastante acotada y no va a ocupar muchísimo espacio.
	
	**Purchase - Review**: Estaría bueno que la Purchase conozca por referencia a la review, ya que no se va a usar muy seguido la Review cuando se quiere usar una Purchase.
	
	**User - Purchase**: Estaría bueno que la Purchase conozca al User por referencia, ya que un mismo User puede tener muchísimas compras asociadas. No sería conveniente que la Purchase tenga embebido a su User, ya que ese mismo User quedaría embebido múltiples veces (Escalando a medida que el sistema crece en compras), por lo que, por ejemplo, sería muy ineficiente actualizar los datos de un User en todas las réplicas.
	
	**TourGuide/Driver - Route**: Sería conveniente que la Route conozca por referencia tanto a los TourGuide como a los Driver, ya que no siempre son necesarios cuando se quiere usar una ruta.
	
	**Purchase - ItemService**: Sería conveniente que el ItemService conozca por referencia a la Purchase, ya que si bien el ItemService siempre utiliza la Purchase, esta última se va a usar de forma independiente, por lo que seríá conveniente que estén separadas, para no tener que mantener la consistencia a la hora de actualizar, por ejemplo.
	
	**Service - ItemService**: Sería conveniente que el Service conozca sus ItemServices en documentos embebidos, ya que muchas veces que usamos un Service se quieren usar los Items. Además, como cada ItemService conoce un único Service, no hay problemas de consistencia y cómo ItemService (En principio) no se va a actualizar, no habría problema de update. El único problema es que un Service puede tener una cantidad muy grande de ItemServices, por lo que la lista ocuparía bastante espacio.

# Sección 2: Operaciones CRUD básicas

Descargue la última versión de MongoDB desde el sitio oficial. Ingrese al cliente de línea de comando para realizar los siguientes ejercicios.

9. Cree una nueva base de datos llamada "tours", y una colección llamada "recorridos".

	```js
	use tours
	db.createCollection('recorridos')

	{ ok: 1 }
	```

	i. En esta nueva colección y utilizando el comando correspondiente inserte un nuevo documento (un recorrido) con los siguientes atributos:
	_{ 'nombre': "City Tour", 'precio': 200, 'stops': ['Diagonal Norte', 'Avenida de Mayo", 'Plaza del Congreso" ], "totalKm":5 }_

	```js
	db.recorridos.insertOne({nombre: 'City Tour', precio: 200, stops: ['Diagonal Norte', 'Avenida de Mayo', 'Plaza del Congreso' ], totalKm:5 })

	{
		acknowledge: true,
		insertedId: ObjectId(...)
	}
	```

	ii. Recupere la información del producto usando el comando db.products.find() (puede agregar la función .pretty() al final de la expresión para ver los datos indentados). Notará que no se encuentran exactamente los atributos que insertó. ¿Cuál es la diferencia?

	La principal diferencia es que el documento recuperado incluye un atributo "_id".

10. Agregue a la colección creada en el punto anterior, utilizando un solo comando, los documentos especificados en el documento "material_adicional_1.json". Sobre ellos realice las siguientes operaciones:

	```js
	db.recorridos.insertMany(/* Contenido del documento */)

	{
		acknowledgde: true,
		insertedIds: {
			'0': ObjectId(...),
			'1': ObjectId(...),
			...
		}
	}
	```

	i. Actualizar el recorrido "Cultural Odyssey" para que su total de kilómetros sea 12.

	```js
	db.recorridos.updateOne({nombre: 'Cultural Odyssey'}, {$set: {totalKm: 12}})

	{
		acknowledged: true,
		insertedId: null,
		matchedCount: 1,
		modifiedCount: 1,
		upsertedCount: 0
	}
	```

	ii. Actualizar el listado de Stops del recorrido con nombre "Delta Tour" para agregar "Tigre".

	```js
	db.recorridos.updateOne({nombre: 'Delta Tour'}, {$push: {stops: 'Tigre'}})

	{
		acknowledged: true,
		insertedId: null,
		matchedCount: 1,
		modifiedCount: 1,
		upsertedCount: 0
	}
	```

	iii. Aumentar un 10% el precio de todos los recorridos.

	```js
	db.recorridos.updateMany({}, {$mul: {precio: 1.10}})

	{
		acknowledged: true,
		insertedId: null,
		matchedCount: 15,
		modifiedCount: 15,
		upsertedCount: 0
	}
	```

	iv. Eliminar el recorrido con nombre 'Temporal Route"

	```js
	db.recorridos.deleteOne({nombre: 'Temporal Route'})

	{
		acknowledged: true,
		deletedCount: 1
	}
	```

	v. Crear el array de etiquetas (tags) para la ruta "Urban Exploration". Agregue el elemento "Gastronomía" a dicho arreglo.

	```js
	db.recorridos.updateOne({nombre: 'Urban Exploration'}, {$set: {tags: ['Gastronomía']}})

	{
		acknowledged: true,
		insertedId: null,
		matchedCount: 1,
		modifiedCount: 1,
		upsertedCount: 0
	}
	```

11. Sobre la colección generada en el punto anterior realice las siguiente consultas utilizando la función find( )

	i. Obtenga la ruta con nombre "Museum Tour"

	```js
	db.recorridos.find({nombre: 'Museum Tour'})

	{
		_id: ObjectId(...),
		nombre: 'Museum Tour',
		precio: 605,
		stops: [
			'Museo Nacional de Bellas Artes',
			'Teatro Colón',
			...
		],
		totalKm: 13
	}
	```

	ii. Las rutas con precio superior a $600

	```js
	db.recorridos.find({precio: {$gt: 600}})

	[
		{
			_id: ObjectId(...),
			nombre: 'Delta Tour',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Artistic Journey',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Gastronomic Delight',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Museum Tour',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Cultural Odyssey',
			...
		}
	]
	```

	iii. Las rutas con precio superior a $500 y con un total de kilómetros mayor a 10.

	```js
	db.recorridos.find({precio: {$gt: 500}, totalKm: {$gt: 10}})

	[
		{
			_id: ObjectId(...),
			nombre: 'Architectural Expedition',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Artistic Journey',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Museum Tour',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Cultural Odyssey',
			...
		}
	]
	```

	iv. Las rutas que incluyan el stop "San Telmo"

	```js
	db.recorridos.find({stops: 'San Telmo'})

	[
		{
			_id: ObjectId(...),
			nombre: 'Architectural Expedition',
			...
		}
		{
			_id: ObjectId(...),
			nombre: 'Artistic Journey',
			...
		}
		{
			_id: ObjectId(...),
			nombre: 'Tango Experience',
			...
		}
		{
			_id: ObjectId(...),
			nombre: 'Gastronomic Delight',
			...
		}
		...
	]
	```

	v. Las rutas que incluyan el stop "Recoleta" y no el stop "Plaza Italia"

	```js
	db.recorridos.find({stops: {$in: ["Recoleta"], $nin: ["Plaza Italia"]}})

	[
		{
			_id: ObjectId(...),
			nombre: 'Architectural Expedition',
			...
		}
		{
			_id: ObjectId(...),
			nombre: 'Tango Experience',
			...
		}
		{
			_id: ObjectId(...),
			nombre: 'Gastronomic Delight',
			...
		}
		{
			_id: ObjectId(...),
			nombre: 'Museum Tour',
			...
		}
	]
	```

	vi. El nombre y el total de km (si es que posee) de las rutas que incluyan el stop "Delta" y tenga un precio menor a 500

	```js
	db.recorridos.find({stops: "Delta", precio: {$lt: 500}}).projection({nombre: 1, totalKm: 1})

	{
		_id: ObjectId(...),
		nombre: 'Nature Escape'
	}
	```

	vii. Las rutas que incluyen tanto "San Telmo" como "Recoleta" y "Avenida de Mayo" entre sus stops.

	```js
	db.recorridos.find({stops: {$all: ['San Telmo', 'Recoleta', 'Avenida de Mayo']}})

	[
		{
			_id: ObjectId(...),
			nombre: 'Tango Experience',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Urban Exploration',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Historic Landmarks',
			...
		}
	]
	```

	viii. Solo el nombre de las rutas que dispongan de más de 5 stops

	```js
	db.recorridos.find({$expr: {$gt: [{$size: '$stops'}, 5]}})

	[
		{
			_id: ObjectId(...),
			nombre: 'Historical Adventure',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Architectural Expedition',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Artistic Journey',
			...
		},
		{
			_id: ObjectId(...),
			nombre: 'Tango Experience',
			...
		},
		...
	]
	```

	ix. Las rutas que no tengan definida el total de sus kilómetros.

	```js
	db.recorridos.find({totalKm: {$exists: false}})

	{
		_id: ObjectId(...),
		nombre: 'Nature Escape',
		...
	}
	```

	x. Los nombres y el listado de stops de aquellas rutas que incluyen algún museo en sus recorridos.

	```js
	db.recorridos.find({stops: {$elemMatch: {$regex: '^Museo'}}}).projection({nombre: 1, stops: 1})

	[
		{
			_id: ObjectId(...),
			nombre: 'Architectural Expedition',
			stops: [...]
		},
		{
			_id: ObjectId(...),
			nombre: 'Artistic Journey',
			stops: [...]
		},
		{
			_id: ObjectId(...),
			nombre: 'Urban Exploration',
			stops: [...]
		},
		{
			_id: ObjectId(...),
			nombre: 'Museum Tour',
			stops: [...]
		},
		{
			_id: ObjectId(...),
			nombre: 'Museum Tour',
			stops: [...]
		}
	]
	```

	xi. Obtenga la cantidad de elementos que posee la colección.

	```js
	db.recorridos.countDocuments();

	14;
	```

# Sección 3: Aggregation Framework

Aggregation Framework es una herramienta de MongoDB que nos permite ampliar la posibilidad de uso de su lenguaje de consulta. Así, podemos realizar consultas complejas, filtrado, agrupación y análisis de datos en tiempo real, utilizando una serie de etapas de procesamiento que se aplican secuencialmente a los documentos en una colección.

12. Cree una nueva base de datos llamada "tours2". Guarde el archivo llamado 'generador1.js' adjunto a esta práctica y ejecútelo con: load(<ruta del archivo 'generador1.js'>). Si utiliza un cliente que lo permita (ej. Robo3T), se puede ejecutar directamente en el espacio de consultas. Examine las colecciones generadas.

13. Utilizando Aggregation Framework, realice las siguientes operaciones:

	i. Obtenga una muestra de 5 rutas aleatorias de la colección

	```js
	db.route.aggregate({$sample: {size: 5}});
	```

	ii. Extienda la consulta anterior para incluir en el resultado toda la información de cada una de las Stops. Note que puede ligarlas por su código.

	```js
	db.route.aggregate([
		{$sample: {size: 5}},
		{$lookup: {
			from: "stop",
			localField: "stops",
			foreignField: "code",
			as: "stops"
		}}
	]);
	```

	iii. Obtenga la información de las Routes (incluyendo la de sus Stops) que tengan un precio mayor o igual a 900

	```js
	db.route.aggregate([
		{$match: {price: {$gte: 900}}},
		{$lookup: {
			from: "stop",
			localField: "stops",
			foreignField: "code",
			as: "stops"
		}}
	]);
	```

	iv. Obtenga la información de las Routes que tengan 5 Stops o más.

	```js
	db.route.aggregate([
		{$match: {$expr: {$gte: [{$size: "$stops"}, 5]}}},
		{$lookup: {
			from: "stop",
			localField: "stops",
			foreignField: "code",
			as: "stops"
		}}
	]);
	```

	v. Obtenga la información de las Routes que tengan incluido en su nombre el string "111".

	```js
	db.route.aggregate([
		{$match: {name: {$regex: "111"}}},
		{$lookup: {
			from: "stop",
			localField: "stops",
			foreignField: "code",
			as: "stops"
		}}
	]);
	```

	vi. Obtenga solo las Stops de la Route con nombre "Route100"

	```js
	db.route.aggregate([
		{$lookup: {
			from: "route",
			localField: "code",
			foreignField: "stops",
			as: "routes"
		}}
		{$match: {"routes.name": "route100"}},
		{$unset: "routes"}
	]);
	```

	vii. Obtenga la información del Stop que más apariciones tiene en Routes.

	```js
	db.route.aggregate([
		{$lookup: {
			from: "route",
			localField: "code",
			foreignField: "stops",
			as: "routes"
		}},
		{$addFields: {routesCount: {$size: "$routes"}}},
		{$sort: {routesCount: -1}},
		{$limit: 1},
		{$project: {
			routes: 0,
			routesCount: 0
		}}
	]);
	```

	viii. Obtenga las Route que tengan un precio inferior a 150. A ellos agregué una nueva propiedad que especifique la cantidad de Stops que posee la Route. Cree una nueva colección llamada "rutas_economicas" y almacene estos elementos.

	```js
	db.route.aggregate([
		{$match: {price: {$lt: 150}}},
		{$addFields: {
			stopCount: {$size: "$stops"}
		}},
		{$out: {
			db: 'tours2',
			coll: 'rutas_economicas'
		}}
	]);
	```

	ix. Por cada Stop existente en su colección, calcule el precio promedio de las Routes que la incluyen

	```js
	db.stop.aggregate([
		{$lookup: {
			from: "route",
			localField: "code",
			foreignField: "stops",
			as: "routes"
		}},
		{$addFields: {
			avgPrice: {$avg: "$routes.price"}
		}},
		{$unset: 'routes'}
	]);
	```

Tener en cuenta: si la consulta se empieza a tornar difícil de leer, se pueden ir guardando los agregadores en variables, que no son más que objetos en formato JSON.
