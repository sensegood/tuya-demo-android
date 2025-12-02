#!/usr/bin/env sh

# Simplified Gradle wrapper script.
APP_HOME=$(cd "$(dirname "$0")" && pwd)
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"
JAVA_EXE="${JAVA_HOME:+${JAVA_HOME}/bin/}java"
if [ ! -x "$JAVA_EXE" ]; then
  JAVA_EXE=java
fi
exec "$JAVA_EXE" $DEFAULT_JVM_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
