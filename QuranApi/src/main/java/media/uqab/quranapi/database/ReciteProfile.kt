package media.uqab.quranapi.database

/**
 * This class helps to save info such as last read etc
 * Multiple profile can be save with different profile object
 *
 * This feature is not complete yet.
 */
class ReciteProfile {
    private val profile: HashMap<Int, Profile> = hashMapOf()

    infix fun isPresent(verseID: Int): Boolean { return profile.containsKey(verseID) }
    fun getProfile(verseID: Int): Profile? = if (profile.containsKey(verseID)) { profile[verseID] } else {null}
    fun setProfile(verseID: Int, profile: Profile) { this.profile[verseID] = profile }
    fun getAllProfile(): MutableCollection<Profile> { return profile.values }


    data class Profile(val profileID: Int,
                       val profileName: String,
                       var color: Int,
                       var readSurah: Int,
                       var readVerse: Int)
}
