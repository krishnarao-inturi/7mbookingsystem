-- Name: usersdetails; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  usersdetails
(
 id serial NOT NULL,
  travelsid int,
  groupsid int,
  age int,
  createdatetime TIMESTAMP WITH TIME ZONE,
  updateddatetime TIMESTAMP WITH TIME ZONE,
  isactive boolean,
  CONSTRAINT "usersdetails_pkey" PRIMARY KEY (id),
  FOREIGN KEY (travelsid) REFERENCES travels(id),
  FOREIGN KEY (groupsid) REFERENCES groups(id)
);
