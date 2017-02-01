-- Name: optionalpackages; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  optionalpackages
(
id serial NOT NULL,
travelsid int,
groupsid int,
packageid int,
adultamount numeric,
childamount numeric,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "optionalpackages_pkey" PRIMARY KEY (id),
FOREIGN KEY (travelsid) REFERENCES travels(id),
FOREIGN KEY (groupsid) REFERENCES groups(id),
FOREIGN KEY (packageid) REFERENCES packages(id)
);
