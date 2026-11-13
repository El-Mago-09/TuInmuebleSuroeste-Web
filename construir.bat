@echo off
rem Compila el modulo web y genera dist\tuinmueble.war
rem Uso: construir.bat "C:\ruta\apache-tomcat-10.1.x"   (o definir CATALINA_HOME)
setlocal
if not "%~1"=="" set "CATALINA_HOME=%~1"
if "%CATALINA_HOME%"=="" (
    echo Indique la carpeta de Tomcat: construir.bat "C:\Herramientas\apache-tomcat-10.1.61"
    exit /b 1
)
if not exist src\main\resources\db.properties (
    echo Falta src\main\resources\db.properties. Copielo desde db.properties.example.
    exit /b 1
)
if exist build rmdir /s /q build
xcopy /e /i /q src\main\webapp build >nul
mkdir build\WEB-INF\classes
dir /s /b src\main\java\*.java > fuentes.txt
javac -encoding UTF-8 -d build\WEB-INF\classes -cp "%CATALINA_HOME%\lib\servlet-api.jar;src\main\webapp\WEB-INF\lib\*" @fuentes.txt || exit /b 1
del fuentes.txt
copy /y src\main\resources\db.properties build\WEB-INF\classes >nul
if not exist dist mkdir dist
jar -cf dist\tuinmueble.war -C build . || exit /b 1
echo Listo: dist\tuinmueble.war
echo Para desplegar: copie dist\tuinmueble.war en "%CATALINA_HOME%\webapps" e inicie Tomcat.
echo Luego abra http://localhost:8080/tuinmueble/
