

--todo add gap
create table if not exists message
(
    message_id       UUID        primary key,
    event_id         UUID        not null,
    body             text            not null,
    isFrom           boolean         not null default false,
    created          timestamp
);

