-- Name: tokens; Type: TABLE; Schema: public; Owner: postgres; Tablespace:
CREATE TABLE IF NOT EXISTS  tokens
(
id text,
user_id int,
created_at TIMESTAMP WITH TIME ZONE,
isloggedout   BOOLEAN,
FOREIGN KEY (user_id) REFERENCES users(id)
);
