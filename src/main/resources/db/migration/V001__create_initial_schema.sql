-- =========================
-- USERS
-- =========================
CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(60) NOT NULL,
                       phone VARCHAR(20) NOT NULL,
                       email VARCHAR(60) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       user_type VARCHAR(20) NOT NULL,

                       CONSTRAINT pk_users PRIMARY KEY (id),
                       CONSTRAINT uk_users_email UNIQUE (email),
                       CONSTRAINT ck_users_type
                           CHECK (user_type IN ('ADMIN', 'USER'))
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;


-- =========================
-- EVENTS
-- =========================
CREATE TABLE events (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(60) NOT NULL,
                        event_date_time DATETIME NOT NULL,
                        location VARCHAR(60) NOT NULL,
                        attraction VARCHAR(60) NOT NULL,
                        price DECIMAL(10,2) NOT NULL,
                        status VARCHAR(20) NOT NULL,

                        CONSTRAINT pk_events PRIMARY KEY (id),
                        CONSTRAINT ck_events_status
                            CHECK (status IN ('ACTIVE', 'CLOSED'))
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;


-- =========================
-- REGISTRATIONS
-- =========================
CREATE TABLE registrations (
                               id BIGINT NOT NULL AUTO_INCREMENT,

                               user_id BIGINT NOT NULL,
                               event_id BIGINT NOT NULL,

                               payment_status VARCHAR(20) NOT NULL,
                               check_in_status VARCHAR(20) NOT NULL,
                               check_in_date DATETIME NULL,

                               CONSTRAINT pk_registrations PRIMARY KEY (id),

                               CONSTRAINT uk_registrations_user_event
                                   UNIQUE (user_id, event_id),

                               CONSTRAINT fk_registrations_user
                                   FOREIGN KEY (user_id)
                                       REFERENCES users (id),

                               CONSTRAINT fk_registrations_event
                                   FOREIGN KEY (event_id)
                                       REFERENCES events (id),

                               CONSTRAINT ck_registrations_payment_status
                                   CHECK (payment_status IN ('PENDING', 'PAID')),

                               CONSTRAINT ck_registrations_checkin_status
                                   CHECK (check_in_status IN ('NOT_COMPLETED', 'COMPLETED'))
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
