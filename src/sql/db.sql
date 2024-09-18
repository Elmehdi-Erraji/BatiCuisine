CREATE TABLE Clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    isProfessional BOOLEAN NOT NULL
);

CREATE TYPE projectStatus AS ENUM ('INPROGRESS', 'COMPLETED', 'CANCELLED');

CREATE TABLE Projects (
    id SERIAL PRIMARY KEY,
    projectName VARCHAR(255) NOT NULL,
    profitMargin DOUBLE PRECISION,
    totalCost DOUBLE PRECISION,
    status projectStatus,
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES Clients(id) ON DELETE CASCADE
);

CREATE TABLE Composants (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            taxRate DOUBLE PRECISION
);

CREATE TABLE Materiaux (
                           id SERIAL PRIMARY KEY,
                           unitCost DOUBLE PRECISION,
                           quantity DOUBLE PRECISION,
                           transportCost DOUBLE PRECISION,
                           qualityCoefficient DOUBLE PRECISION
) inherits (Composants);

CREATE TABLE MainDœuvre (
                            id SERIAL PRIMARY KEY,
                            hourlyRate DOUBLE PRECISION,
                            workHoursCount DOUBLE PRECISION,
                            productivityRate DOUBLE PRECISION
) inherits (Composants);


CREATE TABLE Devis (
                       id SERIAL PRIMARY KEY,
                       estimatedPrice DOUBLE PRECISION,
                       issueDate DATE,
                       validityDate DATE,
                       accepted BOOLEAN,
                       project_id INT,
                       FOREIGN KEY (project_id) REFERENCES Projets(id) ON DELETE CASCADE
);
