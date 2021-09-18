use number_sign_db;

create table authority
(
  user_id int unsigned not null,
  note_id int unsigned not null,
  authority enum ('read', 'write') not null default 'read',
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  primary key (user_id, note_id),
  foreign key (user_id) references user (id),
  foreign key (note_id) references note (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
