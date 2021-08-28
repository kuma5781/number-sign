use number_sign_db;

create table applicant
(
  applicant_id char(36) not null,
  applicant_name varchar(255) not null,
  applicant_password varchar(255) not null unique,
  applicant_status enum ('BEFORE_TEST', 'DURING_TEST', 'AFTER_TEST', 'COMPLETED') not null default 'BEFORE_TEST',
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  primary key (applicant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into applicant (applicant_id, applicant_name, applicant_password, applicant_status, created_at)
values ('67bc9701-991a-4179-991a-31431503673d', '綾波レイ', '714f746275', 'COMPLETED', '2021-05-01 14:00:00'),
       ('14d4041a-05f4-487a-a8ed-bc2c8357c4a7', '碇シンジ', 'e85c9f6512', 'COMPLETED', '2021-05-02 14:00:00'),
       ('97688c8b-7b1d-f5bc-030f-e8cb8380a6a0', '渚カヲル', 'd5c70825b0', 'AFTER_TEST', '2021-05-03 14:00:00'),
       ('289e78b5-0923-e48d-555a-fdee22b5d99b', '加持リョウジ', '050f392928', 'AFTER_TEST', '2021-05-04 14:00:00'),
       ('2150164c-8411-ef5e-f082-6d18654b1ed2', '赤木リツコ', 'd50427b783', 'AFTER_TEST', '2021-05-05 14:00:00'),
       ('c4e18ee6-6d21-fdeb-c313-22eadeed38f0', '鈴原トウジ', '9fc85e8112', 'AFTER_TEST', '2021-05-06 14:00:00'),
       ('0d4b8b2a-cc80-cdc9-1e60-e6437d0cf8c9', '伊吹マヤ', 'd5cd0833ac', 'DURING_TEST', '2021-05-07 14:00:00'),
       ('5e298e9c-1ff9-bf91-cfc0-19e35af38056', '青葉シゲル', 'a2120c4003', 'BEFORE_TEST', '2021-05-08 14:00:00'),
       ('0b9e0f47-2994-1124-1355-19a1399cb25a', '日向マコト', 'b827eab481', 'BEFORE_TEST', '2021-05-09 14:00:00'),
       ('e190b52c-9d41-4108-7262-835ba53331aa', '冬月コウゾウ', 'acfc29d070', 'BEFORE_TEST', '2021-05-10 14:00:00'),
       ('e1614bd7-b12f-d56c-5422-38c7f6fdc2bf', '洞木ヒカリ', 'e88c6d19ba', 'BEFORE_TEST', '2021-05-11 14:00:00'),
       ('9aedc0bf-6ea8-d963-51d8-3eb482c6d460', '相田ケンスケ', '0bcc186a83', 'BEFORE_TEST', '2021-05-12 14:00:00');
