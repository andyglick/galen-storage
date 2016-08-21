create table projects (
    project_id int(11) not null auto_increment,
    name varchar(256) not null,
    unique key(name),
    primary key(project_id)
) ENGINE=MYISAM;

create table pages (
    page_id int(11) not null auto_increment,
    project_id int(11) not null,
    name varchar(256) not null,
    unique key(project_id, name),
    primary key(page_id)
) ENGINE=MYISAM;

create table objects (
    object_id int(11) not null auto_increment,
    name varchar(256) not null,
    page_id int(11) not null,
    unique key(page_id, name),
    primary key(object_id)
) ENGINE=MYISAM;

create table object_images (
    object_image_id int(11) not null auto_increment,
    object_id int(11) not null,
    image_path varchar(2048) not null,
    created_date datetime not null,
    hash varchar(128) not null,
    status enum('candidate', 'approved') not null default 'candidate',
    primary key(object_image_id)
) ENGINE=MYISAM;
