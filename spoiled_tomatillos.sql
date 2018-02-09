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
		CreatedAt DATE,
		UpdatedAt DATE,
		Role ENUM('default', 'group admin', 'admin', 'moderator'),
		DisplayPicture LONGBLOB,
		PRIMARY KEY(UserID)
		);

DROP TABLE IF EXISTS Relationships;
CREATE TABLE Relationships (
		UserID INT,
		Friend INT,
		PRIMARY KEY (UserID, Friend),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID) ON DELETE CASCADE,
		FOREIGN KEY (Friend) REFERENCES UserAccount(UserID) ON DELETE CASCADE
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


DROP PROCEDURE IF EXISTS create_user;
CREATE PROCEDURE create_user(account VARCHAR(50), username VARCHAR(50), firstname VARCHAR(50), lastname VARCHAR(50))
	INSERT INTO UserAccount (Account, Username, FirstName, LastName, Role, CreatedAt) VALUES (account, username, firstname, lastname, 'default', CURDATE());

DROP PROCEDURE IF EXISTS edit_names;
CREATE PROCEDURE edit_names(fname VARCHAR(50), lname VARCHAR(50), username VARCHAR(50))
	UPDATE UserAccount SET FirstName = fname, LastName = lname, UpdatedAt = CURDATE() WHERE Username = username;

DROP PROCEDURE IF EXISTS edit_username;
CREATE PROCEDURE edit_username(username VARCHAR(50), userid INT)
	UPDATE UserAccount SET Username = name, UpdatedAt = CURDATE() WHERE UserID = userid;

DROP PROCEDURE IF EXISTS edit_pic;
CREATE PROCEDURE edit_pic(username VARCHAR(50), pic LONGBLOB)
	UPDATE UserAccount SET DisplayPicture = pic, UpdatedAt = CURDATE() WHERE Username = username;

DROP PROCEDURE IF EXISTS rate_movie;
CREATE PROCEDURE rate_movie(user INT, title VARCHAR(255), rating Enum('1', '2', '3', '4', '5'))
	INSERT INTO MovieReview (UserID, MovieTitle, StarRating) VALUES (user, title, rating);

DROP PROCEDURE IF EXISTS fetch_friends;
CREATE PROCEDURE fetch_friends (user INT)
	SELECT Account_ID FROM Relationships WHERE UserID = user;

DROP PROCEDURE IF EXISTS add_friend;
CREATE PROCEDURE add_friend (adder INT, addee INT)
	INSERT INTO Relationships (UserID, Account) VALUES (adder, addee);

DROP PROCEDURE IF EXISTS unfriend;
CREATE PROCEDURE unfriend (remover INT, username VARCHAR(50))
	DELETE FROM Relationships WHERE UserID = remover AND Account = username;

DROP PROCEDURE IF EXISTS create_group;
CREATE PROCEDURE create_group (owner INT, groupName VARCHAR(75), description VARCHAR(500))
	INSERT INTO Groups (Owner, GroupName, Description) VALUES (owner, groupName, description);
	
DROP PROCEDURE IF EXISTS add_to_group;
CREATE PROCEDURE add_to_group (owner INT, groupName VARCHAR(75), addee INT)
	INSERT INTO GroupMemberships (Owner, GroupName, Member) VALUES (owner, groupName, addee);

DROP PROCEDURE IF EXISTS list_group_members;
CREATE PROCEDURE list_group_members (owner INT, groupid VARCHAR(75))
	SELECT Username FROM GroupMemberships JOIN UserAccount ON Member = UserAccount WHERE Owner = owner AND GroupName = groupid;

