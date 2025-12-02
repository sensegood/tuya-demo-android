@echo off
set DIR=%~dp0
set APP_HOME=%DIR%
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

set DEFAULT_JVM_OPTS=

if not defined JAVA_HOME goto findJavaFromPath
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_EXE%" goto execute

echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
exit /b 1

:findJavaFromPath
set JAVA_EXE=java.exe
for %%i in (%JAVA_EXE%) do set JAVA_EXE=%%~$PATH:i
if not defined JAVA_EXE goto fail

:execute
set APP_ARGS=%*
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %APP_ARGS%
exit /b 0

:fail
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
exit /b 1
