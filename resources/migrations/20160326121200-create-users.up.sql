-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  users
(
id serial NOT NULL,
firstname text,
lastname text,
email text,
password text,
mobilenumber text,
role text,
lastlogin TIME WITH TIME ZONE,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
isactive BOOLEAN,
CONSTRAINT "users_pkey" PRIMARY KEY (id)
);


-- INSERT INTO users(id, firstname, lastname, email, password, mobilenumber, role, 
-- lastlogin, createdatetime, updateddatetime, isactive) VALUES (1, 'venkat', 'chitturi', 'venkat@gmail.com','bcrypt+sha512$a80b58e633ec6a93895ff266$12$24326124313224307442736b30496953417157364a6443506b3733612e534b6246735450536870717265573644522f6b794a517136614b4150667532', '9966126757', 'Admin','18:02:59.268093+05:30','2014-01-01 00:00:00+05:30','2014-01-01 00:00:00+05:30', true);

-- INSERT INTO users(id, firstname, lastname, email, password, mobilenumber, role, 
-- lastlogin, createdatetime, updateddatetime, isactive) VALUES (2, 'venkat', 'chitturi', 'standard@gmail.com','bcrypt+sha512$a80b58e633ec6a93895ff266$12$24326124313224307442736b30496953417157364a6443506b3733612e534b6246735450536870717265573644522f6b794a517136614b4150667532', '9966126757', 'Standard','18:02:59.268093+05:30','2014-01-01 00:00:00+05:30','2014-01-01 00:00:00+05:30', true);

-- INSERT INTO users(id, firstname, lastname, email, password, mobilenumber, role, 
-- lastlogin, createdatetime, updateddatetime, isactive) VALUES (3, 'venkat', 'chitturi', 'group@gmail.com','bcrypt+sha512$a80b58e633ec6a93895ff266$12$24326124313224307442736b30496953417157364a6443506b3733612e534b6246735450536870717265573644522f6b794a517136614b4150667532', '9966126757', 'Group','18:02:59.268093+05:30','2014-01-01 00:00:00+05:30','2014-01-01 00:00:00+05:30', true);

