

create table collection (
  id                        varchar(255) not null,
  name                      varchar(255),
  hash_tag                  varchar(255),
  constraint pk_collection primary key (id))
;

create table person (
  id                        varchar(255) not null,
  name                      varchar(255),
  constraint pk_person primary key (id))
;

create table result_collection (
  id                        varchar(255) not null,
  c_name                    varchar(255),
  instagram_username        varchar(255),
  urlname                   varchar(255),
  img                       varchar(255),
  constraint pk_result_collection primary key (id))
;

create table task (
  id                        varchar(255) not null,
  contents                  varchar(255),
  constraint pk_task primary key (id))
;

create sequence collection_seq;

create sequence person_seq;

create sequence result_collection_seq;

create sequence task_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists collection;

drop table if exists person;

drop table if exists result_collection;

drop table if exists task;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists collection_seq;

drop sequence if exists person_seq;

drop sequence if exists result_collection_seq;

drop sequence if exists task_seq;

