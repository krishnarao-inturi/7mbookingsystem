-- Name: hotelinfromation; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  hotelinfromation
(
  id serial NOT NULL,
  bookingid int,
  cityid int,
  hotelid int,
  address text,
  telephonenumber text,
  faxnumber text,
  checkin TIMESTAMP WITHOUT TIME ZONE,
  checkout TIMESTAMP WITHOUT TIME ZONE,
  numerofrooms  int,
  numberofpeople int,
  description text,
  createdatetime TIMESTAMP WITH TIME ZONE,
  updateddatetime TIMESTAMP WITH TIME ZONE,
  createdoperatorid int,
  modifiedoperatorid int,
  isactive boolean,
  CONSTRAINT "hotelinfromation_pkey" PRIMARY KEY (id),
  FOREIGN KEY (bookingid) REFERENCES bookings(id),
   FOREIGN KEY (hotelid) REFERENCES hotels(id),
  FOREIGN KEY (cityid) REFERENCES cities(id)
);
