CREATE TABLE `clothing` (
  `asset_id` char(36) NOT NULL,
  `name` varchar(255) NULL,
  `brand` varchar(255) NULL,
  `serial_number` varchar(255) NULL,
  `gender_type` varchar(255) NULL,
  `price` int NULL,
  `associate_link` varchar(255) NULL,
  `detail_description` text NULL,
  PRIMARY KEY (`asset_id`),
  CONSTRAINT FK_clothing_asset_id FOREIGN KEY (`asset_id`) REFERENCES assets(`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
