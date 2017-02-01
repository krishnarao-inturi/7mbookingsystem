ALTER TABLE bookings ADD COLUMN guestname  text;
ALTER TABLE bookingsdetails ADD COLUMN confirmationnumber text;
ALTER TABLE hotelinfromation ADD COLUMN isbreakfastincluded boolean,
ADD COLUMN confirmationnumber text, ADD COLUMN bookedundername text;
