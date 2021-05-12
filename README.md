# Educational project.

The essence of the project is to write a PortMapper server that will redirect requests and responses from the Client to the Target server and back.

## Startup sequence:

    1.TargetServerApplication.java (port 8080)

    2.PortMapperRun.java (port 6666 changes in properties)

    3.Run.java

Write query to Target Server

GET /home HTTP/1.1
Host: localhost
DONE
