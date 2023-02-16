# AndroidStudio_LoginApp

_Aplicación de login hecha en Android Studio_

## Comenzando 🚀

Tenemos 5 activities: Welcome, Sign Up, Sign In, Start y Edit Info.
> Welcome: Activity de bienvenida que nos permite llegar a Sign In y Sign Up.

> Sign Up: Activity donde hay un formulario para registrarnos, cuando le damos al botón hace unas verificaciones de formato y manda los datos a una API para almacenarlos, si se almacena correctamente pasamos al activity de Sign In.

> Sign In: Activity donde hay un formulario para iniciar sesión, cuando le damos al botón hace unas verificaciones de formato y manda los datos a una API para comprobar si el email y la contraseña son correctos, en el caso de que lo sean nos devuelve un token y, con ese token y el email pasaríamos a la activity Start.

> Start: Activity que verifica lo primero si el email y token están correctos, si lo están nos muestran una bienvenida con los datos del usuario y si no, nos piden que hagamos Sign Out. Si le damos al botón de Sign Out nos lleva a la activity de Welcome y si pulsamos el de Edit Info nos lleva a la activity EditInfo.

> Edit Info: Activity donde podemos actualizar los datos del usuario.

Para poder probar correctamenta el funcionamiento, está implementado un "Mock" que nos simulará nuestro endpoint, las clases se llaman MyMockAPI...

### Pre-requisitos 📋

_Tener instalado Android Studio_

## Autores ✒️

* **Luis Serrano** - *Trabajo Inicial* - luisserranomarin@gmail.com
* **Luis Serrano** - *Documentación* - luisserranomarin@gmail.com
