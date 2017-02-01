-- Name: hotels; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  hotels
(
id serial NOT NULL,
cityid int,
category text,
hotelname text,
address text,
phonenumber text,
faxnumber text,
contactpersonname text,
contactpersonphonenumber text,
contactpersonemail text,
type text,
cost numeric,
remarks text,
cancellationpolicy text,
description text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "hotels_pkey" PRIMARY KEY (id)
);
