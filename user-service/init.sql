DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_database WHERE datname = 'cinema_userdb'
   ) THEN
      CREATE DATABASE cinema_userdb;
END IF;
END
$$;

