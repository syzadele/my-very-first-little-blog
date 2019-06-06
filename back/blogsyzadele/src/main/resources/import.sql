INSERT INTO TOPIC (name,presentation) VALUES ('beauty','This is a topic about beauty.'),('cat','This is a topic about cat.'), ('Life style','This is a topic about life style.');
INSERT INTO POST (auther,content,title,read_times,topic_id) VALUES ('adele','...','post1',0,1), ('nathan','...','post2',0,1), ('lamian','...','post3',0,2);
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
INSERT INTO users(created_at, updated_at, email, name, password, username) VALUES('2019-06-06 10:10:48', '2019-06-06 10:10:48', 'user1@outlook.com', 'user1', '$2a$10$LCddLqJTNYlQRtJ27Z4epOWIwL7kBNERmP8vRKzqNDxmv0h8Q38ny', 'user1');
INSERT INTO user_roles(user_id,role_id) VALUES ('1','1'),('1','2');