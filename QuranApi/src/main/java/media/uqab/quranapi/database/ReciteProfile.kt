package media.uqab.quranapi.database

/**
 * This class helps to save info such as last read etc
 * Multiple profile can be save with different profile object
 *
 * This feature is not complete yet.
 */
class ReciteProfile {
    private val profile: HashMap<Int, Profile> = hashMapOf()

    infix fun isPresent(profileID: Int): Boolean { return profile.containsKey(profileID) }
    fun getProfile(profileID: Int): Profile? = if (profile.containsKey(profileID)) { profile[profileID] } else {null}
    fun setProfile(profile: Profile) { this.profile[profile.profileID] = profile }
    fun getAllProfile(): MutableCollection<Profile> { return profile.values }


    data class Profile(val profileID: Int,
                       val profileName: String,
                       var color: Int,
                       var readSurah: Int,
                       var readVerse: Int)
}
