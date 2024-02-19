create table souvenirs.manufacturers
(
    id      varchar(8)   not null
        primary key,
    name    varchar(100) not null,
    country varchar(100) not null
);

create table souvenirs.souvenirs
(
    id                     varchar(8)   not null
        primary key,
    name                   varchar(100) not null,
    manufacturer_s_details varchar(8)   not null,
    date                   date         not null,
    price                  double       not null,
    constraint souvenirs_manufacturers_id_fk
        foreign key (manufacturer_s_details) references souvenirs.manufacturers (id)
            on delete cascade
);

