CREATE TABLE IF NOT EXISTS monkey_data.users (
	email VARCHAR (256) NOT NULL PRIMARY KEY,
	password CHAR (60) NOT NULL,
	role VARCHAR (14) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	created_by VARCHAR(256) REFERENCES users (email),
	updated_at TIMESTAMP NOT NULL,
	updated_by VARCHAR(256) REFERENCES users (email)
);

INSERT INTO monkey_data.users (email, password, role, created_at, created_by, updated_at, updated_by)
	VALUES (
		'admin@admin.es', '$2a$10$loDmV/55LKAXDfbFC.RFKeluqey05KPTh6ezL.gQ50iajKGtxQM/y', 'ADMIN',
		now(), 'admin@admin.es', now(), 'admin@admin.es'
	);

INSERT INTO monkey_data.users (email, password, role, created_at, created_by, updated_at, updated_by)
	VALUES (
		'juan@monkeyshop.es', '$2a$10$loDmV/55LKAXDfbFC.RFKeluqey05KPTh6ezL.gQ50iajKGtxQM/y', 'USER',
		now(), 'admin@admin.es', now(), 'admin@admin.es'
	);

INSERT INTO monkey_data.users (email, password, role, created_at, created_by, updated_at, updated_by)
	VALUES (
		'eva@monkeyshop.es', '$2a$10$loDmV/55LKAXDfbFC.RFKeluqey05KPTh6ezL.gQ50iajKGtxQM/y', 'USER',
		now(), 'admin@admin.es', now(), 'admin@admin.es'
	);
