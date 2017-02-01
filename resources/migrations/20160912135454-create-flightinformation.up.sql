-- Name: flightinfromation; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  flightinfromation
(
  id serial NOT NULL,
  bookingid int,
  cityid int,
  journeytype text,
  flightnumber text,
  date TIMESTAMP WITHOUT TIME ZONE,
  depart text,
  arrive text,
  departtime text,
  arrivetime text,
  description text,
  createdatetime TIMESTAMP WITH TIME ZONE,
  updateddatetime TIMESTAMP WITH TIME ZONE,
  createdoperatorid int,
  modifiedoperatorid int,
  isactive boolean,
  CONSTRAINT "flightinfromation_pkey" PRIMARY KEY (id),
  FOREIGN KEY (bookingid) REFERENCES bookings(id),
  FOREIGN KEY (cityid) REFERENCES cities(id)
);
