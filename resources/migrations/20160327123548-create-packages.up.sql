-- Name: packages; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  packages
(
id serial NOT NULL,
name text,
adultamount numeric,
childamount numeric,
categories text,
description text,
isgroup boolean,
min numeric,
max numeric,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "packages_pkey" PRIMARY KEY (id)
);
