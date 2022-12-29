CREATE TABLE `assets` (
  `asset_id` char(36) NOT NULL,
  `asset_type` varchar(255) NOT NULL,
  `s3_asset_uuid` char(36) NOT NULL,
  `extension` varchar(255) NOT NULL,
  `s3_directory` varchar(255) NOT NULL,
  `public_type` varchar(255) NOT NULL,
  `inserted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`asset_id`),
  UNIQUE KEY `UK_assets_s3_asset_uuid` (`s3_asset_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
