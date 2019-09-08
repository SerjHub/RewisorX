package com.app.rewizor.data.model

data class PublicationDetailed(
    val guid: String,
    val category: String?,
    val name: String?,
    val subtitle: String?,
    val description: String?,
    val fullDescription: String?,
    val date: String?,
    val end: String?,
    val nearestDate: String?,
    val age: Int,
    val website: String?,
    val phone: String?,
    val workingDaysHours: String?,
    val source: String?,
    val address: String?,
    val lat: Long,
    val lon: Long,
    val parent: String,
    val image: ImageInfo?,
    val photos: List<ImageInfo>,
    val votes: Int,
    val ratings: Int?,
    val views: Int,
    val likes: Int,
    val hasLike: Boolean,
    val undergrounds: Map<Int, String>,

    val parentAddress: String?,
    val parentName: String?
) {
    var parentView: String = ""
    var categoryView: String = ""
}





//guid	String	Глобальный идентификатор
//category	String	Глобальный идентификатор категории
//name	String	Наименование
//subTitle	String	Подзаголовок
//description	String	Краткое описание
//fullDescription	String	Полное описание
//date	Nullable<DateTime>	Дата
//end	Nullable<DateTime>	Завершение
//age	Nullable<Int32>	Возрастное ограничение
//website	String	Веб сайт
//phone	String	Веб сайт
//workingDaysHours	String	Часы работы
//source	String	Источник информации
//address	String	Адрес
//lat	Nullable<Decimal>	Широта
//lon	Nullable<Decimal>	Долгота
//parent	String	Глобальный идентификатор родителя
//image	ImageInfo	Изображение
//photos	ImageInfo[]	Фотографии
//votes	Int32	Количество оценок
//rating	Nullable<Int32>	Рейтинг
//comments	Int32	Количество комментариев
//views	Int32	Количество просмотров
//likes	Int32	Количество лайков
//hasLike	Boolean	Наличие лайка
//undergrounds	DictionaryInfo[]	Ближайшие станции метро