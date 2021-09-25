![](bismillah.png)

#QuranApi

QuranApi is a small library intended to minimize the
efforts of adding Quran inside Android Application.

In muslim community there are several writing formats of the Quran. Though these
formats have the same verses, but the writing style is slightly different from each other.

This projects includes three of these styles.

    1. IndoPak style with minimal Harqat
    2. Saudi style without any Harqat
    3. International with extra Harqats

Currently this Project is focused on IndoPak style of Quran.


### How to Use

Firstly you'll need an instance of QuranApi. QuranApi provides a singleton to use.
Use the static method to instantiate. After that you can use the api.

```kotlin
val api = QuranApi.getInstance(context)
```

#### To get a single verses

```kotlin
api.getVerse(surahNo = 2, verseNo = 2) { /*it: Verse*/
    // you got the verse with
    // this callback
}
```

Each Verse contains information about

- **verseID:** this is simply the verse index in the Quran out of 6236 verses
- **verse:** International style of verse
- **verseAr:** Arabic style with no Harqat
- **verseIndo:** IndoPak style of writing
- **verseNo:** Index of Verse in Surah
- **surahNo:** Index of Surah
- **spannedIndo:** Spanned object of IndoPak style verse

#### To get all the surah information:

This is a static method. So you can get the list without instantiating QuranApi.

```kotlin
QuranApi.getSurahInfoList()
```

#### To get a single Surah

```kotlin
api.getSurah(surahIndex) { /*it: surah*/
    // Surah with contents
}
```

Each Surah contains these informations

- **surahNo**: index of Surah
- **name**: Surah name in English
- **nameAr**: Surah name in Arabic
- **type**: Makkiah or Madaniyah
- **verses**: verses of this surah

#### To get a Page
i.e. IndoPak page which is also known as Hafeji Quran in Indian subcontinent,
you have two options:

1. Either get a single page. Or,
2. The surah with pages.

```kotlin
api.getByPage(pageNo) {
    // single page
}

api.getBySurah(surahNo) {
    // whole surah with
    // verse formated inside pages
}
```

Since each verse contains information about **surahNo**,
you can query Surah information by static method as described above.


#### To get Tajweed Color:

Currently tajweed color of IndoPak format is included inside each verse.
But you can query it by yourself.

```kotlin
TajweedApi.getTajweedColored(verse.verseIndo) // only IndoPak is avaiable for now
```

The spanned verse is created on a separate thread.
This is done for performance improvement, since calculation of each tajweed is CPU intensive.
So use it with **try..catch**
For example, in **recyclerView**

```kotlin
try { binding.textView.text = verse.spannedIndo }
catch (e: Exception) { binding.textView.text = TajweedApi.getTajweedColored(verse.verseIndo) }
```

This project is in very early stage. So feel free to issue any bug you find.
You can also contribute to this project.
