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
CREATE PROCEDURE add_to_group (gid INT, addee INT)
	INSERT INTO GroupMemberships (GroupID, MemberID) VALUES (gid, addee);

DROP PROCEDURE IF EXISTS list_group_members;
CREATE PROCEDURE list_group_members (gid INT)
	SELECT Username FROM GroupMemberships JOIN UserAccount ON GroupMemberships.MemberID = UserAccount.UserID 
	WHERE GroupMemberships.GroupID = gid;
