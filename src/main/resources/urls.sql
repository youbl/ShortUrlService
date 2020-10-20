create table urls
(
    id    INTEGER primary key autoincrement not null ,
    url varchar(500) not null,
    desc varchar(100) not null COLLATE NOCASE,
    addtime TIMESTAMP not null default (datetime('now', 'localtime')),
    addip varchar(100) not null,
    code varchar(10) not null
);