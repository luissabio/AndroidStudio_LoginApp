# AndroidStudio_LoginApp

_Aplicaci贸n de login hecha en Android Studio_

## Comenzando 馃殌

Tenemos 5 activities: Welcome, Sign Up, Sign In, Start y Edit Info.
> Welcome: Activity de bienvenida que nos permite llegar a Sign In y Sign Up.

> Sign Up: Activity donde hay un formulario para registrarnos, cuando le damos al bot贸n hace unas verificaciones de formato y manda los datos a una API para almacenarlos, si se almacena correctamente pasamos al activity de Sign In.

> Sign In: Activity donde hay un formulario para iniciar sesi贸n, cuando le damos al bot贸n hace unas verificaciones de formato y manda los datos a una API para comprobar si el email y la contrase帽a son correctos, en el caso de que lo sean nos devuelve un token y, con ese token y el email pasar铆amos a la activity Start.

> Start: Activity que verifica lo primero si el email y token est谩n correctos, si lo est谩n nos muestran una bienvenida con los datos del usuario y si no, nos piden que hagamos Sign Out. Si le damos al bot贸n de Sign Out nos lleva a la activity de Welcome y si pulsamos el de Edit Info nos lleva a la activity EditInfo.

> Edit Info: Activity donde podemos actualizar los datos del usuario.

Para poder probar correctamenta el funcionamiento, est谩 implementado un "Mock" que nos simular谩 nuestro endpoint, las clases se llaman MyMockAPI...

### Pre-requisitos 馃搵

_Tener instalado Android Studio_

## Autores 鉁掞笍

* **Luis Serrano** - *Trabajo Inicial* - luisserranomarin@gmail.com
* **Luis Serrano** - *Documentaci贸n* - luisserranomarin@gmail.com
