use goottflix;

DROP TABLE IF EXISTS user;
CREATE TABLE  user  (
                        id 	int auto_increment primary key	NOT NULL,
                        username 	varchar(50)	NULL,
                        email 	varchar(100)	NULL,
                        loginId 	varchar(100)	NULL,
                        password_hash 	varchar(255)	NULL,
                        oauthId 	varchar(255)	NULL,
                        birth 	date	NULL,
                        gender 	ENUM('M','F')	NULL,
                        created_at 	timestamp	NULL	DEFAULT now()	COMMENT 'timestamp는 시간 초까지',
                        last_login 	timestamp	NULL	DEFAULT now(),
                        role 	varchar(50)	NULL	DEFAULT 'ROLE_USER',
                        is_active 	boolean	NULL,
                        preferences 	json	NULL,
                        subscribe 	varchar(50)	NULL	DEFAULT 'free'	COMMENT 'free,sub,expired',
                        profile_image varchar(255)
);

DROP TABLE IF EXISTS movies;
CREATE TABLE  movies  (
                          id 	int auto_increment primary key	NOT NULL,
                          title 	varchar(255)	NULL,
                          intro varchar(255) NULL COMMENT '영화 한줄설명',
                          description 	text	NULL,
                          release_date 	date	NULL	COMMENT '(2024-05-06)date형식',
                          rating 	decimal(3,2)	NULL	COMMENT '영화 평균별점 (1.0~5.0)',
                          genre 	varchar(50)	NULL	COMMENT '액션,코미디,드라마 등등',
                          director 	varchar(50)	NULL,
                          poster_url 	varchar(255)	NULL,
                          video_url varchar(255) NULL,
                          nation varchar(100) default '한국' NULL
);

DROP TABLE IF EXISTS notifys;
CREATE TABLE  notifys  (
                           id 	int auto_increment primary key	NOT NULL,
                           user_id 	int	NULL,
                           movie_id 	int	NULL,
                           content 	varchar(255)	NULL,
                           url 	varchar(255)	NULL,
                           is_read 	boolean	default false NOT NULL ,
                           notify_type 	enum('movieUpdate','friendadd')	NULL
);
DROP TABLE IF EXISTS cards;
CREATE TABLE  cards  (
                         id 	int auto_increment primary key	NOT NULL,
                         user_id 	int	NULL,
                        card_id varchar(255) null,
                         show_time 	timestamp	NULL,
                         room_number 	int	NULL,
                         seat_number 	varchar(20)	NULL,
                         movie_id 	int	NULL,
                        entered varchar(50) default 'noshow'
);

DROP TABLE IF EXISTS review;
CREATE TABLE  review  (
                          id 	int auto_increment primary key	NOT NULL,
                          user_id 	int	NULL,
                          movie_id 	int	NULL,
                          rating 	int	NULL,
                          review 	varchar(255)	NULL,
                          recommend 	int	NULL	DEFAULT 0,
                          spoiler int default 0,
                          review_date timestamp null default now()
);

DROP TABLE IF EXISTS subscribe;
CREATE TABLE  subscribe  (
                             id 	int auto_increment primary key	NOT NULL,
                             user_id 	int	NULL,
                             subscribe_start 	timestamp	NULL,
                             subscribe_end 	timestamp	NULL
);

DROP TABLE IF EXISTS apimovie;
CREATE TABLE apimovie (
     id int auto_increment primary key	NOT NULL,
    movieName varchar(255),
    director varchar(50),
    openDt varchar(50),
    prdtYear varchar(50),
    nationAlt varchar(100),
    movieCd varchar(50),
    genre varchar(100),
    repGenre varchar(50),
    filename varchar(255),
    filepath varchar(255)

);

DROP TABLE IF EXISTS friends;
CREATE TABLE friends(
    id int auto_increment primary key NOT NULL,
    user_id int,
    friend_id int,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS chat_room;
CREATE TABLE chat_room (
id INT AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(255) NOT NULL
);


DROP TABLE IF EXISTS chat_message;
CREATE TABLE chat_message (
id INT AUTO_INCREMENT PRIMARY KEY,
room_id INT NOT NULL,
sender VARCHAR(255) NOT NULL,
message LONGTEXT NOT NULL,
type VARCHAR(255),
timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (room_id) REFERENCES chat_room(id)
);

ALTER TABLE chat_message
    DROP FOREIGN KEY chat_message_ibfk_1;

ALTER TABLE chat_message
    ADD CONSTRAINT chat_message_ibfk_1
        FOREIGN KEY (room_id) REFERENCES chat_room(id)
            ON DELETE CASCADE;

DROP TABLE IF EXISTS likes;
CREATE TABLE likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    review_id INT NOT NULL
);

DROP TABLE IF EXISTS notice;
CREATE TABLE notice
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    title    VARCHAR(255) NOT NULL,
    content  VARCHAR(255) NOT NULL,
    image    VARCHAR(255),
    writer   INT          NOT NULL,
    password VARCHAR(20)
);


