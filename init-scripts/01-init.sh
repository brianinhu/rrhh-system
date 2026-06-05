#!/bin/bash
set -e

function create_user_and_database() {
    local database=$1
    echo "  Provisioning database: $database"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
        CREATE DATABASE $database;
        GRANT ALL PRIVILEGES ON DATABASE $database TO $POSTGRES_USER;
EOSQL
}

# Verificamos si las variables existen antes de usarlas
if [ -n "${DB_NAME_CORE}" ] && [ -n "${DB_NAME_IAM}" ]; then
    echo "Arquitectura Multi-DB detectada: ${DB_NAME_CORE}, ${DB_NAME_IAM}"
    for db in ${DB_NAME_CORE} ${DB_NAME_IAM}; do
        create_user_and_database $db
    done
else
    echo "ERROR: Variables DB_NAME_CORE o DB_NAME_IAM no detectadas en el entorno del contenedor."
    exit 1
fi