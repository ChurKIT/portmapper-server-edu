CREATE TABLE Clients(
                        id int NOT NULL IDENTITY,
                        clientAddress varchar(255),
                        context_id int,
                        PRIMARY KEY (id),
                        FOREIGN KEY (context_id) REFERENCES CONTEXTS(ID) ON DELETE CASCADE ON UPDATE CASCADE
)