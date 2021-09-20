/*
*
*/
SELECT * FROM projet.photos where visible='true';

SELECT count(*) FROM projet.photos where visible='true';

/*
*
*/
SELECT * FROM projet.furnitures WHERE condition='vendu' OR condition='propose';

SELECT count(*) FROM projet.furnitures WHERE condition='vendu' OR condition='propose';

/*
*
*/
SELECT * FROM projet.photos WHERE prefered IS TRUE;

SELECT count(*) FROM projet.photos WHERE prefered IS TRUE;

/*
*
*/
INSERT INTO projet.furnitures (id_furniture,id_type,description,condition)
	VALUES (DEFAULT,6,'Bureau Ecolier 1950','visite');

INSERT INTO projet.furnitures (id_furniture,id_type,description,condition)
	VALUES (DEFAULT,6,'Bureau écolier ancien en chêne et hêtre','visite');

/*
*
*/
UPDATE projet.furnitures SET purchase_price=45, condition = 'achete' WHERE id_furniture = 11;

UPDATE projet.furnitures SET purchase_price=47, condition = 'achete' WHERE id_furniture = 12;
