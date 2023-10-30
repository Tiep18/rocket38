CREATE DATABASE project_management;

USE project_management;

-- Question a
CREATE TABLE Employee (
	EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    EmployeeLastName VARCHAR(20),
    EmployeeFirstName VARCHAR(20),
    EmployeeHireDate DATE,
    EmployeeStatus ENUM ('Active','Terminated'),
    SupervisorID INT,
    SocialSecurityNumber INT,
    FOREIGN KEY (SupervisorID) REFERENCES Employee(EmployeeID)
);

CREATE TABLE Projects (
	ProjectID INT AUTO_INCREMENT PRIMARY KEY,
    EmployeeID INT,
    ProjectName VARCHAR(30),
    ProjectStartDate DATE,
    ProjectDescription TEXT,
    ProjectDetail TEXT,
    ProjectCompetedOn DATE,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID)
);

CREATE TABLE Project_Modules (
	ModuleID INT AUTO_INCREMENT PRIMARY KEY,
    ProjectID INT,
    EmployeeID INT,
    ProjectModuleDate DATE,
    ProjectModuleCompetedOn DATE,
    ProjectModuleDescription TEXT,
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE,
    FOREIGN KEY (ProjectID) REFERENCES Projects(ProjectID) ON DELETE CASCADE
);

CREATE TABLE Work_Done (
	WorkDoneID INT AUTO_INCREMENT PRIMARY KEY,
    EmployeeID INT,
    ModuleID INT,
    WorkDoneDate DATE,
    WorkDoneDescription TEXT,
    WorkDoneStatus ENUM ('In Progress','Completed'),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(EmployeeID) ON DELETE CASCADE,
    FOREIGN KEY (ModuleID) REFERENCES Project_Modules(ModuleID) ON DELETE CASCADE
);

-- Question b
DROP PROCEDURE IF EXISTS remove_projects;

DELIMITER $$
CREATE PROCEDURE remove_projects ()
	BEGIN
		SELECT 	p.ProjectID AS ProjectDeleted,
				m.ModuleID AS ModuleDeleted, 
				w.WorkDoneID AS WorkDoneDeleted 
		FROM projects AS p
        JOIN project_modules AS m ON p.ProjectID = m.ProjectID
        JOIN work_done AS w ON m.ModuleID = w.ModuleID
		WHERE datediff(current_date(), ProjectCompletedOn) > 120;
        
        CREATE TEMPORARY TABLE temp_project_ids AS
			SELECT ProjectID 
			FROM projects
			WHERE datediff(current_date(), ProjectCompletedOn) > 120;
        
        DELETE FROM projects
        WHERE ProjectID IN (SELECT ProjectID FROM temp_project_ids);
        
        DROP TEMPORARY TABLE IF EXISTS temp_project_ids;
    END$$
DELIMITER ;

CALL remove_projects();

-- Question c
DROP PROCEDURE IF EXISTS in_progress_modules;

DELIMITER $$
CREATE PROCEDURE in_progress_modules ()
	BEGIN
		SELECT * FROM project_modules
        WHERE ProjectModuleCompletedOn IS NULL;
	END$$
DELIMITER ;

CALL in_progress_modules();

-- Question d
DROP FUNCTION IF EXISTS find_employee;
DELIMITER $$
	CREATE FUNCTION find_employee (moduleID_praram INT) RETURNS INT
    DETERMINISTIC
		BEGIN
			DECLARE employeeID_not_in_module INT;
            SELECT EmployeeID INTO employeeID_not_in_module FROM work_done
            WHERE ModuleID = moduleID_praram
            AND EmployeeID NOT IN
				(SELECT EmployeeID FROM project_modules
				WHERE ModuleID = moduleID_praram)
			ORDER BY rand()
			LIMIT 1;
            RETURN (employeeID_not_in_module);
		END $$           
DELIMITER ;

SELECT find_employee(13)




