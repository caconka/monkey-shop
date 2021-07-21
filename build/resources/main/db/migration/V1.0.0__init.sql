CREATE TABLE IF NOT EXISTS monkey_data.user (
	email VARCHAR (256) NOT NULL PRIMARY KEY,
	password CHAR (60) NOT NULL,
	role VARCHAR (14) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS monkey_data.employee (
	id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	email VARCHAR (256) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	created_by INTEGER NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	updated_by INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS monkey_data.customer (
	id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	email VARCHAR (256) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	created_by INTEGER NOT NULL,
	updated_at TIMESTAMP NOT NULL,
	updated_by INTEGER NOT NULL
);

INSERT INTO monkey_data.user (email, password, role, created_at, updated_at)
	VALUES ('admin@admin.es', '$2a$10$loDmV/55LKAXDfbFC.RFKeluqey05KPTh6ezL.gQ50iajKGtxQM/y', 'ADMIN', now(), now());
