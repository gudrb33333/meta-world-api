CREATE TABLE `profiles` (
  `profile_id` char(36) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `asset_id` char(36) NULL,
  `inserted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`profile_id`),
  CONSTRAINT FK_profiles_asset_id FOREIGN KEY (`asset_id`) REFERENCES avatars(`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
