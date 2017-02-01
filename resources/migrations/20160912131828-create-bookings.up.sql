-- Name: bookings; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  bookings
(
  id serial NOT NULL,
  travelsid int,
  groupsid int,
  bookingnumber text,
  description text,
  createdatetime TIMESTAMP WITH TIME ZONE,
  updateddatetime TIMESTAMP WITH TIME ZONE,
  createdoperatorid int,
  modifiedoperatorid int,
  isactive boolean,
  CONSTRAINT "bookings_pkey" PRIMARY KEY (id),
  FOREIGN KEY (travelsid) REFERENCES travels(id),
  FOREIGN KEY (groupsid) REFERENCES groups(id)
);
