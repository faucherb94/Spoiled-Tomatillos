DROP DATABASE IF EXISTS Spoiled_Tomatillos_Backend;
CREATE DATABASE Spoiled_Tomatillos_Backend;
USE Spoiled_Tomatillos_Backend;

DROP TABLE IF EXISTS UserAccount;
CREATE TABLE UserAccount (
		UserID INT AUTO_INCREMENT,
		Account_ID VARCHAR(50) UNIQUE,
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
		Account_ID VARCHAR(50),
		PRIMARY KEY (UserID, Account_ID),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID) ON DELETE CASCADE,
		FOREIGN KEY (Account_ID) REFERENCES UserAccount(Account_ID) ON DELETE CASCADE
		);

DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups (
		Owner INT,
		GroupName VARCHAR(75),
		Member INT,
		Description VARCHAR(500),
		PRIMARY KEY (Owner, GroupName, Member),
		FOREIGN KEY (Owner) REFERENCES UserAccount(UserID) ON DELETE CASCADE,
		FOREIGN KEY (Member) REFERENCES UserAccount(UserID) ON DELETE CASCADE
		);

DROP TABLE IF EXISTS MovieReview;
CREATE TABLE MovieReview (
		UserID INT,
		MovieTitle VARCHAR(255) UNIQUE,
		Review VARCHAR(1000),
		PRIMARY KEY(UserID, MovieTitle),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID)
		);

DROP PROCEDURE IF EXISTS fetch_friends;

CREATE PROCEDURE fetch_friends (user INT)
		SELECT Account_ID FROM Relationships WHERE UserID = user;

DROP PROCEDURE IF EXISTS add_friend;

CREATE PROCEDURE add_friend (adder INT(255), username VARCHAR(50))
	INSERT INTO Relationships VALUES (adder, username);

DROP PROCEDURE IF EXISTS list_group_members;

CREATE PROCEDURE list_group_members (owner INT(255), groupid VARCHAR(75))
	SELECT Username FROM Groups JOIN UserAccount ON Member = UserAccount WHERE Owner = owner AND GroupName = groupid;

