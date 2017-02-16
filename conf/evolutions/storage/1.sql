# Users schema

# --- !Ups
CREATE TABLE usertokens (
	token	TEXT NOT NULL UNIQUE,
	username	TEXT NOT NULL,
	expirationtime	INTEGER NOT NULL,
	PRIMARY KEY(token)
);

CREATE TABLE sendlogs (
	id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	username	REAL NOT NULL,
	sender	TEXT NOT NULL,
	destination	TEXT NOT NULL,
	message	TEXT NOT NULL,
	sendtime	INTEGER NOT NULL
);

# --- !Downs
DROP TABLE usertokens;

DROP TABLE sendlogs;