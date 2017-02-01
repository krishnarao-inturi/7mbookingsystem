-- Name: activitylog; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  activitylog
(
 id serial NOT NULL,
  userid int,
  bookingid int,
  cityid int,
  vendorid text,
  bookingdetailid int,
  activitytext text,
  type text,
  createdatetime TIMESTAMP WITH TIME ZONE,
  updateddatetime TIMESTAMP WITH TIME ZONE,
  createdoperatorid int,
  modifiedoperatorid int,
  isactive boolean,
  CONSTRAINT "activitylog_pkey" PRIMARY KEY (id)
);
