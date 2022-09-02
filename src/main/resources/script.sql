create table "Invoice"
(
    id      varchar not null
        constraint invoice_pk
            primary key,
    created timestamp,
    price   double precision
)
    using ???;

alter table "Invoice"
    owner to postgres;

create table "BusinessAuto"
(
    id                  varchar not null
        constraint businessauto_pk
            primary key,
    model               varchar,
    manufacturer        varchar,
    price               double precision,
    business_class_auto varchar,
    count               integer,
    foreign_key         varchar
        constraint businessauto_invoice_id_fk
            references "Invoice"
)
    using ???;

alter table "BusinessAuto"
    owner to postgres;

create table "Auto"
(
    id           varchar not null
        constraint auto_pk
            primary key,
    model        varchar,
    manufacturer varchar,
    price        double precision,
    body_type    varchar,
    count        integer,
    currency     varchar,
    created      timestamp,
    foreign_key  varchar
        constraint auto_invoice_id_fk
            references "Invoice"
)
    using ???;

alter table "Auto"
    owner to postgres;

create table "SportAuto"
(
    id           varchar not null
        constraint sportauto_pk
            primary key,
    model        varchar,
    manufacturer varchar,
    price        double precision,
    body_type    varchar,
    max_speed    integer,
    count        integer,
    foreign_key  varchar
        constraint sportauto_invoice_id_fk
            references "Invoice"
)
    using ???;

alter table "SportAuto"
    owner to postgres;


