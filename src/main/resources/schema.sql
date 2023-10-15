alter table article drop foreign key const_foreign_key_article_to_collection;
alter table article drop foreign key const_foreign_key_article_to_user;
alter table article_tag drop foreign key const_foreign_key_article_tag_to_article;
alter table article_tag drop foreign key const_foreign_key_article_tag_to_tag;
alter table bookmark drop foreign key const_foreign_key_bookmark_to_collection;
alter table bookmark drop foreign key const_foreign_key_bookmark_to_user;
alter table collection drop foreign key const_foreign_key_collection_to_user;


drop table if exists article;
drop table if exists article_tag;
drop table if exists bookmark;
drop table if exists collection;
drop table if exists tag;
drop table if exists user;

create table article (
  article_id bigint not null auto_increment,
  content varchar(255),
  created_at datetime(6),
  recent_access_time datetime(6),
  title varchar(255),
  collection_id bigint,
  user_id bigint,
  primary key (article_id)
) engine=InnoDB;

create table article_tag (
  article_tag_id bigint not null auto_increment,
  article_id bigint,
  tag_id bigint,
  primary key (article_tag_id)
) engine=InnoDB;

create table bookmark (
  bookmark_id bigint not null auto_increment,
  collection_id bigint,
  user_id bigint,
  primary key (bookmark_id)
) engine=InnoDB;

create table collection (
  collection_id bigint not null auto_increment,
  description varchar(255),
  is_public bit,
  user_id bigint,
  primary key (collection_id)
) engine=InnoDB;

create table tag (
  tag_id bigint not null auto_increment,
  created_at datetime(6),
  title varchar(255),
  primary key (tag_id)
) engine=InnoDB;

create table user (
  user_id bigint not null auto_increment,
  password varchar(255),
  username varchar(255),
  primary key (user_id)
) engine=InnoDB;



alter table article add constraint const_foreign_key_article_to_collection
  foreign key (collection_id) references collection (collection_id);

alter table article add constraint const_foreign_key_article_to_user
  foreign key (user_id) references user (user_id);

alter table article_tag add constraint const_foreign_key_article_tag_to_article
  foreign key (article_id) references article (article_id);

alter table article_tag add constraint const_foreign_key_article_tag_to_tag
  foreign key (tag_id) references tag (tag_id);

alter table bookmark add constraint const_foreign_key_bookmark_to_collection
  foreign key (collection_id) references collection (collection_id);

alter table bookmark add constraint const_foreign_key_bookmark_to_user
  foreign key (user_id) references user (user_id);

alter table collection add constraint const_foreign_key_collection_to_user
  foreign key (user_id) references user (user_id);