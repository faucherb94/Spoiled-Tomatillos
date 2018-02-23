DROP DATABASE IF EXISTS Spoiled_Tomatillos_Backend;
CREATE DATABASE Spoiled_Tomatillos_Backend;
USE Spoiled_Tomatillos_Backend;

DROP TABLE IF EXISTS UserAccount;
CREATE TABLE UserAccount (
		Email VARCHAR(50) UNIQUE,
		Username VARCHAR(50) UNIQUE, 
		FirstName VARCHAR(50),
		LastName VARCHAR(50),
		Hometown VARCHAR(50),
		DisplayPicture LONGBLOB,
		Role ENUM('default', 'group admin', 'admin', 'moderator'),
		CreatedAt TIMESTAMP,
		UpdatedAt TIMESTAMP,
		PRIMARY KEY(Email)
		);

DROP TABLE IF EXISTS Relationships;
CREATE TABLE Relationships (
		Email VARCHAR(50),
		FriendEmail VARCHAR(50),
		PRIMARY KEY (Email),
		FOREIGN KEY (Email) REFERENCES UserAccount(Email) ON DELETE CASCADE, ON UPDATE CASCADE,
		FOREIGN KEY (FriendEmail) REFERENCES UserAccount(Email) ON DELETE CASCADE, ON UPDATE CASCADE,
		);

DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups (
		Email VARCHAR(50),
		GroupName VARCHAR(75) UNIQUE,
		Description VARCHAR(500),
		PRIMARY KEY (Email, GroupName)
		FOREIGN KEY (Email) REFERENCES UserAccount(Email) ON DELETE CASCADE, ON UPDATE CASCADE
		);

DROP TABLE IF EXISTS GroupMemberships;
CREATE TABLE GroupMemberships (
		Email VARCHAR(50),
		GroupName VARCHAR(75),
		MemberEmail VARCHAR(50),
		PRIMARY KEY (Email, GroupName, MemberEmail),
		FOREIGN KEY (Email, GroupName) REFERENCES Groups(Email, GroupName) ON DELETE CASCADE, ON UPDATE CASCADE
		);

DROP TABLE IF EXISTS MovieReview;
CREATE TABLE MovieReview (
		Email VARCHAR(50),
		MovieTitle VARCHAR(255) UNIQUE,
		Review VARCHAR(1000),
		StarRating Enum('1', '2', '3', '4', '5'),
		PRIMARY KEY(Email, MovieTitle),
		FOREIGN KEY (Email) REFERENCES UserAccount(Email) ON DELETE CASCADE, ON UPDATE CASCADE
		);


DROP PROCEDURE IF EXISTS create_user;
CREATE PROCEDURE create_user(email VARCHAR(50), username VARCHAR(50), fname VARCHAR(50), lname VARCHAR(50))
	INSERT INTO UserAccount (Email, Username, FirstName, LastName, Role, CreatedAt) VALUES (email, username, fname, lname, 'default', NOW());

DROP PROCEDURE OF EXISTS edit_hometown;
CREATE PROCEDURE edit_hometown(email VARCHAR(50), hometown VARCHAR(50))
	UPDATE UserAccount SET Hometown = hometown, UpdatedAt = NOW() WHERE Email = email;

DROP PROCEDURE IF EXISTS edit_names;
CREATE PROCEDURE edit_names(email VARCHAR(50), fname VARCHAR(50), lname VARCHAR(50))
	UPDATE UserAccount SET FirstName = fname, LastName = lname, UpdatedAt = NOW() WHERE Email = email;

DROP PROCEDURE IF EXISTS edit_username;
CREATE PROCEDURE edit_username(email VARCHAR(50), username VARCHAR(50))
	UPDATE UserAccount SET Username = username, UpdatedAt = NOW() WHERE Email = email;

DROP PROCEDURE IF EXISTS edit_pic;
CREATE PROCEDURE edit_pic(email VARCHAR(50), pic LONGBLOB)
	UPDATE UserAccount SET DisplayPicture = pic, UpdatedAt = NOW() WHERE Email = email;

DROP PROCEDURE IF EXISTS rate_movie;
CREATE PROCEDURE rate_movie(email VARCHAR(50), title VARCHAR(255), rating Enum('1', '2', '3', '4', '5'))
	INSERT INTO MovieReview (Email, MovieTitle, StarRating) VALUES (email, title, rating);

DROP PROCEDURE IF EXISTS fetch_friends;
CREATE PROCEDURE fetch_friends (email VARCHAR(50))
	SELECT FriendEmail FROM Relationships WHERE Email = email;

DROP PROCEDURE IF EXISTS add_friend;
CREATE PROCEDURE add_friend (adder VARCHAR(50), addee VARCHAR(50))
	INSERT INTO Relationships (Email, FriendEmail) VALUES (adder, addee);

DROP PROCEDURE IF EXISTS unfriend;
CREATE PROCEDURE unfriend (remover VARCHAR(50), removee VARCHAR(50))
	DELETE FROM Relationships WHERE Email = remover AND FriendEmail = removee;

DROP PROCEDURE IF EXISTS create_group;
CREATE PROCEDURE create_group (owner VARCHAR(50), groupName VARCHAR(75), description VARCHAR(500))
	INSERT INTO Groups (Email, GroupName, Description) VALUES (owner, groupName, description);
	
DROP PROCEDURE IF EXISTS add_to_group;
CREATE PROCEDURE add_to_group (owner VARCHAR(50), groupName VARCHAR(75), addee VARCHAR(50))
	INSERT INTO GroupMemberships (Email, GroupName, MemberEmail) VALUES (owner, groupName, addee);

DROP PROCEDURE IF EXISTS list_group_members;
CREATE PROCEDURE list_group_members (owner VARCHAR(50), groupid VARCHAR(75))
	SELECT MemberEmail FROM Groups JOIN GroupMemberships ON Groups.Email = GroupMemberships.Email WHERE Email = owner AND GroupName = groupid;
