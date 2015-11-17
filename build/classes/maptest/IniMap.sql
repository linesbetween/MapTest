use game;

DROP table if exists Map;
CREATE table Map(
blockId VARCHAR(5) not null,
landType INT(1),
buildingId VARCHAR(5),
eventId VARCHAR(5),
ownerId VARCHAR(2),
visitorId VARCHAR(2),
PRIMARY KEY (blockId)
);

insert into Map(blockId) values ('blo1');





