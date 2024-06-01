INSERT INTO orders (id, device_name, device_sn, issues, client_id, total_price)
VALUES ('e1b64829-6a03-45aa-a12f-211153c3d19c', 'Asus Notebook AB9299', '918390SDA21', 'Erro ao iniciar sistema',
        'd6a0eb46-d011-4fb1-95ab-44f6d415d9bf', 1000.00);

INSERT INTO orders (id, device_name, device_sn, issues, client_id, total_price)
VALUES ('fdfe1855-8c37-4fce-bdeb-2dcd00ab2ca0', 'Asus Notebook AB9499', '91233301', 'Erro ao iniciar sistema',
        '88e3c5f7-d0c1-4829-b5e0-61541f9aa2dc',
        1000.00);

INSERT INTO orders (id, device_name, device_sn, issues, client_id, total_price)
VALUES ('1b4d3287-296c-4a81-9f5b-e234f5aa7aef', 'Asus Notebook AB4599', '233314445', 'Erro ao iniciar sistema',
        '2e8d5d11-61d7-4976-b5c9-54adfea13e5b',
        1200.00);

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('e1b64829-6a03-45aa-a12f-211153c3d19c', '0def47bf-ea7e-430a-a1e9-019a1f48d0ec');

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('e1b64829-6a03-45aa-a12f-211153c3d19c', '78e4961d-ee65-4a7b-b0c0-280b72654b7c');

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('e1b64829-6a03-45aa-a12f-211153c3d19c', '817a23cc-8e40-487f-ae27-fed67a113413');

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('fdfe1855-8c37-4fce-bdeb-2dcd00ab2ca0', '0def47bf-ea7e-430a-a1e9-019a1f48d0ec');

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('fdfe1855-8c37-4fce-bdeb-2dcd00ab2ca0', '78e4961d-ee65-4a7b-b0c0-280b72654b7c');

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('fdfe1855-8c37-4fce-bdeb-2dcd00ab2ca0', '817a23cc-8e40-487f-ae27-fed67a113413');

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('1b4d3287-296c-4a81-9f5b-e234f5aa7aef', '0def47bf-ea7e-430a-a1e9-019a1f48d0ec');

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('1b4d3287-296c-4a81-9f5b-e234f5aa7aef', '78e4961d-ee65-4a7b-b0c0-280b72654b7c');

INSERT INTO rel_order_order_items (order_id, order_item_id)
VALUES ('1b4d3287-296c-4a81-9f5b-e234f5aa7aef', '817a23cc-8e40-487f-ae27-fed67a113413');