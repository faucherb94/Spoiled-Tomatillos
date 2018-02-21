DROP DATABASE IF EXISTS Spoiled_Tomatillos_Backend;
CREATE DATABASE Spoiled_Tomatillos_Backend;
USE Spoiled_Tomatillos_Backend;

DROP TABLE IF EXISTS UserAccount;
CREATE TABLE UserAccount (
		UserID INT AUTO_INCREMENT,
		Email VARCHAR(50) UNIQUE,
		Username VARCHAR(50) UNIQUE, 
		FirstName VARCHAR(50),
		LastName VARCHAR(50),
		CreatedAt TIMESTAMP,
		UpdatedAt TIMESTAMP,
		Role ENUM('default', 'group admin', 'admin', 'moderator'),
		DisplayPicture LONGBLOB,
		PRIMARY KEY(UserID)
		);

DROP TABLE IF EXISTS Relationships;
CREATE TABLE Relationships (
		Email VARCHAR(50),
		FriendEmail VARCHAR(50),
		PRIMARY KEY (Email, FriendEmail),
		FOREIGN KEY (Email) REFERENCES UserAccount(Email) ON DELETE CASCADE,
		FOREIGN KEY (FriendEmail) REFERENCES UserAccount(Email) ON DELETE CASCADE
		);

DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups (
		OwnerEmail VARCHAR(50),
		GroupName VARCHAR(75) UNIQUE,
		Description VARCHAR(500),
		PRIMARY KEY (OwnerEmail, GroupName),
		FOREIGN KEY (OwnerEmail) REFERENCES UserAccount(Email) ON DELETE CASCADE
		);

DROP TABLE IF EXISTS GroupMemberships;
CREATE TABLE GroupMemberships (
		OwnerEmail VARCHAR(50),
		GroupName VARCHAR(75),
		MemberEmail VARCHAR(50),
		PRIMARY KEY (OwnerEmail, GroupName, MemberEmail),
		FOREIGN KEY (OwnerEmail, GroupName) REFERENCES Groups(OwnerEmail, GroupName) ON DELETE CASCADE
		);

DROP TABLE IF EXISTS MovieReview;
CREATE TABLE MovieReview (
		Email VARCHAR(50),
		MovieTitle VARCHAR(255) UNIQUE,
		Review VARCHAR(1000),
		StarRating Enum('1', '2', '3', '4', '5'),
		PRIMARY KEY(Email, MovieTitle),
		FOREIGN KEY (Email) REFERENCES UserAccount(Email)
		);


DROP PROCEDURE IF EXISTS create_user;
CREATE PROCEDURE create_user(email VARCHAR(50), username VARCHAR(50), firstname VARCHAR(50), lastname VARCHAR(50))
	INSERT INTO UserAccount (Email, Username, FirstName, LastName, Role, CreatedAt) VALUES (email, username, firstname, lastname, 'default', NOW());

DROP PROCEDURE IF EXISTS edit_names;
CREATE PROCEDURE edit_names(email VARCHAR(50), fname VARCHAR(50), lname VARCHAR(50))
	UPDATE UserAccount SET FirstName = fname, LastName = lname, UpdatedAt = CURDATE() WHERE Email = email;

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
	SELECT FriendAccount FROM Relationships WHERE OwnerEmail = email;

DROP PROCEDURE IF EXISTS add_friend;
CREATE PROCEDURE add_friend (adder VARCHAR(50), addee VARCHAR(50))
	INSERT INTO Relationships (OwnerEmail, FriendEmail) VALUES (adder, addee);

DROP PROCEDURE IF EXISTS unfriend;
CREATE PROCEDURE unfriend (remover VARCHAR(50), removee VARCHAR(50))
	DELETE FROM Relationships WHERE OwnerEmail = remover AND FriendEmail = removee;

DROP PROCEDURE IF EXISTS create_group;
CREATE PROCEDURE create_group (owner VARCHAR(50), groupName VARCHAR(75), description VARCHAR(500))
	INSERT INTO Groups (OwnerEmail, GroupName, Description) VALUES (owner, groupName, description);
	
DROP PROCEDURE IF EXISTS add_to_group;
CREATE PROCEDURE add_to_group (owner VARCHAR(50), groupName VARCHAR(75), addee VARCHAR(50))
	INSERT INTO GroupMemberships (OwnerEmail, GroupName, MemberEmail) VALUES (owner, groupName, addee);

DROP PROCEDURE IF EXISTS list_group_members;
CREATE PROCEDURE list_group_members (owner VARCHAR(50), groupid VARCHAR(75))
	SELECT MemberEmail FROM Groups JOIN GroupMemberships ON Groups.OwnerEmail = GroupMemberships.OwnerEmail WHERE OwnerEmail = owner AND GroupName = groupid;
