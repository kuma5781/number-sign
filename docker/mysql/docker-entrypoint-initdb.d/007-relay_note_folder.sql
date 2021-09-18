use number_sign_db;

create table relay_note_folder
(
  note_id int unsigned not null,
  parent_folder_id int unsigned not null,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  primary key (note_id),
  foreign key (note_id) references note (id),
  foreign key (parent_folder_id) references folder (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
