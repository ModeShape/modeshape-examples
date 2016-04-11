#! /bin/sh
REPOSITORY_HOME=`pwd`
export REPOSITORY_HOME

CLASS_PATH=$REPOSITORY_HOME/conf
echo $CLASS_PATH

REPO_ENDORSED_DIR=$REPOSITORY_HOME/lib
echo $REPO_ENDORSED_DIR


java -Djava.ext.dirs="$REPO_ENDORSED_DIR" -classpath "$CLASS_PATH" org.modeshape.shell.ShellConsole


