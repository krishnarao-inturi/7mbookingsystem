-- Name: bookingsdetails; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  bookingsdetails
(
  id serial NOT NULL,
  bookingid int,
  cityid int,
  vendorid int,
  itemid text,
  date TIMESTAMP WITHOUT TIME ZONE,
  time text,
  amount numeric,
  paymentmode text,
  manualdescription text,
  remarks text,
  numberofpeoples int,
  createdatetime TIMESTAMP WITH TIME ZONE,
  updateddatetime TIMESTAMP WITH TIME ZONE,
  createdoperatorid int,
  modifiedoperatorid int,
  isactive boolean,
  CONSTRAINT "bookingsdetails_pkey" PRIMARY KEY (id),
  FOREIGN KEY (bookingid) REFERENCES bookings(id),
  FOREIGN KEY (cityid) REFERENCES cities(id),
  FOREIGN KEY (vendorid) REFERENCES vendors(id)
  --FOREIGN KEY (itemid) REFERENCES items(id)
);
