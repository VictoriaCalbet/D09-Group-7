# D09-Group-7

Las siguientes decisiones de diseño fueron elegidas con la aprobación de Carlos Müller.

- El proyecto que encontrará en el Item 3 no está configurado para el uso de HTTPS. El proyecto configurado para el uso de HTTPS se incluye en una carpeta diferente. Hemos tomado esta decisión para que el profesor encargado de las correcciones pueda disponer de ambas versiones si así lo desea sin que afecte a lo que se nos pide en los requisitos de esta entrega. NOTA: El proyecto que incluye los tests de los controladores (perteneciente al A+) no está configurado para utilizar HTTPS, ya que ese proyecto no hace falta desplegarlo. Su única función es la ejecución de los test del controlador.

- Se han realizado dos trabajos de investigación (comúnmente llamados A++) sobre una mejoras que hemos realizado a los test y al tratamiento de las cookies de creditCard. Ambos documentos podrá encontrarlos en la carpeta "A++".

- El esquema del Domain Model tiene entidades aisladas que representan los objetos de formulario. Algunos objetos del formulario tiene relaciones con objetos de dominio pero dichas relaciones no se han incluido ya que al no persistir en la base de datos, no consideramos que deban aparecer.