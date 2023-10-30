CREATE DATABASE student_management;
USE student_management;

CREATE TABLE Country (
	country_id INT AUTO_INCREMENT PRIMARY KEY,
    country_name VARCHAR(50) NOT NULL
);

CREATE TABLE Location (
	location_id INT AUTO_INCREMENT PRIMARY KEY,
    street_address VARCHAR(100),
    postal_code CHAR(8),
    country_id INT NOT NULL,
    FOREIGN KEY (country_id) REFERENCES Country(country_id)
);

CREATE TABLE Employee (
	employee_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(50),
    email VARCHAR(30),
    location_id INT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES Location(location_id)
);

-- Question 2
-- a. Lấy tất cả các nhân viên thuộc Việt Nam
SELECT e.* 
FROM Employee AS e
JOIN Location AS l ON e.location_id = l.location_id
JOIN Country AS c ON l.country_id = c.country_id
WHERE c.country_id = 1;

-- b. Lấy ra tên quốc gia của employee có email là "minhtrang818@gmail.com"
SELECT c.country_name 
FROM Country AS c
JOIN Location AS l ON c.country_id = l.country_id
JOIN Employee AS e ON l.location_id = e.location_id
WHERE e.email = "minhtrang818@gmail.com";

-- c. Thống kê mỗi Country, mỗi Location có bao nhiêu Employee đang làm việc
CREATE VIEW count_employee_in_location AS
	SELECT location_id, count(location_id) AS number_of_employees
    FROM Employee
    GROUP BY location_id;

SELECT * FROM count_employee_in_location;

CREATE VIEW count_employee_in_country AS
SELECT l.country_id, sum(cl.number_of_employees) AS number_of_employees_c
FROM Location AS l
JOIN count_employee_in_location AS cl ON l.location_id = cl.location_id
GROUP BY l.country_id;

-- Question 3: Tạo trigger chon table Employee chỉ cho phép insert mỗi quốc gia tối đa 10 employee
DROP TRIGGER IF EXISTS before_insert_employee;

DELIMITER $$
CREATE TRIGGER before_insert_employee
	BEFORE INSERT ON Employee
    FOR EACH ROW
		BEGIN
			DECLARE count_employee INT;
            SELECT number_of_employees_c INTO count_employee
            FROM count_employee_in_country
            WHERE country_id = (SELECT country_id FROM Location Where location_id = NEW.location_id);
            IF count_employee >= 10 THEN
				SIGNAL SQLSTATE 'HY000'
				SET MESSAGE_TEXT = 'Each location only allows a maximum of 10 employees';
			END IF;
		END$$
DELIMITER ;

-- Question 4: Cấu hình sao cho khi xóa 1 location nào đó thì tất cả employee ở location đó sẽ có location_id = null
DROP TRIGGER IF EXISTS before_delete_location;
			
DELIMITER $$
CREATE TRIGGER before_delete_location
	BEFORE DELETE ON Location
    FOR EACH ROW
		BEGIN
			DECLARE v_location_id INT;
            SET v_location_id = OLD.location_id;
            UPDATE Employee
            SET location_id = NULL
            WHERE location_id = v_location_id;
		END$$
DELIMITER ;

DELETE FROM Location
WHERE location_id = 12
    


