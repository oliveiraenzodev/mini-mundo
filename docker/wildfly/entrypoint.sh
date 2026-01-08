#!/usr/bin/env bash
set -euo pipefail

exec /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
