create database gastrofest;
create user gastrofest with encrypted password 'gastrofest';
grant all privileges on database gastrofest to gastrofest;
alter database gastrofest owner to gastrofest;

create database gastrofest2;
grant all privileges on database gastrofest2 to gastrofest;
alter database gastrofest2 owner to gastrofest;

-- PGPASSWORD="gastrofest" psql -U gastrofest -d gastrofest2 -f docker-entrypoint-initdb.d/02.dump.sql
