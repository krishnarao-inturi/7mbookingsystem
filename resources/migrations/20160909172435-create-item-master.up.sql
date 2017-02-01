-- Name: items; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  items
(
id serial NOT NULL,
cityid int,
vendorid int,
type text,
itemname text,
adultcost numeric,
childcost numeric,
category text,
description text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "items_pkey" PRIMARY KEY (id),
 FOREIGN KEY (cityid) REFERENCES cities(id)
);
