DROP DATABASE IF EXISTS Spoiled_Tomatillos_Backend;
CREATE DATABASE Spoiled_Tomatillos_Backend;
USE Spoiled_Tomatillos_Backend;

DROP TABLE IF EXISTS UserAccount;
CREATE TABLE UserAccount (
		UserID INT AUTO_INCREMENT,
		Account VARCHAR(50) UNIQUE,
		Username VARCHAR(50) UNIQUE, 
		FirstName VARCHAR(50),
		LastName VARCHAR(50),
		Role ENUM('default', 'group admin', 'admin', 'moderator'),
		DisplayPicture LONGBLOB,
		PRIMARY KEY(UserID)
		);

DROP TABLE IF EXISTS Relationships;
CREATE TABLE Relationships (
		UserID INT,
		Account VARCHAR(50),
		PRIMARY KEY (UserID, Account),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID) ON DELETE CASCADE,
		FOREIGN KEY (Account) REFERENCES UserAccount(Account) ON DELETE CASCADE
		);

DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups (
		Owner INT,
		GroupName VARCHAR(75) UNIQUE,
		Description VARCHAR(500),
		PRIMARY KEY (Owner, GroupName),
		FOREIGN KEY (Owner) REFERENCES UserAccount(UserID) ON DELETE CASCADE
		);

DROP TABLE IF EXISTS GroupMemberships;
CREATE TABLE GroupMemberships (
		Owner INT,
		GroupName VARCHAR(75),
		Member INT,
		PRIMARY KEY (Owner, GroupName, Member),
		FOREIGN KEY (Owner, GroupName) REFERENCES Groups(Owner, GroupName) ON DELETE CASCADE
		);

DROP TABLE IF EXISTS MovieReview;
CREATE TABLE MovieReview (
		UserID INT,
		MovieTitle VARCHAR(255) UNIQUE,
		Review VARCHAR(1000),
		StarRating Enum('1', '2', '3', '4', '5'),
		PRIMARY KEY(UserID, MovieTitle),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID)
		);

DROP PROCEDURE IF EXISTS fetch_friends;
CREATE PROCEDURE fetch_friends (user INT)
		SELECT Account_ID FROM Relationships WHERE UserID = user;

DROP PROCEDURE IF EXISTS add_friend;
CREATE PROCEDURE add_friend (adder INT, username VARCHAR(50))
	INSERT INTO Relationships VALUES (adder, username);

DROP PROCEDURE IF EXISTS unfriend;
CREATE PROCEDURE unfriend (remover INT, username VARCHAR(50))
	DELETE FROM Relationships WHERE UserID = remover AND Account = username;

DROP PROCEDURE IF EXISTS create_group;
CREATE PROCEDURE create_group (owner INT, groupName VARCHAR(75), description VARCHAR(500))
	INSERT INTO Groups VALUES (owner, groupName, description);
	
DROP PROCEDURE IF EXISTS add_to_group;
CREATE PROCEDURE add_to_group (owner INT, groupName VARCHAR(75), addee INT)
	INSERT INTO GroupMemberships VALUES (owner, groupName, addee);

DROP PROCEDURE IF EXISTS list_group_members;
CREATE PROCEDURE list_group_members (owner INT, groupid VARCHAR(75))
	SELECT Username FROM GroupMemberships JOIN UserAccount ON Member = UserAccount WHERE Owner = owner AND GroupName = groupid;

