#! /bin/sh
REPOSITORY_HOME=`pwd`
export REPOSITORY_HOME

CLASS_PATH=$REPOSITORY_HOME/conf
#echo $CLASS_PATH

REPO_ENDORSED_DIR=$REPOSITORY_HOME/lib
#echo $REPO_ENDORSED_DIR


java -Djava.ext.dirs="$REPO_ENDORSED_DIR" \
        -Dcom.sun.management.jmxremote \
        -Dcom.sun.management.jmxremote.port=9999 \
        -Dcom.sun.management.jmxremote.authenticate=false \
        -Dcom.sun.management.jmxremote.ssl=false \
        -Dcom.sun.management.jmxremote.local.only=false \
        -classpath "$CLASS_PATH" org.modeshape.example.embedded.ModeShapeExample


