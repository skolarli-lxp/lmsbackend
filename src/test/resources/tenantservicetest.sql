-- Create a table to store the tenants --

CREATE TABLE `tenants` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `company_name` varchar(255) DEFAULT NULL,
                           `domain_name` varchar(255) DEFAULT NULL,
                           `website` varchar(255) DEFAULT NULL,
                           `address` varchar(255) DEFAULT NULL,
                           `country_code` int NOT NULL,
                           `currency` varchar(255) DEFAULT NULL,
                           `phone_number` varchar(255) DEFAULT NULL,
                           `time_zone` varchar(255) DEFAULT NULL,
                           `tenant_is_deleted` bit(1) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `domainname` (`domain_name`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insert two tenants -- one active and one deleted --

INSERT INTO `tenants` (`id`,`company_name`,`domain_name`,`website`,`address`,`country_code`,`currency`,`phone_number`,`time_zone`,`tenant_is_deleted`)
 VALUES (1,"My Company Name","domainName1","www.mywebsite.com",NULL,91,'INR',"98349822782",NULL,b'0');

INSERT INTO `tenants` (`id`,`company_name`,`domain_name`,`website`,`address`,`country_code`,`currency`,`phone_number`,`time_zone`,`tenant_is_deleted`)
VALUES (2,"My Company Name2","domainName2","www.mywebsite.com",NULL,91,'INR',"98349822782",NULL,b'1');
