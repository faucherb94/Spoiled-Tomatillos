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
		DisplayPicture VARCHAR(100),
		Role ENUM('default', 'group admin', 'admin', 'moderator'),
		CreatedAt TIMESTAMP,
		UpdatedAt TIMESTAMP
		);

DROP TABLE IF EXISTS Groups;
CREATE TABLE Groups (
        GroupID INT PRIMARY KEY AUTO_INCREMENT,
        CreatorID INT NOT NULL,
        Name VARCHAR(75) NOT NULL,
		Description VARCHAR(500),
		Picture LONGBLOB,
		CreatedAt TIMESTAMP,
		UpdatedAt TIMESTAMP,
		FOREIGN KEY (CreatorID) REFERENCES UserAccount(UserID)
		ON DELETE CASCADE ON UPDATE CASCADE
		);

DROP TABLE IF EXISTS Relationships;
CREATE TABLE Relationships (
        RelationshipID INT PRIMARY KEY AUTO_INCREMENT,
        UserID1 INT NOT NULL,
		UserID2 INT NOT NULL,
		CreatedAt TIMESTAMP,
		UpdatedAt TIMESTAMP,
		FOREIGN KEY (UserID1) REFERENCES UserAccount(UserID)
		ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (UserID2) REFERENCES UserAccount(UserID)
		ON DELETE CASCADE ON UPDATE CASCADE,
		CONSTRAINT unique_users UNIQUE (UserID1, UserID2)
		);

DROP TABLE IF EXISTS GroupMemberships;
CREATE TABLE GroupMemberships (
        MembershipID INT PRIMARY KEY AUTO_INCREMENT,
        GroupID INT NOT NULL,
		UserID INT NOT NULL,
        CreatedAt TIMESTAMP,
        UpdatedAt TIMESTAMP,
		FOREIGN KEY (GroupID) REFERENCES Groups(GroupID) 
		ON DELETE CASCADE ON UPDATE CASCADE,
		FOREIGN KEY (UserID) REFERENCES UserAccount(UserID)
		ON DELETE CASCADE ON UPDATE CASCADE,
		CONSTRAINT group_user_unique UNIQUE (GroupID, UserID)
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
