create table article_categories (
    id varchar(100) primary key,
    title varchar(255) not null,
    description text not null
) engine=InnoDB;

create table captcha_codes (
    id bigint not null auto_increment primary key,
    code varchar(100) not null unique
) engine=InnoDB;
