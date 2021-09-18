use number_sign_db;

create table note
(
  id int unsigned not null auto_increment,
  user_id int unsigned not null,
  title varchar(255) not null,
  content text not null,
  status enum ('active', 'trashed') not null default 'active',
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  primary key (id),
  foreign key (user_id) references user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
