alter table legal_insights add column tags text;
alter table legal_insights add column status varchar(50) not null default 'published';
alter table legal_insights add column views bigint not null default 0;
alter table legal_insights add column shares bigint not null default 0;

alter table case_references add column type varchar(100);
alter table case_references add column court varchar(255);
alter table case_references add column judgment_year integer;
alter table case_references add column significance varchar(255);
alter table case_references add column relevant_articles text;
alter table case_references add column judge_name varchar(255);
alter table case_references add column pdf_url varchar(500);
alter table case_references add column status varchar(50) not null default 'published';

alter table advisory_requests add column subject varchar(255);

alter table forum_topics add column guest_name varchar(255);
alter table forum_topics add column guest_profession varchar(255);

alter table forum_replies add column guest_name varchar(255);
