#!/bin/bash

set -e

function set_svc_home {
  local SOURCE="${BASH_SOURCE[0]}"
  while [[ -h "$SOURCE" ]]; do # resolve $SOURCE until the file is no longer a symlink
    SVC_HOME="$( cd -P "$( dirname "$SOURCE" )" >/dev/null 2>&1 && pwd )"
    SOURCE="$(readlink "$SOURCE")"
    [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
  done
  SVC_HOME="$( cd -P "$( dirname "$SOURCE" )" >/dev/null 2>&1 && pwd )"
}

set_svc_home

PIDFILE="${SVC_HOME}/config/run.pid"

if [[ -f "$PIDFILE" ]]; then
  pid=`cat "$PIDFILE"`
  echo "Sending TERM signal to process $pid"
    kill -- -$(ps -o pgid= $pid | grep -o [0-9]*)
    echo "Verify $pid has been terminated"
    rm "$PIDFILE"
else
  echo "PID file not found $PIDFILE.  If the process is running it may have to be manually killed."
fi