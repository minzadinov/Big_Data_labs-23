# Отчет по 2 лабораторной работе

1. Сформировать отчёт с информацией о 10 наиболее популярных языках программирования по итогам года за период с 2010 по 2020 годы.
   Отчёт будет отражать динамику изменения популярности языков программирования и представлять собой набор таблиц "топ-10" для каждого года.

2. Получившийся отчёт сохранить в формате Apache Parquet.

## Ход работы
- Подключил Google Drive и загрузил с него csv и xml файлы с данными. 

- Реализовал поиск первого в списке languageList языка по тегу, который содержится в передаваемом в качестве аргумента объекте.

- Реализовал функцию, которая осуществляет отбор языков по датам для получения списков топа по годам. Отобразил их.

- Сохранил отчеты на Google Drive в формате Apache Parquet, затем выгрузил [сюда](./report)