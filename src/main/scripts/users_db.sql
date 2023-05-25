 /**
 * LAB 2
 * Name :  Eugene Shin <hongju.shin@dcmail.ca>
 * Date : feb 12, 2022
 * WEBD4201
* description : This SQL is for creating a users table
 */

CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS users;
-- CREATE the table,
CREATE TABLE users(
	id int PRIMARY KEY,
	password VARCHAR(40) NOT NULL,
	firstname VARCHAR(40) NOT NULL,
	lastname VARCHAR(40) NOT NULL,
	email VARCHAR(40),
	lastaccess DATE DEFAULT CURRENT_TIMESTAMP,
	enroldate DATE DEFAULT CURRENT_TIMESTAMP,
	enabled boolean NOT NULL DEFAULT TRUE,
	type VARCHAR(1)
);

-- insert the users with hashed password.
INSERT INTO
	users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
	
VALUES
(100000000,encode(digest('mypassword','sha1'), 'hex'),'admin','admin',
	'admin@dcmail.ca',DEFAULT,DEFAULT,TRUE,'a');
	
INSERT INTO
users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
VALUES
(100000001,encode(digest('password','sha1'), 'hex'),'eugene','shin',
	'hongju.shin@dcmail.ca',DEFAULT,DEFAULT,TRUE,'s');

INSERT INTO
	users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
VALUES
(100111111,encode(digest('password','sha1'), 'hex'),'Mike','Jones',
	'mikejones@somemail.com',DEFAULT,DEFAULT,TRUE,'s');

INSERT INTO
	users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
VALUES
(100000002,encode(digest('password','sha1'), 'hex'),'Kevin','Smith',
	'mikejones@somemail.com',DEFAULT,DEFAULT,TRUE,'s');
	
INSERT INTO
	users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
VALUES
(100000003,encode(digest('password','sha1'), 'hex'),'Nelson','Cory',
	'nellson@somemail.com',DEFAULT,DEFAULT,TRUE,'s');

INSERT INTO
	users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
VALUES
(200000001,encode(digest('password','sha1'), 'hex'),'Peter','Hector',
	'hector@somemail.com',DEFAULT,DEFAULT,TRUE,'f');

INSERT INTO
	users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
VALUES
(200000002,encode(digest('password','sha1'), 'hex'),'Kyle','Nelson',
	'kylenelson@somemail.com',DEFAULT,DEFAULT,TRUE,'f');

INSERT INTO
	users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
VALUES
(200000003,encode(digest('password','sha1'), 'hex'),'Sara','Beilias',
	'sarabeilias@somemail.com',DEFAULT,DEFAULT,TRUE,'f');
	
INSERT INTO
	users(id,password,firstname,lastname,email,lastaccess,enroldate,enabled,type)
VALUES
(200000004,encode(digest('password','sha1'), 'hex'),'Demian','Willows',
	'demian@somemail.com',DEFAULT,DEFAULT,TRUE,'f');
	
	
	
DROP TABLE IF EXISTS faculty;
-- CREATE the table,
CREATE TABLE faculty(
	id int PRIMARY KEY,
	school_code VARCHAR(10) NOT NULL,
	school_description VARCHAR(255),
	office VARCHAR(255),
	extension int DEFAULT 1234,
	FOREIGN KEY (id) REFERENCES users(id)
);

-- insert the faculteis information into the database, the faculteis's personal information is stored in the users table.
INSERT INTO
	faculty(id, school_code , school_description, office, extension)
VALUES 
	(200000001,'CGC1P1','An Imagenary School', 'Somewhere good place', DEFAULT);

INSERT INTO
	faculty(id, school_code , school_description, office, extension)
VALUES 
	(200000002,'CGC1P2','great school', 'Somewhere good place2', DEFAULT);
	
INSERT INTO
	faculty(id, school_code , school_description, office, extension)
VALUES 
	(200000003,'CGC1P3','nice faculty', 'Somewhere good place3', DEFAULT);
	
INSERT INTO
	faculty(id, school_code , school_description, office, extension)
VALUES 
	(200000004,'CGC1P4','An Imagenary School', 'Somewhere good place4', DEFAULT);

	
	
	
	
	
DROP TABLE IF EXISTS students;
-- CREATE the table,
CREATE TABLE students(
	id int PRIMARY KEY,
	program_code VARCHAR(10) NOT NULL,
	program_description VARCHAR(255),
	year INT,
	FOREIGN KEY (id) REFERENCES users(id)
);

-- insert the students information into the database, the students's personal information is stored in the users table.
INSERT INTO
	students(id, program_code , program_description, year)
VALUES 
	(100000001,'CP','Computer Programmer', 2);

INSERT INTO
	students(id, program_code , program_description, year)
VALUES 
	(100111111,'CSTY','Computer System Technology', 3);

INSERT INTO
	students(id, program_code , program_description, year)
VALUES 
	(100000002,'QCL','Quality Control Supervisor', 1);
	
INSERT INTO
	students(id, program_code , program_description, year)
VALUES 
	(100000003,'QCL','Quality Control Manager', 4);
	