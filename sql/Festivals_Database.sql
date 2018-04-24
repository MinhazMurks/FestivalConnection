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

DROP TRIGGER IF EXISTS trg_check_proper_dates;
CREATE TRIGGER trg_check_proper_dates BEFORE INSERT ON Festival
FOR EACH ROW
  BEGIN
    DECLARE msg VARCHAR(128);
    IF (NEW.end_date < NEW.start_date) THEN
      SET msg = 'Festival table error: End Date is before Start Date';
      SIGNAL SQLSTATE '45000' SET message_text = msg;
    END IF;
  END;

DROP TRIGGER IF EXISTS trg_check_proper_price;
CREATE TRIGGER trg_check_proper_price BEFORE INSERT ON Festival
FOR EACH ROW
  BEGIN
    DECLARE msg VARCHAR(128);
    IF (NEW.price < 0) THEN
      SET msg = 'Festival table error: Price is negative';
      SIGNAL SQLSTATE '45000' SET message_text = msg;
    END IF;
  END;

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
  genre        varchar(20),
  outdoor      bool,
  camping      bool,
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create table Comedy(
  festID       varchar(36) NOT NULL,
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create table Art(
  festID       varchar(36) NOT NULL,
  genre        varchar(20),
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);

create table Beer(
  festID       varchar(36) NOT NULL,
  FOREIGN KEY (festID) REFERENCES Festival(festID)
);


delimiter //
create procedure delete_friend
(friend_id varchar(36))
begin
delete from Friends where user2 = friend_id;
end //
delimiter ;


delimiter //
create procedure delete_bookmark
  (cur_id varchar(36), fest_id varchar(36))
  begin
    delete from bookmarks where userID = cur_id and festID = friend_id;
  end //
delimiter ;

delimiter //
create procedure search_friends
  (cur_user_id varchar(36), searched_name varchar(50))
  begin
    select user_name, friends.user1 as cur_user, users.userID as friend_ids
      from users
        join friends on users.userID = friends.user2
    where friends.user1 = cur_user_id
    and user_name like concat('%', searched_name, '%');
  end //
delimiter ;

-- The SQL script shown here includes all SQL required to --
-- create the database. But most of the queries are run   --
-- from inside our program, including calls to the        --
-- procedures, select statements deletes etc.             --