#!/bin/bash

# Impostazioni
MEMORY_MB=1000
CORES=1
TARGET_CLASS="org.apache.bookkeeper.bookie.BufferedChannel"
TIME_PER_CLASS=1

echo ">>> Pulizia progetto..."
mvn clean compile

echo ">>> Generazione test EvoSuite per: $TARGET_CLASS"
mvn evosuite:generate -DtargetClass=org.apache.bookkeeper.bookie.BufferedChannel -DmemoryInMB=1000 -Dcores=1 -DtimeInMinutesPerClass=1


echo ">>> Esportazione test generati in src/test/java..."
mvn evosuite:export

echo ">>> Fatto! Puoi ora eseguire i test con: mvn test"
