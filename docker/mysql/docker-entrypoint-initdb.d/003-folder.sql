use number_sign_db;

create table folder
(
  id int unsigned not null auto_increment,
  user_id int unsigned not null,
  name varchar(255) not null,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  primary key (id),
  foreign key (user_id) references user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
