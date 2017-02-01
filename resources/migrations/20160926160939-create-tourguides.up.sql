-- Name: tourguides; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  tourguides
(
id serial NOT NULL,
cityid int,
vendorid int,
tourguidename text,
contactnumber text,
cellnumber text,
email text,
amount numeric,
hours int,
address text,
description text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "tourguides_pkey" PRIMARY KEY (id)
);
