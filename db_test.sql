DROP DATABASE IF EXISTS Spoiled_Tomatillos_Backend;
CREATE DATABASE Spoiled_Tomatillos_Backend;
USE Spoiled_Tomatillos_Backend;

DROP TABLE IF EXISTS UserAccount;
CREATE TABLE UserAccount (
		UserID INT PRIMARY KEY AUTO_INCREMENT,
		Email VARCHAR(50) UNIQUE,
		Username VARCHAR(50) UNIQUE,
		FirstName VARCHAR(50) NOT NULL,
		LastName VARCHAR(50) NOT NULL,
		Hometown VARCHAR(50),
		DisplayPicture LONGBLOB,
		Role ENUM('default', 'group admin', 'admin', 'moderator'),
		CreatedAt TIMESTAMP,
		UpdatedAt TIMESTAMP
		);

DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups (
		GroupID INT AUTO_INCREMENT,
		UserID INT,
		GroupName VARCHAR(75),
		Description VARCHAR(500),
		PRIMARY KEY (GroupID),
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
		GroupID INT,
		MemberID INT,
		PRIMARY KEY (GroupID, MemberID),
		FOREIGN KEY (GroupID) REFERENCES Groups(GroupID)
		ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (MemberID) REFERENCES UserAccount(UserID)
		ON DELETE CASCADE ON UPDATE CASCADE
		);

DROP TABLE IF EXISTS MovieRating;
CREATE TABLE MovieRating (
        RatingID INT PRIMARY KEY AUTO_INCREMENT,
        MovieID VARCHAR(20) NOT NULL,
		UserID INT NOT NULL,
		StarRating Enum('1', '2', '3', '4', '5') NOT NULL,
        CreatedAt TIMESTAMP,
        UpdatedAt TIMESTAMP,
        CONSTRAINT movie_user_unique UNIQUE (MovieID, UserID),
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID)
		ON DELETE CASCADE ON UPDATE CASCADE
		);

DROP TABLE IF EXISTS MovieReview;
CREATE TABLE MovieReview (
        ReviewID INT PRIMARY KEY AUTO_INCREMENT,
        MovieID VARCHAR(20) NOT NULL,
		UserID INT NOT NULL,
		Review VARCHAR(1000) NOT NULL,
		CreatedAt TIMESTAMP,
		UpdatedAt TIMESTAMP,
		CONSTRAINT movie_user_unique UNIQUE (MovieID, UserID),
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
CREATE PROCEDURE rate_movie(movieID VARCHAR(20), userID INT, rating Enum('1', '2', '3', '4', '5'))
	INSERT INTO MovieRating (MovieID, UserID, StarRating, CreatedAt, UpdatedAt)
	VALUES (movieID, userID, rating, NOW(), NOW());

DROP PROCEDURE IF EXISTS review_movie;
CREATE PROCEDURE review_movie(movieID VARCHAR(20), userID INT, review VARCHAR(1000))
	INSERT INTO MovieReview (MovieID, UserID, Review, CreatedAt, UpdatedAt)
	VALUES (movieID, userID, review, NOW(), NOW());

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
CREATE PROCEDURE add_to_group (gid INT, addee INT)
	INSERT INTO GroupMemberships (GroupID, MemberID) VALUES (gid, addee);

DROP PROCEDURE IF EXISTS list_group_members;
CREATE PROCEDURE list_group_members (gid INT)
	SELECT Username FROM GroupMemberships JOIN UserAccount ON GroupMemberships.MemberID = UserAccount.UserID 
	WHERE GroupMemberships.GroupID = gid;
	
INSERT INTO UserAccount (Email, Username, FirstName, LastName, Role, CreatedAt)
VALUES ('abinader@neu.edu', 'abinader', 'george', 'abinader', 'default', NOW()),
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
CALL create_group(2, 'bar', 'foo');
SELECT GroupName, Description FROM Groups WHERE GroupName = 'foo';
SELECT GroupName, Description FROM Groups WHERE GroupName = 'bar';
CALL add_to_group(1, 4);
CALL add_to_group(1, 2);
CALL add_to_group(2, 3);
CALL list_group_members(1);
CALL list_group_members(2);

# TESTS FOR rate_movie, review_movie
CALL rate_movie('tt798423', 1, '5');
CALL review_movie('tt42387', 1, 'A lot of violence');
SELECT * FROM MovieRating;
SELECT * FROM MovieReview;
