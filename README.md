PGPASSWORD="gastrofest" pg_dump -U gastrofest --inserts | gzip > dump.sql.gz

PGPASSWORD="gastrofest" pg_dump -U gastrofest > dump.sql --inserts

PGPASSWORD="gastrofest" psql -U gastrofest -d gastrofest -f dump.sql

https://dashboard.scrape.do/playground - зайти как corpsetrup@gmail.com
