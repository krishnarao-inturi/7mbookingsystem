--name: register
INSERT INTO users(firstname,lastname,email,password,mobilenumber,role,lastlogin,createdatetime,updateddatetime,isactive, type)
    VALUES(:firstname,:lastname,:email,:password,:mobilenumber,:role,now(),now(),now(),true, 'Booking')
RETURNING id;

--name: get-user
SELECT id,firstname,lastname,role,password,email FROM users WHERE  email = :email AND type = 'Booking';

--name: get-user-by-id
SELECT id,firstname,lastname,email FROM users WHERE id = :user_id AND AND type = 'Booking';

--name: get-user-details
SELECT id,role FROM users WHERE id=:user_id

--name: get-users
SELECT firstname,lastname,role,password,email FROM users where type ='Booking';

--name: get-total-users
SELECT COUNT(id) AS totalusers from users where isactive = 'TRUE' AND  type ='Booking';

--name: get-all-users-by-index-pagesize
SELECT MyRowNumber, id, firstname, lastname, email, mobilenumber, role, to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM users WHERE  isactive = 'TRUE' AND type ='Booking') tblUsers WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-total-users-by-email
SELECT COUNT(id) AS totalusers from users where isactive = 'TRUE' AND  type ='Booking' AND lower(email) LIKE ('%' || lower(:text) || '%');

--name: get-all-users-by-index-pagesize-by-email
SELECT MyRowNumber, id, firstname, lastname, email, mobilenumber, role, to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM users WHERE isactive = 'TRUE' AND type ='Booking' AND lower(email) LIKE ('%' || lower(:text) || '%')) tblUsers WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-users-by-id
SELECT id,firstname,lastname, email, mobilenumber, role FROM users WHERE id = :id  AND type = 'Booking';

--name: update-user
UPDATE users set firstname = :firstname, lastname = :lastname, role = :role, updateddatetime = now() where id = :id RETURNING id, firstname, lastname, email, role;

--name: delete-user-by-id
UPDATE users set isactive = 'FALSE', updateddatetime = now() where id = :id RETURNING id;

--name: insert-token
INSERT INTO tokens(id,user_id,created_at,isloggedout)
VALUES (:id,:user_id,now(),false)
RETURNING id;

--name: validate-token
SELECT user_id FROM tokens WHERE isloggedout = false AND id = :token AND EXTRACT(EPOCH FROM now() - created_at)/60 <= 360;


--name: insert-packages
INSERT INTO packages (name, adultamount, childamount, categories,  description, isgroup, min, max, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive) VALUES (:name, :adultamount, :childamount, :categories, :description, :isgroup, :min, :max, now(), now(), 1, 1, true) RETURNING id;

--name: get-total-packages
SELECT COUNT(id) AS totalpackages from packages where isactive = 'TRUE';

--name: get-total-packages-by-name
SELECT COUNT(id) AS totalpackages from packages where isactive = 'TRUE' AND  lower(name) LIKE ('%' || lower(:name) || '%');

--name: get-all-packages-by-index-pagesize
SELECT MyRowNumber, id, name, adultamount, childamount, categories, description, isgroup, min, max, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM packages WHERE isactive = 'TRUE') tblPackages WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-packages-by-index-pagesize-name
SELECT MyRowNumber, id, name, adultamount, childamount, categories, description, isgroup, min, max, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM packages WHERE isactive = 'TRUE' AND lower(name) LIKE ('%' || lower(:name) || '%') ) tblPackages WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-packages-by-id
SELECT id, name, adultamount, childamount, categories, description, isgroup, min, max FROM packages where id = :id;

--name: get-packages-by-name
SELECT id,  name, adultamount, childamount, categories, isgroup, FROM packages WHERE isactive = 'TRUE' and  lower(name) LIKE ('%' || lower(:name) || '%');

--name: get-all-packages
SELECT id,  name, adultamount, childamount, categories, description, isgroup  FROM Packages WHERE isactive = 'TRUE' AND isgroup = 'FALSE';

--name: get-all-packages1
SELECT id,  name, adultamount, childamount, categories, description, isgroup, min, max  FROM Packages WHERE isactive = 'TRUE' AND isgroup = 'TRUE';

--name: get-all-packages-between
SELECT id,  name, adultamount, childamount, categories, description, isgroup, min, max  FROM Packages WHERE isactive = 'TRUE' AND isgroup = 'TRUE'  and  :total between  min and max;

--name: update-packages
UPDATE packages SET  name = :name, adultamount = :adultamount, childamount = :childamount, categories = :categories, description = :description,  isgroup = :isgroup, min = :min, max = :max, updateddatetime = now(), modifiedoperatorid = 1  where id = :id   RETURNING id, name, adultamount, childamount, categories, isactive;

--name: delete-packages-by-id
UPDATE packages set isactive = 'FALSE' where id = :id RETURNING id;


--name: logout
UPDATE tokens SET isloggedout = true WHERE user_id = :id AND id = :token AND isloggedout = false RETURNING id;


--name: get-package-by-min-max-name
select adultamount,childamount, categories, min,max from packages pk where isgroup=true and pk.name in (:name);


--name:get-vendors
SELECT id,vendorname as name FROM vendors  WHERE isactive = 'TRUE'

--name: get-vendors-by-cityid
SELECT id, cityid, vendorname as name FROM vendors  WHERE cityid = :cityid

--name:get-vendors-by-id
SELECT id, cityid, vendorname, contactname, phonenumber, emailid, address FROM vendors  WHERE id = :id;


--name:insert-vendor
INSERT INTO vendors (cityid, vendorname, contactname, phonenumber, emailid, address, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive) VALUES (:cityid, :vendorname, :contactname, :phonenumber, :emailid, :address, now(), now(), 1, 1, true) RETURNING id;

--name: get-all-vendors-by-index-pagesize
SELECT MyRowNumber, id, (SELECT cityname FROM cities  WHERE id = cityid ) as  cityid, vendorname, contactname, phonenumber, emailid, address, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM vendors WHERE isactive = 'TRUE') tblvendors WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-active-vendors-count
select Count(id) from vendors Where isactive='TRUE'


--name:check-vendors-by-id
SELECT Count(id) FROM vendors Where id=:id and isactive = 'TRUE';

--name: update-vendors
UPDATE vendors set cityid= :cityid, vendorname = :vendorname, contactname = :contactname, phonenumber = :phonenumber, emailid = :emailid, address = :address where id = :id RETURNING id, cityid, vendorname, contactname, phonenumber, emailid, address;

--name: delete-vendors-by-id
UPDATE vendors set isactive = 'FALSE' where id = :id RETURNING id;

--name: get-items
SELECT id,itemname as name, vendorid, category, description FROM items  WHERE isactive = 'TRUE'

--name: get-items-by-id
SELECT id, cityid, vendorid1 as vendorid, type,  itemname, adultcost, childcost,  category, description, activitytitle, subcategory FROM items  WHERE id = :id and isactive = 'TRUE';

--name: get-items-by-fit-cityid
SELECT id, cityid, type, itemname as name, vendorid, isactive FROM items  WHERE isactive ='TRUE' and cityid = :cityid AND (type ='FIT' OR type ='Both');

--name: get-items-by-group-cityid
SELECT id, cityid, type, itemname as name, vendorid, isactive FROM items  WHERE isactive ='TRUE' and cityid = :cityid AND (type ='Group' OR type ='Both');

--name:insert-item
INSERT INTO items (cityid, vendorid1, type,  itemname, adultcost, childcost, category, description,createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, activitytitle, subcategory) VALUES (:cityid, :vendorid, :type, :itemname, :adultcost, :childcost, :category, :description, now(), now(), 1, 1, true, :activitytitle, :subcategory) RETURNING id;

--name: get-all-items-by-index-pagesize
SELECT MyRowNumber, id, (SELECT cityname FROM cities  WHERE id = cityid ) as  cityid, (SELECT cityname  FROM cities  WHERE isactive = 'TRUE' AND  id = cityid ) as cityid,(SELECT name  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid,  ev.eventname AS name FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, r.restaurantname AS name  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid, tg.tourguidename AS name  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid, tp.transportationname AS name  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = vendorid) AS vendorid, type,  itemname, adultcost, childcost, category, description, createdatetime,  isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM items WHERE isactive = 'TRUE') tblitems WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-active-items-count
select Count(id) from items Where isactive='TRUE';

--name: get-items-count-by-cityname
SELECT COUNT(id) AS totalitems from items where isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'));

--name: get-all-items-by-index-pagesize-cityname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE isactive = 'TRUE' AND  id = cityid ) as cityid,(SELECT name  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid,  ev.eventname AS name FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, r.restaurantname AS name  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid, tg.tourguidename AS name  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid, tp.transportationname AS name  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = vendorid) AS vendorid, type,  itemname, adultcost, childcost, category, description, createdatetime,  isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM items WHERE isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND  ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'))) tblitems WHERE MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-items-count-by-itemname
SELECT COUNT(id) AS totalitems from items where isactive = 'TRUE' AND (lower(itemname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-items-by-index-pagesize-itemname
SELECT MyRowNumber, id, (SELECT cityname FROM cities  WHERE id = cityid ) as  cityid,  (SELECT name  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid,  ev.eventname AS name FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, r.restaurantname AS name  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid, tg.tourguidename AS name  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid, tp.transportationname AS name  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = vendorid) AS vendorid, type,  itemname, adultcost, childcost, category, description, createdatetime,  isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM items WHERE isactive = 'TRUE'  AND (lower(itemname) LIKE ('%' || lower(:text) || '%'))) tblitems WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-items-count-by-category
SELECT COUNT(id) AS totalitems from items where isactive = 'TRUE' AND (lower(category) LIKE ('%' || lower(:text) || '%'));

--name: get-all-items-by-index-pagesize-category
SELECT MyRowNumber, id, (SELECT cityname FROM cities  WHERE id = cityid ) as  cityid, (SELECT name  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid,  ev.eventname AS name FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, r.restaurantname AS name  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid, tg.tourguidename AS name  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid, tp.transportationname AS name  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = vendorid) AS vendorid, type,  itemname, adultcost, childcost, category, description, createdatetime,  isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM items WHERE isactive = 'TRUE'  AND (lower(category) LIKE ('%' || lower(:text) || '%'))) tblitems WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-items-count-by-description
SELECT COUNT(id) AS totalitems from items where isactive = 'TRUE' AND (lower(description) LIKE ('%' || lower(:text) || '%'));

--name: get-all-items-by-index-pagesize-description
SELECT MyRowNumber, id, (SELECT cityname FROM cities  WHERE id = cityid ) as  cityid, (SELECT name  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid,  ev.eventname AS name FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, r.restaurantname AS name  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid, tg.tourguidename AS name  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid, tp.transportationname AS name  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = vendorid) AS vendorid, type,  itemname, adultcost, childcost, category, description, createdatetime,  isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM items WHERE isactive = 'TRUE'  AND (lower(description) LIKE ('%' || lower(:text) || '%'))) tblitems WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;


--name: get-items-count-by-vendorname
SELECT COUNT(id) AS totalitems from items where isactive = 'TRUE' AND vendorid in (SELECT id  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid,  ev.eventname AS name FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, r.restaurantname AS name  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid, tg.tourguidename AS name  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid, tp.transportationname AS name  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = vendorid AND (lower(name) LIKE ('%' || lower(:text) || '%')))

--name: get-all-items-by-index-pagesize-vendorname
SELECT MyRowNumber, id, (SELECT cityname FROM cities  WHERE id = cityid ) as  cityid, (SELECT name  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid,  ev.eventname AS name FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, r.restaurantname AS name  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid, tg.tourguidename AS name  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid, tp.transportationname AS name  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = vendorid) AS vendorid, type,  itemname, adultcost, childcost, category, description, createdatetime,  isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM items WHERE isactive = 'TRUE'  AND vendorid in (SELECT id  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid,  ev.eventname AS name FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, r.restaurantname AS name  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid, tg.tourguidename AS name  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid, tp.transportationname AS name  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = vendorid AND (lower(name) LIKE ('%' || lower(:text) || '%')))) tblitems WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;


--name:check-items-by-id
SELECT Count(id) FROM items Where id=:id and isactive = 'TRUE';

--name: update-items
UPDATE items set cityid = :cityid, vendorid1 = :vendorid, type = :type, itemname = :itemname, adultcost = :adultcost, childcost = :childcost, category = :category, description= :description, activitytitle= :activitytitle, subcategory= :subcategory  where id = :id RETURNING id, cityid, vendorid, type,  itemname, adultcost, childcost, category, description;

--name: delete-items-by-id
UPDATE items set isactive = 'FALSE' where id = :id RETURNING id;

--name:insert-cities
INSERT INTO cities (cityname,createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive) VALUES (:cityname, now(), now(), 1, 1, true) RETURNING id;

--name:get-cities
SELECT id,cityname as name FROM cities  WHERE isactive = 'TRUE';

--name: get-cities-by-id
SELECT id, cityname  FROM cities  WHERE id = :id and isactive = 'TRUE';


--name:check-cities-by-id
SELECT Count(id) FROM cities Where id=:id

--name: get-all-cities-by-index-pagesize
SELECT MyRowNumber, id, cityname, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM cities WHERE isactive = 'TRUE') tblcities WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-active-cities-count
select Count(id) from cities Where isactive='TRUE';

--name: update-cities
UPDATE cities set cityname = :cityname where id = :id RETURNING id, cityname;

--name: delete-cities-by-id
UPDATE cities set isactive = 'FALSE' where id = :id RETURNING id;

--name:insert-hotels
INSERT INTO hotels (cityid, category, hotelname, address, phonenumber, faxnumber, contactpersonname, contactpersonphonenumber, contactpersonemail, type, cost, remarks, cancellationpolicy, description, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating, filename,estimatedroomrate) VALUES (:cityid, :category, :hotelname, :address, :phonenumber, :faxnumber, :contactpersonname, :contactpersonphonenumber, :contactpersonemail, :type, :cost, :remarks, :cancellationpolicy, :description, now(), now(), 1, 1, true, :rating, :filename,:estimatedroomrate) RETURNING id;

--name:get-hotels
SELECT id, hotelname as name FROM hotels  WHERE isactive = 'TRUE';

--name:get-hotels-cityname
SELECT id, hotelname as name, (SELECT cityname  FROM cities  WHERE id = cityid) as city FROM hotels  WHERE isactive = 'TRUE';

--name: get-hotels-by-id
SELECT id, cityid, category, hotelname, address, phonenumber, faxnumber, contactpersonname, contactpersonphonenumber, contactpersonemail, type, cost, remarks, cancellationpolicy, description, rating, filename,estimatedroomrate FROM hotels  WHERE id = :id and isactive = 'TRUE';

--name: get-hotels-by-cityid
SELECT id, cityid, hotelname as name, category, address, phonenumber, faxnumber, contactpersonname, contactpersonphonenumber, contactpersonemail, type, cost, remarks, cancellationpolicy, description, rating, filename FROM hotels  WHERE (cityid = :cityid OR (select count(*) From cities c WHERE isactive='TRUE' and c.id = cityid  and lower(cityname)='master city') > 0) and isactive = 'TRUE';

--name:check-hotels-by-id
SELECT Count(id) FROM hotels Where id=:id and isactive = 'TRUE';

--name: get-all-hotels-by-index-pagesize
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, type, cost, category, hotelname, phonenumber, faxnumber, contactpersonname, contactpersonphonenumber, contactpersonemail, description, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM hotels WHERE isactive = 'TRUE') tblhotels WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-hotels-count-by-cityname
SELECT COUNT(id) AS totalhotels from hotels where isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'));

--name: get-all-hotels-by-index-pagesize-cityname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE isactive = 'TRUE' AND  id = cityid ) as cityid, type, cost, category, hotelname, phonenumber, faxnumber, contactpersonname, contactpersonphonenumber, contactpersonemail, description, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM hotels WHERE isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'))) tblhotels WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-hotels-count-by-hotelname
SELECT COUNT(id) AS totalhotels from hotels where isactive = 'TRUE' AND (lower(hotelname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-hotels-by-index-pagesize-hotelname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, type, cost, category, hotelname, phonenumber, faxnumber, contactpersonname, contactpersonphonenumber, contactpersonemail, description, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM hotels WHERE isactive = 'TRUE'  AND (lower(hotelname) LIKE ('%' || lower(:text) || '%'))) tblhotels WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-hotels-count-by-category
SELECT COUNT(id) AS totalhotels from hotels where isactive = 'TRUE' AND (lower(category) LIKE ('%' || lower(:text) || '%'));

--name: get-all-hotels-by-index-pagesize-category
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid) as cityid, type, cost, category, hotelname, phonenumber, faxnumber, contactpersonname, contactpersonphonenumber, contactpersonemail, description, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM hotels WHERE isactive = 'TRUE'  AND (lower(category) LIKE ('%' || lower(:text) || '%'))) tblhotels WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;


--name: get-active-hotels-count
select Count(id) from hotels Where isactive='TRUE';

--name: update-hotels
UPDATE hotels set cityid = :cityid, category = :category, hotelname = :hotelname, address = :address, phonenumber = :phonenumber, faxnumber = :faxnumber, contactpersonname = :contactpersonname, contactpersonphonenumber = :contactpersonphonenumber, contactpersonemail = :contactpersonemail, type = :type, cost = :cost, remarks = :remarks, cancellationpolicy = :cancellationpolicy, description = :description, rating = :rating, filename = :filename,estimatedroomrate = :estimatedroomrate where id = :id RETURNING id, cityid, category, hotelname, address, phonenumber, faxnumber, contactpersonname, contactpersonphonenumber, contactpersonemail, type, cost, remarks, cancellationpolicy, description, rating, filename;

--name: delete-hotels-by-id
UPDATE hotels set isactive = 'FALSE' where id = :id RETURNING id;


--name: get-active-logos-count
SELECT Count(id) FROM logos Where isactive='TRUE';

--name: get-all-logos-by-index-pagesize
SELECT MyRowNumber, id, filename, logoname, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM logos WHERE isactive = 'TRUE') tbllogos WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;


--name: get-logo-count-by-logoname
SELECT COUNT(id) AS totallogos from logos where isactive = 'TRUE' AND (lower(logoname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-logo-by-index-pagesize-logoname
SELECT MyRowNumber, id, filename, logoname, createdatetime, updateddatetime  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM logos WHERE isactive = 'TRUE'  AND (lower(logoname) LIKE ('%' || lower(:text) || '%'))) tbllogos WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;


--name: insert-logos
INSERT INTO logos (filename, logoname, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive) VALUES (:filename, :logoname, now(), now(), 1, 1, true) RETURNING id;

--name: update-logos-by-id
UPDATE logos set filename = :filename, logoname = :logoname  where id = :id RETURNING id, filename, logoname;

--name:get-logos
SELECT id, filename, logoname as name FROM logos  WHERE isactive = 'TRUE';

--name: get-logos-by-id
select id, filename, logoname  FROM  logos  where id = :id;

--name: delete-logos-by-id
UPDATE logos set isactive = 'FALSE' where id = :id RETURNING id;


--name: get-total-bookings
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE';

--name: get-fit-total-bookings
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' and travelsid IS NOT NULL;

--name: get-groups-total-bookings
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' and groupsid IS NOT NULL;

--name: get-fit-agents
select id,concat(firstname, ' ', lastname) as name from users where role='bookstandard'

--name: get-group-agents
select id,concat(firstname, ' ', lastname) as name from users where role='bookgroup'

--name: get-all-bookings-by-index-pagesize
SELECT MyRowNumber, id, email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid  UNION ALL SELECT qrnumber FROM groups where id = groupsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE') tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-fit-bookings-by-index-pagesize
SELECT MyRowNumber, id, email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' and travelsid IS NOT NULL) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-fit-bookings-count-by-agentname
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND travelsid IS NOT NULL AND (lower(agentname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-fit-bookings-by-index-pagesize-agentname
SELECT MyRowNumber, id , email, agentname, bookingnumber,isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND travelsid IS NOT NULL AND (lower(agentname) LIKE ('%' || lower(:text) || '%'))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-fit-bookings-count-by-bookingnumber
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND travelsid IS NOT NULL AND (lower(bookingnumber) LIKE ('%' || lower(:text) || '%'));

--name: get-all-fit-bookings-by-index-pagesize-bookingnumber
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND travelsid IS NOT NULL AND (lower(bookingnumber) LIKE ('%' || lower(:text) || '%'))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-fit-bookings-count-by-quotationnumber
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND travelsid IS NOT NULL AND travelsid IN (SELECT id FROM travels where isactive = 'TRUE' AND  (lower(qrnumber) LIKE ('%' || lower(:text) || '%')));

--name: get-all-fit-bookings-by-index-pagesize-quotationnumber
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM travels where  isactive = 'TRUE' AND id = travelsid) as qrnumber, to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND travelsid IS NOT NULL AND travelsid IN (SELECT id FROM travels where isactive = 'TRUE' AND  (lower(qrnumber) LIKE ('%' || lower(:text) || '%')))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;


--name: get-all-groups-bookings-by-index-pagesize
SELECT MyRowNumber, id, email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM groups where id = groupsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' and groupsid IS NOT NULL) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-group-bookings-count-by-agentname
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND groupsid IS NOT NULL AND (lower(agentname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-group-bookings-by-index-pagesize-agentname
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND groupsid IS NOT NULL AND (lower(agentname) LIKE ('%' || lower(:text) || '%'))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-group-bookings-count-by-bookingnumber
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND groupsid IS NOT NULL AND (lower(bookingnumber) LIKE ('%' || lower(:text) || '%'));

--name: get-all-group-bookings-by-index-pagesize-bookingnumber
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND groupsid IS NOT NULL AND (lower(bookingnumber) LIKE ('%' || lower(:text) || '%'))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-group-bookings-count-by-quotationnumber
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND groupsid IS NOT NULL AND groupsid IN (SELECT id FROM groups where isactive = 'TRUE' AND  (lower(qrnumber) LIKE ('%' || lower(':text') || '%')));

--name: get-all-group-bookings-by-index-pagesize-quotationnumber
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM groups where id = groupsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND groupsid IS NOT NULL AND  groupsid IN (SELECT id FROM groups where isactive = 'TRUE' AND  (lower(qrnumber) LIKE ('%' || lower(':text') || '%'))))  tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-active-bookings-count
select Count(id) from bookings Where isactive='TRUE';

--name: insert-bookings
INSERT INTO bookings (travelsid, groupsid,  bookingnumber, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, email, agentname, guestname, isfinalbooking, emergencynumbers, termsandconditions, logoid,noofpax,nameoftourmanager,remarks, bookingagents) VALUES (:travelsid, :groupsid,  :bookingnumber, now(), now(), :createdoperatorid, :modifiedoperatorid, true, :email, :agentname, :guestname, :isfinalbooking, :emergencynumbers, :termsandconditions, :logoid,:noofpax,:nameoftourmanager,:remarks, Array[ :bookingagents ] :: int[]) RETURNING id;

--name: update-bookings-number-by-id
UPDATE bookings set bookingnumber = :bookingnumber where id = :id RETURNING bookingnumber;

--name: get-bookings-number-by-id
select bookingnumber FROM  bookings  where id = :id;

--name: delete-bookings-by-id
UPDATE bookings set isactive = 'FALSE' where id = :id RETURNING id;

--name: get-booking-by-id
SELECT id, (SELECT  id as quotationid  FROM Travels where id = travelsid  UNION ALL SELECT  id as quotationid FROM groups where id = groupsid), (SELECT  qrnumber FROM Travels where id = travelsid  UNION ALL SELECT  qrnumber FROM groups where id = groupsid), bookingnumber, isactive, email, agentname, guestname, isfinalbooking, emergencynumbers, termsandconditions, logoid,noofpax,nameoftourmanager,remarks, bookingagents FROM bookings  where id = :id and isactive = 'TRUE';

--name: update-bookings-by-id
UPDATE bookings set travelsid = :travelsid, groupsid = :groupsid, modifiedoperatorid = :modifiedoperatorid, updateddatetime = now(), email = :email, agentname = :agentname, guestname = :guestname, bookingnumber = :bookingnumber, isfinalbooking = :isfinalbooking, emergencynumbers = :emergencynumbers, termsandconditions = :termsandconditions, logoid = :logoid,noofpax=:noofpax,nameoftourmanager=:nameoftourmanager,remarks=:remarks, bookingagents = Array[ :bookingagents ] :: int[]  where id = :id RETURNING id, travelsid, groupsid, bookingnumber, email, agentname, guestname, isfinalbooking, emergencynumbers, termsandconditions, logoid, bookingagents;


--name: insert-bookingsdetails
INSERT INTO bookingsdetails (bookingid, cityid, vendorid1, itemid, date, time, amount, paymentmode, manualdescription, remarks, numberofpeoples, confirmationnumber, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,activitytitle,category,subcategory) VALUES (:bookingid, :cityid, :vendorid1, :itemid, :date, :time, :amount, :paymentmode, :manualdescription, :remarks, :numberofpeoples, :confirmationnumber, now(), now(), :createdoperatorid, :modifiedoperatorid, true,:activitytitle,:category,:subcategory) RETURNING id;


--name: update-bookingsdetails-by-id
UPDATE bookingsdetails set cityid = :cityid, vendorid1 = :vendorid1, itemid = :itemid, date = :date, time = :time, amount = :amount, paymentmode = :paymentmode, manualdescription = :manualdescription, remarks = :remarks, numberofpeoples = :numberofpeoples, confirmationnumber= :confirmationnumber,  modifiedoperatorid = :modifiedoperatorid, updateddatetime = now(),activitytitle=:activitytitle,category=:category,subcategory=:subcategory   where id = :id RETURNING  id, bookingid, cityid, vendorid, itemid, date, time, amount, paymentmode, manualdescription, remarks, numberofpeoples, confirmationnumber;


--name: get-booking-details-by-booking-id
SELECT id, bookingid, cityid, vendorid1,activitytitle,category,subcategory,itemid, to_char(date, 'dd-Mon-yyyy') as date, time, amount, paymentmode, manualdescription, remarks, numberofpeoples, confirmationnumber  FROM bookingsdetails  where bookingid = :bookingid and isactive = 'TRUE'  order by id ASC;


--name: delete-bookingdetails-by-bookingdetailid
UPDATE bookingsdetails set modifiedoperatorid = :modifiedoperatorid,  updateddatetime = now(),  isactive = 'FALSE' where id = :bookingdetailid RETURNING id;

--name: delete-bookingdetails-by-cityid
UPDATE bookingsdetails set modifiedoperatorid = :modifiedoperatorid,  updateddatetime = now(),  isactive = 'FALSE' where bookingid = :bookingid AND cityid = :cityid  RETURNING id;


--name: delete-bookingdetails-by-bookingid
UPDATE bookingsdetails set isactive = 'FALSE' where bookingid = :bookingid RETURNING bookingid;

--name:insert-activitylog
INSERT INTO activitylog (userid, bookingid, vendorid1, bookingdetailid, activitytext, type, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category) VALUES (:userid, :bookingid, :vendorid1, :bookingdetailid, :activitytext, :type, now(), now(), :createdoperatorid, :modifiedoperatorid, true,:category) RETURNING id;


--name: get-activitylog-by-bookingdetail-id
SELECT id, (select concat(firstname ,' ',lastname) FROM users where id = al.userid) username, al.bookingid, al.cityid, al.vendorid, al.bookingdetailid, al.activitytext, al.type, to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime FROM activitylog al where al.bookingdetailid = :bookingdetailid and isactive = 'TRUE' order by al.id DESC;

--name: get-activitylog-by-flightinformation
SELECT id, (select concat(firstname ,' ',lastname) FROM users where id = al.userid) username, al.bookingid, al.cityid, al.vendorid, al.bookingdetailid, al.activitytext, al.type, to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime FROM activitylog al where al.bookingid = :bookingid and al.cityid = :cityid  and al.isactive = 'TRUE' order by al.id DESC;

--name: get-activitylog-by-hotelinformation
SELECT id, (select concat(firstname ,' ',lastname) FROM users where id = al.userid) username, al.bookingid, al.cityid, al.vendorid, al.bookingdetailid, al.activitytext, al.type, to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime FROM activitylog al where al.bookingid = :bookingid and al.cityid = :cityid  and al.isactive = 'TRUE' order by al.id DESC;


--name: insert-flightinfromation
INSERT INTO flightinfromation (bookingid, cityid,  journeytype, flightnumber, date, depart, arrive, departtime, arrivetime, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,noofpax,flightname) VALUES (:bookingid, :cityid,  :journeytype, :flightnumber, :date, :depart, :arrive, :departtime, :arrivetime, now(), now(), :createdoperatorid, :modifiedoperatorid, true,:noofpax,:flightname) RETURNING id;

--name: update-flightinfromation-by-id
UPDATE flightinfromation set cityid = :cityid, journeytype = :journeytype, flightnumber = :flightnumber, date = :date, depart = :depart, arrive = :arrive, departtime = :departtime, arrivetime = :arrivetime ,  modifiedoperatorid = :modifiedoperatorid, updateddatetime = now(),noofpax=:noofpax,flightname=:flightname where id = :id RETURNING bookingid, cityid,  journeytype, flightnumber, date, depart, arrive, departtime, arrivetime;


--name: get-flightinformation-by-booking-id
SELECT id, bookingid, cityid,  journeytype, flightnumber, to_char(date, 'dd-Mon-yyyy') as date , depart, arrive, departtime, arrivetime,noofpax,flightname FROM flightinfromation where bookingid = :bookingid and isactive = 'TRUE' order by id ASC;

--name: delete-flightinformation-by-cityid
UPDATE flightinfromation set modifiedoperatorid = :modifiedoperatorid,  updateddatetime = now(),  isactive = 'FALSE' where bookingid = :bookingid AND cityid = :cityid  RETURNING id;

--name: delete-flightinformation-by-flightid
UPDATE flightinfromation set modifiedoperatorid = :modifiedoperatorid,  updateddatetime = now(),  isactive = 'FALSE' where id = :flightid RETURNING id;

--name: delete-flightinformation-by-bookingid
UPDATE flightinfromation set isactive = 'FALSE' where bookingid = :bookingid RETURNING bookingid;


--name: insert-hotelinfromation
INSERT INTO hotelinfromation (bookingid, cityid, hotelid, address, telephonenumber, faxnumber, checkin, checkout, numerofrooms, numberofpeople, isbreakfastincluded, confirmationnumber, bookedundername, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, amount, paymentmode, vendorid1) VALUES (:bookingid, :cityid, :hotelid, :address, :telephonenumber, :faxnumber, :checkin, :checkout, :numerofrooms, :numberofpeople, :isbreakfastincluded, :confirmationnumber, :bookedundername,  now(), now(), :createdoperatorid, :modifiedoperatorid, true, :amount, :paymentmode, :vendorid1) RETURNING id;


--name: update-hotelinfromation-by-id
UPDATE hotelinfromation set cityid = :cityid, hotelid = :hotelid, address = :address, telephonenumber = :telephonenumber, faxnumber = :faxnumber, checkin = :checkin, checkout = :checkout, numerofrooms = :numerofrooms, numberofpeople = :numberofpeople, isbreakfastincluded = :isbreakfastincluded, confirmationnumber = :confirmationnumber, bookedundername = :bookedundername, modifiedoperatorid = :modifiedoperatorid, updateddatetime = now(), amount = :amount, paymentmode = :paymentmode, vendorid1 = :vendorid1   where id = :id RETURNING id, bookingid, cityid, hotelid, address, telephonenumber, faxnumber,checkin checkin, checkout, numerofrooms, numberofpeople, amount, paymentmode, vendorid1;


--name: get-hotelinformation-by-booking-id
SELECT id, bookingid, cityid, hotelid, address, telephonenumber, faxnumber,  to_char(checkin, 'dd-Mon-yyyy') as checkin, to_char(checkout, 'dd-Mon-yyyy') as checkout, numerofrooms, numberofpeople as numberofpeoples, isbreakfastincluded, confirmationnumber, bookedundername, amount, paymentmode, vendorid1 FROM hotelinfromation where bookingid = :bookingid and isactive = 'TRUE' order by id ASC;

--name: delete-hotelinformation-by-cityid
UPDATE hotelinfromation set modifiedoperatorid = :modifiedoperatorid,  updateddatetime = now(),  isactive = 'FALSE' where bookingid = :bookingid AND cityid = :cityid  RETURNING id;

--name: delete-hotelinformation-by-bookingid
UPDATE hotelinfromation set isactive = 'FALSE' where bookingid = :bookingid RETURNING bookingid;

--name: get-active-fit-quotations-count
SELECT  count(id) as count FROM travels where  transfertobookings = 'TRUE';

--name: get-active-groups-quotations-count
SELECT  count(id) as count FROM groups where  transfertobookings = 'TRUE';

--name: get-fit-quotations
select id, qrnumber as name,agentname from travels WHERE transfertobookings = true and isactive = 'TRUE';

--name: get-groups-quotations
select id, qrnumber as name  from groups WHERE transfertobookings = true and isactive = 'TRUE';

--name: get-all-fit-quotations-by-index-pagesize
SELECT  id, qrnumber as quotationid, agentname,  to_char(createdatetime, 'dd-Mon-yyyy') as date   FROM (SELECT  ROW_NUMBER() OVER (ORDER BY createdatetime DESC) as MyRowNumber,* FROM travels WHERE transfertobookings = 'TRUE') tblquotations WHERE MyRowNumber BETWEEN  (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-groups-quotations-by-index-pagesize
SELECT  id, qrnumber as quotationid, agentname,  to_char(createdatetime, 'dd-Mon-yyyy') as date   FROM (SELECT  ROW_NUMBER() OVER (ORDER BY createdatetime DESC) as MyRowNumber,* FROM groups WHERE transfertobookings = 'TRUE') tblquotations WHERE MyRowNumber BETWEEN  (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;



--name:insert-tourguides
INSERT INTO tourguides (cityid, vendorid, tourguidename, contactnumber, cellnumber, email, amount, hours, address, description, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating,bankorpaymentinfo) VALUES (:cityid, :vendorid, :tourguidename, :contactnumber, :cellnumber, :email, :amount, :hours, :address, :description, now(), now(), 1, 1, true, :rating,:bankorpaymentinfo) RETURNING id;

--name:get-tourguides
SELECT id,tourguidename as name FROM tourguides  WHERE isactive = 'TRUE'

--name: get-tourguides-by-id
SELECT id, cityid, vendorid, tourguidename, contactnumber, cellnumber, email, amount, hours, address, description, rating,bankorpaymentinfo FROM tourguides  WHERE id = :id and isactive = 'TRUE';

--name:check-tourguides-by-id
SELECT Count(id) FROM tourguides Where id=:id and isactive = 'TRUE';

--name: get-all-tourguides-by-index-pagesize
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, vendorid, tourguidename, contactnumber, cellnumber, email, amount, hours, address, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM tourguides WHERE isactive = 'TRUE') tbltourguides WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-tourguides-count-by-cityname
SELECT COUNT(id) AS totaltourguides from tourguides where isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'));

--name: get-all-tourguides-by-index-pagesize-cityname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE isactive = 'TRUE' AND id = cityid ) as cityid, vendorid, tourguidename, contactnumber, cellnumber, email, amount, hours, address, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM tourguides WHERE isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND  ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'))) tbltourguides WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-tourguides-count-by-tourguidename
SELECT COUNT(id) AS totaltourguides from tourguides where isactive = 'TRUE' AND (lower(tourguidename) LIKE ('%' || lower(:text) || '%'));

--name: get-all-tourguides-by-index-pagesize-tourguidename
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, vendorid, tourguidename, contactnumber, cellnumber, email, amount, hours, address, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM tourguides WHERE isactive = 'TRUE'  AND (lower(tourguidename) LIKE ('%' || lower(:text) || '%'))) tbltourguides WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-active-tourguides-count
select Count(id) from tourguides Where isactive='TRUE'

--name: update-tourguides
UPDATE tourguides set cityid = :cityid, vendorid = :vendorid, tourguidename = :tourguidename, contactnumber = :contactnumber, cellnumber = :cellnumber, email = :email, amount = :amount,address = :address, description = :description, rating = :rating,bankorpaymentinfo=:bankorpaymentinfo where id = :id RETURNING id, cityid, tourguidename, contactnumber, cellnumber, email, amount, hours, address, description, rating,bankorpaymentinfo;

--name: delete-tourguides-by-id
UPDATE tourguides set isactive = 'FALSE' where id = :id RETURNING id;


--name:insert-restaurants
INSERT INTO restaurants (cityid, area, restaurantname, address, cuisine, contactperson, contactnumber, faxnumber,  cellnumber, lunch, dinner, kids, remarks, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating, email) VALUES (:cityid, :area, :restaurantname, :address, :cuisine, :contactperson, :contactnumber, :faxnumber,  :cellnumber, :lunch, :dinner, :kids, :remarks, now(), now(), 1, 1, true, :rating, :email) RETURNING id;

--name:get-restaurants
SELECT id,restaurantname as name FROM restaurants  WHERE isactive = 'TRUE'

--name: get-restaurants-by-id
SELECT id, cityid, area, restaurantname, address, cuisine, contactperson, contactnumber, faxnumber,  cellnumber, lunch, dinner, kids, waytoconfirm, remarks, description, rating, email  FROM restaurants  WHERE id = :id and isactive = 'TRUE';

--name:check-restaurants-by-id
SELECT Count(id) FROM restaurants Where id=:id and isactive = 'TRUE';

--name: get-all-restaurants-by-index-pagesize
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, area, restaurantname, address, cuisine, contactperson, contactnumber, faxnumber,  cellnumber, lunch, dinner, kids, waytoconfirm, remarks, description, rating  FROM  (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM restaurants WHERE isactive = 'TRUE') tblrestaurants WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-restaurants-count-by-cityname
SELECT COUNT(id) AS totalrestaurants from restaurants where isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'));

--name: get-all-restaurants-by-index-pagesize-cityname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE isactive = 'TRUE' AND id = cityid ) as cityid, area, restaurantname, address, cuisine, contactperson, contactnumber, faxnumber,  cellnumber, lunch, dinner, kids, waytoconfirm, remarks, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM restaurants WHERE isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'))) tblrestaurants WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-restaurants-count-by-restaurantname
SELECT COUNT(id) AS totalrestaurants from restaurants where isactive = 'TRUE' AND (lower(restaurantname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-restaurants-by-index-pagesize-restaurantname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid,area, restaurantname, address, cuisine, contactperson, contactnumber, faxnumber,  cellnumber, lunch, dinner, kids, waytoconfirm, remarks, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM restaurants WHERE isactive = 'TRUE'  AND (lower(restaurantname) LIKE ('%' || lower(:text) || '%'))) tblrestaurants WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-restaurants-count-by-area
SELECT COUNT(id) AS totalrestaurants from restaurants where isactive = 'TRUE' AND (lower(area) LIKE ('%' || lower(:text) || '%'));

--name: get-all-restaurants-by-index-pagesize-area
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid,area, restaurantname, address, cuisine, contactperson, contactnumber, faxnumber,  cellnumber, lunch, dinner, kids, waytoconfirm, remarks, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM restaurants WHERE isactive = 'TRUE'  AND (lower(area) LIKE ('%' || lower(:text) || '%'))) tblrestaurants WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-active-restaurants-count
select Count(id) from restaurants Where isactive='TRUE'

--name: update-restaurants
UPDATE restaurants set cityid = :cityid, area = :area, restaurantname = :restaurantname, address = :address, cuisine = :cuisine, contactperson = :contactperson, contactnumber = :contactnumber, faxnumber = :faxnumber, cellnumber = :cellnumber, lunch = :lunch, dinner = :dinner, kids = :kids, waytoconfirm = :waytoconfirm, remarks = :remarks, description = :description, rating = :rating, email = :email  where id = :id RETURNING id,  cityid, area, restaurantname, address, cuisine, contactperson, contactnumber, faxnumber,  cellnumber, lunch, dinner, kids, waytoconfirm, remarks, description, rating;

--name: delete-restaurants-by-id
UPDATE restaurants set isactive = 'FALSE' where id = :id RETURNING id;


--name:insert-transportation
INSERT INTO transportation (cityid, vendorid, type, transportationname, address, contactperson, contactnumber, faxnumber,  email, bankinformation, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating) VALUES (:cityid, :vendorid, :type, :transportationname, :address, :contactperson, :contactnumber, :faxnumber,  :email, :bankinformation, now(), now(), 1, 1, true, :rating) RETURNING id;

--name:get-transportation
SELECT id,transportationname as name FROM transportation  WHERE isactive = 'TRUE'


--name: get-transportation-by-id
SELECT id, cityid, vendorid, type, transportationname, address, contactperson, contactnumber, faxnumber,  email, bankinformation, description, rating  FROM transportation  WHERE id = :id and isactive = 'TRUE';

--name:check-transportation-by-id
SELECT Count(id) FROM transportation Where id=:id and isactive = 'TRUE';

--name: get-all-transportation-by-index-pagesize
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, vendorid, type, transportationname, address, contactperson, contactnumber, faxnumber,  email, bankinformation, description, rating  FROM  (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM transportation WHERE isactive = 'TRUE') tbltransportation WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-transportations-count-by-cityname
SELECT COUNT(id) AS totaltransportations from transportation where  isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where  isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'));

--name: get-all-transportations-by-index-pagesize-cityname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE isactive = 'TRUE' AND  id = cityid ) as cityid, vendorid, type, transportationname, address, contactperson, contactnumber, faxnumber,  email, bankinformation, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM transportation WHERE isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'))) tbltransportations WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-transportations-count-by-transportationname
SELECT COUNT(id) AS totaltransportations from transportation where isactive = 'TRUE' AND (lower(transportationname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-transportations-by-index-pagesize-transportationname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, vendorid, type, transportationname, address, contactperson, contactnumber, faxnumber,  email, bankinformation, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM transportation  WHERE isactive = 'TRUE'  AND (lower(transportationname) LIKE ('%' || lower(:text) || '%'))) tbltransportations WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-active-transportation-count
select Count(id) from transportation Where isactive='TRUE'

--name: update-transportation
UPDATE transportation set cityid = :cityid, vendorid= :vendorid, type = :type, transportationname = :transportationname, address = :address, contactperson = :contactperson, contactnumber = :contactnumber, faxnumber = :faxnumber, email = :email, bankinformation = :bankinformation, rating = :rating where id = :id RETURNING id, type, transportationname, address, contactperson, contactnumber, faxnumber,  email, bankinformation, description, rating;

--name: delete-transportation-by-id
UPDATE transportation set isactive = 'FALSE' where id = :id RETURNING id;

--name: insert-bookingfits
INSERT INTO bookingfits (cityid, vendorid, contactperson, contactnumber, rate, sictrans, pvttrans, meetgreet, pvtcitytour, siccitytour, email, address, bankaccountinfo, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive) VALUES (:cityid, :vendorid, :contactperson, :contactnumber, :rate, :sictrans, :pvttrans, :meetgreet, :pvtcitytour, :siccitytour, :email, :address, :bankaccountinfo, now(), now(), 1, 1, true) RETURNING id;

--name: get-bookingfits-by-id
SELECT id, cityid, vendorid, contactperson, contactnumber, rate, sictrans, pvttrans, meetgreet, pvtcitytour, siccitytour, email, address, bankaccountinfo FROM bookingfits  WHERE id = :id and isactive = 'TRUE';

--name: check-bookingfits-by-id
SELECT Count(id) FROM bookingfits Where id=:id and isactive = 'TRUE';

--name: get-all-bookingfits-by-index-pagesize
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, (SELECT vendorname  FROM vendors  WHERE id = vendorid ) as  vendorid, contactperson, contactnumber, rate, email, address, bankaccountinfo FROM  (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookingfits WHERE isactive = 'TRUE') tblbookingfits WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-active-bookingfits-count
select Count(id) from bookingfits Where isactive='TRUE'

--name: update-bookingfits
UPDATE bookingfits set cityid = :cityid, vendorid = :vendorid, contactperson = :contactperson, contactnumber = :contactnumber, rate = :rate, sictrans = :sictrans, pvttrans = :pvttrans, meetgreet = :meetgreet, pvtcitytour = :pvtcitytour, siccitytour = :siccitytour,  email = :email, address = :address, bankaccountinfo = :bankaccountinfo where id = :id RETURNING id, vendorid, contactperson, contactnumber, rate, sictrans, pvttrans, meetgreet, pvtcitytour, siccitytour, email, address, bankaccountinfo;

--name: delete-bookingfits-by-id
UPDATE bookingfits set isactive = 'FALSE' where id = :id RETURNING id;

--name: insert-events
INSERT INTO events (cityid, vendorid, eventname, adultcost, childcost, st, cp,  contactnumber, phonenumber, faxnumber, email, website, actual, meal, remarks, contract, description, createdatetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive, rating,bookingguide) VALUES (:cityid, :vendorid, :eventname, :adultcost, :childcost, :st, :cp,  :contactnumber, :phonenumber, :faxnumber, :email, :website, :actual, :meal, :remarks, :contract, :description, now(), now(), 1, 1, true, :rating,:bookingguide) RETURNING id;

--name: get-events-by-id
SELECT id, cityid, vendorid, eventname, adultcost, childcost, st, cp,  contactnumber, phonenumber, faxnumber, email, website, actual, meal, remarks, contract, description, rating,bookingguide FROM events  WHERE id = :id and isactive = 'TRUE';

--name: check-events-by-id
SELECT Count(id) FROM bookingfits Where id=:id and isactive = 'TRUE';

--name: get-all-events-by-index-pagesize
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid, (SELECT vendorname  FROM vendors  WHERE id = vendorid ) as  vendorid, eventname, adultcost, childcost, st, cp,  contactnumber, phonenumber, faxnumber, email, website, actual, meal, remarks, contract, description, rating FROM  (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM events WHERE isactive = 'TRUE') tblevents WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-events-count-by-cityname
SELECT COUNT(id) AS totalevents from events where isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'));

--name: get-all-events-by-index-pagesize-cityname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE isactive = 'TRUE' AND id = cityid ) as cityid,(SELECT vendorname  FROM vendors  WHERE id = vendorid ) as  vendorid, eventname, adultcost, childcost, st, cp,  contactnumber, phonenumber, faxnumber, email, website, actual, meal, remarks, contract, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM events WHERE isactive = 'TRUE' AND cityid IN (SELECT id FROM cities where isactive = 'TRUE' AND ((lower(cityname) LIKE ('%' || lower(:text) || '%')) OR lower(cityname)='master city'))) tblevents WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-events-count-by-eventname
SELECT COUNT(id) AS totalevents from events where isactive = 'TRUE' AND (lower(eventname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-events-by-index-pagesize-eventname
SELECT MyRowNumber, id , (SELECT cityname  FROM cities  WHERE id = cityid ) as cityid,(SELECT vendorname  FROM vendors  WHERE id = vendorid ) as  vendorid, eventname, adultcost, childcost, st, cp,  contactnumber, phonenumber, faxnumber, email, website, actual, meal, remarks, contract, description, rating FROM (SELECT  ROW_NUMBER() OVER (ORDER BY rating DESC) as MyRowNumber,* FROM events WHERE isactive = 'TRUE'  AND (lower(eventname) LIKE ('%' || lower(:text) || '%'))) tblevents WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;


--name: get-active-events-count
select Count(id) from events Where isactive='TRUE';

--name: update-events
UPDATE events set cityid = :cityid, vendorid = :vendorid, eventname = :eventname, adultcost = :adultcost, childcost = :childcost, st = :st, cp = :cp,  contactnumber = :contactnumber, phonenumber = :phonenumber, faxnumber = :faxnumber, email = :email, website = :website, actual = :actual, meal = :meal, remarks = :remarks, contract = :contract, description = :description, rating = :rating,bookingguide=:bookingguide where id = :id RETURNING id, vendorid, adultcost, childcost, st, cp,  contactnumber, phonenumber, faxnumber, email, website, actual, meal, remarks, contract, description, rating;

--name: delete-events-by-id
UPDATE events set isactive = 'FALSE' where id = :id RETURNING id;

--name: get-vendors-vv-by-cityid
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid, 'Restaurant' as category, ev.adultcost, ev.childcost, ev.eventname AS name, ev.description  FROM  events ev where ev.cityid = :cityid and ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, 'Restaurant' as category, 0, 0,  r.restaurantname AS name, r.description  FROM restaurants r where r.cityid = :cityid and r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid,'Restaurant' as category, 0, 0, tg.tourguidename AS name, tg.description  FROM  tourguides tg where tg.cityid = :cityid and tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid,'Restaurant' as category, 0, 0, tp.transportationname AS name, tp.description  FROM  transportation tp where tp.cityid = :cityid and tp.isactive = 'TRUE';

--name: get-vendors-view
SELECT *  FROM (
SELECT concat('E_' ,  cast(ev.id as text)) AS id, ev.cityid, 'Events' as category, ev.adultcost, ev.childcost, ev.eventname AS name, ev.description  FROM  events ev where  ev.isactive = 'TRUE' UNION
SELECT concat('R_' ,  cast(r.id as text)) as id, r.cityid, 'Restaurant' as category, 0, 0,  r.restaurantname AS name, r.description  FROM restaurants r where  r.isactive = 'TRUE' UNION
SELECT concat('TG_' ,  cast(tg.id as text)) AS id, tg.cityid,'Tourguids' as category, 0, 0, tg.tourguidename AS name, tg.description  FROM  tourguides tg where tg.isactive = 'TRUE' UNION
SELECT concat('TP_' ,  cast(tp.id as text)) AS id, tp.cityid,'Transportation' as category, 0, 0, tp.transportationname AS name, tp.description  FROM  transportation tp where tp.isactive = 'TRUE'
) AS Vendors where id = :text;

--name: get-events-by-city
SELECT id, eventname as name,contactnumber,email as vendoremail,bookingguide as address,
    'Attractions' as category,'' as contactperson from events where
    (cityid=:cityid OR (select count(*) from cities c where isactive='TRUE' AND (c.id=cityid
           AND lower(cityname) = 'master city')) > 0) and isactive='TRUE'
--name: get-events-name-by-id
SELECT eventname as name  from events where id=:id and isactive='TRUE'

--name: get-tour-guides-by-city
SELECT id,tourguidename as name,contactnumber,email as vendoremail,address,'Guided City Tours' as category,'' as contactperson from tourguides where (cityid=:cityid OR (select count(*) from cities c where isactive='TRUE' AND (c.id=cityid AND
            lower(cityname) = 'master city')) > 0) AND isactive='TRUE'

--name: get-tour-guides-name-by-id
SELECT tourguidename as name  from tourguides where id=:id and isactive='TRUE'

--name: get-restaurents-by-city
SELECT id,restaurantname as name,contactnumber,email as vendoremail,address,'Meals' as category,contactperson from restaurants where (cityid=:cityid OR (select count(*) from cities c where isactive='TRUE' AND (c.id=cityid AND
            lower(cityname) = 'master city')) > 0) AND isactive='TRUE'

--name: get-restaurents-name-by-id
SELECT restaurantname as name  from restaurants where id=:id and isactive='TRUE'

--name: get-hotels-by-city
SELECT id,hotelname as name,contactpersonphonenumber as contactnumber,contactpersonemail as vendoremail,address,'Hotel' as category,contactpersonname as contactperson from hotels where (cityid=:cityid OR (select count(*) from cities c where isactive='TRUE' AND (c.id=cityid AND
            lower(cityname) = 'master city')) > 0) AND  isactive='TRUE'

--name: get-hotels-name-by-id
SELECT hotelname as name  from hotels where id=:id and isactive='TRUE'

--name: get-transportation-by-city
 SELECT id,transportationname as name,contactnumber,email as vendoremail,address,'Hotel' as category,contactperson from transportation where (cityid=:cityid OR (select count(*) from cities c where isactive='TRUE' AND (c.id=cityid AND
            lower(cityname) = 'master city')) > 0) AND isactive='TRUE'

--name: get-transportation-name-by-id
SELECT transportationname as name  from transportation where id=:id and isactive='TRUE'


--name: get-city-name-by-id
SELECT cityname FROM cities where id = :cityid

--name: update-restaurant-quickbook-id
update restaurants SET quickbookid = cast(:quickbookid as integer) where id=:id RETURNING id;

--name: update-tour-guide-quickbook-id
update tourguides SET quickbookid = cast(:quickbookid as integer) where id=:id RETURNING id;

--name: update-transportation-quickbook-id
update transportation SET quickbookid = cast(:quickbookid as integer) where id=:id RETURNING id;

--name: get-fit-activities-by-city-category
Select id,vendorid1 as vendorid,itemname as name,activitytitle,description as activitydescription FROM items  WHERE (lower(type) = 'fit' OR lower(type)='both') AND (cityid = :cityid OR (select count(*) from cities c where isactive='TRUE' AND (c.id=cityid AND
            lower(c.cityname) = 'master city')) > 0) AND lower(category)=lower(:category) AND isactive = 'TRUE'

--name: get-group-activities-by-city-category
Select id,vendorid1 as vendorid,itemname as name,activitytitle,description as activitydescription FROM items WHERE (lower(type) = 'group' OR lower(type)='both') AND  (cityid = :cityid OR  (select count(*) from cities c where isactive='TRUE' AND (c.id=cityid AND
            lower(c.cityname) = 'master city')) > 0) AND lower(category)=lower(:category) AND isactive = 'TRUE'


--name: get-payment-logs-index-page-size
SELECT MyRowNumber,id, paymentid,bookingid,vendorid,paymentmode,type,amount,to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category,(Select u.firstname || ' ' || u.lastname From users u where u.id = createdoperatorid) as username,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid)
    ELSE ''
    END as vendorname,(select bookingnumber from bookings b where b.id = bookingid) as bookingnumber,
    CASE
    WHEN (select travelsid from bookings b where b.id = bookingid) is null THEN 'GROUP'
    ELSE 'FIT'
    END as bookingtype
FROM (SELECT ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM paymentlog WHERE isactive = 'TRUE') tblPaymentLogs WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-payment-logs
SELECT COUNT(*) FROM paymentlog WHERE isactive='TRUE'

--name: get-payment-logs-by-payment-id-index-page-size
SELECT MyRowNumber,id, paymentid,bookingid,vendorid,paymentmode,type,amount,to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category,(Select u.firstname ||' ' || u.lastname From users u where u.id = createdoperatorid) as username,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid)
    ELSE ''
    END as vendorname,(select bookingnumber from bookings b where b.id = bookingid) as bookingnumber,
    CASE
    WHEN (select travelsid from bookings b where b.id = bookingid) is null THEN 'GROUP'
    ELSE 'FIT'
    END as bookingtype
FROM (SELECT ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM paymentlog WHERE isactive = 'TRUE' AND paymentid = :paymentid) tblPaymentLogs WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-payment-logs-by-payment-id-count
SELECT COUNT(*) FROM paymentlog WHERE paymentid=:paymentid AND isactive='TRUE'

--name: get-payment-logs-by-booking-id-index-page-size
SELECT MyRowNumber,id, paymentid,bookingid,vendorid,paymentmode,type,amount,to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category,(Select u.firstname || ' ' || u.lastname From users u where u.id = createdoperatorid) as username,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid)
    ELSE ''
    END as vendorname,(select bookingnumber from bookings b where b.id = bookingid) as bookingnumber,
    CASE
    WHEN (select travelsid from bookings b where b.id = bookingid) is null THEN 'GROUP'
    ELSE 'FIT'
    END as bookingtype
FROM (SELECT ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM paymentlog WHERE isactive = 'TRUE' AND bookingid = :bookingid) tblPaymentLogs WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-payment-logs-by-booking-id-count
SELECT COUNT(*) FROM paymentlog WHERE bookingid=:bookingid AND isactive='TRUE'


--name: get-payment-logs-by-payment-id-booking-id-index-page-size
SELECT MyRowNumber,id, paymentid,bookingid,vendorid,paymentmode,type,amount,to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category,(Select u.firstname || ' ' || u.lastname From users u where u.id = createdoperatorid) as username,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid)
    ELSE ''
    END as vendorname,(select bookingnumber from bookings b where b.id = bookingid) as bookingnumber,
    CASE
    WHEN (select travelsid from bookings b where b.id = bookingid) is null THEN 'GROUP'
    ELSE 'FIT'
    END as bookingtype
FROM (SELECT ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM paymentlog WHERE isactive = 'TRUE' AND paymentid=:paymentid AND bookingid = :bookingid) tblPaymentLogs WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-payment-logs-by-payment-id-booking-id-count
SELECT COUNT(*) FROM paymentlog WHERE paymentid=:paymentid AND bookingid=:bookingid AND isactive='TRUE'


--name: insert-payment-log
INSERT INTO paymentlog (paymentid,bookingid,vendorid,paymentmode,type,amount,createdatetime,updateddatetime,createdoperatorid,modifiedoperatorid,isactive,category) VALUES(:paymentid,:bookingid,:vendorid,:paymentmode,:type,:amount,now(),now(),:createdoperatorid,:modifiedoperatorid,'TRUE',:category) Returning id;

--name: delete-hotel-information
UPDATE hotelinfromation SET isactive = 'FALSE',modifiedoperatorid=:modifiedoperatorid,updateddatetime=now() WHERE id=:id RETURNING id;



--name: get-activity-logs-index-page-size
SELECT MyRowNumber,id,bookingdetailid as activityid,bookingid,vendorid1 as vendorid,type,activitytext,to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category,(Select u.firstname || ' ' || u.lastname From users u where u.id = createdoperatorid) as username,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid1)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid1)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid1)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid1)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid1)
    ELSE ''
    END as vendorname,(select bookingnumber from bookings b where b.id = bookingid) as bookingnumber,
    CASE
    WHEN (select travelsid from bookings b where b.id = bookingid) is null THEN 'GROUP'
    ELSE 'FIT'
    END as bookingtype
FROM (SELECT ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM activitylog WHERE isactive = 'TRUE') tblActivityLogs WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-activity-logs
SELECT COUNT(*) FROM activitylog WHERE isactive='TRUE'

--name: get-activity-logs-by-activity-id-index-page-size
SELECT MyRowNumber,id,bookingdetailid as activityid,bookingid,vendorid1 as vendorid,type,activitytext,to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category,(Select u.firstname ||' ' || u.lastname From users u where u.id = createdoperatorid) as username,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid1)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid1)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid1)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid1)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid1)
    ELSE ''
    END as vendorname,(select bookingnumber from bookings b where b.id = bookingid) as bookingnumber,
    CASE
    WHEN (select travelsid from bookings b where b.id = bookingid) is null THEN 'GROUP'
    ELSE 'FIT'
    END as bookingtype
FROM (SELECT ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM activitylog WHERE isactive = 'TRUE' AND bookingdetailid = :activityid) tblActivityLogs WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-activity-logs-by-activity-id-count
SELECT COUNT(*) FROM activitylog WHERE bookingdetailid=:activityid AND isactive='TRUE'

--name: get-activity-logs-by-booking-id-index-page-size
SELECT MyRowNumber,id, bookingdetailid as activityid,bookingid,vendorid1 as vendorid,type,activitytext,to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category,(Select u.firstname || ' ' || u.lastname From users u where u.id = createdoperatorid) as username,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid1)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid1)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid1)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid1)
    ELSE ''
    END as vendorname,(select bookingnumber from bookings b where b.id = bookingid) as bookingnumber,
    CASE
    WHEN (select travelsid from bookings b where b.id = bookingid) is null THEN 'GROUP'
    ELSE 'FIT'
    END as bookingtype
FROM (SELECT ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM activitylog WHERE isactive = 'TRUE' AND bookingid = :bookingid) tblActivityLogs WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-activity-logs-by-booking-id-count
SELECT COUNT(*) FROM activitylog WHERE bookingid=:bookingid AND isactive='TRUE'


--name: get-activity-logs-by-activity-id-booking-id-index-page-size
SELECT MyRowNumber,id,bookingdetailid as activityid,bookingid,vendorid1 as vendorid,type,activitytext,to_char(createdatetime, 'dd-Mon-yyyy HH:SS') as datetime, updateddatetime, createdoperatorid, modifiedoperatorid, isactive,category,(Select u.firstname || ' ' || u.lastname From users u where u.id = createdoperatorid) as username,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid1)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid1)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid1)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid1)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid1)
    ELSE ''
    END as vendorname,(select bookingnumber from bookings b where b.id = bookingid) as bookingnumber,
    CASE
    WHEN (select travelsid from bookings b where b.id = bookingid) is null THEN 'GROUP'
    ELSE 'FIT'
    END as bookingtype
FROM (SELECT ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM activitylog WHERE isactive = 'TRUE' AND bookingdetailid=:activityid AND bookingid = :bookingid) tblActivityLogs WHERE  MyRowNumber BETWEEN (((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-activity-logs-by-activity-id-booking-id-count
SELECT COUNT(*) FROM activitylog WHERE bookingdetailid=:activityid AND bookingid=:bookingid AND isactive='TRUE'

--name: get-all-fit-bookings-count-by-agentname-by-user
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND travelsid IS NOT NULL AND (lower(agentname) LIKE ('%' || lower(:text) || '%')) AND (createdoperatorid=:userid OR bookingagents @> array [ :userid ]);

--name: get-all-fit-bookings-by-index-pagesize-agentname-user
SELECT MyRowNumber, id , email, agentname, bookingnumber,isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND travelsid IS NOT NULL AND (lower(agentname) LIKE ('%' || lower(:text) || '%'))) AND (createdoperatorid=:userid OR bookingagents @> array [ :userid ]) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-fit-bookings-count-by-quotationnumber-user
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND (createdoperatorid=:userid OR bookingagents @> array [ :userid ]) AND travelsid IS NOT NULL AND travelsid IN (SELECT id FROM travels where isactive = 'TRUE' AND  (lower(qrnumber) LIKE ('%' || lower(:text) || '%')));

--name: get-all-fit-bookings-by-index-pagesize-quotationnumber-user
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM travels where  isactive = 'TRUE' AND id = travelsid) as qrnumber, to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND travelsid IS NOT NULL AND (createdoperatorid = :userid OR bookingagents @> array [ :userid ]) AND travelsid IN (SELECT id FROM travels where isactive = 'TRUE' AND  (lower(qrnumber) LIKE ('%' || lower(:text) || '%')))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-fit-bookings-count-by-bookingnumber-user
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND (createdoperatorid = :userid   OR bookingagents @> array [ :userid ]) AND travelsid IS NOT NULL AND (lower(bookingnumber) LIKE ('%' || lower(:text) || '%'));

--name: get-all-fit-bookings-by-index-pagesize-bookingnumber-user
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND (createdoperatorid = :userid  OR bookingagents @> array [ :userid ]) AND travelsid IS NOT NULL AND (lower(bookingnumber) LIKE ('%' || lower(:text) || '%'))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-fit-total-bookings-by-user
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' and (createdoperatorid= :userid OR  bookingagents @> array [ :userid ]) and travelsid IS NOT NULL;

--name: get-all-fit-bookings-by-index-pagesize-user
SELECT MyRowNumber, id, email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' and (createdoperatorid=:userid OR  bookingagents @> array [ :userid ]) and travelsid IS NOT NULL) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-restaurents-excel-data
SELECT (Select cityname FROM cities c WHERE c.id = cityid) as cityname,area,restaurantname,address,cuisine,contactperson,contactnumber,faxnumber,cellnumber,lunch,dinner,kids,waytoconfirm,remarks,description,rating,email FROM restaurants WHERE isactive = 'TRUE'

--name: get-hotels-excel-data
SELECT (Select cityname FROM cities c WHERE c.id = cityid) as cityname,category,hotelname,address,phonenumber,contactpersonname,contactpersonphonenumber,faxnumber,type,cost,remarks,cancellationpolicy,description,rating,filename,estimatedroomrate,contactpersonemail FROM hotels WHERE isactive = 'TRUE'

--name: get-tourguides-excel-data
SELECT (Select cityname FROM cities c WHERE c.id = cityid) as cityname,tourguidename,address,contactnumber,cellnumber,amount,hours,rating,email,bankorpaymentinfo FROM tourguides WHERE isactive = 'TRUE'

--name: get-transportation-excel-data
SELECT (Select cityname FROM cities c WHERE c.id = cityid) as cityname,type,transportationname,address,contactperson,contactnumber,faxnumber,email,bankinformation,description,rating FROM transportation WHERE isactive = 'TRUE'

--name: get-events-excel-data
SELECT (Select cityname FROM cities c WHERE c.id = cityid) as cityname,eventname,adultcost,childcost,st,cp,contactnumber,phonenumber,faxnumber,email,website,actual,meal,remarks,contract,description,rating,bookingguide FROM events WHERE isactive = 'TRUE'

--name: get-activities-excel-data
SELECT (Select cityname FROM cities c WHERE c.id = cityid) as cityname,itemname,category,description,activitytitle,subcategory,type,CASE
    WHEN (lower(category)='transportation') THEN (select transportationname FROM transportation t Where t.id=vendorid1)
    WHEN (lower(category)='meals') THEN (select restaurantname FROM restaurants r Where r.id=vendorid1)
    WHEN (lower(category)='hotel') THEN (select hotelname FROM hotels h Where h.id=vendorid1)
    WHEN (lower(category)='attractions') THEN (select eventname FROM events e Where e.id=vendorid1)
    WHEN (lower(category)='guidedcitytour') THEN (select tourguidename FROM tourguides tr Where tr.id=vendorid1)
    ELSE ''
    END as vendorname FROM items WHERE isactive = 'TRUE'

--name: get-all-group-bookings-count-by-agentname-user
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND (createdoperatorid = :user_id OR bookingagents @> array [ :user_id ]) AND groupsid IS NOT NULL AND (lower(agentname) LIKE ('%' || lower(:text) || '%'));

--name: get-all-group-bookings-by-index-pagesize-agentname-user
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE'AND (createdoperatorid = :user_id OR bookingagents @> array [ :user_id ]) AND groupsid IS NOT NULL AND (lower(agentname) LIKE ('%' || lower(:text) || '%'))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-all-group-bookings-count-by-quotationnumber-user
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE'AND (createdoperatorid=:user_id OR bookingagents @> array [ :user_id ]) AND groupsid IS NOT NULL AND groupsid IN (SELECT id FROM groups where isactive = 'TRUE' AND  (lower(qrnumber) LIKE ('%' || lower(':text') || '%')));

--name: get-all-group-bookings-by-index-pagesize-quotationnumber-user
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM groups where id = groupsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND (createdoperatorid = :user_id OR bookingagents @> array [ :user_id ]) AND groupsid IS NOT NULL AND  groupsid IN (SELECT id FROM groups where isactive = 'TRUE' AND  (lower(qrnumber) LIKE ('%' || lower(':text') || '%'))))  tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;


--name: get-all-group-bookings-count-by-bookingnumber-user
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' AND (createdoperatorid = :user_id OR bookingagents @> array [ :user_id ]) AND groupsid IS NOT NULL AND (lower(bookingnumber) LIKE ('%' || lower(:text) || '%'));

--name: get-all-group-bookings-by-index-pagesize-bookingnumber-user
SELECT MyRowNumber, id , email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM Travels where id = travelsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' AND (createdoperatorid = :user_id OR bookingagents @> array [ :user_id ]) AND groupsid IS NOT NULL AND (lower(bookingnumber) LIKE ('%' || lower(:text) || '%'))) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;

--name: get-groups-total-bookings-user
SELECT COUNT(id) AS totalbookings from bookings where isactive = 'TRUE' and (createdoperatorid = :user_id OR bookingagents @> array [ :user_id ]) and groupsid IS NOT NULL;

--name: get-all-groups-bookings-by-index-pagesize-user
SELECT MyRowNumber, id, email, agentname, bookingnumber, isfinalbooking, emergencynumbers, termsandconditions, (SELECT qrnumber FROM groups where id = groupsid) as qrnumber,  to_char(createdatetime, 'dd-Mon-yyyy') as createdatetime, isactive  FROM (SELECT  ROW_NUMBER() OVER (ORDER BY id DESC) as MyRowNumber,* FROM bookings WHERE isactive = 'TRUE' and (createdoperatorid = :user_id OR bookingagents @> array [ :user_id ]) and groupsid IS NOT NULL) tblbookings WHERE  MyRowNumber BETWEEN ( ((:index - 1) * :pagesize )+ 1) AND :index*:pagesize;
