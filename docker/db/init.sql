create database gastrofest;
create user gastrofest with encrypted password 'gastrofest';
grant all privileges on database gastrofest to gastrofest;
grant all on schema public to gastrofest;
alter database gastrofest owner to gastrofest;
