create database if not exists festivals_project;
use festivals_project;

drop table Festival;
drop table Users;
drop table Friends;
drop table Bookmarks;
drop table Location;
drop table Providers;
drop table Music;
drop table Comedy;
drop table Art;
drop table Beer;

create table Festival(
  festID varchar(36) primary key,
  fest_type varchar(10),
  name   varchar(50),
  production_comp varchar(36) references Users(userID),
  start_date date,
  end_date date,
  price decimal(9,2)
);

create table Users(
  userID         varchar(36) primary key,
  user_name      varchar(50) unique,
  password       varchar(12),
  birthdate      date,
  is_company     bool
);

create table Friends(
  user1         varchar(36) references Users(userID),
  user2         varchar(36) references Users(userID),
  primary key (user1, user2)
);

create table Bookmarks(
  userID       varchar(36) references Users(userID),
  festID       varchar(36) references Festival(festID),
  primary key (userID, festID)
);

create table Location(
  userID         VARCHAR(36),
  festId         VARCHAR(36),
  city           varchar(20),
  state          varchar(20),
  streetAddress  varchar(50),
  zip            INTEGER(5),
  CONSTRAINT UNIQUE (userID, festID),
  CONSTRAINT CHECK (userID IS NOT NULL OR festID IS NOT NULL),
  FOREIGN KEY (userID) REFERENCES Users(userID),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

DROP TRIGGER IF EXISTS trg_check_location_id;

CREATE TRIGGER trg_check_location_id BEFORE INSERT ON Location
  FOR EACH ROW
  BEGIN
    DECLARE msg VARCHAR(128);
    IF ((NEW.userID IS NOT NULL) AND (NEW.festId IS NOT NULL)) THEN
      SET msg = 'Location table error: Either userID or festID must be NULL';
      SIGNAL SQLSTATE '45000' SET message_text = msg;
    END IF;
  END;

create table Providers(
  festID       varchar(36) references Festival(festID),
  name         varchar(50),
  primary key (festID, name)
);

create table Music(
  festID       varchar(36) NOT NULL,
  type_fest    char(10)     references Festival(fest_type),
  musicians    varchar(20) references Providers(festID, name),
  genre        varchar(20),
  outdoor      bool,
  camping      bool,
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create table Comedy(
  festID       varchar(36) NOT NULL,
  type_fest    char(10)     references Festival(fest_type),
  comedians    varchar(50)  references Providers(festID, name),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create table Art(
  festID       varchar(36) NOT NULL,
  type_fest    char(10)     references Festival(fest_type),
  artist       varchar(20) references Providers(festID, name),
  genre        varchar(20),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create table Beer(
  festID       varchar(36) NOT NULL,
  type_fest    char(10)     references Festival(fest_type),
  breweries    varchar(50) references Providers(festID, name),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

select * from users;

insert into users(userID, user_name, password, birthdate, is_company)
values (uuid(), 'MinhazMurks', 'pass', '1990-08-17', false);

delete from users where user_name = 'MinhaMurks';

insert into location(userID, festId, city, state, streetAddress, zip) VALUES ('6306800b-46c1-11e8-b215-28841f0de34e', null, 'Richmond', 'VA', null, null);

SELECT userID, password FROM Users WHERE user_name = 'MinhazMurks' and password = 'pass';

select * from users;

select * from Friends;

SELECT * FROM Friends;