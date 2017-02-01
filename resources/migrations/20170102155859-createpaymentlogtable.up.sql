-- Name: paymentlog; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  paymentlog
(
 id serial NOT NULL,
  paymentid int,
  bookingid int,
  vendorid int,
  paymentmode text,
  type text,
  amount numeric,
  createdatetime TIMESTAMP WITH TIME ZONE,
  updateddatetime TIMESTAMP WITH TIME ZONE,
  createdoperatorid int,
  modifiedoperatorid int,
  isactive boolean,
  CONSTRAINT "paymentlog_pkey" PRIMARY KEY (id)
);
