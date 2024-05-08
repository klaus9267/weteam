CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    organization VARCHAR(255),
    uid VARCHAR(255),
    email VARCHAR(255),
    deviceToken VARCHAR(255),
    role VARCHAR(255),
    CONSTRAINT role_enum CHECK (role IN ('ADMIN', 'USER')), -- Assuming UserRole is an enum with these values
    CONSTRAINT unique_email UNIQUE (email)
);

CREATE TABLE profile_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255),
    user_id BIGINT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_id INT,
    name VARCHAR(255),
    explanation TEXT,
    hashed_id VARCHAR(255),
    started_at DATE,
    ended_at DATE,
    done BOOLEAN DEFAULT FALSE,
    host_id BIGINT,
    CONSTRAINT fk_host_id FOREIGN KEY (host_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE project_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(255),
    enable BOOLEAN DEFAULT TRUE,
    user_id BIGINT,
    project_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE meetings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_id BIGINT,
    title VARCHAR(255),
    hashed_id VARCHAR(255),
    started_at DATETIME,
    ended_at DATETIME,
    host_id BIGINT,
    project_id BIGINT,
    FOREIGN KEY (host_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE meeting_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    accept BOOLEAN DEFAULT FALSE,
    meeting_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (meeting_id) REFERENCES meetings(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE timeslots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    started_at DATETIME,
    ended_at DATETIME,
    meeting_user_id BIGINT,
    FOREIGN KEY (meeting_user_id) REFERENCES meeting_users(id) ON DELETE CASCADE
);

CREATE TABLE alarms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(255),
    date DATE,
    is_read BOOLEAN DEFAULT FALSE,
    project_id BIGINT,
    user_id BIGINT,
    target_user_id BIGINT,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (target_user_id) REFERENCES users(id) ON DELETE CASCADE
);