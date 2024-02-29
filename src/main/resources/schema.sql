create table if not exists telegram_user (
	id BIGSERIAL PRIMARY KEY,
	telegram_id BIGINT UNIQUE NOT NULL
);

create table if not exists phase (
	id BIGSERIAL PRIMARY KEY,
	handler_name varchar(250) UNIQUE NOT NULL
);

create table if not exists user_phase (
	user_id BIGINT UNIQUE NOT NULL,
	phase_id BIGINT NOT NULL,
	FOREIGN KEY (phase_id) REFERENCES phase (id)
);

create table if not exists user_message (
    id BIGSERIAL PRIMARY KEY,
	message_id BIGINT UNIQUE NOT NULL,
	user_id BIGINT NOT NULL,
	formatting_type varchar(50) NOT NULL,
	created_at TIMESTAMP NOT NULL
);

create table if not exists driver (
	id BIGINT PRIMARY KEY,
	first_name varchar(50),
	last_name varchar(50),
	phone_number varchar(50) UNIQUE,
	car_model varchar(50),
	car_color varchar(50),
	plate_number varchar(50) UNIQUE,
	seats_number INT,
	finished BOOLEAN NOT NULL DEFAULT false
);

create table if not exists country (
	id BIGSERIAL PRIMARY KEY,
	name varchar(50) UNIQUE NOT NULL
);

create table if not exists city (
	id BIGSERIAL PRIMARY KEY,
	name varchar(50) UNIQUE NOT NULL,
	country_id BIGINT NOT NULL,
	FOREIGN KEY (country_id) REFERENCES country (id)
);

create table if not exists route (
	id BIGSERIAL PRIMARY KEY,
	telegram_user_id BIGINT NOT NULL,
	country_from_id BIGINT,
	FOREIGN KEY (country_from_id) REFERENCES country (id),
	city_from_id BIGINT,
	FOREIGN KEY (city_from_id) REFERENCES city (id),
	country_to_id BIGINT,
	FOREIGN KEY (country_to_id) REFERENCES country (id),
	city_to_id BIGINT,
	FOREIGN KEY (city_to_id) REFERENCES city (id),
	status varchar(250) NOT NULL,
	user_type varchar(250) NOT NULL
);

create table if not exists trip (
	id BIGSERIAL PRIMARY KEY,
	route_id BIGINT NOT NULL,
	departure_date DATE,
	departure_time TIME,
	arrival_date DATE,
	arrival_time TIME,
	pickup_point VARCHAR(250),
	dropoff_point VARCHAR(250),
	currency VARCHAR(250),
	price INT,
	baggage_info VARCHAR(250),
	other_info VARCHAR(250),
	status VARCHAR(250),
	FOREIGN KEY (route_id) REFERENCES route (id)
);

create table if not exists booking (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    telegram_user_id BIGINT,
    passenger_name VARCHAR(250),
    passenger_phone_number VARCHAR(250),
    number_of_seats INT,
    status VARCHAR(250),
    FOREIGN KEY (trip_id) REFERENCES trip (id),
    FOREIGN KEY (telegram_user_id) REFERENCES telegram_user (id)
);