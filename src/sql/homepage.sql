create table homepage(
  ip binary(4) not null PRIMARY key,
  url VARCHAR (200) not null,
  host VARCHAR (100) not null,
  keyword VARCHAR (200),
  description VARCHAR (200),
  charset VARCHAR (20) not null,
  gettedData DateTime not null,
  txt  MEDIUMTEXT,
  title VARCHAR (200)
);