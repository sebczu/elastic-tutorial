#!/bin/bash
STATUS=''
MANAGEMENT_PORT=8080
EXECUTION_TIME=0
TIMEOUT_LIMIT=300
SERVICE_NAME=$1

while [ "$STATUS" != "200" ]
do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}\n" http://$SERVICE_NAME:$MANAGEMENT_PORT/actuator/health)
  echo "Waiting for fully functional service"
  echo "Connecting to http://$SERVICE_NAME:$MANAGEMENT_PORT/actuator/health"
  echo $(curl -i http://$SERVICE_NAME:$MANAGEMENT_PORT/actuator/health)
  if [ "$STATUS" != "200" ]; then
    sleep 5
    let "EXECUTION_TIME=EXECUTION_TIME+5"
  fi
  if [ "$EXECUTION_TIME" -ge "$TIMEOUT_LIMIT" ]; then
    echo "Service timeout"
    exit 1
  fi
done
echo "Service ready"
