-- Name: rooms; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  rooms
(
 id serial NOT NULL,
  travelsid int,
  groupsid int,
  nights int,
  numberofrooms int,
  hotelname text,
  name text,
  checkin TIMESTAMP WITHOUT TIME ZONE,
  checkout TIMESTAMP WITHOUT TIME ZONE,
  amount numeric,
  createdatetime TIMESTAMP WITH TIME ZONE,
  updateddatetime TIMESTAMP WITH TIME ZONE,
  createdoperatorid int,
  modifiedoperatorid int,
  isactive boolean,
  CONSTRAINT "rooms_pkey" PRIMARY KEY (id),
  FOREIGN KEY (travelsid) REFERENCES travels(id),
  FOREIGN KEY (groupsid) REFERENCES groups(id)
);
