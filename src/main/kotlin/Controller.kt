import org.apache.poi.xssf.usermodel.*

class Controller(tableFile: XSSFWorkbook, indexSheet: Int) {
    // Получаем лист, с которого будем брать информацию
    private val table = tableFile.getSheetAt(indexSheet)


    private fun analyzingInfo(metaString: String, disciplineString: String): Lesson
    {
        val lesson = Lesson(mutableListOf(), 0,"", String(), String())
        lesson.group = analyzingGroupInfo(metaString.substringBefore(" "))
        lesson.housing = metaString.substring(metaString.indexOf(".")+1,metaString.indexOf("-")).toInt()
        lesson.audience = metaString.substringAfter("-")
        lesson.type =if(disciplineString.filter {
                !it.isWhitespace() } == "") {
            null
        }
        else {disciplineString.substringBefore(".").filter { !it.isWhitespace() }}
        lesson.name = if(disciplineString.filter { !it.isWhitespace() } == "") { null } else {disciplineString.substringAfter(".")}
        return lesson
    }


    private fun analyzingGroupInfo(info: String): MutableList<Group>
    {
        val groupList = mutableListOf<Group>()
        if (info.length > 3)
        {
            groupList.add(Group(info.substringBefore(info[3]).filter{it.isDigit()}.toInt(),info.substringBefore(info[3]).filterNot{it.isDigit()}
            ))

            groupList.add(Group(info.substringAfter(info[3]).filter{it.isDigit()}.toInt(),info.substringAfter(info[3]).filterNot{it.isDigit()}))
        }
        else
            groupList.add(Group(info.filter{it.isDigit()}.toInt(),info.filterNot{it.isDigit()}))
        return groupList
    }


    private fun getTimeForOneTeacher(idTeacher: Int, table: XSSFSheet): TimeTable
    {
        val timeTable = TimeTable(mutableListOf(), mutableListOf())
        var currentLesson: Lesson?
        var currentDayOdd: Day //нечет
        var currentDayEven: Day //чет
        var currentRow = 32 * idTeacher + 8
        var currentCell = 1
        val lastRow = currentRow + 5 * 4
        val lastCell = 6
        while (currentCell <= lastCell)
        {
            currentDayOdd = Day(
                when(currentCell)
                {
                    1->"Monday"
                    2->"Tuesday"
                    3->"Wednesday"
                    4->"Thursday"
                    5->"Friday"
                    6->"Saturday"
                    else->""
                }, mutableListOf())

            currentDayEven = currentDayOdd
            while (currentRow < lastRow)
            {
                if (table.getRow(currentRow).getCell(currentCell).toString().filter {
                !it.isWhitespace() } == "")
                {
                    if (table.getRow(currentRow+1).getCell(currentCell).toString().filter {
                    !it.isWhitespace() } != "")
                    {
                        currentRow++
                        currentLesson = analyzingInfo(
                            table.getRow(currentRow++).getCell(currentCell).toString(),
                            table.getRow(currentRow).getCell(currentCell).toString()
                        )
                        currentRow++
                        currentDayOdd.lessons.add(currentLesson)
                    }
                    else
                    {
                        currentLesson = null
                        currentRow++
                    }
                }
                else
               {
                    currentLesson = analyzingInfo(
                        table.getRow(currentRow).getCell(currentCell).toString(),
                    table.getRow(++currentRow).getCell(currentCell).toString())
                }
                if(currentRow%4-1 == 0)
                    currentDayOdd.lessons.add(currentLesson)
                else
                    currentDayEven.lessons.add(currentLesson)
                currentRow++
            }
            timeTable.evenWeek.add(currentDayEven)
            timeTable.oddWeek.add(currentDayOdd)
            currentCell++
            currentRow = 32 * idTeacher + 8
        }
        return timeTable
    }


    private fun getTeachers(table: XSSFSheet): MutableList<Teacher> {
        val arrayTeachers: MutableList<Teacher> = mutableListOf()
        var currentTeacher: Teacher
        var i = 0
        while (table.getRow(32 * i + 6)?.getCell(1) != null) {
            currentTeacher = Teacher(name = table.getRow(32 * i +
                    6).getCell(1).toString(), timeTable = getTimeForOneTeacher(i,table))
            arrayTeachers.add(currentTeacher)
            i++
        }
        return arrayTeachers
    }
    val teachers = getTeachers(table)
}