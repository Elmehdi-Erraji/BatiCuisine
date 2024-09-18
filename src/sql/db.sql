create table clients
(
    id             serial
        primary key,
    name           varchar(255) not null,
    address        varchar(255),
    phone          varchar(20),
    isprofessional boolean      not null
);

alter table clients
    owner to "batiCuisine";

create table projects
(
    id           serial
        primary key,
    name         varchar(255) not null,
    profitmargin double precision,
    totalcost    double precision,
    status       projectstatus,
    client_id    integer
        references clients
            on delete cascade
);

alter table projects
    owner to "batiCuisine";

create table composants
(
    id      serial
        primary key,
    name    varchar(255) not null,
    taxrate double precision
);

alter table composants
    owner to "batiCuisine";

create table material
(
    id                 integer default nextval('materiaux_id_seq'::regclass) not null
        constraint materiaux_pkey
            primary key,
    unitcost           double precision,
    quantity           double precision,
    transportcost      double precision,
    qualitycoefficient double precision
)
    inherits (composants);

alter table material
    owner to "batiCuisine";

create table labours
(
    id               integer default nextval('maindoeuvre_id_seq'::regclass) not null
        constraint maindoeuvre_pkey
            primary key,
    hourlyrate       double precision,
    workhourscount   double precision,
    productivityrate double precision
)
    inherits (composants);

alter table labours
    owner to "batiCuisine";

create table quotes
(
    id             integer default nextval('devis_id_seq'::regclass) not null
        constraint devis_pkey
            primary key,
    estimatedprice double precision,
    issuedate      date,
    validitydate   date,
    accepted       boolean,
    project_id     integer
        constraint devis_project_id_fkey
            references projects
            on delete cascade
);

alter table quotes
    owner to "batiCuisine";

