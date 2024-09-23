CREATE TABLE clients (
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    address        VARCHAR(255),
    phone          VARCHAR(20),
    isprofessional BOOLEAN NOT NULL
);


CREATE TABLE projects (
    id           SERIAL PRIMARY KEY,
    projectname          VARCHAR(255) NOT NULL,
    profit   DOUBLE PRECISION,
    totalcost    DOUBLE PRECISION,
    status       projectstatus,
    client_id    INTEGER REFERENCES clients(id) ON DELETE CASCADE
    discount DOUBLE PRECISION
);


CREATE TABLE components  (
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    tax_rate DOUBLE PRECISION
    componenttype VARCHAR(50),
    project_id INTEGER REFERENCES projects(id) ON DELETE CASCADE
);


CREATE TABLE materials  (
    unitcost           DOUBLE PRECISION,
    quantity           DOUBLE PRECISION,
    transportcost      DOUBLE PRECISION,
    qualitycoefficient DOUBLE PRECISION
) INHERITS (composants);


CREATE TABLE labours (
    hourlyrate       DOUBLE PRECISION,
    workhourscount   DOUBLE PRECISION,
    productivityrate DOUBLE PRECISION
) INHERITS (composants);

ALTER TABLE labours OWNER TO "batiCuisine";

CREATE TABLE quotes (
    id             INTEGER DEFAULT nextval('devis_id_seq'::regclass) NOT NULL PRIMARY KEY,
    estimatedprice DOUBLE PRECISION,
    issuedate      DATE,
    validitydate   DATE,
    accepted       BOOLEAN,
    project_id     INTEGER REFERENCES projects(id) ON DELETE CASCADE
);

-- Insert statements

INSERT INTO clients (name, address, phone, isprofessional) VALUES('Joe', '123 Main St', '55512343434', TRUE);

INSERT INTO projects (projectname, profit, totalcost, status, discount, client_id) VALUES ('Kitchen ', 15000.00, 12000.00, 'INPROGRESS', 500.00, 1);

INSERT INTO materials (name, tax_rate, componenttype, project_id, unitcost, quantity, transportcost, qualitycoefficient) VALUES('Wood', 18.00, 'MATERIAL', 1, 50.00, 100, 100.00, 1.2);

INSERT INTO labour (name, tax_rate, componenttype, project_id, hourlyrate, workhourscount, productivityrate) VALUES('Carpentry', 10.00, 'LABOUR', 1, 25.00, 40, 1.1);

INSERT INTO quotes (estimatedprice, issuedate, validitydate, accepted, project_id) VALUES (14000.00, '2024-01-01', '2024-01-15', TRUE, 1);
