#!/usr/bin/env bash

set -e

function get_script_dir {
  local DIR=
  local SOURCE="${BASH_SOURCE[0]}"
  while [[ -h "$SOURCE" ]]; do # resolve $SOURCE until the file is no longer a symlink
    DIR="$( cd -P "$( dirname "$SOURCE" )" >/dev/null 2>&1 && pwd )"
    SOURCE="$(readlink "$SOURCE")"
    [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
  done
  DIR="$( cd -P "$( dirname "$SOURCE" )" >/dev/null 2>&1 && pwd )"
  echo "$DIR"
}

function trim {
    local var="$*"
    # remove leading whitespace characters
    var="${var#"${var%%[![:space:]]*}"}"
    # remove trailing whitespace characters
    var="${var%"${var##*[![:space:]]}"}"
    echo -n "$var"
}

function set_template_vars {
  local FILE="$1"
  while IFS='=' read -r key value || [[ -n "$key" ]]
  do
    local k="$(trim "$key")"
    local v="$(trim "$value")"

    if [[ "$k" = USER ]]; then
      USER="$v"
    fi

    if [[ "$k" = JAVA_HOME ]]; then
      JAVA_HOME="$v"
    fi
  done < "$FILE"
}

function check_user {
  local tmp="$(id -u "$1")"
}

function string_replace_all {
  local line="$1"
  line="${line//\$\{JAVA_HOME\}/$JAVA_HOME}"
  line="${line//\$\{USER\}/$USER}"
  line="${line//\$\{SVC_HOME\}/$SVC_HOME}"
  CONTENT="$line"
}


if [[ $(whoami) != root ]]; then
  echo "Installer must be run as root"
  exit 1
fi

DIR="$(get_script_dir)"
INSTALL_PROPS="$DIR/install-service.properties"

set_template_vars "$INSTALL_PROPS"

if [[ -z "$JAVA_HOME" ]]; then
  echo "JAVA_HOME was not set in install-service.properties"
  exit 1
fi

if [[ ! -d "$JAVA_HOME" ]]; then
  echo "$JAVA_HOME does not exist"
  exit 1
fi

if [[ -z "$USER" ]]; then
  echo "USER was not set in install-service.properties"
  exit 1
fi

check_user "$USER"

SERVICE_NAME=geosamples-api
SERVICE_FILE_NAME="$SERVICE_NAME.service"
TEMPLATE_FILE="$DIR/template/$SERVICE_FILE_NAME"
SERVICE_FILE="/etc/systemd/system/$SERVICE_FILE_NAME"
SVC_HOME="$(realpath "$DIR/..")"

RUN_SH="$SVC_HOME/run.sh"
SERVICE_LOG="$SVC_HOME/log/stdout.log"

string_replace_all "$(<"$TEMPLATE_FILE")"

echo "Installing systemd service..."
touch "$SERVICE_FILE"
chmod 664 "$SERVICE_FILE"
echo "$CONTENT" > "$SERVICE_FILE"
echo "... Success!"

echo "Reloading daemon..."
systemctl daemon-reload
echo "... Success!"

echo "Starting service..."
systemctl start "$SERVICE_FILE_NAME"
echo "... Success!"

echo "Enabling service..."
systemctl enable "$SERVICE_FILE_NAME"
echo "... Install finished!"

