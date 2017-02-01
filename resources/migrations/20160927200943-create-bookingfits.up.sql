-- Name: bookingfits; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  bookingfits
(
id serial NOT NULL,
cityid int,
vendorid int,
contactperson text,
contactnumber text,
rate numeric,
sictrans text,
pvttrans text,
meetgreet text,
pvtcitytour text,
siccitytour text,
email text,
address text,
bankaccountinfo text,
description text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "bookingfits_pkey" PRIMARY KEY (id)
);
