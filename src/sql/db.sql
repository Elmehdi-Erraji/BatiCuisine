CREATE TABLE clients (
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    address        VARCHAR(255),
    phone          VARCHAR(20),
    isprofessional BOOLEAN NOT NULL
);

ALTER TABLE clients OWNER TO "batiCuisine";

CREATE TABLE projects (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    profitmargin DOUBLE PRECISION,
    totalcost    DOUBLE PRECISION,
    status       projectstatus,
    client_id    INTEGER REFERENCES clients(id) ON DELETE CASCADE
);

ALTER TABLE projects OWNER TO "batiCuisine";

CREATE TABLE composants (
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    taxrate DOUBLE PRECISION
);

ALTER TABLE composants OWNER TO "batiCuisine";

CREATE TABLE material (
    id                 INTEGER DEFAULT nextval('materiaux_id_seq'::regclass) NOT NULL PRIMARY KEY,
    unitcost           DOUBLE PRECISION,
    quantity           DOUBLE PRECISION,
    transportcost      DOUBLE PRECISION,
    qualitycoefficient DOUBLE PRECISION
) INHERITS (composants);

ALTER TABLE material OWNER TO "batiCuisine";

CREATE TABLE labours (
    id               INTEGER DEFAULT nextval('maindoeuvre_id_seq'::regclass) NOT NULL PRIMARY KEY,
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

ALTER TABLE quotes OWNER TO "batiCuisine";
