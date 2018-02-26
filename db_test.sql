DROP DATABASE IF EXISTS Spoiled_Tomatillos_Backend;
CREATE DATABASE Spoiled_Tomatillos_Backend;
USE Spoiled_Tomatillos_Backend;

DROP TABLE IF EXISTS UserAccount;
CREATE TABLE UserAccount (
		UserID INT AUTO_INCREMENT,
		Email VARCHAR(50) UNIQUE,
		Username VARCHAR(50) UNIQUE, 
		FirstName VARCHAR(50) NOT NULL,
		LastName VARCHAR(50) NOT NULL,
		Hometown VARCHAR(50),
		DisplayPicture LONGBLOB,
		Role ENUM('default', 'group admin', 'admin', 'moderator'),
		CreatedAt TIMESTAMP,
		UpdatedAt TIMESTAMP,
		PRIMARY KEY(UserID)
		);

DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups (
		UserID INT,
		GroupName VARCHAR(75),
		Description VARCHAR(500),
		PRIMARY KEY (UserID, GroupName),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID) 
		ON DELETE CASCADE ON UPDATE CASCADE
		);
	
DROP TABLE IF EXISTS Relationships;
CREATE TABLE Relationships (
		UserID INT,
		MemberID INT,
		PRIMARY KEY (UserID, MemberID),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID) 
		ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (MemberID) REFERENCES UserAccount(UserID) 
		ON DELETE CASCADE ON UPDATE CASCADE
		);

DROP TABLE IF EXISTS GroupMemberships;
CREATE TABLE GroupMemberships (
		UserID INT,
		GroupName VARCHAR(75),
		MemberID INT,
		PRIMARY KEY (UserID, GroupName, MemberID),
		FOREIGN KEY (UserID, GroupName) REFERENCES Groups(UserID, GroupName) 
		ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (MemberID) REFERENCES UserAccount(UserID)
		ON DELETE CASCADE ON UPDATE CASCADE
		);

DROP TABLE IF EXISTS MovieRating;
CREATE TABLE MovieRating (
		UserID INT,
		MovieTitle VARCHAR(255),
		ReleaseDate DATE,
		StarRating Enum('1', '2', '3', '4', '5'),
		PRIMARY KEY(UserID, MovieTitle, ReleaseDate),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID) 
		ON DELETE CASCADE ON UPDATE CASCADE
		);

DROP TABLE IF EXISTS MovieReview;
CREATE TABLE MovieReview (
		UserID INT,
		MovieTitle VARCHAR(255),
		ReleaseDate DATE,
		Review VARCHAR(1000),
		PRIMARY KEY(UserID, MovieTitle, ReleaseDate),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID) 
		ON DELETE CASCADE ON UPDATE CASCADE
		);

DROP PROCEDURE IF EXISTS create_user;
CREATE PROCEDURE create_user(eml VARCHAR(50), usernm VARCHAR(50), fname VARCHAR(50), lname VARCHAR(50))
	INSERT INTO UserAccount (Email, Username, FirstName, LastName, Role, CreatedAt) VALUES (eml, usernm, fname, lname, 'default', NOW());

DROP PROCEDURE IF EXISTS edit_hometown;
CREATE PROCEDURE edit_hometown(id INT, town VARCHAR(50))
	UPDATE UserAccount SET Hometown = town, UpdatedAt = NOW() WHERE UserID = id; 

DROP PROCEDURE IF EXISTS edit_names;
CREATE PROCEDURE edit_names(id INT, fname VARCHAR(50), lname VARCHAR(50))
	UPDATE UserAccount SET FirstName = fname, LastName = lname, UpdatedAt = NOW() WHERE UserID = id;

DROP PROCEDURE IF EXISTS edit_username;
CREATE PROCEDURE edit_username(id INT, usernm VARCHAR(50))
	UPDATE UserAccount SET Username = usernm, UpdatedAt = NOW() WHERE UserID = id;

DROP PROCEDURE IF EXISTS edit_pic;
CREATE PROCEDURE edit_pic(id INT, pic LONGBLOB)
	UPDATE UserAccount SET DisplayPicture = pic, UpdatedAt = NOW() WHERE UserID = id;

DROP PROCEDURE IF EXISTS rate_movie;
CREATE PROCEDURE rate_movie(id INT, title VARCHAR(255), released DATE, rating Enum('1', '2', '3', '4', '5'))
	INSERT INTO MovieRating (UserID, MovieTitle, ReleaseDate, StarRating) VALUES (id, title, released, rating);

DROP PROCEDURE IF EXISTS review_movie;
CREATE PROCEDURE review_movie(id INT, title VARCHAR(255), released DATE, review VARCHAR(1000))
	INSERT INTO MovieReview (UserID, MovieTitle, ReleaseDate, Review) VALUES (id, title, released, review);

DROP PROCEDURE IF EXISTS fetch_friends;
CREATE PROCEDURE fetch_friends (id INT)
	SELECT Username FROM Relationships JOIN UserAccount ON Relationships.MemberID = UserAccount.UserID 
	WHERE Relationships.UserID = id;

DROP PROCEDURE IF EXISTS add_friend;
CREATE PROCEDURE add_friend (adder INT, addee INT)
	INSERT INTO Relationships (UserID, MemberID) VALUES (adder, addee);

DROP PROCEDURE IF EXISTS unfriend;
CREATE PROCEDURE unfriend (remover INT, removee int)
	DELETE FROM Relationships WHERE UserID = remover AND MemberID = removee;

DROP PROCEDURE IF EXISTS create_group;
CREATE PROCEDURE create_group (id INT, gname VARCHAR(75), gdesc VARCHAR(500))
	INSERT INTO Groups (UserID, GroupName, Description) VALUES (id, gname, gdesc);
	
DROP PROCEDURE IF EXISTS add_to_group;
CREATE PROCEDURE add_to_group (id INT, gname VARCHAR(75), addee INT)
	INSERT INTO GroupMemberships (UserID, GroupName, MemberID) VALUES (id, gname, addee);

DROP PROCEDURE IF EXISTS list_group_members;
CREATE PROCEDURE list_group_members (id INT, gname VARCHAR(75))
	SELECT Username FROM GroupMemberships JOIN UserAccount ON GroupMemberships.MemberID = UserAccount.UserID
	WHERE GroupName = gname AND GroupMemberships.UserID = id;
	
INSERT INTO UserAccount (Email, Username, FirstName, LastName, Role, CreatedAt) VALUES ('abinader@neu.edu', 'abinader', 'george', 'abinader', 'default', NOW()),
	   ('testAccountUser tdonovan@neu.edu', 'joe', 'joseph', 'donovan', 'default', NOW()),
	   ('testfaucher@neu.edu', 'benji', 'benjamin', 'faucher', 'default', NOW()),
	   ('testledger@neu.edu', 'maddy', 'madaline', 'ledger', 'default', NOW());

# TESTS FOR create_user, edit_hometown, edit_names, edit_username, add_friend, fetch_friends, unfriend
CALL create_user('tula@hotmail.com', 'mitz', 'mirtula', 'papa');
CALL edit_hometown(5, 'Athens');
CALL edit_names(5, 'Tulie', 'Papas');
CALL edit_username(5, 'Mitz');
CALL add_friend(1, 2);
CALL add_friend(1, 3);
CALL add_friend(3, 4);
CALL add_friend(3, 5);
CALL fetch_friends(1);
CALL fetch_friends(3);
CALL unfriend(3, 4);
CALL fetch_friends(3);

# TESTS FOR create_group, add_to_group, list_group_members
CALL create_group(1, 'foo', 'bar');
SELECT GroupName, Description FROM Groups WHERE GroupName = 'foo';
CALL add_friend(1, 4);
CALL add_to_group(1, 'foo', 4);
CALL add_to_group(1, 'foo', 2);
CALL list_group_members(1, 'foo');

# TESTS FOR rate_movie, review_movie
CALL rate_movie(1, 'Scarface', '1983-12-09', '5');
CALL review_movie(1, 'Goodfellas', '1990-09-19', 'A lot of violence');
SELECT * FROM MovieRating;
SELECT * FROM MovieReview;
