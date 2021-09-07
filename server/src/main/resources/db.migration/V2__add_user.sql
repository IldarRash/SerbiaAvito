
create table  if not exists app_user (
                                         app_user_id bigserial primary key,
                                         first_name varchar,
                                         last_name varchar
);

create table if not exists token (
                                     token_id bigserial primary key,
                                     secure_id bytea not null,
                                     app_user_id bigint not null references app_user on delete cascade,
                                     expiry timestamp with time zone not null,
                                     last_touched timestamp with time zone
);