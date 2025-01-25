# Application Deployment and Running Guide

## Prerequisites
Before deploying and running the application, make sure you have the following installed:

- **Docker**: [Download Docker](https://www.docker.com/get-started)
- **Docker Compose**: Docker Compose is included in Docker Desktop, but if you're using a different setup, you can install it [here](https://docs.docker.com/compose/install/).

## Steps for Deployment

### 1. Clone the Repository (If you haven't already)
If you haven't cloned the repository yet, you can do so by running the following command:
```bash
git clone <repository-url>
cd <repository-name>
```

### 2. Navigate to the Application Folder
Change to the application directory where the Docker configuration files are located:
```bash
cd app/
```

### 3. Build and Run the Containers
To build and start the containers, run the following command:
```bash
docker-compose up --build
```
This will:

- Build the necessary Docker images.
- Start the required containers (web server, databases, etc.).

### 4. Access the Website
Once the containers are up and running, you can access the website at the following URL:

Website URL: [http://localhost:5173](http://localhost:5173)

### 5. Check Port Availability
Before running the application, make sure that the following ports are not in use by other processes:

- **8080**: Used by the web server.
- **5173**: Used by the frontend application (if applicable).
- **80**: HTTP port for the web server.
- **587**: SMTP (email sending) port for mail services.

If any of these ports are already in use, you may need to stop the conflicting services or modify the `docker-compose.yml` file to use different ports.

### 6. Stopping the Containers
To stop the containers when you're done, simply run:
```bash
docker-compose down
```
This will stop and remove the containers, but leave the data volumes intact.
