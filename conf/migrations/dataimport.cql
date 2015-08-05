use system;

DROP KEYSPACE gee;

CREATE KEYSPACE gee WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};

use gee;

CREATE TABLE users (id uuid PRIMARY KEY,
                    firstname text,
                    lastname text,
                    email text,
                    email_confirmed text,
                    pwd text,
                    image text ) ;

INSERT INTO users (id, firstname, lastname, email, email_confirmed, pwd,image) VALUES
                  (756716f7-2e54-4715-9f00-91dcbea6cf51,
                   'Houran',
                   'Ketabdar',
                   'houran.ketabdar@gmail.com',
                   'houran.ketabdar@gmail.com',
                   '1234',
                   'http://...' );



