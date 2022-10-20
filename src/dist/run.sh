#!/usr/bin/env bash

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

function string_replace_all {
  local line="$1"
  line="${line//\$\{date\}/$DATE}"
  line="${line//\$\{svc.home\}/$SVC_HOME}"
  echo "$line"
}

function set_java_opts {
  local FILE="$1"
  while read -r line || [[ -n "$line" ]]
  do
    if [[ -n "$line" && ! "$line" =~ ^# ]]; then
      local rl="$(string_replace_all "$line")"
      JAVA_OPTS+=("$rl")
    fi
  done < "$FILE"
}

function set_java {
  if [[ -n "$JAVA_HOME" ]]; then
    JAVA="$JAVA_HOME/bin/java"
  else
    echo "WARNING: JAVA_HOME is not set.  Using java from path may lead to inconsistent behavior."
    JAVA=java
  fi
}

function set_exe_jar {
  local home="$1"
  EXE_JAR="$(ls "$home" | egrep geosamples-api-.*-exe.jar)"
}

DATE="$(date +%Y%m%d_%H%M%S)"
SVC_HOME=
JVM_OPTS=()
JAVA=
EXE_JAR=

set_svc_home
set_java
set_java_opts "$SVC_HOME/config/jvm.options"
set_exe_jar "$SVC_HOME"

cd "$SVC_HOME"
"$JAVA" "${JAVA_OPTS[@]}" -jar "$EXE_JAR"
