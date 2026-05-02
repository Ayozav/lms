#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Создаём схему для нашего приложения
    CREATE SCHEMA IF NOT EXISTS ${DATABASE_SCHEMA};
EOSQL