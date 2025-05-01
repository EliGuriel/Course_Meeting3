
<div dir="rtl">

# תרגיל מעשי: יצירת אפליקציית ספרייה עם MongoDB

## מטרת התרגיל

בתרגיל זה תבנה אפליקציית Spring Boot פשוטה לניהול ספרייה המשתמשת ב-MongoDB כבסיס הנתונים. האפליקציה תאפשר לשמור, לחפש ולנהל ספרים בספרייה.

## דרישות בסיסיות

1. יצירת מודל `Book` (Collection)
2. יצירת Repository עם מספר שאילתות בסיסיות
3. יצירת DTO, Service ו-Controller בסיסיים

## שלב 1: הגדרת המודל

הגדר מחלקה בשם `Book` שתייצג ספר בספרייה. המחלקה צריכה לכלול את השדות הבאים:
- מזהה ייחודי
- כותרת הספר
- שם המחבר
- שנת פרסום
- ז'אנר
- האם זמין להשאלה (boolean)
- מערך של תגיות (tags)
- דירוג (בין 1-5)

**דוגמה חלקית**:

</div>

```java
@Document(collection = "books")
public class Book {
    @Id
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    // הוסף את שאר השדות הנדרשים
}
```

<div dir="rtl">

## שלב 2: יצירת Repository

צור ממשק `BookRepository` שיירש מ-`MongoRepository` ויכלול שלוש שאילתות פשוטות:

1. מציאת ספרים לפי מחבר
2. מציאת ספרים זמינים לפי ז'אנר
3. מציאת ספרים לפי תג (חיפוש בתוך מערך)

**רמז**:

</div>

```java
public interface BookRepository extends MongoRepository<Book, String> {
    // שיטה למציאת ספרים לפי מחבר
    List<Book> findByAuthor(String author);
    
    // כתוב את שתי השיטות הנוספות
}
```

<div dir="rtl">

## שלב 3: יצירת DTO

צור מחלקת DTO בשם `BookDto` שתיהיה מקבילה למחלקת `Book` ותשמש להעברת מידע בין השכבות.

## שלב 4: יצירת Mapper

צור מחלקת `BookMapper` שתהיה אחראית על המרה בין `Book` ל-`BookDto` ולהיפך.

**דוגמה חלקית**:

</div>

```java
@Component
public class BookMapper {
    public Book toEntity(BookDto dto) {
        // המר את ה-DTO לאובייקט Book
    }
    
    public BookDto toDto(Book entity) {
        // המר את אובייקט Book ל-DTO
    }
}
```

<div dir="rtl">

## שלב 5: יצירת Service

צור ממשק `BookService` ומחלקת מימוש `BookServiceImpl` שתספק פעולות בסיסיות כמו:
- קבלת כל הספרים
- קבלת ספר לפי מזהה
- יצירת ספר חדש
- עדכון ספר קיים
- מחיקת ספר
- שיטות המשתמשות בשאילתות שיצרת ב-Repository

## שלב 6: יצירת Controller

צור מחלקת `BookController` שתחשוף את הפונקציונליות דרך REST API.

**דוגמה**:

</div>

```java
@RestController
@RequestMapping("/api/books")
public class BookController {
    // הוסף את הנדרש
    
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDto>> getBooksByAuthor(@PathVariable String author) {
        // קוד לקבלת ספרים לפי מחבר
    }
    
    // הוסף את שאר ה-endpoints
}
```

<div dir="rtl">

## שלב 7: טיפול בשגיאות

צור מחלקה `GlobalExceptionHandler` שתטפל בחריגות כמו:
- `ResourceNotFoundException`
- שגיאות ולידציה
- שגיאות כלליות

## שלב 8: הגדרת MongoDB

הגדר את החיבור ל-MongoDB בקובץ `application.properties`.

</div>

```properties
# MongoDB Configuration
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.database=library
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.username=admin
spring.data.mongodb.password=admin
```

<div dir="rtl">

## משימות תרגול - הרחבת השאילתות

לאחר שסיימת את הפרויקט הבסיסי, הוסף את השאילתות הבאות ל-Repository:

1. הוסף שאילתה שמחזירה ספרים משנת פרסום מסוימת ומעלה
2. הוסף שאילתה שמחפשת ספרים לפי חלק מהכותרת (ללא תלות ברישיות)
3. הוסף שאילתת MongoDB Query מותאמת אישית שמחזירה ספרים עם דירוג מינימלי מסוים שזמינים להשאלה

**רמז לשאילתה מותאמת אישית**:

</div>

```java
@Query("{ 'rating': { $gte: ?0 }, ... }")
List<Book> findAvailableBooksWithMinRating(Integer minRating);
```

<div dir="rtl">

## תרגול קצר: הסבר האופרטורים ב-MongoDB

הסבר בקצרה מה עושה כל אחד מהאופרטורים הבאים ב-MongoDB:

</div>

1. `$eq`
2. `$ne`
3. `$gt`
4. `$lt`
5. `$in`
6. `$regex`
7. `$exists`

<div dir="rtl">

## בדיקת האפליקציה

כדי לבדוק את האפליקציה, השתמש ב-Postman או כלי דומה לשליחת בקשות:

1. צור מספר ספרים באמצעות POST
2. בדוק את השאילתות השונות באמצעות GET
3. עדכן ספר באמצעות PUT
4. מחק ספר באמצעות DELETE

## אתגר נוסף

הוסף אפשרות לחיפוש מתקדם של ספרים לפי שילוב של מספר תנאים (למשל: מחבר, טווח שנים, וזמינות).

</div>