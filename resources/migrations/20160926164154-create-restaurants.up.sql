-- Name: restaurants; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  restaurants
(
id serial NOT NULL,
cityid int,
area text,
restaurantname text,
address text,
cuisine text,
contactperson text,
contactnumber text,
faxnumber text,
cellnumber text,
lunch numeric,
dinner numeric,
kids numeric,
waytoconfirm text,
remarks text,
description text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "restaurants_pkey" PRIMARY KEY (id)
);
