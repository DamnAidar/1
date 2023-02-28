import org.apache.poi.xssf.usermodel.*
import org.litote.kmongo.*
fun main() {
    val filePath = "C:\\Games\\kkk2\\src\\main\\resources\\Автоматика и системы.xlsx"
    val excelTable = XSSFWorkbook(filePath)
    val controller = Controller(excelTable,0)
    val dbTeachers = mongoDatabase.getCollection<Teacher>().apply{ drop() }
    dbTeachers.insertMany(controller.teachers)
    prettyPrintCursor(dbTeachers.find())
}