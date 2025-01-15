-- Users
INSERT INTO users (username, organization, uid, email, device_token, role, receive_permission) VALUES
('test user1', 'test org1', 'hIGOWUmXSugwCftVJ2HsF9kiqfh1', 'test1@test.com', 'test_token1' ,'USER', true),
('test_user2', 'test_org2', 'test_uid2', 'test2@test.com', 'test_token2', 'USER', true),
('test_user3', 'test_org3', 'test_uid3', 'test3@test.com', 'test_token3', 'USER', true),
('test_user4', 'test_org4', 'test_uid4', 'test4@test.com', 'test_token4', 'USER', true),
('test_user5', 'test_org5', 'test_uid5', 'test5@test.com', 'test_token5', 'USER', true),
('test_user6', 'test_org6', 'test_uid6', 'test6@test.com', 'test_token6', 'USER', true),
('test_user7', 'test_org7', 'test_uid7', 'test7@test.com', 'test_token7', 'USER', true),
('test_user8', 'test_org8', 'test_uid8', 'test8@test.com', 'test_token8', 'USER', true),
('test_user9', 'test_org9', 'test_uid9', 'test9@test.com', 'test_token9', 'USER', true),
('test_user10', 'test_org10', 'test_uid10', 'test10@test.com', 'test_token10', 'USER', true);

-- Profile Images
INSERT INTO profile_images (image_idx, user_id) VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,5),
(6,6),
(7,7),
(8,8),
(9,9),
(10,10);

-- Projects
INSERT INTO projects (name, explanation, hashed_id, started_at, ended_at, host_id,is_done) VALUES
('test_project1', 'test_explanation1', 'test_hash1', '2024-01-01', '2024-12-31', 1, false),
('test_project2', 'test_explanation2', 'test_hash2', '2024-02-01', '2024-12-31', 2, false),
('test_project3', 'test_explanation3', 'test_hash3', '2024-03-01', '2024-12-31', 3, false),
('test_project4', 'test_explanation4', 'test_hash4', '2024-04-01', '2024-12-31', 4, false),
('test_project5', 'test_explanation5', 'test_hash5', '2024-05-01', '2024-12-31', 5, false),
('test_project6', 'test_explanation6', 'test_hash6', '2024-06-01', '2024-12-31', 6, false),
('test_project7', 'test_explanation7', 'test_hash7', '2024-07-01', '2024-12-31', 7, false),
('test_project8', 'test_explanation8', 'test_hash8', '2024-08-01', '2024-12-31', 8, false),
('test_project9', 'test_explanation9', 'test_hash9', '2024-09-01', '2024-12-31', 9, false),
('test_project10', 'test_explanation10', 'test_hash10', '2024-10-01', '2024-12-31', 10, false);

-- Project Users
INSERT INTO project_users (role, user_id, project_id, is_black) VALUES
('MEMBER', 1, 1, false),
('MEMBER', 2, 2, false),
('MEMBER', 3, 3, false),
('MEMBER', 4, 4, false),
('MEMBER', 5, 5, false),
('MEMBER', 6, 6, false),
('MEMBER', 7, 7, false),
('MEMBER', 8, 8, false),
('MEMBER', 9, 9, false),
('MEMBER', 10, 10, false);

-- Meetings
INSERT INTO meetings (title, hashed_id, started_at, ended_at, host_id, project_id) VALUES
('test_meeting1', 'test_hash1', '2024-01-01 10:00:00', '2024-01-01 11:00:00', 1, 1),
('test_meeting2', 'test_hash2', '2024-01-02 10:00:00', '2024-01-02 11:00:00', 2, 1),
('test_meeting3', 'test_hash3', '2024-01-03 10:00:00', '2024-01-03 11:00:00', 3, 2),
('test_meeting4', 'test_hash4', '2024-01-04 10:00:00', '2024-01-04 11:00:00', 4, 2),
('test_meeting5', 'test_hash5', '2024-01-05 10:00:00', '2024-01-05 11:00:00', 5, 3),
('test_meeting6', 'test_hash6', '2024-01-06 10:00:00', '2024-01-06 11:00:00', 6, 3),
('test_meeting7', 'test_hash7', '2024-01-07 10:00:00', '2024-01-07 11:00:00', 7, 4),
('test_meeting8', 'test_hash8', '2024-01-08 10:00:00', '2024-01-08 11:00:00', 8, 4),
('test_meeting9', 'test_hash9', '2024-01-09 10:00:00', '2024-01-09 11:00:00', 9, 5),
('test_meeting10', 'test_hash10', '2024-01-10 10:00:00', '2024-01-10 11:00:00', 10, 5);

-- Meeting Users
INSERT INTO meeting_users (is_displayed, meeting_id, user_id) VALUES
(true, 1, 1),
(true, 1, 2),
(true, 2, 3),
(true, 2, 4),
(true, 3, 5),
(true, 3, 6),
(true, 4, 7),
(true, 4, 8),
(true, 5, 9),
(true, 5, 10);

-- Timeslots
INSERT INTO timeslots (started_at, ended_at, meeting_user_id) VALUES
('2024-01-01 10:00:00', '2024-01-01 11:00:00', 1),
('2024-01-01 11:00:00', '2024-01-01 12:00:00', 2),
('2024-01-02 10:00:00', '2024-01-02 11:00:00', 3),
('2024-01-02 11:00:00', '2024-01-02 12:00:00', 4),
('2024-01-03 10:00:00', '2024-01-03 11:00:00', 5),
('2024-01-03 11:00:00', '2024-01-03 12:00:00', 6),
('2024-01-04 10:00:00', '2024-01-04 11:00:00', 7),
('2024-01-04 11:00:00', '2024-01-04 12:00:00', 8),
('2024-01-05 10:00:00', '2024-01-05 11:00:00', 9),
('2024-01-05 11:00:00', '2024-01-05 12:00:00', 10);

-- Alarms
INSERT INTO alarms (status, date, project_id, user_id, target_user_id) VALUES
(0, '2024-01-01', 1, 1, 2),
(0, '2024-01-02', 1, 2, 3),
(0, '2024-01-03', 2, 3, 4),
(0, '2024-01-04', 2, 4, 5),
(0, '2024-01-05', 3, 5, 6),
(0, '2024-01-06', 3, 6, 7),
(0, '2024-01-07', 4, 7, 8),
(0, '2024-01-08', 4, 8, 9),
(0, '2024-01-09', 5, 9, 10),
(0, '2024-01-10', 5, 10, 1);