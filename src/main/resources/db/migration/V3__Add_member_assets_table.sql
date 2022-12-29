CREATE TABLE `member_assets` (
  `member_asset_id` char(36) NOT NULL,
  `inserted_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `member_id` char(36) NULL,
  `asset_id` char(36) NULL,
  PRIMARY KEY (`member_asset_id`),
  CONSTRAINT `FK_member_assets_member_id` FOREIGN KEY (`member_id`) REFERENCES members (`member_id`),
  CONSTRAINT `FK_member_assets_asset_id` FOREIGN KEY (`asset_id`) REFERENCES assets (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
