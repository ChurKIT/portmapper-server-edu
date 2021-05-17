CREATE TABLE Contexts(
                         id int NOT NULL IDENTITY,
                         requestBytes int,
                         responseBytes int,
                         startSession datetime,
                         stopSession datetime,
                         workTime int,
                         message_id int,
                         PRIMARY KEY (id),
                         FOREIGN KEY (message_id) REFERENCES MESSAGES(ID) ON DELETE CASCADE ON UPDATE CASCADE
)