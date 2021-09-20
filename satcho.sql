DROP SCHEMA IF EXISTS projet CASCADE;
CREATE SCHEMA projet;

/*
CREATE TABLES
*/
CREATE TABLE projet.addresses (
	id_address SERIAL PRIMARY KEY,
	street VARCHAR(100),
	building_number VARCHAR(10),
	unit_number VARCHAR(3),
	postcode VARCHAR(100),
	commune VARCHAR(100),
	country VARCHAR(100) 
);

CREATE TABLE projet.users (
	id_user VARCHAR(100) PRIMARY KEY,
	username VARCHAR(100),
	last_name VARCHAR(100),
	first_name VARCHAR(100),
	email VARCHAR(100),
	role VARCHAR(50),
	password VARCHAR(100),
	id_address INTEGER REFERENCES projet.addresses (id_address),
	register_date timestamp without time zone,
	confirmation Boolean
);

CREATE TABLE projet.typeFurnitures(
	id_typeFurniture SERIAL PRIMARY KEY,
	type VARCHAR(100)
);

CREATE TABLE projet.furnitures(
	id_furniture SERIAL PRIMARY KEY,
	id_type INTEGER REFERENCES projet.typeFurnitures (id_typeFurniture),
	description VARCHAR(100),
	condition VARCHAR(100),
	purchase_price REAL,
	selling_price REAL
);


CREATE TABLE projet.photos(
	id_photo SERIAL PRIMARY KEY,
	id_furniture INTEGER REFERENCES projet.furnitures (id_furniture),
	base64_value VARCHAR,
	visible Boolean,
	prefered Boolean
);

CREATE TABLE projet.sales(
	id_sale SERIAL PRIMARY KEY,
	id_furniture INTEGER REFERENCES projet.furnitures (id_furniture),
	id_user VARCHAR(100) REFERENCES projet.users (id_user),
	date_of_sale timestamp without time zone,
	last_selling_price REAL
);


SELECT * FROM projet.furnitures 