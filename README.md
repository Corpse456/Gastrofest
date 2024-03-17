PGPASSWORD="gastrofest" pg_dump -U gastrofest gastrofest > dump.sql --inserts -s

PGPASSWORD="gastrofest" psql -U gastrofest -d gastrofest2 -f dump.sql
