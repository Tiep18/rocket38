-- Question 1
DELIMITER $$
CREATE PROCEDURE get_all_account_in_department(IN DepartmentName VARCHAR(30))
	BEGIN
		SELECT * FROM `account` AS a
        JOIN department AS d ON a.DepartmentID = d.DepartmentID
        WHERE d.DepartmentName = DepartmentName;
	END$$
DELIMITER ;

CALL get_all_account_in_department('Phòng 2');

-- Question 6

DROP PROCEDURE IF EXISTS get_group_or_account;

DELIMITER $$
CREATE PROCEDURE get_group_or_account(IN searchKeyword VARCHAR(30), OUT groupSearch VARCHAR(30), OUT accountName VARCHAR(30))
	BEGIN
		SELECT GroupName INTO groupSearch FROM `group`
        WHERE LOCATE(searchKeyword, GroupName) > 0 LIMIT 1;
        SELECT UserName INTO accountName FROM `account`
        WHERE LOCATE(searchKeyword, UserName) > 0 LIMIT 1;
	END$$
DELIMITER ;

SET @groupName = '';
SET @accountName = '';
CALL get_group_or_account('tiep', @groupName, @accountName);
CALL get_group_or_account('roup', @groupName, @accountName);
SELECT @groupName, @accountName;

-- Question 7

DROP PROCEDURE IF EXISTS add_user;

DELIMITER $$
CREATE PROCEDURE add_user (IN fullName VARCHAR(40), IN email VARCHAR(40))
	BEGIN
		DECLARE prev_length INT;
        DECLARE curr_length INT;
        -- check email
        IF email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$' THEN
			SIGNAL SQLSTATE 'HY000'
			SET MESSAGE_TEXT = 'Add new user failed';
		END IF;
        
        SET prev_length = (SELECT count(AccountID) FROM `account`);
		INSERT INTO `account`(Email, FullName, Username, PositionID)
			VALUE (email, fullName, SUBSTRING_INDEX(email, '@', 1), 1);        
        SET curr_length = (SELECT count(AccountID) FROM `account`);
		IF (curr_length = prev_length + 1) THEN
			SIGNAL SQLSTATE '01000'
			SET MESSAGE_TEXT = 'New user added successfully';
		ELSE
			SIGNAL SQLSTATE 'HY000'
			SET MESSAGE_TEXT = 'Add new user failed';
		END IF;
	END$$
DELIMITER ;

CALL add_user(123, 'tiep123gmail.com')


