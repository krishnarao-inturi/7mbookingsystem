ALTER TABLE hotelinfromation DROP  constraint  hotelinfromation_hotelid_fkey;
ALTER TABLE hotelinfromation DROP COLUMN hotelid;
ALTER TABLE hotelinfromation ADD COLUMN hotelid  int;
