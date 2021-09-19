use number_sign_db;

create table comment
(
  id int unsigned not null auto_increment,
  user_id int unsigned not null,
  note_id int unsigned not null,
  content text not null,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  primary key (id),
  foreign key (user_id) references user (id),
  foreign key (note_id) references note (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
