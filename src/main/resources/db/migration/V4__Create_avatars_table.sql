CREATE TABLE `avatars` (
  `asset_id` char(36) NOT NULL,
  `gender_type` varchar(255) NULL,
  PRIMARY KEY (`asset_id`),
  CONSTRAINT FK_avatars_asset_id FOREIGN KEY (`asset_id`) REFERENCES assets(`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
