import kotlinx.serialization.*
@Serializable
class Day(
    var dayOfWeek: String,
    var lessons: MutableList<Lesson?>
) {
    override fun toString() = "\n$dayOfWeek\n$lessons\n"
}


@Serializable
class Group(
    var flow: Int,
    var groupName: String
) {
    override fun toString(): String = "$flow$groupName"
}


@Serializable
class Lesson(
    var group: MutableList<Group>,
    var housing: Int,
    var audience: String,
    var type: String?,
    var name: String?
)
{
    override fun toString() = when (type) {
        null -> "\nGroup:$group, Audience:$audience"
        else -> "\nGroup:$group, Audience:$housing-$audience, Typediscipline:$type, Dicipline:$name"
    }
}


@Serializable
data class Teacher(
    var name: String,
    var timeTable: TimeTable
) {
    override fun toString() = "\n$name\n$timeTable\n"
}


@Serializable
class TimeTable(
    val evenWeek: MutableList<Day>,
    val oddWeek: MutableList<Day>
) {
    override fun toString() = "Нижняя неделя:\n$evenWeek\nВерхняя неделя:\n$oddWeek\n"
}