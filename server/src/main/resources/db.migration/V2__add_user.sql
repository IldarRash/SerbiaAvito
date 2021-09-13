
create table  if not exists app_user (
                                         app_user_id bigserial primary key,
                                         username varchar,
                                         email varchar,
                                         pass varchar,
                                         bio varchar,
                                         image varchar
);

create table if not exists token (
                                     token_id bigserial primary key,
                                     secure_id bytea not null,
                                     app_user_id bigint not null references app_user on delete cascade,
                                     expiry timestamp with time zone not null,
                                     last_touched timestamp with time zone
);

create table if not exists bot (
                                     id bigserial primary key,
                                     bot_category_id varchar,
                                     answers json,
                                     words json,
                                     buttons json,
                                     client_id,
                                     created timestamp with time zone
);


create table if not exists bot_category (
    id bigserial primary key,
    api varchar,
    links json,
    templates json
)



create table jsontest (id serial primary key, data json);
