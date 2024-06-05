-- Создание таблицы Гости
CREATE TABLE guest (
	id serial PRIMARY KEY NOT NULL,
	name varchar(125) NOT NULL,
	email varchar(30),
	phone numeric(15,0)
);

-- Создание пользователя Manager с паролем 123456
CREATE USER manager with password '123456';

-- Выдача прав на просмотр списка гостей
GRANT SELECT ON guest to manager;

-- Выдача прав на занесение данных в список гостей.
GRANT INSERT ON guest to manager;
GRANT ALL ON guest_id_seq to manager;

-- Создание view guest_name, просмотр только имен гостей.
CREATE OR REPLACE VIEW guest_name as (
	SELECT name
	FROM guest
);

-- Создание пользователя guard с паролем 654321
CREATE USER guard with password '654321';

-- Выдача прав пользователю guard только на просмотр view guest_name
GRANT SELECT ON guest_name TO guard;
