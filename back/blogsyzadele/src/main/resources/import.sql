INSERT INTO TOPIC (name,presentation) VALUES ('beauty','This is a topic about beauty.'),('cat','This is a topic about cat.'), ('Life style','This is a topic about life style.');
INSERT INTO POST (auther,content,title,read_times,topic_id) VALUES ('adele','...','post1',0,1), ('nathan','...','post2',0,1), ('lamian','...','post3',0,2);
insert  into `sys_role`(`id`,`name`) values (1,'ROLE_ADMIN'),(2,'ROLE_USER');
insert  into `sys_user`(`id`,`password`,`username`) values (1,'root','root'),(2,'sang','sang');
insert  into `sys_user_roles`(`sys_user_id`,`roles_id`) values (1,1),(2,2);