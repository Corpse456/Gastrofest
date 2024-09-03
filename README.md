PGPASSWORD="gastrofest" pg_dump -U gastrofest gastrofest > dump.sql --inserts

PGPASSWORD="gastrofest" psql -U gastrofest -d gastrofest -f dump.sql
