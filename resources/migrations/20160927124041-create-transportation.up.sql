-- Name: transportation; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  transportation
(
id serial NOT NULL,
cityid int,
vendorid int,
type text,
transportationname text,
address text,
contactperson text,
contactnumber text,
faxnumber text,
email text,
-- website text,
bankinformation text,
description text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "transportation_pkey" PRIMARY KEY (id)
);
