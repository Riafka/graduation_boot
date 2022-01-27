DELETE
FROM vote;
DELETE
FROM menu_item;
DELETE
FROM restaurant;
DELETE
FROM users;
DELETE
FROM user_role;

INSERT INTO users (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@gmail.com', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('User2', 'user2@gmail.com', '{noop}password');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO restaurant (NAME)
VALUES ('Blue Lagoon'),
       ('MC Donald''s'),
       ('Krusty Krabs'),
       ('Riga'),
       ('Suliko');

INSERT INTO menu_item(RESTAURANT_ID, NAME, PRICE, MENU_DATE)
VALUES (1, 'Oysters', 100000, NOW()),
       (1, 'Fried squid', 50000, NOW()),
       (1, 'Salmon with lemon', 50000, NOW()),
       (1, 'Red Wine', 250000, NOW()),
       (1, 'Tasty desert', 25000, NOW());

INSERT INTO menu_item(RESTAURANT_ID, NAME, PRICE, MENU_DATE)
VALUES (2, 'Big Mac', 13000, NOW()),
       (2, 'Big Tasty', 26000, NOW()),
       (2, 'Hamburger', 5000, NOW()),
       (2, 'Coca Cola', 3000, NOW()),
       (2, 'Coffee', 5000, NOW());

INSERT INTO menu_item(RESTAURANT_ID, NAME, PRICE, MENU_DATE)
VALUES (3, 'Krabby meal', 25050, NOW()),
       (3, 'Krabby Patty', 26030, NOW()),
       (3, 'Kelp Rings', 20000, NOW()),
       (3, 'Kelp Shake', 18040, NOW()),
       (3, 'Sailors Surprise', 30000, NOW());

INSERT INTO menu_item(RESTAURANT_ID, NAME, PRICE, MENU_DATE)
VALUES (4, 'Crayfish salad', 50000, NOW()),
       (4, 'Onion soup', 75000, NOW()),
       (4, 'Butter bean ragout', 80000, NOW()),
       (4, 'Bacon wrapped chicken', 70000, NOW()),
       (4, 'Fish of the Day', 90000, NOW());

INSERT INTO menu_item(RESTAURANT_ID, NAME, PRICE, MENU_DATE)
VALUES (5, 'Khinkali', 40000, NOW()),
       (5, 'Khachapuri in Adjarian style', 50000, NOW()),
       (5, 'Khachapuri in Imeretian style', 50000, NOW());

INSERT INTO menu_item(RESTAURANT_ID, NAME, PRICE, MENU_DATE)
VALUES (1, 'Old Oysters', 130040, NOW() - 1),
       (1, 'Old Fried squid', 53000, NOW() - 1),
       (1, '2 days old Salmon with lemon', 52000, NOW() - 2),
       (1, '2 days old Red Wine', 252000, NOW() - 2),
       (1, '2 days old desert', 25300, NOW() - 2),
       (2, '5 days old Big Mac', 13000, NOW() - 5),
       (2, '5 days old Big Tasty', 26000, NOW() - 5),
       (2, '5 days old Hamburger', 5000, NOW() - 5),
       (2, '6 days old Coca Cola', 3000, NOW() - 6),
       (2, '6 days old Coffee', 5000, NOW() - 6),
       (3, '3 days old Krabby meal', 25050, NOW() - 3),
       (3, '3 days old Krabby Patty', 26030, NOW() - 3),
       (3, '3 days old Kelp Rings', 20000, NOW() - 3),
       (3, '3 days old Kelp Shake', 18040, NOW() - 3),
       (3, '3 days old Sailors Surprise', 30000, NOW() - 3),
       (4, 'Crayfish salad', 50000, NOW() + 1),
       (4, 'Future Onion soup', 75000, NOW() + 1),
       (4, 'Future Butter bean ragout', 80000, NOW() + 1),
       (4, 'Future Bacon wrapped chicken', 70000, NOW() + 1),
       (4, 'Future fish of the Day', 90000, NOW() + 1);

INSERT INTO vote(USER_ID, RESTAURANT_ID, VOTE_DATE)
VALUES (1, 1, now()),
       (2, 5, now()),
       (1, 2, now() - 1),
       (2, 3, now() - 1),
       (1, 4, now() - 2),
       (2, 1, now() - 2);