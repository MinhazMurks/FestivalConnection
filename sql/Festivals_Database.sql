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
  festID Decimal(9,0) primary key,
  location varchar(50),
  production_comp varchar(36) references Users(userID),
  start_date date,
  end_date date,
  price decimal(9,2)
);

create table Users(
  userID         varchar(36) primary key,
  user_name      varchar(50) unique,
  birthdate      date,
  user_location  varchar(50) references Location(city, state, streetAddress),
  is_company     bool
);

create table Friends(
  user1         varchar(36) references Users(userID),
  user2         varchar(36) references Users(userID),
  primary key (user1, user2)
);

create table Bookmarks(
  userID       varchar(36) references Users(userID),
  festID       Decimal(9,0) references Festival(festID),
  primary key (userID, festID)
);

create table Location(
  city           varchar(20),
  state          varchar(20),
  streetAddress  varchar(50),
  address        varchar(20),
  primary key (city, state, streetaddress)
);

create table Providers(
  festID       Decimal(9, 0) references Festival(festID),
  name         varchar(50),
  primary key (festID, name)
);

create table Music(
  festID       Decimal(9,0) references Festival(festID),
  musicians    varchar(20)  references Providers(festID, name),
  type_fest    char(10),
  genre        varchar(20),
  outdoor      bool,
  camping      bool
);

create trigger type_check_music before insert on Music
  for each row set NEW.type_fest = 'Music';

create table Comedy(
  festID       Decimal(9,0) references Festival(festID),
  type_fest    char(10),
  comedians    varchar(50)  references Providers(festID, name)
);

create trigger type_check_comedy before insert on Comedy
  for each row set NEW.type_fest = 'Comedy';

create table Art(
  festID       Decimal(9,0) references Festival(festID),
  type_fest    char(10),
  artist       varchar(20) references Providers(festID, name),
  genre        varchar(20)
);

create trigger type_check_art before insert on Art
  for each row set NEW.type_fest = 'Art';

create table Beer(
  festID       Decimal(9,0) references Festival(festID),
  type_fest    char(10),
  breweries    varchar(50) references Providers(festID, name)
);

create trigger type_check before insert on Beer
  for each row set NEW.type_fest = 'Beer';

create view full_festival_view as
  select *
  from Festival
    join music on Festival.festID = Music.festID
    join art on Festival.festID = art.festID
    join beer on Festival.festID = Beer.festID
    join comedy on Festival.festID = Comedy.festID
    join providers on Festival.festID = Providers.festID;

