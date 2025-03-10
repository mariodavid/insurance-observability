#!/bin/bash

set -e
set -u

POSTGRES_DATABASES=("quote" "policy" "account")

for db in "${POSTGRES_DATABASES[@]}"; do
    echo "Creating database $db"
    psql -v ON_ERROR_STOP=1 --username "postgres" <<-EOSQL
                CREATE DATABASE "$db";
                GRANT ALL PRIVILEGES ON DATABASE "$db" TO postgres;
EOSQL
done
