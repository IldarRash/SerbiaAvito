

--todo add gap
create table if not exists message
(
    message_id       varchar        primary key,
    event_id         varchar        not null,
    body            text            not null,
    isFrom            boolean         not null default false,
    time            timestamp
);

