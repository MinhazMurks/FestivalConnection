create database if not exists festivals_project;
use festivals_project;

create table Festival(
  festID varchar(36) primary key,
  name   varchar(50),
  production_comp varchar(36) references Users(userID),
  start_date date,
  end_date date,
  price decimal(9,2)
);

create table Users(
  userID         varchar(36) primary key,
  user_name      varchar(50) unique,
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

create table Providers(
  festID       varchar(36) references Festival(festID),
  name         varchar(50),
  primary key (festID, name)
);

create table Music(
  festID       varchar(36) NOT NULL,
  musicians    varchar(20) references Providers(festID, name),
  type_fest    char(10),
  genre        varchar(20),
  outdoor      bool,
  camping      bool,
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create trigger type_check_music before insert on Music
  for each row set NEW.type_fest = 'Music'
;

create table Comedy(
  festID       varchar(36) NOT NULL,
  type_fest    char(10),
  comedians    varchar(50)  references Providers(festID, name),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create trigger type_check_comedy before insert on Comedy
  for each row set NEW.type_fest = 'Comedy'
;

create table Art(
  festID       varchar(36) NOT NULL,
  type_fest    char(10),
  artist       varchar(20) references Providers(festID, name),
  genre        varchar(20),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create trigger type_check_art before insert on Art
  for each row set NEW.type_fest = 'Art'
;

create table Beer(
  festID       varchar(36) NOT NULL,
  type_fest    char(10),
  breweries    varchar(50) references Providers(festID, name),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create trigger type_check before insert on Beer
  for each row set NEW.type_fest = 'Beer'
;