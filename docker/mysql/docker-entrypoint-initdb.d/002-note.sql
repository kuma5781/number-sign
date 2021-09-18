use number_sign_db;

create table note
(
  id int unsigned not null auto_increment,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
