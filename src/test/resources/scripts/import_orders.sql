INSERT INTO orders (id, device_name, device_sn, problems, client_id, total_price)
VALUES (1, 'Asus Notebook AB9299', '91839021', 'Erro ao iniciar sistema', 1, 100.00);

INSERT INTO orders (id, device_name, device_sn, problems, client_id, total_price)
VALUES (2, 'Asus Notebook AB9499', '91233301', 'Erro ao iniciar sistema', 2, 1000.00);

INSERT INTO orders (id, device_name, device_sn, problems, client_id, total_price)
VALUES (3, 'Asus Notebook AB4599', '233314445', 'Erro ao iniciar sistema', 3, 1200.00);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (1, 1);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (1, 2);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (1, 3);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (2, 1);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (2, 2);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (3, 3);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (3, 1);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (3, 2);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES (3, 3);