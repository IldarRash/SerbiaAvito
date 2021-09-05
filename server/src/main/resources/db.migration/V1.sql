create domain MessageId varchar(255);
create domain EventId varchar(255);

--todo add gap
create table Message
(
    zoneId       MessageId       primary key,
    systemId     EventId         not null,
    body         string          not null,
    from         boolean         not null default false,
    time         timestamp
);

