use number_sign_db;

create table user
(
  id int not null auto_increment,
  name varchar(255) not null,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  primary key (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
