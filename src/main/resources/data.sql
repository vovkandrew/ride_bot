insert into country (name) values ('Україна'), ('Польща'), ('Чехія'), ('Німеччина'), ('Франція'), ('Іспанія'),
('Португалія'), ('Бельгія') on conflict do nothing;

insert into city (name, country_id) values ('Київ', 1), ('Дніпро', 1), ('Львів', 1), ('Запоріжжя', 1), ('Житомир', 1),
('Чернігів', 1), ('Ужгород', 1), ('Івано-Франківськ', 1) on conflict do nothing;

insert into city (name, country_id) values ('Варшава', 2), ('Краків', 2), ('Лодзь', 2), ('Вроцлав', 2), ('Познань', 2),
('Гданськ', 2), ('Щецин', 2), ('Бидгощ', 2) on conflict do nothing;

insert into city (name, country_id) values ('Прага', 3), ('Брно', 3), ('Острава', 3), ('Пльзень', 3), ('Ліберец', 3),
('Оломоуц', 3), ('Усті-Над-Лабем', 3), ('Градец Кралове', 3) on conflict do nothing;

insert into city (name, country_id) values ('Берлін', 4), ('Гамбург', 4), ('Мюнхен', 4), ('Кельн', 4),
('Франкфурт На Майні', 4), ('Штутгарт', 4), ('Дюссельдорф', 4), ('Дортмунд', 4) on conflict do nothing;

insert into city (name, country_id) values ('Париж', 5), ('Марсель', 5), ('Ліон', 5), ('Тулуза', 5), ('Ніцца', 5),
('Нант', 5), ('Страсбург', 5), ('Монпельє', 5) on conflict do nothing;

insert into city (name, country_id) values ('Мадрид', 6), ('Барселона', 6), ('Валенсія', 6), ('Севілья', 6),
('Сарагоса', 6), ('Малага', 6), ('Мурсія', 6), ('Пальма-Де-Майорка', 6) on conflict do nothing;

insert into city (name, country_id) values ('Лісабон', 7), ('Порту', 7), ('Лоуреш', 7), ('Віла-Нова-Ді-Гая', 7),
('Амадора', 7), ('Брага', 7), ('Гондомар', 7), ('Матозіньюш', 7) on conflict do nothing;

insert into city (name, country_id) values ('Антверпен', 8), ('Гент', 8), ('Шарлеруа', 8), ('Льєж', 8), ('Брюссель', 8),
('Брюгге', 8), ('Намюр', 8), ('Монс', 8) on conflict do nothing;







