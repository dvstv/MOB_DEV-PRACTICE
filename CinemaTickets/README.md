# Кинотеатр — выбор сеанса, мест и корзина

Учебное Android-приложение (Kotlin, XML-разметка, без Jetpack Compose).

## Экраны
1. **MoviesActivity** (стартовый) — афиша, выбор фильма и времени сеанса.
2. **SeatsActivity** — схема зала, выбор мест.
3. **CartActivity** — корзина: список билетов, удаление, итоговая сумма, оформление.

Переходы — явные Intent. Данные о сеансе передаются через `putExtra()`.

## Версии
| Параметр | Значение |
|---|---|
| minSdk | 26 (Android 8.0) |
| targetSdk / compileSdk | 35 (Android 15) |
| Язык | Kotlin 2.0.21 |

## Логи
Теги: `MoviesActivity`, `SeatsActivity`, `CartActivity`.
Сохранить журнал: `adb logcat -d -s MoviesActivity SeatsActivity CartActivity > logs/logs.txt`

## Снимки экранов
(добавить после запуска на эмуляторе)

## APK
(ссылка на Releases)
