use number_sign_db;

create table relay_folders
(
  folder_id int unsigned not null,
  parent_folder_id int unsigned not null,
  created_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  updated_at TIMESTAMP not null default CURRENT_TIMESTAMP,
  primary key (folder_id),
  foreign key (folder_id) references folder (id),
  foreign key (parent_folder_id) references folder (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
