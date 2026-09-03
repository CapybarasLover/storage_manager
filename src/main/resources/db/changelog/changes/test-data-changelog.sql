-- liquibase formatted sql

-- changeset Petra:seed-data-1
INSERT INTO storage (name) VALUES ('Склад №1 - Центральный');
INSERT INTO storage (name) VALUES ('Склад №2 - Северный');
INSERT INTO storage (name) VALUES ('Склад №3 - Южный');

-- changeset Petra:seed-data-2
-- Центральный
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №1 - Центральный'), 'Ноутбук Dell', 25, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №1 - Центральный'), 'Монитор Samsung 24"', 15, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №1 - Центральный'), 'Клавиатура Logitech', 30, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №1 - Центральный'), 'Мышь беспроводная', 50, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №1 - Центральный'), 'USB Hub 4-port', 20, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №1 - Центральный'), 'Веб-камера HD', 5, 'FEW');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №1 - Центральный'), 'Наушники Sony', 3, 'FEW');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №1 - Центральный'), 'Принтер HP LaserJet', 0, 'OUT');
-- Северный
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №2 - Северный'), 'Ноутбук Lenovo', 10, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №2 - Северный'), 'Монитор LG 27"', 12, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №2 - Северный'), 'Планшет iPad', 8, 'FEW');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №2 - Северный'), 'Смартфон Samsung', 4, 'FEW');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №2 - Северный'), 'Зарядное устройство USB-C', 2, 'FEW');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №2 - Северный'), 'Кабель HDMI', 0, 'OUT');
-- Южный
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №3 - Южный'), 'Сервер Dell PowerEdge', 11, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №3 - Южный'), 'Сетевой коммутатор', 15, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №3 - Южный'), 'Роутер Wi-Fi 6', 18, 'ENOUGH');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №3 - Южный'), 'ИБП APC 1000VA', 7, 'FEW');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №3 - Южный'), 'Патч-корд Cat6', 5, 'FEW');
INSERT INTO item (storage_id, item, count, status) VALUES ((SELECT id FROM storage WHERE name = 'Склад №3 - Южный'), 'Оптический кабель', 0, 'OUT');

-- changeset Petra:seed-data-3
-- Поступления (ADMISSION)
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'ADMISSION', 'Ноутбук Dell',             30, '2026-01-15 09:00:00+03', 'Первичная поставка ноутбуков');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'ADMISSION', 'Монитор Samsung 24"',       20, '2026-01-20 10:30:00+03', 'Поступление мониторов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'ADMISSION', 'Клавиатура Logitech',       50, '2026-02-01 08:45:00+03', 'Партия клавиатур');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'ADMISSION', 'Мышь беспроводная',         50, '2026-02-15 11:00:00+03', 'Поступление периферии');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'ADMISSION', 'USB Hub 4-port',             30, '2026-04-01 09:15:00+03', 'Поступление USB-хабов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'ADMISSION', 'Веб-камера HD',              10, '2026-05-01 10:00:00+03', 'Поступление веб-камер');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'ADMISSION', 'Наушники Sony',              8,  '2026-05-15 14:00:00+03', 'Поступление наушников');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'ADMISSION', 'Принтер HP LaserJet',        5,  '2026-03-20 09:30:00+03', 'Поступление принтеров');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'ADMISSION', 'Ноутбук Lenovo',             15, '2026-02-05 08:00:00+03', 'Поступление ноутбуков Lenovo');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'ADMISSION', 'Монитор LG 27"',             20, '2026-02-10 09:00:00+03', 'Поступление мониторов LG');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'ADMISSION', 'Планшет iPad',               12, '2026-04-01 10:00:00+03', 'Поступление планшетов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'ADMISSION', 'Смартфон Samsung',           7,  '2026-04-20 11:00:00+03', 'Поступление смартфонов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'ADMISSION', 'Зарядное устройство USB-C', 10, '2026-03-25 09:45:00+03', 'Поступление зарядных устройств');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'ADMISSION', 'Кабель HDMI',                15, '2026-03-01 08:30:00+03', 'Поступление HDMI-кабелей');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'ADMISSION', 'Сервер Dell PowerEdge',     15, '2026-03-01 07:00:00+03', 'Поступление серверного оборудования');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'ADMISSION', 'Сетевой коммутатор',         20, '2026-03-10 08:00:00+03', 'Поступление коммутаторов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'ADMISSION', 'Роутер Wi-Fi 6',             25, '2026-03-15 09:00:00+03', 'Поступление маршрутизаторов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'ADMISSION', 'ИБП APC 1000VA',             12, '2026-04-15 10:30:00+03', 'Поступление ИБП');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'ADMISSION', 'Патч-корд Cat6',             20, '2026-02-20 08:00:00+03', 'Поступление патч-кордов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'ADMISSION', 'Оптический кабель',          10, '2026-02-25 09:00:00+03', 'Поступление оптических кабелей');
-- Продажи (SELL)
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'SELL', 'Ноутбук Dell',             5,  '2026-02-28 15:00:00+03', 'Продажа корпоративному клиенту');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'SELL', 'Монитор Samsung 24"',       5,  '2026-03-10 14:30:00+03', 'Продажа мониторов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'SELL', 'Клавиатура Logitech',       20, '2026-04-05 13:00:00+03', 'Продажа клавиатур офису');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'SELL', 'USB Hub 4-port',             10, '2026-06-15 11:00:00+03', 'Продажа USB-хабов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'SELL', 'Веб-камера HD',              3,  '2026-07-20 10:00:00+03', 'Продажа веб-камер');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'SELL', 'Наушники Sony',              5,  '2026-07-01 12:00:00+03', 'Продажа наушников');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'SELL', 'Принтер HP LaserJet',        3,  '2026-05-30 16:00:00+03', 'Продажа принтеров');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'SELL', 'Ноутбук Lenovo',             5,  '2026-04-10 14:00:00+03', 'Продажа ноутбуков');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'SELL', 'Монитор LG 27"',             8,  '2026-05-20 13:30:00+03', 'Продажа мониторов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'SELL', 'Планшет iPad',               4,  '2026-06-20 15:00:00+03', 'Продажа планшетов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'SELL', 'Смартфон Samsung',           3,  '2026-07-05 11:30:00+03', 'Продажа смартфонов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'SELL', 'Зарядное устройство USB-C', 6,  '2026-06-01 10:00:00+03', 'Продажа зарядных устройств');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'SELL', 'Кабель HDMI',                10, '2026-05-15 14:00:00+03', 'Продажа HDMI-кабелей');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'SELL', 'Сервер Dell PowerEdge',     4,  '2026-06-10 09:00:00+03', 'Продажа серверов дата-центру');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'SELL', 'Сетевой коммутатор',         5,  '2026-07-15 10:00:00+03', 'Продажа коммутаторов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'SELL', 'Роутер Wi-Fi 6',             7,  '2026-08-05 11:00:00+03', 'Продажа маршрутизаторов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'SELL', 'ИБП APC 1000VA',             5,  '2026-07-25 13:00:00+03', 'Продажа ИБП');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'SELL', 'Патч-корд Cat6',             10, '2026-05-05 09:30:00+03', 'Продажа патч-кордов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'SELL', 'Патч-корд Cat6',             5,  '2026-07-30 14:00:00+03', 'Повторная продажа патч-кордов');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'SELL', 'Оптический кабель',          7,  '2026-05-10 10:00:00+03', 'Продажа оптического кабеля');
-- Списания (WRITE_OFF)
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'WRITE_OFF', 'Веб-камера HD',         2,  '2026-08-15 09:00:00+03', 'Списание: механические повреждения');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №1 - Центральный', 'WRITE_OFF', 'Принтер HP LaserJet',   2,  '2026-08-01 10:00:00+03', 'Списание: заводской брак');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'WRITE_OFF', 'Зарядное устройство USB-C', 2, '2026-08-10 11:00:00+03', 'Списание: перегрев, брак');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №2 - Северный',    'WRITE_OFF', 'Кабель HDMI',           5,  '2026-08-20 12:00:00+03', 'Списание: повреждены при хранении');
INSERT INTO operation (storage_name, operation_type, product_name, amount, operation_date_time, comment) VALUES ('Склад №3 - Южный',       'WRITE_OFF', 'Оптический кабель',     3,  '2026-08-25 13:00:00+03', 'Списание: повреждение при монтаже');
