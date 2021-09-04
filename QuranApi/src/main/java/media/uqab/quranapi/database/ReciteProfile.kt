package media.uqab.quranapi.database


class ReciteProfile {
    private val profile: HashMap<Int, Profile> = hashMapOf()

    infix fun isPresent(verseID: Int): Boolean { return profile.containsKey(verseID) }
    fun getProfile(verseID: Int): Profile? = if (profile.containsKey(verseID)) { profile[verseID] } else {null}
    fun setProfile(verseID: Int, profile: Profile) { this.profile[verseID] = profile }


    data class Profile(val profileID: Int,
                       val profileName: String,
                       var readSurah: Int,
                       var readVerse: Int)
}
