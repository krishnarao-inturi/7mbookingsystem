-- Name: events; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  events
(
id serial NOT NULL,
cityid int,
vendorid int,
eventname text,
adultcost numeric,
childcost numeric,
st numeric,
cp text,
contactnumber text,
phonenumber text,
faxnumber text,
email text,
website text,
actual text,
meal text,
remarks text,
contract text,
description text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "events_pkey" PRIMARY KEY (id)
);
