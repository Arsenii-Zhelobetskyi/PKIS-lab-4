create table souvenirs.manufacturers
(
    id      int auto_increment
        primary key,
    name    varchar(100) not null,
    country varchar(100) not null
);

create table souvenirs.souvenirs
(
    id                     int auto_increment
        primary key,
    name                   varchar(100) not null,
    manufacturer_s_details int          not null,
    date                   date         not null,
    price                  int          not null,
    constraint souvenirs_manufacturers_id_fk
        foreign key (manufacturer_s_details) references souvenirs.manufacturers (id)
            on delete cascade
);

