CREATE TABLE MESSAGES(
                         id int NOT NULL IDENTITY,
                         request varchar(255),
                         response longvarchar,
                         PRIMARY KEY (id)
)