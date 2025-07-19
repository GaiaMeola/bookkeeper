#!/bin/bash

# USO: ./run-randoop.sh <fully-qualified-class-name> [time-limit]
# Esempio: ./run-randoop.sh org.apache.bookkeeper.bookie.BufferedChannel 60

if [ $# -lt 1 ]; then
  echo "❌ Uso: $0 <fully-qualified-class-name> [time-limit]"
  exit 1
fi

CLASS=$1
TIME=${2:-60}

# Percorsi personalizzati
RANDOOP_JAR="/Users/gaiameola/Dev/ISW2/randoop-4.3.4/randoop-all-4.3.4.jar"
CLASSES="/Users/gaiameola/Dev/ISW2/bookkeeper/bookkeeper-server/target/classes"
DEPS_DIR="/Users/gaiameola/Dev/ISW2/bookkeeper/bookkeeper-server/target/dependency"

# Controllo dei file/directory
if [ ! -f "$RANDOOP_JAR" ]; then
  echo "❌ Randoop jar non trovato a: $RANDOOP_JAR"
  exit 1
fi

if [ ! -d "$CLASSES" ]; then
  echo "❌ Directory classi non trovata a: $CLASSES"
  exit 1
fi

if [ ! -d "$DEPS_DIR" ]; then
  echo "❌ Directory delle dipendenze non trovata a: $DEPS_DIR"
  exit 1
fi

OUT_DIR="./randoop_tests/${CLASS//./_}"
mkdir -p "$OUT_DIR"

echo "▶️ Generazione test per la classe: $CLASS"
echo "⏱️ Tempo massimo: ${TIME}s"
echo "📁 Output in: $OUT_DIR"

# Comando Randoop
java -classpath "$RANDOOP_JAR:$CLASSES":"$DEPS_DIR"/* \
  randoop.main.Main gentests \
  --testclass="$CLASS" \
  --methodlist=methodlist.txt \
  --time-limit="$TIME" \
  --usethreads=false \
  --randomseed=42 \
  --clear=100 \
  --junit-output-dir="$OUT_DIR" \
  --regression-test-basename=RandoopRegression \
  --error-test-basename=RandoopError \
  2>&1 | tee "$OUT_DIR/randoop-output.log"

echo "✅ Test generati. Log in: $OUT_DIR/randoop-output.log"
