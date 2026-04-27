create table profiles (
    id binary(16) primary key,
    full_name varchar(255),
    email varchar(255) not null unique,
    role varchar(50) not null,
    avatar_url varchar(500),
    created_at datetime(6) not null,
    updated_at datetime(6) not null
) engine=InnoDB;

create table app_users (
    id binary(16) primary key,
    password_hash varchar(255) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_app_users_profile
        foreign key (id) references profiles(id) on delete cascade
) engine=InnoDB;

create table auth_sessions (
    token varchar(100) primary key,
    user_id binary(16) not null,
    expires_at datetime(6) not null,
    created_at datetime(6) not null,
    constraint fk_auth_sessions_user
        foreign key (user_id) references profiles(id) on delete cascade
) engine=InnoDB;

create table articles (
    id binary(16) primary key,
    category varchar(100) not null,
    number int not null,
    title varchar(255) not null,
    full_text text,
    explanation text,
    key_points text,
    important_cases text,
    created_at datetime(6) not null,
    updated_at datetime(6) not null
) engine=InnoDB;

create table legal_insights (
    id binary(16) primary key,
    expert_id binary(16),
    title varchar(255) not null,
    content text not null,
    category varchar(100),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_legal_insights_expert
        foreign key (expert_id) references profiles(id) on delete set null
) engine=InnoDB;

create table case_references (
    id binary(16) primary key,
    title varchar(255) not null,
    citation varchar(255),
    summary text,
    article_id binary(16),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_case_references_article
        foreign key (article_id) references articles(id) on delete set null
) engine=InnoDB;

create table advisory_requests (
    id binary(16) primary key,
    citizen_id binary(16),
    expert_id binary(16),
    question text not null,
    category varchar(100),
    status varchar(50) not null,
    urgent boolean not null,
    response text,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_advisory_requests_citizen
        foreign key (citizen_id) references profiles(id) on delete cascade,
    constraint fk_advisory_requests_expert
        foreign key (expert_id) references profiles(id) on delete set null
) engine=InnoDB;

create table quizzes (
    id bigint primary key,
    title varchar(255) not null,
    description text,
    created_at datetime(6) not null,
    updated_at datetime(6) not null
) engine=InnoDB;

create table quiz_questions (
    id bigint not null auto_increment primary key,
    quiz_id bigint not null,
    question text not null,
    options text not null,
    correct_answer int not null,
    explanation text,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_quiz_questions_quiz
        foreign key (quiz_id) references quizzes(id) on delete cascade
) engine=InnoDB;

create table forum_topics (
    id binary(16) primary key,
    title varchar(255) not null,
    content text not null,
    author_id binary(16),
    category varchar(100),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_forum_topics_author
        foreign key (author_id) references profiles(id) on delete cascade
) engine=InnoDB;

create table forum_replies (
    id binary(16) primary key,
    topic_id binary(16),
    author_id binary(16),
    content text not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_forum_replies_topic
        foreign key (topic_id) references forum_topics(id) on delete cascade,
    constraint fk_forum_replies_author
        foreign key (author_id) references profiles(id) on delete cascade
) engine=InnoDB;
