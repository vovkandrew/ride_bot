package org.project.util.constants;

import static org.project.util.constants.Constants.DEFAULT_OFFSET;

public class Messages {
    public static String LINE_BREAK = "\n\n";
    public static String joinMessages(String...messages) {
        StringBuilder builder = new StringBuilder(messages[DEFAULT_OFFSET]);

        for (int i = 1; i < messages.length; i++) {
            builder.append(LINE_BREAK).append(messages[i]);
        }

        return builder.toString();
    }
    public static String EXCEPTION_MESSAGE = "Упс, щось трапилось \uD83D\uDE23 і бот нажаль не може вам відповісти. " + "Будь ласка зв'яжиться з нашою підтримкою і вони вам допоможуть! ✌";
    public static String GENERAL_GREETING = "Вітаємо тебе в боті перевезень ✋";
    public static String OFFER_REGISTRATION = "Спочатку треба зареєструватися як водій перш ніж створювати поїздки. " + "Почнемо реєстрацію?";
    public static String DRIVER_DETAILS = "Ваша поточна анкета водія.\nІм'я: <b>%s</b>\nПрізвище: <b>%s</b>" + "\nНомер телефону: <b>%s</b>\nМарка авто: <b>%s</b>\nКолір авто: <b>%s</b>\nРеєстраційний номер ТЗ: " + "<b>%s</b>\nКількість пасажирських місць: <b>%s</b>";
    public static String EDIT_INFO = "Щоб відредагувати те чи інше поле, натисніть відповідну кнопку";
    public static String PROVIDE_FIRST_NAME = "Будь-ласка введіть інформацию про своє <b><i>ім'я</i></b> " + "(разом і без пробілів) \uD83D\uDC47";
    public static String FIRST_NAME_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Вашe ім'я: <b><i>%s</i></b>";
    public static String FIRST_NAME_INVALID = "Ваше <b><i>ім'я</i></b> не повинно містити пробілів та бути довшим за " + "15 символів ⛔";
    public static String PROVIDE_LAST_NAME = "Будь-ласка введіть інформацию про своє <b><i>прізвище</i></b> (разом " + "і без пробілів) \uD83D\uDC47";
    public static String LAST_NAME_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Вашe прізвище: <b><i>%s</i></b>";
    public static String LAST_NAME_INVALID = "Ваше <b><i>прізвище</i></b> не повинно містити пробілів та бути довшим " + "за 15 символів ⛔";
    public static String PROVIDE_PHONE_NUMBER = "Будь-ласка запровадьте свій <b><i>номер телефону</i></b> " + "натиснувши кнопку ☎";
    public static String PHONE_NUMBER_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Ваш номер телефону: <b><i>%s</i></b>";

    public static String USER_INPUT_INSTEAD_BUTTON_CLICK_PROVIDED = "Будь-ласка скористайтесь кнопками вище щоб " + "обрати потрібну вам опцію, не треба вводити вашу відповідь вручну на цьому кроці \uD83D\uDC47";
    public static String PROVIDE_CAR_MODEL = "Будь-ласка введіть інформацию про <b><i>модель та марку</i></b> свого " + "транспортного засобу \uD83D\uDE97";
    public static String CAR_MODEL_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Модель та марка вашого авто: <b><i>%s" + "</i></b>";
    public static String CAR_MODEL_INVALID = "Ваша <b><i>модель та марка авто</i></b> повинна складатись лише з літер" + " верхнього або нижнього регистрів та цифр⛔";
    public static String PROVIDE_CAR_COLOR = "Будь-ласка введіть інформацию про <b><i>колір</i></b> свого " + "транспортного засобу \uD83C\uDF08";
    public static String CAR_COLOR_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Колір вашого авто: <b><i>%s</i></b>";
    public static String CAR_COLOR_INVALID = "<b><i>Колір</i></b> вашого ТЗ повинен складатись з одного чи декількох " + "слів та може містити пробіли чи дефіси❗";
    public static String PROVIDE_PLATE_NUMBER = "Будь-ласка введіть інформацию про <b><i>реєстраційний номер</i></b> " + "свого транспортного засобу\uD83D\uDD20";
    public static String PLATE_NUMBER_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Реєстраційний номер вашого авто: " + "<b><i>%s</i></b>";
    public static String PLATE_NUMBER_INVALID = "<b><i>Реєстраційний номер</i></b> повинен складатись лише з букв " + "будь-якого регістру та цифр❗";
    public static String PROVIDE_SEATS_NUMBER = "Будь-ласка введіть інформацию про <b><i>кількість пассажирських " + "місць</i></b> у вашому транспортному засобі\uD83D\uDD22";
    public static String SEATS_NUMBER_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Кількість пасажирських місць в" + " вашому авто: <b><i>%s</i></b>";
    public static String SEATS_NUMBER_INVALID = "<b><i>Кількість пассажирських місць</i></b> повинна складатись з " + "одної чи декількох цифр❗";
    public static String DATA_UPDATED = "Ваші персональні данні оновлено!\uD83D\uDC4D";
    public static String USER_AGREEMENT = "Будь-ласка ознайомтесь з угодою водія \uD83D\uDCC4. Якщо вас усе" + " влаштовує, натисніть \"Погоджуюсь\"➕ та завершить реєстрацію. В іншому випадку натиснить " + "\"Відмовляюсь\"❌, усі ваші персональні дані будуть видалені та реєстрацію буде скасовано.";
    public static String USER_AGREEMENT_CONFIRMED = "Реєстрацію завершено, вітаємо!✨ Тепер ви можете створювати " + "маршрути та поїздки згідно ваших маршрутів!\uD83D\uDE80 За потреби, також, ви можете змінити вашу " + "персональну інформацію.";
    public static String USER_AGREEMENT_DECLINED = "Дуже прикро що ви вирішили не реєструватися.\uD83D\uDE2D Проте " + "ви все ще маєте змогу шукати поїздки та користуватися сервісом! ✨ Якщо зміните вашу думку, ви завжди " + "можете знову зареєструватися щоб пропонувати свою поїздки!\uD83D\uDE09";
    public static String GREETING_DRIVER = "Вітаємо у головному меню водія.\uD83D\uDCBB Тут ви можете переглядати, " + "змінювати, створювати чи видаляти свої маршрути, поїздки та персональні дані.";
    public static String GREETING_PASSENGER = "Вітаємо у головному меню пасажира.\uD83D\uDE8F Тут ви можете " + "відстежувати маршрути, бронювати та переглядати ваші поїздки.";
    public static String PROVIDE_COUNTY_FROM = "Будь-ласка оберіть <b>країну</b> відправлення⏬";
    public static String COUNTRY_FROM_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Обрана країна відправлення: " + "<b><i>%s</i></b>";
    public static String PROVIDE_CITY_FROM = "Будь-ласка оберіть <b>місто</b> відправлення⏬";
    public static String CITY_FROM_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Обране місто відправлення: " + "<b><i>%s</i></b>";
    public static String PROVIDE_COUNTY_TO = "Будь-ласка оберіть <b>країну</b> прибуття⏬";
    public static String COUNTRY_TO_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Обрана країна прибуття: " + "<b><i>%s</i></b>";
    public static String PROVIDE_CITY_TO = "Будь-ласка оберіть <b>місто</b> прибуття⏬";
    public static String CITY_TO_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Обране місто прибуття: " + "<b><i>%s</i></b>";
    public static String ROUTE_DATA = "Дані вашого маршруту.\n\nКраїна відправлення: <b>%s</b>\nМісто " + "відправлення: <b>%s</b>\nКраїна прибуття: <b>%s</b>\nМісто прибуття: <b>%s</b>\n";
    public static String ROUTE_DATA_INFO = "Натисніть відповідну кнопку для зміни " + "того чи іншого поля. \uD83D\uDEA5";
    public static String WRONG_ROUTE_DATA_PROVIDED = "Будь-ласка, скористуйтесь вказівками вище та оберіть " + "країну/місто. Ввод інформації вручну не підтримується на цьому етапі❌";
    public static String NEW_ROUTE_CREATED = "Вітаю✨, новий маршрут створено: \n\uD83D\uDEA9 %s - \uD83C\uDFC1 %s";
    public static String ROUTES_MENU = "Вітаємо у меню маршрутів.\uD83D\uDE8F Тут ви можете створювати нові та " + "переглядати поточні наявні маршрути";
    public static String DRIVER_TRIPS_MENU = "Вітаємо у меню поїздок. \uD83D\uDE8F Тут ви маєте змогу створювати нові" + " та редагувати або переглядати вже створені поїздки";
    public static String CONTINUE_CREATE_DRIVER_TRIP = "У вас є незавершенне створення поїздки: \nМаршрут: %s\n" + "Дата відправлення: %s\nЧас відправлення: %s\n Дата прибуття: %s\nЧас прибуття: %s\nМісце збору: %s\n" + "Місце прибуття: %s\nВалюта: %s\nЦіна: %s\nІнформація про багаж: %s\nЗагальна інформація: %s\nБажаєте продовжити " + "створювати цю поїздку чи краще почати заново?";
    public static String TRIP_CREATING_CHOOSE_ROUTE = "Оберіть маршрут за яким ви бажаєте створити поїздку " + "\uD83D\uDCCB";
    public static String TRIP_CREATING_ROUTE_CHOSEN = "Дякую за відповідь!\uD83D\uDE4C Обраний маршрут для поїздки: " + "<b><i>%s</i></b>";
    public static String DRIVER_TRIP_ENTER_DEPARTURE_DATE = "Будь-ласка, введіть інформацию про дату відправлення у " + "форматі: день.місяць.рік (01.02.2023) \uD83D\uDCC6";
    public static String DRIVER_TRIP_DEPARTURE_DATE_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Обрана дата " + "відправленя: <b><i>%s</i></b>";
    public static String DRIVER_TRIP_WRONG_DEPARTURE_DATE = "❌ <b><i>Дата відправленя</i></b> повинна " + "відповідати формату день(дві цифри).місяць(дві цифри).рік(чотири цифри), наприклад: 03.10.2023, та " + "не повинна бути раніше ніж сьогоднішня дата.";
    public static String DRIVER_TRIP_ENTER_DEPARTURE_TIME = "Будь-ласка, введіть інформацию про час відправлення у " + "24 часовому форматі: часи:хвилини (13:15) ⌚";
    public static String DRIVER_TRIP_DEPARTURE_TIME_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Обраний час " + "відправленя: <b><i>%s</i></b>";
    public static String DRIVER_TRIP_ENTER_ARRIVAL_DATE = "Будь-ласка, введіть інформацию про дату прибуття у " + "форматі: день.місяць.рік (01.02.2023) \uD83D\uDCC6";
    public static String DRIVER_TRIP_WRONG_DEPARTURE_TIME = "❌ <b><i>Час відправленя</i></b> повинен " + "відповідати 24х часовому формату: часи:хвилини, наприклад: 15:30, і поїздка повинна починатися не " + "раніше ніж за годину";
    public static String DRIVER_TRIP_ARRIVAL_DATE_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Обрана дата " + "прибуття: <b><i>%s</i></b>";
    public static String DRIVER_TRIP_ENTER_ARRIVAL_TIME = "Будь-ласка, введіть інформацию про час прибуття у форматі: " + "часи:хвилини (13:15) ⌚";
    public static String DRIVER_TRIP_WRONG_ARRIVAL_DATE = "❌ <b><i>Дата прибуття</i></b> не повинна бути раніше " + "дати відправлення але може співпадати з нею, та відповідати формату день(дві цифри).місяць(дві цифри)." + "рік(чотири цифри), наприклад: 13.10.2023";
    public static String DRIVER_TRIP_ARRIVAL_TIME_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Обраний час " + "прибуття: <b><i>%s</i></b>";
    public static String DRIVER_TRIP_ENTER_PICKUP_POINT = "Будь-ласка, введіть інформацию про місце відправлення з " + "міста <b><i>%s</i></b> \uD83D\uDE8F: це повинно бути одне повідомлення не довше за 200 символів з " + "урахуванням знаків пунктуації. Наприклад: \"вихід зі станціі метро Житомірська, біля зупинки міського " + "транспорту\"";
    public static String DRIVER_TRIP_WRONG_ARRIVAL_TIME = "❌ <b><i>Час прибуття</i></b> повинен бути не раніше часу " + "відправлення, якщо поїздка відбувається протягом одного дня, та відповідати 24х часовому формату: " + "часи:хвилини, наприклад: 15:30";
    public static String DRIVER_TRIP_PICKUP_POINT_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Місце відправлення: " + "<b><i>%s</i></b>";
    public static String DRIVER_TRIP_ENTER_DROPOFF_POINT = "Будь-ласка, введіть інформацию про місце прибуття у " + "місто <b><i>%s</i></b> \uD83D\uDE8F: це повинно бути одне повідомлення не довше за 200 символів з " + "урахуванням знаків пунктуації. Наприклад: \"Центральній авто вокзал, платформа №3\"";
    public static String DRIVER_TRIP_WRONG_PICKUP_POINT = "❌ <b><i>Місце відправлення</i></b> повинно " + "містити в одному повідомленні не меньше 10 та не більше 200 символів з урахованням знаків пунктуації";
    public static String DRIVER_TRIP_DROPOFF_POINT_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Місце прибуття: " + "<b><i>%s</i></b>";
    public static String DRIVER_TRIP_CHOOSE_CURRENCY = "Будь-ласка, оберіть валюту вартості квитка \uD83D\uDCB5";
    public static String DRIVER_TRIP_CURRENCY_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Валюта: " + "<b><i>%s</i></b>";
    public static String DRIVER_TRIP_ENTER_PRICE = "Будь-ласка, введіть ціну квитка у валюті <b><i>%s</i></b>, " + "наприклад: 1500 \uD83D\uDCB5";
    public static String DRIVER_TRIP_WRONG_DROPOFF_POINT = "❌ <b><i>Місце прибуття</i></b> повинно " + "містити в одному повідомленні не меньше 10 та не більше 200 символів з урахованням знаків пунктуації";
    public static String DRIVER_TRIP_PRICE_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Ціна поїздки: " + "<b><i>%d</i></b>";
    public static String DRIVER_TRIP_WRONG_PRICE = "❌ <b><i>Ціна поїздки</i></b> повинна складатись з однієї цифри " + "не меньше ніж 100 грн. та не більше 9999 грн.";
    public static String DRIVER_TRIP_ENTER_BAGGAGE_INFO = "Будь-ласка, введіть інформацію про допустимий багаж " + "\uD83D\uDC5C: це повинно бути одне повідомлення не довше за 200 символів з урахуванням знаків " + "пунктуації. Наприклад: \"Дозволяється брати з собою не більше одної середньої валізи\"";
    public static String DRIVER_TRIP_BAGGAGE_INFO_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Інформація про багаж: " + "<b><i>%s</i></b>";
    public static String DRIVER_TRIP_EDIT_OTHER_INFO = "Будь-ласка, введіть додаткову інформацию \uD83D\uDCC3:"
            + " це повинно бути одне повідомлення не довше за 200 символів з урахуванням знаків пунктуації."
            + " Наприклад: \"Зупинки кожні 3 години. В автобусі є безкоштовний WiFi\". Щоб залишити це поле пустим, "
            + "введіть \"-\"";
    public static String DRIVER_TRIP_WRONG_BAGGAGE_INFO = "❌ <b><i>Інформація про багаж</i></b> повинна містити в"
            + " одному повідомленні не меньше 10 та не більше 200 символів з урахованням знаків пунктуації.";
    public static String DRIVER_TRIP_OTHER_INFO_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Додаткова інформація: "
            + "<b><i>%s</i></b>";
    public static String DRIVER_TRIP_WRONG_OTHER_INFO = "❌ <b><i>Додаткова інформація</i></b> повинна містити в"
            + " одному повідомленні не меньше 10 та не більше 200 символів з урахованням знаків пунктуації.";
    public static String TRIP_CREATED = "Вітаю, поїздка створена!\uD83D\uDE4C Тепер клієнти зможуть бронювати місця "
            + "на цю поїздку";
    public static String TRIP_DETAILS = "Деталі поїздки:\n\nМаршрут: <b><i>%s</i></b>\nДата відправлення: "
            + "<b><i>%s</i></b>\nЧас відправлення: <b><i>%s</i></b>\nДата прибуття: <b><i>%s</i></b>\nЧас прибуття: "
            + "<b><i>%s</i></b>\nМісце відправлення: <b><i>%s</i></b>\nМісце прибуття: <b><i>%s</i></b>\nВалюта: "
            + "<b><i>%s</i></b>\nЦіна: <b><i>%s</i></b>\nІнформація про багаж: <b><i>%s</i></b>\nДодаткова "
            + "інформація: <b><i>%s</i></b>\nКількість вільних місць: <b><i>%d</i></b>\nКількість заброньованих місць: "
            + "<b><i>%d</i></b>\n\nЩоб відредагувати те чи інше поле, натисніть відповідну кнопку";
    public static String DELETE_DRIVER_TRIP = "Ви точно бажаєте видалити поїздку %s?";
    public static String DRIVER_TRIP_DELETED = "Вітаю! Вашу поїздку видалено ✂";
    public static String FIND_TRIP = "Щоб знайти поїдку, треба спочатку визначити деталі маршруту \uD83D\uDCDD";
    public static String FIND_TRIP_NO_TRIPS = "Нажаль зараз відсутні поїздки за обраним маршрутом ❌. Бажаєте почати"
            + " відслідковувати коли з'являться нові поїздки за обраним маршрутом? \uD83D\uDD0E";
    public static String FIND_TRIP_LOOKING_FOR_TRIPS = "Шукаэмо поїздки за маршрутом <b><i>%s</i></b> \uD83D\uDD0E...";
    public static String FIND_TRIP_CHOOSE_TRIPS = "Будь ласка скористайтесь кнопками навігації аби переглянути наявні"
            + " поїздки за маршрутом <b><i>%s</i></b>";
    public static String FIND_TRIP_DETAILS_DESCRIPTION = "Деталі маршруту \uD83D\uDEE3\uFE0F \nВідправлення: <b><i>%s</i></b>," +
            " <b><i>%s</i></b>, <b><i>%s</i></b>\nДата: <b><i>%s</i></b> о <b><i>%s</i></b>\nПрибуття: <b><i>%s</i></b>," +
            " <b><i>%s</i></b>, <b><i>%s</i></b>\nДата: <b><i>%s</i></b> о <b><i>%s</i></b>\nБагаж: <b><i>%s</i></b>" +
            "\nІнше: <b><i>%s</i></b>\nЦіна: <b><i>%s</i></b> <b><i>%s</i></b>\nВільних місць: <b><i>%s</i></b>\n";
    public static String START_TRACKING_ROUTE = "Тепер ви відслідковуєте маршрут %s \uD83D\uDEE3\uFE0F. Коли хтось створить " +
            "поїздку за цим маршрутом, ми вас сповістимо!";
    public static String TRACKING_ROUTES_MENU = "Тут ви можете подивитись список маршрутів котрі ви відслідковуєте " +
            "\uD83D\uDEE3\uFE0F";
    public static String NEW_TRACKING_TRIP_CREATED = "\uD83D\uDCE2 Увага! \uD83C\uDD95 З'явилась нова доступна поїздка" +
            " за маршрутом який ви відслідковуєте %s. Скористайтеся кнопками навігації щоб переглянути деталі поїздки.";
    public static final String NO_EMPTY_SEATS_LEFT = "\uD83D\uDCBA Нажаль в даній поїздці не залишилось вільних місць!";
    public static final String EDIT_TRIP_ADD_PASSENGER_SET_NAME = "\uD83C\uDFF7\uFE0F Будь-ласка запровадьте" +
            " ідентіфікаційні дані пасажира у форматі: Ілля, Надія К., Володимир Одарчук, тощо.";
    public static final String PASSENGER_ENTER_SEATS = "Введіть кількість місць, які потрібно забронювати (В форматі: 2)";
    public static final String CHECK_AVAILABLE_SEATS = "Триває перевірка наявності вільних пасажирських місць";
    public static final String PASSENGER_ENTER_SEATS_PROVIDED = "Дякую за відповідь!\uD83D\uDE4C Кількість пасажирських" +
            " місць, які ви хочете забронювати: <b><i>%d</i></b>";
    public static final String PASSENGER_SEATS_FOUND = "Кількість доступних місць: <b><i>%d</i></b>";
    public static final String PASSENGER_SEATS_TOO_MUCH = "Кількість введених місць не має перевищувати кількість доступних!";
    public static final String RESTRICTED_ROUTE_DELETION = "Ви маєте заплановані поїздки за маршрутом:\n<b><i>%s</i></b>.\nМи не можемо його видалити.";
    public static final String ROUTE_DELETED = "Маршрут: <b><i>%s</i></b> та всі прострочені поїздки з пов'язані ним видалено!";
}
