-- Name: vendors; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  vendors
(
id serial NOT NULL,
cityid int,
vendorname text,
contactname text,
phonenumber text,
emailid text,
address text,
createdatetime TIMESTAMP WITH TIME ZONE,
updateddatetime TIMESTAMP WITH TIME ZONE,
createdoperatorid int,
modifiedoperatorid int,
isactive boolean,
CONSTRAINT "vendors_pkey" PRIMARY KEY (id),
FOREIGN KEY (cityid) REFERENCES cities(id)
);
