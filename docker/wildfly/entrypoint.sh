#!/usr/bin/env bash
set -euo pipefail

WILDFLY_HOME="${WILDFLY_HOME:-/opt/jboss/wildfly}"
CLI="${WILDFLY_HOME}/bin/jboss-cli.sh"
STANDALONE="${WILDFLY_HOME}/bin/standalone.sh"
LOG_FILE="${WILDFLY_HOME}/standalone/log/server.log"

echo "Iniciando WildFly..."
${STANDALONE} -b 0.0.0.0 -bmanagement 0.0.0.0 &
WILDFLY_PID=$!

term_handler() {
  echo "Encerrando WildFly (PID=${WILDFLY_PID})..."
  kill -TERM "${WILDFLY_PID}" 2>/dev/null || true
  wait "${WILDFLY_PID}" 2>/dev/null || true
  exit 0
}
trap term_handler TERM INT

echo "Aguardando WildFly ficar pronto..."
READY=false
for i in {1..90}; do
  if ${CLI} --connect ':read-attribute(name=server-state)' >/dev/null 2>&1; then
    READY=true
    break
  fi
  sleep 1
done

if [[ "${READY}" != "true" ]]; then
  echo "ERRO: WildFly não ficou pronto a tempo."
  echo "Últimas linhas do log:"
  tail -n 200 "${LOG_FILE}" || true
  exit 1
fi

echo "Aplicando init.cli..."
${CLI} --connect --file="${WILDFLY_HOME}/init.cli" || true

echo "WildFly pronto. Mantendo container..."
tail -F "${LOG_FILE}" &
TAIL_PID=$!

wait "${WILDFLY_PID}"
kill "${TAIL_PID}" 2>/dev/null || true
