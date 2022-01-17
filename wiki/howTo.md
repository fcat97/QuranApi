![](bismillah.png)

#QuranApi

QuranApi is a small library intended to minimize the
efforts of adding the holy Quran inside Android Application.

In muslim community there are several writing formats of the Quran. Though these
formats have the same verses, but the writing style is slightly different from each other.

This projects includes three of these styles.

    1. IndoPak(hafeji) style with minimal Harqat
    2. Arabic without any Harqat
    3. International(uthmani)

![](verse_styles.png)

Currently this Project is focused on **IndoPak** style of Quran.


### How to Use

Firstly you'll need an instance of QuranApi. QuranApi provides a singleton to use.
Use the static method to instantiate. After that you can use the api.

```kotlin
val api = QuranApi.getInstance(context)
```

#### To get a single verses

There is an normal blocking method and an async method. Use which match your need.

**Note:** verseIndex is the position of verse in Quran having a range between 1(Fatiha 1:1) to 6236(Suratun Naas 114:6).

```kotlin
// blocking method
Thread {
    val verse = api.getVerse(verseIndex = 4)
}.start()

// async method
api.getVerseAsync(surahNo = 2, verseNo = 2) { /*it: Verse*/
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
- **pageIndoNo:** Page number of this verse in Hafeji Quran
- **spannedIndo:** Spanned object of IndoPak style verse. Created on separate thread, so use with `try...catch`

#### To get all the surah information:

This is a static method. So you can get the list without instantiating QuranApi.
This method block the thread for the first time. The time is very little. Mostly
not noticable.

```kotlin
QuranApi.getSurahInfoList()
```

#### To get a single Surah

```kotlin
api.getSurahAsync(surahIndex) { /*it: surah*/
    // Surah with contents
}

// or blocking
val surah = api.getSurah()
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
api.getByPageAsync(pageNo) {
    // single page
}

api.getBySurahAsync(surahNo) {
    // whole surah with
    // verse formated inside pages
}
```

Since each verse contains information about **surahNo**,
you can query Surah information by static method as described above.

**Note:** You may not need to use `getBySurah()` since `getSurah()` will return verses of that surah
and all the verses has information of the page they belongs to. See the use of `VerseAdapter` in
`app` module for better understanding.

#### To get Tajweed Color:

Currently tajweed color of only *IndoPak* format is included inside each verse.
You can use any string in Arabic formatted as *IndoPak* style to get a spanned text with tajweed colors.

***Note:*** The TajweedApi is still in BETA, some tajweed may not work.

```kotlin
TajweedApi.getTajweedColored(verse.verseIndo) // only IndoPak is avaiable for now
```

The spanned verse is created on a separate thread while user query for Verse.
This is done for performance improvement, since calculation of each tajweed is a little CPU intensive.
So use it with **try..catch**
For example, in **recyclerView**

```kotlin
try { binding.textView.text = verse.spannedIndo }
catch (ignored: Exception) { binding.textView.text = TajweedApi.getTajweedColored(verse.verseIndo) }
```

This project is in very early stage. So feel free to issue any bug you find.
You can also contribute to this project.
