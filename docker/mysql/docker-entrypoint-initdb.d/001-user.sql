use number_sign_db;

create table user
(
  user_id int not null auto_increment,
  user_name varchar(255) not null,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  primary key (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into user (user_name)
values ('綾波レイ'),
       ('碇シンジ'),
       ('渚カヲル'),
       ('加持リョウジ'),
       ('赤木リツコ'),
       ('鈴原トウジ'),
       ('伊吹マヤ'),
       ('青葉シゲル'),
       ('日向マコト'),
       ('冬月コウゾウ'),
       ('洞木ヒカリ'),
       ('相田ケンスケ');