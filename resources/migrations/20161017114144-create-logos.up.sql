-- Name: logos; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  logos
(
id serial NOT NULL,
filename text,
logoname text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "logos_pkey" PRIMARY KEY (id)
);
