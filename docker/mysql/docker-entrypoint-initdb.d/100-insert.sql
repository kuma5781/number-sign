use number_sign_db;

insert into user (id, name, email)
values (1, '緑谷出久' ,'deku@heroaca.com'),
       (2, '爆豪勝己' ,'xxx@heroaca.com'),
       (3, '轟焦凍' ,'short@heroaca.com'),
       (4, '麗日お茶子' ,'uravity@xheroaca.com'),
       (5, '蛙吹梅雨' ,'froppy@heroaca.com');

insert into note (id, user_id, title, content)
values (1, 1, 'ヒーローの本質', '余計なお世話はヒーローの本質なんだって'),
       (2, 2, '気付けねえぞ', 'いつまでも見下したままじゃ自分の弱さに気付けねえぞ'),
       (3, 3, 'ちゃんと見ろ', 'なりてえもんちゃんと見ろ！！'),
       (4, 4, '決勝で', '決勝で会おうぜ！'),
       (5, 5, '自分のペース', '自分のペースでいいのよ');

insert into folder (id, user_id, name)
values (1, 1, '6巻'),
       (2, 1, '52話'),
       (3, 2, '18巻'),
       (4, 2, '166話'),
       (5, 3, '6巻'),
       (6, 3, '53話'),
       (7, 4, '3巻'),
       (8, 4, '21話'),
       (9, 5, '2巻'),
       (10, 5, '15話');

insert into comment (user_id, note_id, content)
values (4, 1, '「デク」って...「頑張れ！！」って感じでなんか好きが私'),
       (2, 4, '死んでねえ');

insert into authority (user_id, note_id, authority)
values (2, 1, 'read'),
       (3, 2, 'write');

insert into relay_folders (folder_id, parent_folder_id)
values (2, 1),
       (4, 3),
       (6, 5),
       (8, 7),
       (10, 9);

insert into relay_note_folder (note_id, parent_folder_id)
values (1 ,2),
       (2, 4),
       (3, 6),
       (4, 8),
       (5, 10);
